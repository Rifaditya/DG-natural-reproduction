// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public final class SpatialBreedingCacheHelper {

    public static final long DENSITY_CACHE_TTL = 100L; // 5 seconds (100 ticks)
    public static final long PASTURE_CACHE_TTL = 200L; // 10 seconds (200 ticks)

    private static final Map<String, CachedDensity> DENSITY_CACHE = new ConcurrentHashMap<>();
    private static final Map<Long, CachedEnrichment> PASTURE_CACHE = new ConcurrentHashMap<>();

    private record CachedDensity(int count, long expiryTime) {
    }

    private record CachedEnrichment(boolean enriched, long expiryTime) {
    }

    private SpatialBreedingCacheHelper() {
    }

    public static int getNearbySameSpeciesCount(ServerLevel level, Animal self, double radius) {
        if (level == null || self == null || !self.isAlive()) {
            return 0;
        }

        long currentTime = level.getGameTime();
        int chunkX = self.blockPosition().getX() >> 4;
        int chunkZ = self.blockPosition().getZ() >> 4;
        String key = self.getType().toString() + "@" + chunkX + "," + chunkZ;

        CachedDensity cached = DENSITY_CACHE.get(key);
        if (cached != null && currentTime < cached.expiryTime) {
            return cached.count;
        }

        List<Animal> sameSpecies = level.getEntitiesOfClass(
            Animal.class,
            self.getBoundingBox().inflate(radius),
            e -> e.getType() == self.getType() && e.isAlive()
        );

        int count = sameSpecies.size();
        DENSITY_CACHE.put(key, new CachedDensity(count, currentTime + DENSITY_CACHE_TTL));
        return count;
    }

    public static boolean isPastureEnrichedCached(ServerLevel level, BlockPos pos, Predicate<BlockPos> scanner) {
        if (level == null || pos == null || scanner == null) {
            return false;
        }

        long currentTime = level.getGameTime();
        long chunkKey = (((long) (pos.getX() >> 4)) & 0xFFFFFFFFL) | ((((long) (pos.getZ() >> 4)) & 0xFFFFFFFFL) << 32);

        CachedEnrichment cached = PASTURE_CACHE.get(chunkKey);
        if (cached != null && currentTime < cached.expiryTime) {
            return cached.enriched;
        }

        boolean enriched = scanner.test(pos);
        PASTURE_CACHE.put(chunkKey, new CachedEnrichment(enriched, currentTime + PASTURE_CACHE_TTL));
        return enriched;
    }

    public static void clearCaches() {
        DENSITY_CACHE.clear();
        PASTURE_CACHE.clear();
    }
}
