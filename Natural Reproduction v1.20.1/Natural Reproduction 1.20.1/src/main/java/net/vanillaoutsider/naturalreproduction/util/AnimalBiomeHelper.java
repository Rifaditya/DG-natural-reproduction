// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 1.20.1
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.Optional;

public final class AnimalBiomeHelper {

    private AnimalBiomeHelper() {
    }

    public static boolean isNativeBiome(ServerLevel level, Animal animal) {
        if (level == null || animal == null) {
            return false;
        }

        Holder<Biome> biomeHolder = level.getBiome(animal.blockPosition());
        Optional<ResourceKey<Biome>> biomeKey = biomeHolder.unwrapKey();
        if (biomeKey.isEmpty()) {
            return false;
        }

        ResourceKey<Biome> key = biomeKey.get();

        var type = animal.getType();

        // 1. Canines & Felines
        if (animal instanceof Wolf) {
            return key.equals(Biomes.TAIGA) || key.equals(Biomes.SNOWY_TAIGA) || key.equals(Biomes.OLD_GROWTH_PINE_TAIGA) || key.equals(Biomes.OLD_GROWTH_SPRUCE_TAIGA) || key.equals(Biomes.GROVE);
        } else if (type == EntityType.FOX) {
            return key.equals(Biomes.TAIGA) || key.equals(Biomes.SNOWY_TAIGA) || key.equals(Biomes.OLD_GROWTH_PINE_TAIGA) || key.equals(Biomes.OLD_GROWTH_SPRUCE_TAIGA) || key.equals(Biomes.GROVE);
        } else if (type == EntityType.OCELOT) {
            return key.equals(Biomes.JUNGLE) || key.equals(Biomes.BAMBOO_JUNGLE) || key.equals(Biomes.SPARSE_JUNGLE);
        } else if (type == EntityType.CAT) {
            return key.equals(Biomes.PLAINS) || key.equals(Biomes.DESERT) || key.equals(Biomes.SAVANNA) || key.equals(Biomes.TAIGA) || key.equals(Biomes.MEADOW);

        // 2. Amphibians & Cold Climate Fauna
        } else if (animal instanceof Frog) {
            return key.equals(Biomes.SWAMP) || key.equals(Biomes.MANGROVE_SWAMP);
        } else if (animal instanceof PolarBear) {
            return key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.ICE_SPIKES) || key.equals(Biomes.FROZEN_OCEAN);
        } else if (animal instanceof Rabbit) {
            return key.equals(Biomes.DESERT) || key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.FLOWER_FOREST);

        // 3. Arid & Mountain Animals
        } else if (type == EntityType.CAMEL) {
            return key.equals(Biomes.DESERT);
        } else if (type == EntityType.GOAT) {
            return key.equals(Biomes.JAGGED_PEAKS) || key.equals(Biomes.FROZEN_PEAKS) || key.equals(Biomes.STONY_PEAKS) || key.equals(Biomes.SNOWY_SLOPES);
        } else if (type == EntityType.LLAMA || type == EntityType.TRADER_LLAMA) {
            return key.equals(Biomes.WINDSWEPT_HILLS) || key.equals(Biomes.WINDSWEPT_GRAVELLY_HILLS) || key.equals(Biomes.WINDSWEPT_FOREST) || key.equals(Biomes.SAVANNA);

        // 4. Jungle & Forest Specialists
        } else if (type == EntityType.PANDA || type == EntityType.PARROT) {
            return key.equals(Biomes.JUNGLE) || key.equals(Biomes.BAMBOO_JUNGLE) || key.equals(Biomes.SPARSE_JUNGLE);
        } else if (type == EntityType.BEE) {
            return key.equals(Biomes.MEADOW) || key.equals(Biomes.FLOWER_FOREST) || key.equals(Biomes.PLAINS) || key.equals(Biomes.SUNFLOWER_PLAINS) || key.equals(Biomes.FOREST) || key.equals(Biomes.BIRCH_FOREST);

        // 5. Equines & Traditional Pasture Livestock
        } else if (animal instanceof Cow || animal instanceof Sheep || animal instanceof Pig || animal instanceof Chicken) {
            return key.equals(Biomes.PLAINS) || key.equals(Biomes.MEADOW) || key.equals(Biomes.SUNFLOWER_PLAINS) || key.equals(Biomes.SAVANNA);
        } else if (type == EntityType.HORSE || type == EntityType.DONKEY || type == EntityType.MULE) {
            return key.equals(Biomes.PLAINS) || key.equals(Biomes.SAVANNA) || key.equals(Biomes.MEADOW) || key.equals(Biomes.SUNFLOWER_PLAINS);

        // 6. Aquatic, Wetland & Ancient Animals
        } else if (type == EntityType.MOOSHROOM) {
            return key.equals(Biomes.MUSHROOM_FIELDS);
        } else if (type == EntityType.TURTLE) {
            return key.equals(Biomes.BEACH) || key.equals(Biomes.STONY_SHORE);
        } else if (type == EntityType.AXOLOTL) {
            return key.equals(Biomes.LUSH_CAVES);
        } else if (type == EntityType.SNIFFER) {
            return key.equals(Biomes.MEADOW) || key.equals(Biomes.PLAINS) || key.equals(Biomes.LUSH_CAVES);

        // 7. Nether Animals
        } else if (type == EntityType.STRIDER) {
            return key.equals(Biomes.NETHER_WASTES) || key.equals(Biomes.CRIMSON_FOREST) || key.equals(Biomes.WARPED_FOREST) || key.equals(Biomes.BASALT_DELTAS);
        } else if (type == EntityType.HOGLIN) {
            return key.equals(Biomes.CRIMSON_FOREST);
        }

        return false;
    }

    public static void applyBiomeVariantAndBoost(ServerLevel level, Animal parent1, Animal parent2, AgeableMob baby, boolean enableVariants, boolean enableFertilityBoost) {
        if (level == null || baby == null) {
            return;
        }

        Holder<Biome> biomeHolder = level.getBiome(baby.blockPosition());
        Optional<ResourceKey<Biome>> biomeKey = biomeHolder.unwrapKey();
        boolean nativeBiome = isNativeBiome(level, parent1);

        // 1. Biome Climate Genetics Quality Boost
        if (enableFertilityBoost && nativeBiome) {
            float currentScale = DasikAnimalGeneticsAPI.getScale(baby);
            float minAllowed = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.MIN_SCALE) / 100.0f;
            float maxAllowed = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.MAX_SCALE) / 100.0f;
            if (minAllowed <= 0) minAllowed = 0.10f;
            if (maxAllowed <= 0) maxAllowed = 1.20f;
            float boostedScale = Mth.clamp(currentScale * 1.15f, minAllowed, maxAllowed);
            DasikAnimalGeneticsAPI.setScale(baby, boostedScale);

            level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                5, 0.3, 0.3, 0.3, 0.02
            );
        }

        // 2. Biome Variant Skin Adaptation
        if (enableVariants && biomeKey.isPresent()) {
            ResourceKey<Biome> key = biomeKey.get();

            // Frog Biome Variant Adaptation
            if (baby instanceof Frog frog) {
                if (key.equals(Biomes.SWAMP) || key.equals(Biomes.MANGROVE_SWAMP)) {
                    frog.setVariant(FrogVariant.TEMPERATE);
                } else if (key.equals(Biomes.DESERT) || key.equals(Biomes.JUNGLE) || key.equals(Biomes.SAVANNA)) {
                    frog.setVariant(FrogVariant.WARM);
                } else if (key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.ICE_SPIKES) || key.equals(Biomes.FROZEN_PEAKS)) {
                    frog.setVariant(FrogVariant.COLD);
                }
            }

            // Rabbit Biome Variant Adaptation
            if (baby instanceof Rabbit rabbit) {
                if (key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.ICE_SPIKES)) {
                    rabbit.setVariant(Rabbit.Variant.WHITE);
                } else if (key.equals(Biomes.DESERT)) {
                    rabbit.setVariant(Rabbit.Variant.GOLD);
                }
            }
        }
    }
}
