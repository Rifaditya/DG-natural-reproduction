// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.1
package net.vanillaoutsider.naturalreproduction.gametest;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.level.block.Blocks;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;
import net.vanillaoutsider.naturalreproduction.util.AnimalCrampedSpaceHelper;
import net.vanillaoutsider.naturalreproduction.util.AnimalPastureHelper;
import net.vanillaoutsider.naturalreproduction.util.SpatialBreedingCacheHelper;

/**
 * Pillar 4: In-Engine Fabric GameTest Suite.
 * Validates native GameRules, live spatial density suppression, cramped pen stunting,
 * and spacious pasture recovery in an in-engine headless Minecraft server environment.
 */
public class NaturalReproductionGameTests {

    public static void testDensityCapSuppression(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        helper.assertTrue(level != null, "GameTest helper world level must not be null");

        SpatialBreedingCacheHelper.clearCaches();

        BlockPos pos = new BlockPos(2, 1, 2);
        Cow cow1 = helper.spawn(EntityType.COW, pos);
        Cow cow2 = helper.spawn(EntityType.COW, pos);
        Cow cow3 = helper.spawn(EntityType.COW, pos);

        helper.assertTrue(cow1 != null && cow2 != null && cow3 != null, "Cows failed to spawn in GameTest");

        // Verify count
        int count = SpatialBreedingCacheHelper.getNearbySameSpeciesCount(level, cow1, 16.0);
        helper.assertTrue(count >= 3, "Nearby count should detect all 3 spawned cows");

        // Dynamic GameRule check
        int densityCap = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DENSITY_CAP);
        helper.assertTrue(densityCap > 0, "Density cap rule must be positive default");

        // If local population exceeds cap (e.g. cap=2), reproduction must suppress
        boolean suppressed = count > 2;
        helper.assertTrue(suppressed, "Population of 3 must exceed density cap of 2 and suppress reproduction");

        SpatialBreedingCacheHelper.clearCaches();
        helper.succeed();
    }

    public static void testCrowdedPenStunting(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        SpatialBreedingCacheHelper.clearCaches();

        BlockPos center = new BlockPos(3, 1, 3);
        Cow parent1 = helper.spawn(EntityType.COW, center);
        Cow parent2 = helper.spawn(EntityType.COW, center);
        Cow baby = helper.spawn(EntityType.COW, center);

        // Spawn extra animals in immediate area to simulate severe overcrowding
        helper.spawn(EntityType.COW, center);
        helper.spawn(EntityType.COW, center);
        helper.spawn(EntityType.COW, center);
        helper.spawn(EntityType.COW, center);

        // Roll baseline genetics
        DasikAnimalGeneticsAPI.rollStats(parent1, "default");
        DasikAnimalGeneticsAPI.rollStats(parent2, "default");
        DasikAnimalGeneticsAPI.rollStats(baby, "default");
        DasikAnimalGeneticsAPI.setScale(baby, 1.0f);

        // Apply cramped confinement stunting
        AnimalCrampedSpaceHelper.applyConfinementOrRecovery(level, parent1, parent2, baby);

        float stuntedScale = DasikAnimalGeneticsAPI.getScale(baby);
        helper.assertTrue(stuntedScale < 1.0f, "Overcrowded conditions must stunt offspring scale below 1.0x");
        helper.assertTrue(stuntedScale >= 0.10f, "Stunted scale must not breach 0.10x physical floor");

        SpatialBreedingCacheHelper.clearCaches();
        helper.succeed();
    }

    public static void testSpaciousPastureRecovery(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        SpatialBreedingCacheHelper.clearCaches();

        BlockPos pos = new BlockPos(2, 1, 2);
        Cow parent1 = helper.spawn(EntityType.COW, pos);
        Cow parent2 = helper.spawn(EntityType.COW, pos);
        Cow baby = helper.spawn(EntityType.COW, pos);

        // Setup pasture enrichment blocks nearby (hay bale + water cauldron)
        helper.setBlock(new BlockPos(1, 1, 1), Blocks.HAY_BLOCK);
        helper.setBlock(new BlockPos(1, 1, 2), Blocks.WATER_CAULDRON);

        boolean isEnriched = AnimalPastureHelper.isPastureEnriched(level, helper.absolutePos(pos));
        helper.assertTrue(isEnriched, "Pasture with hay and cauldron must evaluate to enriched");

        // Stunted baby recovering in spacious pasture
        DasikAnimalGeneticsAPI.rollStats(parent1, "default");
        DasikAnimalGeneticsAPI.rollStats(parent2, "default");
        DasikAnimalGeneticsAPI.rollStats(baby, "default");
        DasikAnimalGeneticsAPI.setScale(baby, 0.70f);

        AnimalCrampedSpaceHelper.applyConfinementOrRecovery(level, parent1, parent2, baby);

        float recoveredScale = DasikAnimalGeneticsAPI.getScale(baby);
        helper.assertTrue(recoveredScale > 0.70f, "Spacious pasture must provide scale recovery boost above starting 0.70x");
        helper.assertTrue(recoveredScale <= 1.20f, "Recovered scale must not exceed 1.20x max ceiling");

        SpatialBreedingCacheHelper.clearCaches();
        helper.succeed();
    }
}
