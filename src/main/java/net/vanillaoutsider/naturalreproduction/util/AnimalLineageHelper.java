// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;

public final class AnimalLineageHelper {

    public static final String TRAIT_INBREEDING_TIER = "inbreeding_tier";
    public static final Identifier INBREEDING_SPEED_MODIFIER = Identifier.fromNamespaceAndPath("natural-reproduction", "inbreeding_speed");

    private AnimalLineageHelper() {
    }

    public static int getInbreedingTier(LivingEntity entity) {
        if (entity == null) {
            return 0;
        }
        float traitVal = DasikAnimalGeneticsAPI.getTrait(entity, TRAIT_INBREEDING_TIER, 0.0f);
        return Math.clamp(Math.round(traitVal), 0, 4);
    }

    public static int computeInbreedingTier(ServerLevel level, Animal parent1, Animal parent2) {
        if (parent1 == null || parent2 == null) {
            return 0;
        }

        int p1Tier = getInbreedingTier(parent1);
        int p2Tier = getInbreedingTier(parent2);

        boolean inbred = DasikAnimalGeneticsAPI.isRelated(parent1, parent2)
            || DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(parent1, parent2) > 0;

        if (inbred) {
            // Inbreeding cross: increment tier from highest parent, capped at Tier 4
            int maxTier = Math.max(p1Tier, p2Tier);
            return Math.clamp(maxTier + 1, 1, 4);
        } else {
            // Outcrossing cross: Gradual Generational Dilution (-1 tier step per generation)
            int maxTier = Math.max(p1Tier, p2Tier);
            return Math.max(0, maxTier - 1);
        }
    }

    public static void applyLineageEffects(ServerLevel level, Animal parent1, Animal parent2, AgeableMob baby) {
        if (level == null || parent1 == null || parent2 == null || baby == null) {
            return;
        }

        int p1Tier = getInbreedingTier(parent1);
        int p2Tier = getInbreedingTier(parent2);
        int tier = computeInbreedingTier(level, parent1, parent2);

        DasikAnimalGeneticsAPI.setTrait(baby, TRAIT_INBREEDING_TIER, (float) tier);

        if (tier == 0) {
            // Check if Tier 0 was achieved via Outcrossing Dilution from degraded lineage -> Hybrid Vigor
            if (p1Tier > 0 || p2Tier > 0) {
                float currentScale = DasikAnimalGeneticsAPI.getScale(baby);
                float boostedScale = Math.clamp(currentScale * 1.15f, 0.25f, 1.30f);
                DasikAnimalGeneticsAPI.setScale(baby, boostedScale);

                level.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    baby.getX(), baby.getY() + 0.5, baby.getZ(),
                    8, 0.3, 0.3, 0.3, 0.02
                );
            }
        } else if (tier == 1) {
            // Tier 1: Mild stunting (-10% scale), birth smoke puff
            float currentScale = DasikAnimalGeneticsAPI.getScale(baby);
            float stuntedScale = Math.clamp(currentScale * 0.90f, 0.25f, 1.30f);
            DasikAnimalGeneticsAPI.setScale(baby, stuntedScale);

            level.sendParticles(
                ParticleTypes.SMOKE,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                5, 0.2, 0.2, 0.2, 0.02
            );
        } else if (tier == 2) {
            // Tier 2: Moderate stunting (-25% scale), -20% speed, smoke & angry villager puff
            float currentScale = DasikAnimalGeneticsAPI.getScale(baby);
            float stuntedScale = Math.clamp(currentScale * 0.75f, 0.25f, 1.30f);
            DasikAnimalGeneticsAPI.setScale(baby, stuntedScale);

            applySpeedPenalty(baby, -0.20f);

            level.sendParticles(
                ParticleTypes.SMOKE,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                10, 0.25, 0.25, 0.25, 0.02
            );
            level.sendParticles(
                ParticleTypes.ANGRY_VILLAGER,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                1, 0.2, 0.2, 0.2, 0.02
            );
        } else if (tier == 3) {
            // Tier 3: Severe degradation (0.35x miniature scale), -30% speed, squid ink & angry villager
            DasikAnimalGeneticsAPI.setScale(baby, 0.35f);
            applySpeedPenalty(baby, -0.30f);

            level.sendParticles(
                ParticleTypes.SQUID_INK,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                12, 0.3, 0.3, 0.3, 0.02
            );
            level.sendParticles(
                ParticleTypes.ANGRY_VILLAGER,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                3, 0.2, 0.2, 0.2, 0.02
            );
        } else if (tier >= 4) {
            // Tier 4: Lethal genetic collapse (0.25x scale), -50% speed, heavy squid ink & smoke
            DasikAnimalGeneticsAPI.setScale(baby, 0.25f);
            applySpeedPenalty(baby, -0.50f);

            level.sendParticles(
                ParticleTypes.SQUID_INK,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                20, 0.4, 0.4, 0.4, 0.02
            );
            level.sendParticles(
                ParticleTypes.ANGRY_VILLAGER,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                5, 0.3, 0.3, 0.3, 0.02
            );
            level.sendParticles(
                ParticleTypes.SMOKE,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                15, 0.3, 0.3, 0.3, 0.02
            );
        }
    }

    public static void tickLethalCollapse(ServerLevel level, Animal animal) {
        if (level == null || animal == null || !animal.isAlive()) {
            return;
        }

        if (getInbreedingTier(animal) >= 4) {
            // Every 20 ticks (1 second), deal 1.0 damage until death
            if ((animal.getId() + level.getGameTime()) % 20 == 0) {
                animal.hurtServer(level, level.damageSources().starve(), 1.0f);

                level.sendParticles(
                    ParticleTypes.SQUID_INK,
                    animal.getX(), animal.getY() + 0.3, animal.getZ(),
                    3, 0.2, 0.2, 0.2, 0.02
                );
                level.sendParticles(
                    ParticleTypes.SMOKE,
                    animal.getX(), animal.getY() + 0.3, animal.getZ(),
                    2, 0.2, 0.2, 0.2, 0.02
                );
            }
        }
    }

    private static void applySpeedPenalty(LivingEntity entity, float modifierValue) {
        var speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) {
            speedAttr.removeModifier(INBREEDING_SPEED_MODIFIER);
            speedAttr.addPermanentModifier(new AttributeModifier(
                INBREEDING_SPEED_MODIFIER,
                modifierValue,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }
    }
}
