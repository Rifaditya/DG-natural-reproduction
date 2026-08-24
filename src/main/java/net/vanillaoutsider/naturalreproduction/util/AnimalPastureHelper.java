// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class AnimalPastureHelper {

    private AnimalPastureHelper() {
    }

    public static boolean isPastureEnriched(ServerLevel level, BlockPos center) {
        if (level == null || center == null) {
            return false;
        }

        return SpatialBreedingCacheHelper.isPastureEnrichedCached(level, center, pos -> computePastureEnrichment(level, pos));
    }

    private static boolean computePastureEnrichment(ServerLevel level, BlockPos center) {
        int score = 0;
        boolean foundTrough = false;
        boolean foundHay = false;
        boolean foundWater = false;

        // Scan compact 17x7x17 radius (8 horizontally, 3 vertically) for enrichment elements
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-8, -3, -8), center.offset(8, 3, 8))) {
            BlockState state = level.getBlockState(pos);

            if (!foundTrough && (state.is(Blocks.CAULDRON) || state.is(Blocks.WATER_CAULDRON) || state.is(Blocks.COMPOSTER))) {
                score++;
                foundTrough = true;
            } else if (!foundHay && state.is(Blocks.HAY_BLOCK)) {
                score++;
                foundHay = true;
            } else if (!foundWater && state.is(Blocks.WATER)) {
                score++;
                foundWater = true;
            }

            if (score >= 2) {
                return true;
            }
        }

        // Add weather shelter point if under a solid barn roof
        if (isUnderShelter(level, center)) {
            score++;
        }

        return score >= 2;
    }

    public static boolean isUnderShelter(ServerLevel level, BlockPos center) {
        if (level == null || center == null) {
            return false;
        }

        for (int yOffset = 2; yOffset <= 6; yOffset++) {
            BlockPos roofPos = center.above(yOffset);
            BlockState state = level.getBlockState(roofPos);

            if (state.isSolid() || state.is(BlockTags.PLANKS) || state.is(BlockTags.SLABS) || state.is(BlockTags.WOOL)) {
                return true;
            }
        }
        return false;
    }

    public static void processOvergrazing(ServerLevel level, Animal animal) {
        if (level == null || animal == null || !animal.isAlive()) {
            return;
        }

        BlockPos groundPos = animal.blockPosition().below();
        BlockState groundState = level.getBlockState(groundPos);

        if (groundState.is(Blocks.GRASS_BLOCK)) {
            // Check local mob density in immediate 6-block radius
            List<Animal> localHerd = level.getEntitiesOfClass(
                Animal.class,
                animal.getBoundingBox().inflate(4.0, 2.0, 4.0),
                e -> e.isAlive()
            );

            // Overgrazing occurs when 5+ livestock animals crowd on grass
            if (localHerd.size() >= 5) {
                if (localHerd.size() >= 8) {
                    level.setBlockAndUpdate(groundPos, Blocks.COARSE_DIRT.defaultBlockState());
                } else {
                    level.setBlockAndUpdate(groundPos, Blocks.DIRT.defaultBlockState());
                }

                level.sendParticles(
                    ParticleTypes.SMOKE,
                    groundPos.getX() + 0.5, groundPos.getY() + 1.1, groundPos.getZ() + 0.5,
                    4, 0.2, 0.1, 0.2, 0.02
                );
            }
        }
    }

    public static void emitWellNourishedParticles(ServerLevel level, net.minecraft.world.entity.Entity entity) {
        if (level == null || entity == null) {
            return;
        }

        level.sendParticles(
            ParticleTypes.WAX_ON,
            entity.getX(), entity.getY() + 0.5, entity.getZ(),
            4, 0.25, 0.25, 0.25, 0.02
        );
    }
}
