// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

import java.util.UUID;

public final class AnimalGestationHelper {

    public static final String FATHER_TAG_PREFIX = "nr_father:";
    public static final String TRAIT_FATHER_SCALE = "father_scale";
    public static final String TRAIT_FATHER_INBREEDING_TIER = "father_inbreeding_tier";

    private AnimalGestationHelper() {
    }

    public static void startGestation(ServerLevel level, Animal mother, Animal father, int durationTicks) {
        if (mother == null || level == null) {
            return;
        }

        int safeDuration = Math.max(100, durationTicks);
        DasikAnimalGeneticsAPI.setTrait(mother, "gestation_ticks", (float)safeDuration);
        DasikAnimalGeneticsAPI.setTrait(mother, "prenatal_nourishment", 0.0f);
        DasikAnimalGeneticsAPI.setTrait(mother, "prenatal_checks", 0.0f);

        // Clear any previous father tags
        mother.entityTags().removeIf(t -> t.startsWith(FATHER_TAG_PREFIX));

        if (father != null) {
            mother.addTag(FATHER_TAG_PREFIX + father.getStringUUID());
            DasikAnimalGeneticsAPI.setTrait(mother, TRAIT_FATHER_SCALE, DasikAnimalGeneticsAPI.getScale(father));
            DasikAnimalGeneticsAPI.setTrait(mother, TRAIT_FATHER_INBREEDING_TIER, (float)AnimalLineageHelper.getInbreedingTier(father));
        }

        level.sendParticles(
            ParticleTypes.HEART,
            mother.getX(), mother.getY() + 0.5, mother.getZ(),
            5, 0.25, 0.25, 0.25, 0.02
        );
    }

    public static boolean isPregnant(Animal animal) {
        if (animal == null) {
            return false;
        }
        return DasikAnimalGeneticsAPI.getTrait(animal, "gestation_ticks", 0.0f) > 0.0f;
    }

    public static void tickGestation(ServerLevel level, Animal mother) {
        if (level == null || mother == null || !mother.isAlive()) {
            return;
        }

        float ticksRemaining = DasikAnimalGeneticsAPI.getTrait(mother, "gestation_ticks", 0.0f);
        if (ticksRemaining <= 0.0f) {
            return;
        }

        ticksRemaining -= 1.0f;
        DasikAnimalGeneticsAPI.setTrait(mother, "gestation_ticks", ticksRemaining);

        // Staggered prenatal pasture quality check every 100 ticks
        if ((mother.getId() + level.getGameTime()) % 100 == 0) {
            boolean enriched = AnimalPastureHelper.isPastureEnriched(level, mother.blockPosition());
            float checks = DasikAnimalGeneticsAPI.getTrait(mother, "prenatal_checks", 0.0f) + 1.0f;
            float nourish = DasikAnimalGeneticsAPI.getTrait(mother, "prenatal_nourishment", 0.0f) + (enriched ? 1.0f : 0.0f);

            DasikAnimalGeneticsAPI.setTrait(mother, "prenatal_checks", checks);
            DasikAnimalGeneticsAPI.setTrait(mother, "prenatal_nourishment", nourish);

            level.sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                mother.getX(), mother.getY() + 0.4, mother.getZ(),
                2, 0.2, 0.2, 0.2, 0.01
            );
        }

        if (ticksRemaining <= 0.0f) {
            deliverOffspringOrEgg(level, mother);
        }
    }

    public static Animal resolveFather(ServerLevel level, Animal mother) {
        if (level == null || mother == null) {
            return null;
        }

        for (String tag : mother.entityTags()) {
            if (tag.startsWith(FATHER_TAG_PREFIX)) {
                try {
                    UUID fatherUuid = UUID.fromString(tag.substring(FATHER_TAG_PREFIX.length()));
                    var entity = level.getEntity(fatherUuid);
                    if (entity instanceof Animal animal) {
                        return animal;
                    }

                    // Reconstruct synthetic parent surrogate with preserved UUID & snapshotted traits
                    Animal surrogate = (Animal)mother.getType().create(level, EntitySpawnReason.BREEDING);
                    if (surrogate != null) {
                        surrogate.setUUID(fatherUuid);
                        float snapScale = DasikAnimalGeneticsAPI.getTrait(mother, TRAIT_FATHER_SCALE, 1.0f);
                        float snapTier = DasikAnimalGeneticsAPI.getTrait(mother, TRAIT_FATHER_INBREEDING_TIER, 0.0f);
                        DasikAnimalGeneticsAPI.setScale(surrogate, snapScale);
                        DasikAnimalGeneticsAPI.setTrait(surrogate, AnimalLineageHelper.TRAIT_INBREEDING_TIER, snapTier);
                        return surrogate;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    public static void deliverOffspringOrEgg(ServerLevel level, Animal mother) {
        if (level == null || mother == null) {
            return;
        }

        BlockPos pos = mother.blockPosition();
        Animal father = resolveFather(level, mother);

        float checks = DasikAnimalGeneticsAPI.getTrait(mother, "prenatal_checks", 1.0f);
        float nourish = DasikAnimalGeneticsAPI.getTrait(mother, "prenatal_nourishment", 0.0f);
        boolean highVitality = (nourish / Math.max(1.0f, checks)) >= 0.50f;
        boolean isEnriched = highVitality || (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.PASTURE_ENRICHMENT)
            && AnimalPastureHelper.isPastureEnriched(level, pos));

        // Check for Oviparous native egg laying
        if (mother.getType() == EntityTypes.FROG) {
            BlockPos waterSurface = pos;
            if (level.getBlockState(pos).is(Blocks.WATER) && level.getBlockState(pos.above()).isAir()) {
                waterSurface = pos.above();
            }
            if (level.getBlockState(waterSurface).isAir()) {
                level.setBlockAndUpdate(waterSurface, Blocks.FROGSPAWN.defaultBlockState());
            } else {
                mother.spawnAtLocation(level, new ItemStack(Items.SLIME_BALL));
            }
        } else if (mother.getType() == EntityTypes.TURTLE) {
            BlockPos ground = pos.below();
            if (level.getBlockState(ground).is(Blocks.SAND) && level.getBlockState(pos).isAir()) {
                level.setBlockAndUpdate(pos, Blocks.TURTLE_EGG.defaultBlockState());
            } else {
                mother.spawnAtLocation(level, new ItemStack(Items.TURTLE_SCUTE));
            }
        } else if (mother.getType() == EntityTypes.SNIFFER) {
            if (level.getBlockState(pos).isAir()) {
                level.setBlockAndUpdate(pos, Blocks.SNIFFER_EGG.defaultBlockState());
            } else {
                mother.spawnAtLocation(level, new ItemStack(Items.TORCHFLOWER_SEEDS));
            }
        } else if (mother.getType() == EntityTypes.CHICKEN) {
            boolean useFertilizedEggs = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS);
            if (useFertilizedEggs && mother.getRandom().nextBoolean()) {
                // 50% chance: Lay a Fertilized Egg item
                mother.spawnAtLocation(level, ChickenEggHelper.createFertilizedEgg(level, mother, father));
            } else {
                // 50% chance: Spawn baby chick directly
                AgeableMob chick = (AgeableMob)EntityTypes.CHICKEN.create(level, EntitySpawnReason.BREEDING);
                if (chick != null) {
                    chick.setBaby(true);
                    chick.setPos(mother.getX(), mother.getY(), mother.getZ());
                    level.addFreshEntity(chick);
                    BreedingPipelineHelper.finalizeNewborn(level, mother, father, chick, isEnriched);
                }
            }
        } else {
            // Viviparous Mammals (Cow, Pig, Sheep, Horse, Wolf, Goat, etc.)
            AgeableMob baby = (AgeableMob)mother.getType().create(level, EntitySpawnReason.BREEDING);
            if (baby != null) {
                baby.setBaby(true);
                baby.setPos(mother.getX(), mother.getY(), mother.getZ());
                level.addFreshEntity(baby);
                BreedingPipelineHelper.finalizeNewborn(level, mother, father, baby, isEnriched);
            }
        }

        // Reset mother gestation state and clean up father tracking tag
        DasikAnimalGeneticsAPI.setTrait(mother, "gestation_ticks", 0.0f);
        DasikAnimalGeneticsAPI.setTrait(mother, "prenatal_nourishment", 0.0f);
        DasikAnimalGeneticsAPI.setTrait(mother, "prenatal_checks", 0.0f);
        DasikAnimalGeneticsAPI.setTrait(mother, TRAIT_FATHER_SCALE, 0.0f);
        DasikAnimalGeneticsAPI.setTrait(mother, TRAIT_FATHER_INBREEDING_TIER, 0.0f);
        mother.entityTags().removeIf(t -> t.startsWith(FATHER_TAG_PREFIX));

        level.sendParticles(
            ParticleTypes.HAPPY_VILLAGER,
            mother.getX(), mother.getY() + 0.5, mother.getZ(),
            5, 0.3, 0.3, 0.3, 0.02
        );
    }
}
