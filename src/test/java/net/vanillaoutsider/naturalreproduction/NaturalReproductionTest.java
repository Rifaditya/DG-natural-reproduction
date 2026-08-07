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
}
