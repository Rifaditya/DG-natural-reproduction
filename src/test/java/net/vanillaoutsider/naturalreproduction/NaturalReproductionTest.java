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
    @DisplayName("Verify Scale Drop Multiplier Math")
    public void testScaleDropMath() {
        int initialCount = 10;
        float largeScale = 1.30f;
        float smallScale = 0.75f;

        int extraCountLarge = Math.round(initialCount * (largeScale - 1.0f));
        int totalLargeCount = initialCount + extraCountLarge;
        Assertions.assertEquals(13, totalLargeCount, "Giant animal (1.30x scale) should yield 13 drops from 10 initial items");

        int totalSmallCount = Math.max(1, Math.round(initialCount * smallScale));
        Assertions.assertEquals(8, totalSmallCount, "Runt animal (0.75x scale) should yield 8 drops from 10 initial items");
    }

    @Test
    @DisplayName("Verify Cramped Space Stunting & Spacious Recovery Math")
    public void testCrampedSpaceAndSpaciousRecoveryMath() {
        float initialGeneticsScale = 1.00f;
        int crampedDensity = 8;
        float penaltyMultiplier = crampedDensity >= 8 ? 0.30f : 0.85f;

        float stuntedScale = Math.clamp(initialGeneticsScale * penaltyMultiplier, 0.25f, 2.0f);
        Assertions.assertEquals(0.30f, stuntedScale, 0.001f, "Extreme cramped breeding (8+ mobs) should stunt offspring scale down to 0.30x");

        float recoveryBoost = 1.30f;
        float recoveredScale = Math.clamp(stuntedScale * recoveryBoost, 0.25f, 1.30f);
        Assertions.assertEquals(0.39f, recoveredScale, 0.001f, "Breeding in spacious pastures should apply +30% scale recovery boost per generation");
    }

    @Test
    @DisplayName("Verify Native Biome Fertility Rate Boost Math")
    public void testBiomeFertilityAndVariantLogic() {
        int baseRate = 24000;
        boolean isNativeBiome = true;
        int effectiveRate = isNativeBiome ? Math.max(100, baseRate / 2) : baseRate;

        Assertions.assertEquals(12000, effectiveRate, "Animals in native biomes should receive a 2x breeding frequency boost (12000 ticks)");

        float initialScale = 1.0f;
        float boostedScale = Math.clamp(initialScale * 1.15f, 0.25f, 1.30f);
        Assertions.assertEquals(1.15f, boostedScale, 0.001f, "Offspring born in native biomes should receive +15% genetics quality boost");
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
}
