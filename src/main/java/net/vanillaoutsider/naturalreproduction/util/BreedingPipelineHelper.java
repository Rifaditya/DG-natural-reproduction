// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

public final class BreedingPipelineHelper {

    private BreedingPipelineHelper() {
    }

    /**
     * Consolidates the complete post-birth lifecycle pipeline for newborn livestock:
     * 1. Genetics inheritance & attribute modifier application.
     * 2. Cramped space stunting vs. spacious pasture size recovery.
     * 3. Multi-generational inbreeding tier effects (Tiers 1-4 & lethal collapse).
     * 4. Enriched pasture vitality boost (+10% scale & particles).
     * 5. Biome variant skin adaptation & native fertility quality boost.
     * 6. Newborn ambient audio cue.
     * 7. Autonomous breeding tracker logging.
     *
     * @param level             The active server world level.
     * @param parent1           The primary parent (mother).
     * @param parent2           The secondary parent (father/mate).
     * @param baby              The newborn offspring entity.
     * @param isEnrichedPasture Whether enriched pasture conditions are satisfied.
     */
    public static void finalizeNewborn(ServerLevel level, Animal parent1, Animal parent2, AgeableMob baby, boolean isEnrichedPasture) {
        if (level == null || baby == null) {
            return;
        }

        // 1. Genetics Inheritance
        if (!DasikAnimalGeneticsAPI.hasGenetics(baby) && parent1 != null && parent2 != null) {
            DasikAnimalGeneticsAPI.inherit(baby, parent1, parent2, "default");
            GeneticsEngine.applyGeneticsModifiers(baby);
        }

        // 2. Cramped Space Stunting vs. Spacious Pasture Recovery
        if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.CRAMPED_SPACE_PENALTY) && parent1 != null && parent2 != null) {
            AnimalCrampedSpaceHelper.applyConfinementOrRecovery(level, parent1, parent2, baby);
        }

        // 3. Inbreeding Lineage Degradation
        if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.INBREEDING_DEGRADATION) && parent1 != null && parent2 != null) {
            AnimalLineageHelper.applyLineageEffects(level, parent1, parent2, baby);
        }

        // 4. Pasture Enrichment Vitality Boost
        if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.PASTURE_ENRICHMENT) && isEnrichedPasture) {
            float currentScale = DasikAnimalGeneticsAPI.getScale(baby);
            float minAllowed = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.MIN_SCALE) / 100.0f;
            float maxAllowed = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.MAX_SCALE) / 100.0f;
            if (minAllowed <= 0) minAllowed = 0.10f;
            if (maxAllowed <= 0) maxAllowed = 1.20f;
            float boostedScale = Math.clamp(currentScale * 1.10f, minAllowed, maxAllowed);
            DasikAnimalGeneticsAPI.setScale(baby, boostedScale);
            AnimalPastureHelper.emitWellNourishedParticles(level, baby);
        }

        // 5. Biome Variant Adaptation & Climate Boost
        if (parent1 != null && parent2 != null) {
            boolean enableVariants = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_VARIANTS);
            boolean enableFertilityBoost = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_FERTILITY);
            AnimalBiomeHelper.applyBiomeVariantAndBoost(level, parent1, parent2, baby, enableVariants, enableFertilityBoost);
        }

        // 6. Native Birth Audio Feedback
        baby.playAmbientSound();

        // 7. Breeding Tracker Logging
        if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.TRACKER_LOGS)) {
            logBirthEvent(level, parent1, baby, isEnrichedPasture);
        }
    }

    private static void logBirthEvent(ServerLevel level, Animal parent1, AgeableMob baby, boolean isEnrichedPasture) {
        float babyScale = DasikAnimalGeneticsAPI.getScale(baby);
        String biomeId = level.getBiome(baby.blockPosition()).unwrapKey().map(k -> k.identifier().toString()).orElse("unknown");
        String speciesName = baby.getType().getDescription().getString();
        int inbreedingTier = AnimalLineageHelper.getInbreedingTier(baby);

        String statusNote = "Standard";
        if (inbreedingTier >= 4) {
            statusNote = "Lethal Collapse (T4)";
        } else if (inbreedingTier == 3) {
            statusNote = "Degraded Meat (T3)";
        } else if (inbreedingTier > 0) {
            statusNote = "Inbred Tier " + inbreedingTier;
        } else if (isEnrichedPasture) {
            statusNote = "Enriched Pasture";
        } else if (parent1 != null && AnimalBiomeHelper.isNativeBiome(level, parent1)) {
            statusNote = "Native Biome Boost";
        } else if (babyScale <= 0.35f) {
            statusNote = "Cramped Stunted";
        } else if (babyScale >= 1.20f) {
            statusNote = "Spacious Pasture";
        }

        BreedingTrackerLogger.logBreeding(
            level, speciesName, baby.blockPosition(), biomeId, babyScale, statusNote
        );
    }
}
