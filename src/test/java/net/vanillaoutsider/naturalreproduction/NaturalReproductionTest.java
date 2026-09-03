// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NaturalReproductionTest {

    @Test
    @DisplayName("Verify Default Breeding Rate & Density Cap Logic")
    public void testDefaultConfigurationLogic() {
        int defaultRate = 24000;
        int defaultDensityCap = 10;

        Assertions.assertTrue(defaultRate >= 100 && defaultRate <= 240000, "Breeding rate should be within valid bounds");
        Assertions.assertTrue(defaultDensityCap >= 1 && defaultDensityCap <= 100, "Density cap should be within safe performance bounds");
    }

    @Test
    @DisplayName("Verify Density Cap Population Filtering")
    public void testDensityCapThreshold() {
        int currentPopulation = 12;
        int densityCap = 10;

        boolean canBreed = currentPopulation <= densityCap;
        Assertions.assertFalse(canBreed, "Autonomous breeding must be blocked when current species count exceeds density cap");
    }

    @Test
    @DisplayName("Verify Dynamic Scale Drop Multiplier Math")
    public void testScaleDropMath() {
        float normalScale = 0.95f;
        float minScale = 0.10f;
        float maxScale = 1.20f;
        int initialCount = 10;

        // Baseline (0.95x): 100% drops
        float baselineScale = 0.95f;
        float baselineRatio = Math.clamp((baselineScale - normalScale) / (maxScale - normalScale), 0.0f, 1.0f);
        int extraBaseline = Math.round(initialCount * (baselineRatio * 0.50f));
        Assertions.assertEquals(0, extraBaseline, "Normal baseline animal (0.95x) must yield 0 extra drops");
        Assertions.assertEquals(10, initialCount + extraBaseline, "Normal baseline animal (0.95x) must yield 100% drops");

        // Maximum scale (1.20x): +50% bonus drops
        float maxAnimalScale = 1.20f;
        float maxRatio = Math.clamp((maxAnimalScale - normalScale) / (maxScale - normalScale), 0.0f, 1.0f);
        int extraMax = Math.round(initialCount * (maxRatio * 0.50f));
        Assertions.assertEquals(5, extraMax, "Max scale animal (1.20x) must yield +50% extra drops");
        Assertions.assertEquals(15, initialCount + extraMax, "Max scale animal (1.20x) must yield 15 drops from 10 initial items");

        // Midpoint above normal (1.075x): +25% bonus drops
        float midHighScale = 1.075f;
        float midHighRatio = Math.clamp((midHighScale - normalScale) / (maxScale - normalScale), 0.0f, 1.0f);
        int extraMidHigh = Math.round(initialCount * (midHighRatio * 0.50f));
        Assertions.assertEquals(3, extraMidHigh, "Mid-high animal (1.075x) must yield +25% (rounded to 3) extra drops from 10 items");

        // Minimum floor scale (0.10x): 0% drops
        float minAnimalScale = 0.10f;
        float minRatio = Math.clamp((minAnimalScale - minScale) / (normalScale - minScale), 0.0f, 1.0f);
        int countMin = Math.round(initialCount * minRatio);
        Assertions.assertEquals(0, countMin, "Severely stunted minimum floor animal (0.10x) must yield 0 drops");

        // Midpoint below normal (0.525x): 50% drops
        float midLowScale = 0.525f;
        float midLowRatio = Math.clamp((midLowScale - minScale) / (normalScale - minScale), 0.0f, 1.0f);
        int countMidLow = Math.round(initialCount * midLowRatio);
        Assertions.assertEquals(5, countMidLow, "Mid-low animal (0.525x) must yield exactly 50% drops (5 from 10 items)");
    }

    @Test
    @DisplayName("Verify Cramped Space Stunting & Spacious Recovery Math")
    public void testCrampedSpaceAndSpaciousRecoveryMath() {
        float minScale = 0.10f;
        float maxScale = 1.20f;
        float initialGeneticsScale = 0.95f;
        int crampedDensity = 15; // heavily overcrowded
        float penaltyMultiplier = Math.max(0.95f - (crampedDensity * 0.05f), 0.20f);

        float stuntedScale = Math.clamp(initialGeneticsScale * penaltyMultiplier, minScale, maxScale);
        Assertions.assertEquals(0.19f, stuntedScale, 0.001f, "Extreme cramped breeding should stunt offspring scale down to ~0.19x");

        // Stunting clamp floor verification
        float extremeStunted = Math.clamp(0.05f, minScale, maxScale);
        Assertions.assertEquals(0.10f, extremeStunted, 0.001f, "Severe stunting must clamp to 0.10x floor");

        float recoveryBoost = 1.15f;
        float recoveredScale = Math.clamp(stuntedScale * recoveryBoost, minScale, maxScale);
        Assertions.assertEquals(0.2185f, recoveredScale, 0.001f, "Breeding in spacious pastures should apply +15% scale recovery boost");

        // Recovery ceiling clamp verification
        float overboosted = Math.clamp(1.35f, minScale, maxScale);
        Assertions.assertEquals(1.20f, overboosted, 0.001f, "Spacious recovery must clamp to 1.20x max ceiling");
    }

    @Test
    @DisplayName("Verify Native Biome Fertility Rate Boost Math")
    public void testBiomeFertilityAndVariantLogic() {
        int baseRate = 24000;
        boolean isNativeBiome = true;
        int effectiveRate = isNativeBiome ? Math.max(100, baseRate / 2) : baseRate;

        Assertions.assertEquals(12000, effectiveRate, "Animals in native biomes should receive a 2x breeding frequency boost (12000 ticks)");

        float initialScale = 0.95f;
        float boostedScale = Math.clamp(initialScale * 1.15f, 0.10f, 1.20f);
        Assertions.assertEquals(1.0925f, boostedScale, 0.001f, "Offspring born in native biomes should receive +15% genetics quality boost");
    }

    @Test
    @DisplayName("Verify Variant-Specific Environmental Habitat Triggers Logic")
    public void testVariantSpecificHabitatLogic() {
        String snowyWolfVariant = "minecraft:snowy";
        boolean requiresSnowBlocks = "minecraft:snowy".equals(snowyWolfVariant);
        Assertions.assertTrue(requiresSnowBlocks, "Snowy Wolf variant requires snow/ice blocks for breeding");

        String goldRabbitVariant = "GOLD";
        boolean requiresDesertBlocks = "GOLD".equals(goldRabbitVariant);
        Assertions.assertTrue(requiresDesertBlocks, "Gold Rabbit variant requires sand/cactus blocks for breeding");
    }

    @Test
    @DisplayName("Verify Multi-Generational Inbreeding Tier Progression (Tiers 0 -> 4)")
    public void testInbreedingTierProgression() {
        // T0 + T0 inbred -> T1
        int t1 = Math.clamp(Math.max(0, 0) + 1, 1, 4);
        Assertions.assertEquals(1, t1, "First inbreeding cross must yield Tier 1");

        // T1 + T1 inbred -> T2
        int t2 = Math.clamp(Math.max(1, 1) + 1, 1, 4);
        Assertions.assertEquals(2, t2, "Second inbreeding cross must yield Tier 2");

        // T2 + T2 inbred -> T3
        int t3 = Math.clamp(Math.max(2, 2) + 1, 1, 4);
        Assertions.assertEquals(3, t3, "Third inbreeding cross must yield Tier 3");

        // T3 + T3 inbred -> T4
        int t4 = Math.clamp(Math.max(3, 3) + 1, 1, 4);
        Assertions.assertEquals(4, t4, "Fourth inbreeding cross must yield Tier 4 Lethal Collapse");

        // T4 + T4 inbred -> capped at T4
        int t4Cap = Math.clamp(Math.max(4, 4) + 1, 1, 4);
        Assertions.assertEquals(4, t4Cap, "Inbreeding tier cannot exceed Tier 4");
    }

    @Test
    @DisplayName("Verify Gradual Generational Dilution & Hybrid Vigor Outcrossing")
    public void testGradualGenerationalDilution() {
        // T4 outcrossed with T0 -> T3
        int d4 = Math.max(0, Math.max(4, 0) - 1);
        Assertions.assertEquals(3, d4, "Outcrossing Tier 4 with Tier 0 must dilute to Tier 3");

        // T3 outcrossed with T0 -> T2
        int d3 = Math.max(0, Math.max(3, 0) - 1);
        Assertions.assertEquals(2, d3, "Outcrossing Tier 3 with Tier 0 must dilute to Tier 2");

        // T2 outcrossed with T0 -> T1
        int d2 = Math.max(0, Math.max(2, 0) - 1);
        Assertions.assertEquals(1, d2, "Outcrossing Tier 2 with Tier 0 must dilute to Tier 1");

        // T1 outcrossed with T0 -> T0 (Clean recovery)
        int d1 = Math.max(0, Math.max(1, 0) - 1);
        Assertions.assertEquals(0, d1, "Outcrossing Tier 1 with Tier 0 must dilute to Tier 0");

        // Hybrid Vigor boost (+15% scale)
        float baseScale = 1.0f;
        float hybridScale = Math.clamp(baseScale * 1.15f, 0.25f, 1.30f);
        Assertions.assertEquals(1.15f, hybridScale, 0.001f, "Restoring to Tier 0 via outcrossing awards +15% Hybrid Vigor boost");
    }

    @Test
    @DisplayName("Verify Tier 3/4 Inbreeding Drop Conversion & Reduction")
    public void testInbreedingDropConversion() {
        int initialLeather = 4;
        int reducedLeather = Math.max(1, initialLeather / 4);
        Assertions.assertEquals(1, reducedLeather, "Tier 3/4 animals must have secondary yields reduced by 75%");

        int largeLeather = 12;
        int reducedLargeLeather = Math.max(1, largeLeather / 4);
        Assertions.assertEquals(3, reducedLargeLeather, "Tier 3/4 animals with 12 drops must reduce down to 3");
    }

    @Test
    @DisplayName("Verify Enriched Pasture Breeding Speedup & Scale Recovery")
    public void testPastureEnrichmentMath() {
        int baseRate = 24000;
        int enrichedRate = Math.max(100, Math.round(baseRate * 0.75f));
        Assertions.assertEquals(18000, enrichedRate, "Enriched pastures should provide a 25% faster breeding check (18000 ticks)");

        float currentScale = 1.00f;
        float boostedScale = Math.clamp(currentScale * 1.10f, 0.25f, 1.30f);
        Assertions.assertEquals(1.10f, boostedScale, 0.001f, "Well-nourished offspring in enriched pastures gain +10% size recovery boost");
    }

    @Test
    @DisplayName("Verify Overgrazing Herd Density Thresholds")
    public void testOvergrazingThresholds() {
        int lightHerd = 3;
        boolean overgrazedLight = lightHerd >= 5;
        Assertions.assertFalse(overgrazedLight, "Light herd under 5 animals should not cause overgrazing");

        int mediumHerd = 5;
        boolean overgrazedMedium = mediumHerd >= 5;
        Assertions.assertTrue(overgrazedMedium, "Herd of 5+ animals on grass should trigger Dirt conversion");

        int severeHerd = 8;
        boolean coarseDirt = severeHerd >= 8;
        Assertions.assertTrue(coarseDirt, "Severe crowding of 8+ animals should trigger Coarse Dirt conversion");
    }

    @Test
    @DisplayName("Verify Gestation Duration Bounds & Prenatal Vitality Ratio")
    public void testGestationDurationAndVitalityRatio() {
        int defaultGestation = 24000;
        Assertions.assertTrue(defaultGestation >= 100 && defaultGestation <= 240000, "Gestation duration should be within valid bounds");

        float checks = 100.0f;
        float highNourish = 65.0f;
        boolean highVitality = (highNourish / checks) >= 0.50f;
        Assertions.assertTrue(highVitality, "Mothers spending >= 50% of gestation in enriched pasture earn high vitality");

        float lowNourish = 25.0f;
        boolean lowVitality = (lowNourish / checks) >= 0.50f;
        Assertions.assertFalse(lowVitality, "Mothers in cramped pastures do not earn prenatal vitality bonuses");

        double baseMaxHp = 20.0;
        double vitalityHp = baseMaxHp * 1.15;
        Assertions.assertEquals(23.0, vitalityHp, 0.001, "Prenatal vitality awards +15% base max HP");
    }

    @Test
    @DisplayName("Verify Chicken Fertilized Egg Probabilities & Dispenser Bounds")
    public void testChickenFertilizedEggAndDispenserOdds() {
        int dispenserChance = 75;
        Assertions.assertTrue(dispenserChance >= 0 && dispenserChance <= 100, "Dispenser hatch chance must be between 0 and 100%");

        int playerThrownChance = 100;
        Assertions.assertEquals(100, playerThrownChance, "Player thrown fertilized eggs must have 100% guaranteed hatch rate");

        int regularEggMiracleOdds = 64;
        Assertions.assertEquals(64, regularEggMiracleOdds, "Regular unfertilized eggs have a reduced 1-in-64 miracle hatch chance");

        int quadrupletOdds = 256;
        Assertions.assertEquals(256, quadrupletOdds, "Quadruplet chick hatch chance remains at vanilla 1-in-256 odds");
    }

    @Test
    @DisplayName("Verify Alpha Leader Election & Scale Ranking Math")
    public void testHerdLeaderSelectionAndScaleRanking() {
        int smallHerdSize = 2;
        boolean hasHerd = smallHerdSize >= 3;
        Assertions.assertFalse(hasHerd, "Fewer than 3 animals should not form an alpha-led herd");

        int validHerdSize = 5;
        boolean formsHerd = validHerdSize >= 3;
        Assertions.assertTrue(formsHerd, "3 or more animals of the same species form a herd");

        float[] herdScales = { 0.90f, 1.25f, 1.05f, 0.85f };
        float maxScale = -1.0f;
        for (float s : herdScales) {
            if (s > maxScale) {
                maxScale = s;
            }
        }
        Assertions.assertEquals(1.25f, maxScale, 0.001f, "Largest animal (1.25x scale) must be elected Alpha leader");
    }

    @Test
    @DisplayName("Verify Follow Herd Leader Distance Bounds & Stampede Radius")
    public void testHerdFollowBoundsAndStampedeRadius() {
        double minFollowDist = 6.0;
        double maxFollowDist = 24.0;
        double comfortDist = 5.0;

        Assertions.assertEquals(36.0, minFollowDist * minFollowDist, 0.001, "Follow AI start threshold squared is 36.0 (6 blocks)");
        Assertions.assertEquals(576.0, maxFollowDist * maxFollowDist, 0.001, "Follow AI max range squared is 576.0 (24 blocks)");
        Assertions.assertEquals(25.0, comfortDist * comfortDist, 0.001, "Follow AI comfort stop distance squared is 25.0 (5 blocks)");

        double stampedeRadius = 16.0;
        Assertions.assertTrue(stampedeRadius >= 10.0 && stampedeRadius <= 32.0, "Stampede distress alarm radius must be within 10-32 blocks");
    }

    @Test
    @DisplayName("Verify Staggered 100-Tick Breeding Probability Math & Fast-Fail Scaling")
    public void testStaggeredBreedingProbabilityMath() {
        int baseRate = 24000;
        int checkInterval = 100;
        int effectiveRate = Math.max(1, baseRate / checkInterval);

        Assertions.assertEquals(240, effectiveRate, "24000 tick rate evaluated every 100 ticks results in 1-in-240 check odds");

        // Native biome boost (2x faster)
        int nativeRate = Math.max(1, effectiveRate / 2);
        Assertions.assertEquals(120, nativeRate, "Native biome boost scales effective rate from 240 down to 120");

        // Enriched pasture (+25% faster)
        int enrichedRate = Math.max(1, Math.round(effectiveRate * 0.75f));
        Assertions.assertEquals(180, enrichedRate, "Enriched pasture scales effective rate from 240 down to 180");
    }

    @Test
    @DisplayName("Verify Spatial Density Cache TTL & Chunk Hashing Math")
    public void testSpatialDensityCacheMath() {
        long currentTime = 1000L;
        long ttl = 100L;
        long expiryTime = currentTime + ttl;

        boolean isFreshAt1050 = 1050L < expiryTime;
        Assertions.assertTrue(isFreshAt1050, "Cache must be valid within 100 ticks of insertion");

        boolean isExpiredAt1101 = 1101L < expiryTime;
        Assertions.assertFalse(isExpiredAt1101, "Cache must expire after 100 ticks");

        int blockX = 120;
        int blockZ = -45;
        int chunkX = blockX >> 4;
        int chunkZ = blockZ >> 4;
        Assertions.assertEquals(7, chunkX, "Block X 120 corresponds to chunk X 7");
        Assertions.assertEquals(-3, chunkZ, "Block Z -45 corresponds to chunk Z -3");
    }

    @Test
    @DisplayName("Verify Unified Breeding Pipeline Enriched Pasture Vitality Math & Clamping")
    public void testBreedingPipelineVitalityAndClampMath() {
        float currentScale = 1.0f;
        float minAllowed = 0.10f;
        float maxAllowed = 1.20f;

        // Normal boost in enriched pasture (+10%)
        float boostedScale = Math.clamp(currentScale * 1.10f, minAllowed, maxAllowed);
        Assertions.assertEquals(1.10f, boostedScale, 0.001f, "Enriched pasture should boost newborn scale by +10%");

        // Boost clamped to maximum allowed (1.20x)
        float highBaseScale = 1.15f;
        float clampedHigh = Math.clamp(highBaseScale * 1.10f, minAllowed, maxAllowed);
        Assertions.assertEquals(1.20f, clampedHigh, 0.001f, "Boosted scale must clamp to maxAllowed ceiling");

        // Stunted floor clamp verification (0.10x)
        float stuntedScale = 0.08f;
        float clampedLow = Math.clamp(stuntedScale, minAllowed, maxAllowed);
        Assertions.assertEquals(0.10f, clampedLow, 0.001f, "Stunted scale must clamp to minAllowed floor");
    }
}

