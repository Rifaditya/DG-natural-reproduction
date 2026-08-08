// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.mixin;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;
import net.vanillaoutsider.naturalreproduction.util.AnimalHabitatHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Animal.class)
public abstract class AnimalBreedingMixin {

    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    private void naturalreproduction$onCustomServerAiStep(ServerLevel level, CallbackInfo ci) {
        Animal self = (Animal)(Object)this;

        // Ensure genetics stats are rolled and applied via DasikLibrary API
        if (!DasikAnimalGeneticsAPI.hasGenetics(self)) {
            DasikAnimalGeneticsAPI.rollStats(self, "default");
            GeneticsEngine.applyGeneticsModifiers(self);
        }

        // Autonomous Wild Breeding Logic
        if (!level.isClientSide() && self.getAge() == 0 && !self.isInLove()) {
            if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.ENABLED) && AnimalHabitatHelper.isSpeciesReproductionAllowed(level, self)) {
                int rate = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.RATE);
                if (rate <= 0) {
                    rate = 24000;
                }

                int effectiveRate = rate;
                if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_FERTILITY) && net.vanillaoutsider.naturalreproduction.util.AnimalBiomeHelper.isNativeBiome(level, self)) {
                    effectiveRate = Math.max(100, rate / 2); // 2x faster breeding checks in native biomes
                }

                if (self.getRandom().nextInt(effectiveRate) == 0 && self.getHealth() >= self.getMaxHealth()) {
                    if (AnimalHabitatHelper.hasEnvironmentalBreedingConditions(level, self)) {
                        int densityCap = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DENSITY_CAP);
                        List<Animal> sameSpecies = level.getEntitiesOfClass(
                            Animal.class,
                            self.getBoundingBox().inflate(16.0),
                            e -> e.getType() == self.getType() && e.isAlive()
                        );

                        if (sameSpecies.size() <= densityCap) {
                            List<Animal> potentialMates = level.getEntitiesOfClass(
                                Animal.class,
                                self.getBoundingBox().inflate(8.0),
                                e -> e != self && e.getType() == self.getType() && e.getAge() == 0 && e.isAlive()
                            );

                            if (!potentialMates.isEmpty()) {
                                Animal mate = potentialMates.get(0);
                                self.setInLove(null);
                                mate.setInLove(null);
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "spawnChildFromBreeding", at = @At("TAIL"))
    private void naturalreproduction$onSpawnChildFromBreeding(ServerLevel level, Animal mate, CallbackInfo ci) {
        Animal parent1 = (Animal)(Object)this;
        // Find newest baby animal near parents
        List<AgeableMob> babies = level.getEntitiesOfClass(
            AgeableMob.class,
            parent1.getBoundingBox().inflate(3.0),
            e -> e.getType() == parent1.getType() && e.isBaby()
        );

        for (AgeableMob baby : babies) {
            if (!DasikAnimalGeneticsAPI.hasGenetics(baby)) {
                DasikAnimalGeneticsAPI.inherit(baby, parent1, mate, "default");
                GeneticsEngine.applyGeneticsModifiers(baby);
            }

            if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.CRAMPED_SPACE_PENALTY)) {
                net.vanillaoutsider.naturalreproduction.util.AnimalCrampedSpaceHelper.applyConfinementOrRecovery(level, parent1, mate, baby);
            }

            boolean enableVariants = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_VARIANTS);
            boolean enableFertilityBoost = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_FERTILITY);
            net.vanillaoutsider.naturalreproduction.util.AnimalBiomeHelper.applyBiomeVariantAndBoost(level, parent1, mate, baby, enableVariants, enableFertilityBoost);

            // Log autonomous reproduction event to tracker if manually enabled
            if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.TRACKER_LOGS)) {
                float babyScale = DasikAnimalGeneticsAPI.getScale(baby);
                String biomeId = level.getBiome(baby.blockPosition()).unwrapKey().map(k -> k.identifier().toString()).orElse("unknown");
                String speciesName = baby.getType().getDescription().getString();

                String statusNote = "Standard";
                if (net.vanillaoutsider.naturalreproduction.util.AnimalBiomeHelper.isNativeBiome(level, parent1)) {
                    statusNote = "Native Biome Boost";
                } else if (babyScale <= 0.35f) {
                    statusNote = "Cramped Stunted";
                } else if (babyScale >= 1.20f) {
                    statusNote = "Spacious Pasture";
                }

                net.vanillaoutsider.naturalreproduction.util.BreedingTrackerLogger.logBreeding(
                    level, speciesName, baby.blockPosition(), biomeId, babyScale, statusNote
                );
            }
        }
    }
}

