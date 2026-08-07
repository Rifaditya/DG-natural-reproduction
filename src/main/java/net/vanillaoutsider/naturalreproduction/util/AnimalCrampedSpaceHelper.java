// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;

import java.util.List;

public final class AnimalCrampedSpaceHelper {

    private AnimalCrampedSpaceHelper() {
    }

    public static void applyConfinementOrRecovery(ServerLevel level, Animal parent1, Animal parent2, AgeableMob baby) {
        if (level == null || parent1 == null || parent2 == null || baby == null) {
            return;
        }

        // Count same-species animals in immediate 4x4 area (5x3x5 bounding box)
        List<Animal> localMobs = level.getEntitiesOfClass(
            Animal.class,
            baby.getBoundingBox().inflate(2.5, 1.5, 2.5),
            e -> e.getType() == baby.getType() && e.isAlive()
        );

        int localCount = localMobs.size();
        float currentScale = DasikAnimalGeneticsAPI.getScale(baby);

        if (localCount >= 4) {
            // Cramped Space Penalty: gradual scale stunting based on density
            float penaltyMultiplier;
            if (localCount <= 4) {
                penaltyMultiplier = 0.85f;
            } else if (localCount <= 6) {
                penaltyMultiplier = 0.65f;
            } else if (localCount <= 8) {
                penaltyMultiplier = 0.45f;
            } else {
                penaltyMultiplier = 0.30f;
            }

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
        } else if (localCount <= 2) {
            // Spacious Recovery: if parents or baby are stunted (<1.0f), boost scale towards normal/giant potential
            float parent1Scale = DasikAnimalGeneticsAPI.getScale(parent1);
            float parent2Scale = DasikAnimalGeneticsAPI.getScale(parent2);
            float avgParentScale = (parent1Scale + parent2Scale) / 2.0f;

            if (avgParentScale < 1.0f || currentScale < 1.0f) {
                float recoveryBoost = 1.30f;
                float newScale = Math.clamp(currentScale * recoveryBoost, 0.25f, 1.30f);
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
}
