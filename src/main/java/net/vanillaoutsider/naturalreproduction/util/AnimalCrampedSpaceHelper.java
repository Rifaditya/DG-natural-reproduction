// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class AnimalCrampedSpaceHelper {

    private AnimalCrampedSpaceHelper() {
    }

    public static void applyConfinementOrRecovery(ServerLevel level, Animal parent1, Animal parent2, AgeableMob baby) {
        if (level == null || parent1 == null || parent2 == null || baby == null) {
            return;
        }

        // Count same-species EXTRA animals in immediate 4x4 area (excluding baby, parent1, parent2)
        List<Animal> localMobs = level.getEntitiesOfClass(
            Animal.class,
            baby.getBoundingBox().inflate(2.5, 1.5, 2.5),
            e -> e.getType() == baby.getType() && e.isAlive()
                && !e.getUUID().equals(parent1.getUUID())
                && !e.getUUID().equals(parent2.getUUID())
                && !e.getUUID().equals(baby.getUUID())
        );

        int extraLocalCount = localMobs.size();
        boolean isConfinedPen = isConfinedArea(level, baby.blockPosition());
        float currentScale = DasikAnimalGeneticsAPI.getScale(baby);
        boolean isOvercrowded = isConfinedPen || extraLocalCount >= 3;

        if (isOvercrowded && extraLocalCount >= 1) {
            // Cramped Space Penalty: smooth gradual scale stunting (-5% per extra local mob)
            float penaltyMultiplier = Math.max(0.95f - (extraLocalCount * 0.05f), 0.40f);
            float newScale = Math.clamp(currentScale * penaltyMultiplier, 0.25f, 2.0f);
            DasikAnimalGeneticsAPI.setScale(baby, newScale);

            // Visual particle feedback for cramped stunting
            level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.SMOKE,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                5, 0.2, 0.2, 0.2, 0.02
            );
            level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.ANGRY_VILLAGER,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                2, 0.2, 0.2, 0.2, 0.02
            );
        } else {
            // Open Pasture / Spacious Recovery: boost scale towards MAX_SCALE (default 1.30x)
            float parent1Scale = DasikAnimalGeneticsAPI.getScale(parent1);
            float parent2Scale = DasikAnimalGeneticsAPI.getScale(parent2);
            float avgParentScale = (parent1Scale + parent2Scale) / 2.0f;

            float maxAllowed = net.dasik.social.api.gamerule.DynamicGameRuleManager.getInt(level, net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric.MAX_SCALE) / 100.0f;

            if (avgParentScale < maxAllowed || currentScale < maxAllowed) {
                float recoveryBoost = 1.15f;
                float newScale = Math.clamp(currentScale * recoveryBoost, 0.25f, maxAllowed);
                DasikAnimalGeneticsAPI.setScale(baby, newScale);

                // Visual particle feedback for spacious pasture size recovery
                level.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                    baby.getX(), baby.getY() + 0.5, baby.getZ(),
                    7, 0.3, 0.3, 0.3, 0.02
                );
            }
        }
    }

    /**
     * Universal confinement check: detects 1x1 / 2x2 pit holes, fences, walls, trapdoors, and solid block enclosures.
     */
    public static boolean isConfinedArea(ServerLevel level, BlockPos center) {
        int blockedDirections = 0;

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos feetPos = center.relative(dir);
            BlockPos eyePos = center.above().relative(dir);

            BlockState feetState = level.getBlockState(feetPos);
            BlockState eyeState = level.getBlockState(eyePos);

            if (isObstacle(feetState) || isObstacle(eyeState)) {
                blockedDirections++;
            }
        }

        // Confined if 3 or 4 horizontal sides are blocked by solid blocks/fences/walls (e.g. 1x1/2x2 pit holes or tight pens)
        return blockedDirections >= 3;
    }

    private static boolean isObstacle(BlockState state) {
        if (state.isAir()) {
            return false;
        }
        return state.is(BlockTags.FENCES)
            || state.is(BlockTags.WALLS)
            || state.is(BlockTags.TRAPDOORS)
            || state.isSolid();
    }
}
