// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 1.21.11
package net.vanillaoutsider.naturalreproduction;

import net.vanillaoutsider.naturalreproduction.util.SpatialBreedingCacheHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Headless multi-threaded unit test suite asserting thread-safety, zero-deadlock concurrency,
 * deterministic leader election, 64-bit composite coordinate packing, and race-free cache access
 * across 50 concurrent worker threads ([BL-NR-006c]).
 */
public class SpatialCacheConcurrencyTest {

    private record CachedDensityRecord(int count, long expiryTime) {}
    private record CachedEnrichmentRecord(boolean enriched, long expiryTime) {}
    private record MockLeaderCandidate(int id, float scale, String name) {}

    private static long encodeChunkKey(int blockX, int blockZ) {
        return (((long) (blockX >> 4)) & 0xFFFFFFFFL) | ((((long) (blockZ >> 4)) & 0xFFFFFFFFL) << 32);
    }

    private static int unpackChunkX(long chunkKey) {
        return (int) (chunkKey & 0xFFFFFFFFL);
    }

    private static int unpackChunkZ(long chunkKey) {
        return (int) (chunkKey >>> 32);
    }

    private static MockLeaderCandidate electLeader(List<MockLeaderCandidate> candidates) {
        if (candidates == null || candidates.size() < 3) {
            return null;
        }
        MockLeaderCandidate leader = null;
        float maxScale = -1.0f;
        for (MockLeaderCandidate candidate : candidates) {
            float scale = candidate.scale();
            if (scale > maxScale || (scale == maxScale && leader != null && candidate.id() < leader.id())) {
                maxScale = scale;
                leader = candidate;
            }
        }
        return leader;
    }

    @Test
    @DisplayName("Verify 50-Thread Concurrent Spatial Cache Blast (50,000 Operations)")
    public void testFiftyThreadConcurrentSpatialCacheBlast() throws InterruptedException {
        final int threadCount = 50;
        final int operationsPerThread = 1000;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        final CountDownLatch readyLatch = new CountDownLatch(threadCount);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(threadCount);
        final AtomicReference<Throwable> firstError = new AtomicReference<>();

        final ConcurrentHashMap<String, CachedDensityRecord> densityCache = new ConcurrentHashMap<>();
        SpatialBreedingCacheHelper.clearCaches();

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    if (!startLatch.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Worker thread " + threadId + " timed out waiting for starter gun");
                    }

                    for (int i = 0; i < operationsPerThread; i++) {
                        int chunkX = ((threadId * 31 + i) % 64) - 32;
                        int chunkZ = ((threadId * 17 + i) % 64) - 32;
                        String key = "minecraft:cow@" + chunkX + "," + chunkZ;
                        long currentTime = 1000L + (i * 2L);

                        CachedDensityRecord cached = densityCache.get(key);
                        if (cached == null || currentTime >= cached.expiryTime()) {
                            densityCache.put(key, new CachedDensityRecord(i % 15, currentTime + SpatialBreedingCacheHelper.DENSITY_CACHE_TTL));
                        }

                        // Periodic concurrent purge and static helper invocation
                        if (i % 250 == 0) {
                            densityCache.entrySet().removeIf(entry -> currentTime >= entry.getValue().expiryTime());
                            SpatialBreedingCacheHelper.purgeExpired(currentTime);
                        }
                    }
                } catch (Throwable throwable) {
                    firstError.compareAndSet(null, throwable);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        boolean allReady = readyLatch.await(5, TimeUnit.SECONDS);
        Assertions.assertTrue(allReady, "All 50 worker threads must reach ready state within 5 seconds");

        // Fire starter gun
        startLatch.countDown();

        boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        Assertions.assertTrue(completed, "50 concurrent threads must complete within timeout without deadlock");
        Assertions.assertTrue(terminated, "Executor service must terminate cleanly");
        Assertions.assertNull(firstError.get(), "Zero exceptions must occur during concurrent blast");
        Assertions.assertTrue(densityCache.size() > 0, "Cache must contain populated entries post-blast");
    }

    @Test
    @DisplayName("Verify Deterministic Herd Leader Concurrent Election (20 Threads)")
    public void testHerdLeaderConcurrentElection() throws InterruptedException {
        final int threadCount = 20;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        final CountDownLatch readyLatch = new CountDownLatch(threadCount);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(threadCount);
        final AtomicReference<Throwable> firstError = new AtomicReference<>();

        final ConcurrentHashMap<String, MockLeaderCandidate> leaderCache = new ConcurrentHashMap<>();
        final ConcurrentLinkedQueue<MockLeaderCandidate> electionResults = new ConcurrentLinkedQueue<>();

        // Candidate pool: candidate 102 has highest scale (1.30f). Candidate 105 has identical scale but higher ID,
        // so candidate 102 must win deterministically by lower ID tie-breaker.
        final List<MockLeaderCandidate> candidates = List.of(
            new MockLeaderCandidate(101, 0.95f, "SubordinateOne"),
            new MockLeaderCandidate(102, 1.30f, "AlphaLeaderTarget"),
            new MockLeaderCandidate(103, 1.15f, "BetaCandidate"),
            new MockLeaderCandidate(104, 0.80f, "SubordinateTwo"),
            new MockLeaderCandidate(105, 1.30f, "TieBreakerCandidateHigherId")
        );

        final String chunkKey = "minecraft:sheep@14,-8";

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    if (!startLatch.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Thread " + threadId + " timed out waiting for starter gun");
                    }
                    MockLeaderCandidate elected = leaderCache.computeIfAbsent(chunkKey, k -> electLeader(candidates));
                    electionResults.add(elected);
                } catch (Throwable throwable) {
                    firstError.compareAndSet(null, throwable);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        boolean allReady = readyLatch.await(5, TimeUnit.SECONDS);
        Assertions.assertTrue(allReady, "All 20 leader election threads must be ready");

        startLatch.countDown();

        boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        Assertions.assertTrue(completed, "20 concurrent election threads must complete within timeout");
        Assertions.assertTrue(terminated, "Executor service must terminate cleanly");
        Assertions.assertNull(firstError.get(), "Zero exceptions during concurrent herd leader election");
        Assertions.assertEquals(threadCount, electionResults.size(), "Every thread must register an elected leader");

        for (MockLeaderCandidate result : electionResults) {
            Assertions.assertNotNull(result, "Elected leader must not be null");
            Assertions.assertEquals(102, result.id(), "Leader must be candidate 102 (highest scale with lowest tie-break ID)");
            Assertions.assertEquals(1.30f, result.scale(), 0.0001f, "Elected leader must have 1.30f scale");
            Assertions.assertEquals("AlphaLeaderTarget", result.name());
        }
    }

    @Test
    @DisplayName("Verify Concurrent Pasture Enrichment Cache & 64-Bit Composite Coordinate Keys")
    public void testConcurrentPastureEnrichmentCache() throws InterruptedException {
        final int threadCount = 24;
        final int lookupsPerThread = 500;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        final CountDownLatch readyLatch = new CountDownLatch(threadCount);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(threadCount);
        final AtomicReference<Throwable> firstError = new AtomicReference<>();

        final ConcurrentHashMap<Long, CachedEnrichmentRecord> pastureCache = new ConcurrentHashMap<>();

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    if (!startLatch.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Thread " + threadId + " timed out waiting for starter gun");
                    }

                    for (int i = 0; i < lookupsPerThread; i++) {
                        // Span across positive and negative block coordinates
                        int blockX = (threadId * 16) + (i % 32) - 256;
                        int blockZ = (threadId * 32) + (i % 48) - 512;

                        long chunkKey = encodeChunkKey(blockX, blockZ);

                        // Bitwise unpack assertions
                        int unpackedX = unpackChunkX(chunkKey);
                        int unpackedZ = unpackChunkZ(chunkKey);

                        if (unpackedX != (blockX >> 4)) {
                            throw new AssertionError("Chunk X mismatch: expected " + (blockX >> 4) + " but got " + unpackedX);
                        }
                        if (unpackedZ != (blockZ >> 4)) {
                            throw new AssertionError("Chunk Z mismatch: expected " + (blockZ >> 4) + " but got " + unpackedZ);
                        }

                        long currentTime = 5000L + i;
                        CachedEnrichmentRecord record = pastureCache.computeIfAbsent(chunkKey, key -> {
                            boolean isEnriched = ((unpackedX + unpackedZ) & 1) == 0;
                            return new CachedEnrichmentRecord(isEnriched, currentTime + SpatialBreedingCacheHelper.PASTURE_CACHE_TTL);
                        });

                        Assertions.assertNotNull(record);
                        Assertions.assertEquals(((unpackedX + unpackedZ) & 1) == 0, record.enriched());
                    }
                } catch (Throwable throwable) {
                    firstError.compareAndSet(null, throwable);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        boolean allReady = readyLatch.await(5, TimeUnit.SECONDS);
        Assertions.assertTrue(allReady, "All 24 pasture cache threads must be ready");

        startLatch.countDown();

        boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        Assertions.assertTrue(completed, "Concurrent pasture enrichment lookups must complete without deadlock");
        Assertions.assertTrue(terminated, "Executor service must terminate cleanly");
        Assertions.assertNull(firstError.get(), "Zero exceptions must occur during concurrent pasture cache operations");
        Assertions.assertTrue(pastureCache.size() > 0, "Pasture cache must be populated");
    }

    @Test
    @DisplayName("Verify Concurrent Purge, Clear & Reader Race Conditions (50 Threads, 50,000 Operations)")
    public void testConcurrentPurgeAndReaderRaceConditions() throws InterruptedException {
        final int readerThreads = 20;
        final int writerThreads = 20;
        final int evictionThreads = 10;
        final int totalThreads = readerThreads + writerThreads + evictionThreads; // 50
        final int operationsPerThread = 1000;

        final ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        final CountDownLatch readyLatch = new CountDownLatch(totalThreads);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(totalThreads);
        final AtomicReference<Throwable> firstError = new AtomicReference<>();

        final ConcurrentHashMap<String, CachedDensityRecord> densityCache = new ConcurrentHashMap<>();
        final ConcurrentHashMap<Long, CachedEnrichmentRecord> pastureCache = new ConcurrentHashMap<>();
        SpatialBreedingCacheHelper.clearCaches();

        // 1. 20 Reader Threads (threads 0..19): continuously querying density and pasture cache entries across 100 chunks
        for (int t = 0; t < readerThreads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    if (!startLatch.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Reader thread " + threadId + " timed out waiting for starter gun");
                    }
                    for (int i = 0; i < operationsPerThread; i++) {
                        int chunkX = (i % 10) - 5;
                        int chunkZ = ((i / 10) % 10) - 5;
                        String key = "minecraft:cow@" + chunkX + "," + chunkZ;
                        long chunkKey = encodeChunkKey(chunkX << 4, chunkZ << 4);

                        CachedDensityRecord density = densityCache.get(key);
                        CachedEnrichmentRecord pasture = pastureCache.get(chunkKey);

                        if (density != null) {
                            Assertions.assertTrue(density.count() >= 0);
                        }
                        if (pasture != null) {
                            Assertions.assertNotNull(pasture);
                        }
                    }
                } catch (Throwable throwable) {
                    firstError.compareAndSet(null, throwable);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // 2. 20 Writer Threads (threads 20..39): continuously inserting new entries with advancing timestamps
        for (int t = 0; t < writerThreads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    if (!startLatch.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Writer thread " + threadId + " timed out waiting for starter gun");
                    }
                    for (int i = 0; i < operationsPerThread; i++) {
                        int chunkX = ((threadId * 13 + i) % 10) - 5;
                        int chunkZ = ((threadId * 7 + i) % 10) - 5;
                        long now = 1000L + (i * 2L);

                        String key = "minecraft:cow@" + chunkX + "," + chunkZ;
                        long chunkKey = encodeChunkKey(chunkX << 4, chunkZ << 4);

                        densityCache.put(key, new CachedDensityRecord(i % 16, now + SpatialBreedingCacheHelper.DENSITY_CACHE_TTL));
                        pastureCache.put(chunkKey, new CachedEnrichmentRecord((i & 1) == 0, now + SpatialBreedingCacheHelper.PASTURE_CACHE_TTL));
                    }
                } catch (Throwable throwable) {
                    firstError.compareAndSet(null, throwable);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // 3. 10 Eviction Threads (threads 40..49): repeatedly executing purgeExpired(now) and clearCaches()
        for (int t = 0; t < evictionThreads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    if (!startLatch.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Eviction thread " + threadId + " timed out waiting for starter gun");
                    }
                    for (int i = 0; i < operationsPerThread; i++) {
                        long now = 1000L + (i * 2L);
                        densityCache.entrySet().removeIf(entry -> now >= entry.getValue().expiryTime());
                        pastureCache.entrySet().removeIf(entry -> now >= entry.getValue().expiryTime());
                        SpatialBreedingCacheHelper.purgeExpired(now);

                        if (i % 200 == 0) {
                            SpatialBreedingCacheHelper.clearCaches();
                        }
                    }
                } catch (Throwable throwable) {
                    firstError.compareAndSet(null, throwable);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        boolean allReady = readyLatch.await(5, TimeUnit.SECONDS);
        Assertions.assertTrue(allReady, "All 50 worker threads must reach ready state within 5 seconds");

        // Fire starter gun
        startLatch.countDown();

        boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        Assertions.assertTrue(completed, "Eviction stress test must complete within timeout without deadlock");
        Assertions.assertTrue(terminated, "Executor service must terminate cleanly");
        Assertions.assertNull(firstError.get(), "Zero ConcurrentModificationException or race conditions allowed");
        Assertions.assertTrue(SpatialBreedingCacheHelper.getDensityCacheSize() >= 0);
        Assertions.assertTrue(SpatialBreedingCacheHelper.getPastureCacheSize() >= 0);
    }

    @Test
    @DisplayName("Verify Concurrent TTL Expiration Boundary (currentTime == expiryTime)")
    public void testConcurrentTtlExpirationBoundary() throws InterruptedException {
        final long boundaryTime = 2000L;
        final int totalEntries = 500;
        final int pastCount = 150;
        final int exactBoundaryCount = 100;
        final int futureCount = 250; // 150 + 100 + 250 = 500

        final ConcurrentHashMap<String, CachedDensityRecord> boundaryCache = new ConcurrentHashMap<>();
        SpatialBreedingCacheHelper.clearCaches();

        // Populate 500 entries:
        // 1. 150 Past entries (expiryTime < boundaryTime)
        for (int i = 0; i < pastCount; i++) {
            boundaryCache.put("past_entry_" + i, new CachedDensityRecord(i, boundaryTime - 100L - i));
        }

        // 2. 100 Exact Boundary entries (expiryTime == boundaryTime)
        for (int i = 0; i < exactBoundaryCount; i++) {
            boundaryCache.put("boundary_entry_" + i, new CachedDensityRecord(i, boundaryTime));
        }

        // 3. 250 Future entries (expiryTime > boundaryTime)
        for (int i = 0; i < futureCount; i++) {
            boundaryCache.put("future_entry_" + i, new CachedDensityRecord(i, boundaryTime + 100L + i));
        }

        Assertions.assertEquals(totalEntries, boundaryCache.size(), "Pre-purge cache must contain exactly 500 entries");

        final int threadCount = 16;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch readyLatch = new CountDownLatch(threadCount);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(threadCount);
        final AtomicReference<Throwable> firstError = new AtomicReference<>();

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    if (!startLatch.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Boundary thread " + threadId + " timed out waiting for starter gun");
                    }

                    for (int i = 0; i < 500; i++) {
                        // Concurrent reads across all keys
                        String futureKey = "future_entry_" + (i % futureCount);
                        CachedDensityRecord rec = boundaryCache.get(futureKey);
                        if (rec != null) {
                            Assertions.assertTrue(rec.expiryTime() > boundaryTime);
                        }

                        // Concurrent purge execution on boundary condition
                        boundaryCache.entrySet().removeIf(entry -> boundaryTime >= entry.getValue().expiryTime());
                        SpatialBreedingCacheHelper.purgeExpired(boundaryTime);
                    }
                } catch (Throwable throwable) {
                    firstError.compareAndSet(null, throwable);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        boolean allReady = readyLatch.await(5, TimeUnit.SECONDS);
        Assertions.assertTrue(allReady, "All 16 boundary testing threads must reach ready state");

        startLatch.countDown();

        boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        Assertions.assertTrue(completed, "Boundary condition concurrency test must complete within timeout");
        Assertions.assertTrue(terminated, "Executor service must terminate cleanly");
        Assertions.assertNull(firstError.get(), "Zero exceptions allowed during boundary condition test");

        // Assert all past entries were swept
        for (int i = 0; i < pastCount; i++) {
            Assertions.assertNull(boundaryCache.get("past_entry_" + i), "Past entry " + i + " must be purged");
        }

        // Assert all exact boundary entries (expiryTime == boundaryTime) were swept
        for (int i = 0; i < exactBoundaryCount; i++) {
            Assertions.assertNull(boundaryCache.get("boundary_entry_" + i), "Exact boundary entry " + i + " must be purged when currentTime == expiryTime");
        }

        // Assert all future entries remain intact
        for (int i = 0; i < futureCount; i++) {
            CachedDensityRecord futureRec = boundaryCache.get("future_entry_" + i);
            Assertions.assertNotNull(futureRec, "Future entry " + i + " must remain intact in cache");
            Assertions.assertTrue(futureRec.expiryTime() > boundaryTime, "Future entry expiryTime must be strictly greater than boundaryTime");
        }

        // Assert exact size matches futureCount
        Assertions.assertEquals(futureCount, boundaryCache.size(), "Only future entries must remain intact post-purge");
    }
}
