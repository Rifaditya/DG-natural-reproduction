// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
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
        } else if (type == EntityTypes.FOX) {
            return key.equals(Biomes.TAIGA) || key.equals(Biomes.SNOWY_TAIGA) || key.equals(Biomes.OLD_GROWTH_PINE_TAIGA) || key.equals(Biomes.OLD_GROWTH_SPRUCE_TAIGA) || key.equals(Biomes.GROVE);
        } else if (type == EntityTypes.OCELOT) {
            return key.equals(Biomes.JUNGLE) || key.equals(Biomes.BAMBOO_JUNGLE) || key.equals(Biomes.SPARSE_JUNGLE);
        } else if (type == EntityTypes.CAT) {
            return key.equals(Biomes.PLAINS) || key.equals(Biomes.DESERT) || key.equals(Biomes.SAVANNA) || key.equals(Biomes.TAIGA) || key.equals(Biomes.MEADOW);

        // 2. Amphibians & Cold Climate Fauna
        } else if (animal instanceof Frog) {
            return key.equals(Biomes.SWAMP) || key.equals(Biomes.MANGROVE_SWAMP);
        } else if (animal instanceof PolarBear) {
            return key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.ICE_SPIKES) || key.equals(Biomes.FROZEN_OCEAN);
        } else if (animal instanceof Rabbit) {
            return key.equals(Biomes.DESERT) || key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.FLOWER_FOREST);

        // 3. Arid & Mountain Animals
        } else if (type == EntityTypes.CAMEL) {
            return key.equals(Biomes.DESERT);
        } else if (type == EntityTypes.ARMADILLO) {
            return key.equals(Biomes.SAVANNA) || key.equals(Biomes.SAVANNA_PLATEAU) || key.equals(Biomes.WINDSWEPT_SAVANNA) || key.equals(Biomes.BADLANDS) || key.equals(Biomes.ERODED_BADLANDS) || key.equals(Biomes.WOODED_BADLANDS);
        } else if (type == EntityTypes.GOAT) {
            return key.equals(Biomes.JAGGED_PEAKS) || key.equals(Biomes.FROZEN_PEAKS) || key.equals(Biomes.STONY_PEAKS) || key.equals(Biomes.SNOWY_SLOPES);
        } else if (type == EntityTypes.LLAMA || type == EntityTypes.TRADER_LLAMA) {
            return key.equals(Biomes.WINDSWEPT_HILLS) || key.equals(Biomes.WINDSWEPT_GRAVELLY_HILLS) || key.equals(Biomes.WINDSWEPT_FOREST) || key.equals(Biomes.SAVANNA);

        // 4. Jungle & Forest Specialists
        } else if (type == EntityTypes.PANDA || type == EntityTypes.PARROT) {
            return key.equals(Biomes.JUNGLE) || key.equals(Biomes.BAMBOO_JUNGLE) || key.equals(Biomes.SPARSE_JUNGLE);
        } else if (type == EntityTypes.BEE) {
            return key.equals(Biomes.MEADOW) || key.equals(Biomes.FLOWER_FOREST) || key.equals(Biomes.PLAINS) || key.equals(Biomes.SUNFLOWER_PLAINS) || key.equals(Biomes.FOREST) || key.equals(Biomes.BIRCH_FOREST);

        // 5. Equines & Traditional Pasture Livestock
        } else if (animal instanceof Cow || animal instanceof Sheep || animal instanceof Pig || animal instanceof Chicken) {
            return key.equals(Biomes.PLAINS) || key.equals(Biomes.MEADOW) || key.equals(Biomes.SUNFLOWER_PLAINS) || key.equals(Biomes.SAVANNA);
        } else if (type == EntityTypes.HORSE || type == EntityTypes.DONKEY || type == EntityTypes.MULE) {
            return key.equals(Biomes.PLAINS) || key.equals(Biomes.SAVANNA) || key.equals(Biomes.MEADOW) || key.equals(Biomes.SUNFLOWER_PLAINS);

        // 6. Aquatic, Wetland & Ancient Animals
        } else if (type == EntityTypes.MOOSHROOM) {
            return key.equals(Biomes.MUSHROOM_FIELDS);
        } else if (type == EntityTypes.TURTLE) {
            return key.equals(Biomes.BEACH) || key.equals(Biomes.STONY_SHORE);
        } else if (type == EntityTypes.AXOLOTL) {
            return key.equals(Biomes.LUSH_CAVES);
        } else if (type == EntityTypes.SNIFFER) {
            return key.equals(Biomes.MEADOW) || key.equals(Biomes.PLAINS) || key.equals(Biomes.LUSH_CAVES);

        // 7. Nether Animals
        } else if (type == EntityTypes.STRIDER) {
            return key.equals(Biomes.NETHER_WASTES) || key.equals(Biomes.CRIMSON_FOREST) || key.equals(Biomes.WARPED_FOREST) || key.equals(Biomes.BASALT_DELTAS);
        } else if (type == EntityTypes.HOGLIN) {
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
            float boostedScale = Math.clamp(currentScale * 1.15f, minAllowed, maxAllowed);
            DasikAnimalGeneticsAPI.setScale(baby, boostedScale);

            level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                baby.getX(), baby.getY() + 0.5, baby.getZ(),
                5, 0.3, 0.3, 0.3, 0.02
            );
        }

        // 2. Biome Variant Skin Adaptation via 26.2 DataComponents API
        if (enableVariants && biomeKey.isPresent()) {
            ResourceKey<Biome> key = biomeKey.get();

            // Frog Biome Variant Adaptation
            if (baby instanceof Frog frog) {
                if (key.equals(Biomes.SWAMP) || key.equals(Biomes.MANGROVE_SWAMP)) {
                    setFrogVariant(level, frog, FrogVariants.TEMPERATE);
                } else if (key.equals(Biomes.DESERT) || key.equals(Biomes.JUNGLE) || key.equals(Biomes.SAVANNA)) {
                    setFrogVariant(level, frog, FrogVariants.WARM);
                } else if (key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.ICE_SPIKES) || key.equals(Biomes.FROZEN_PEAKS)) {
                    setFrogVariant(level, frog, FrogVariants.COLD);
                }
            }

            // Wolf Biome Variant Adaptation
            if (baby instanceof Wolf wolf) {
                if (key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.ICE_SPIKES)) {
                    setWolfVariant(level, wolf, WolfVariants.SNOWY);
                } else if (key.equals(Biomes.OLD_GROWTH_PINE_TAIGA) || key.equals(Biomes.OLD_GROWTH_SPRUCE_TAIGA)) {
                    setWolfVariant(level, wolf, WolfVariants.BLACK);
                } else if (key.equals(Biomes.TAIGA)) {
                    setWolfVariant(level, wolf, WolfVariants.PALE);
                }
            }

            // Rabbit Biome Variant Adaptation
            if (baby instanceof Rabbit rabbit) {
                if (key.equals(Biomes.SNOWY_PLAINS) || key.equals(Biomes.ICE_SPIKES)) {
                    rabbit.setComponent(DataComponents.RABBIT_VARIANT, Rabbit.Variant.WHITE);
                } else if (key.equals(Biomes.DESERT)) {
                    rabbit.setComponent(DataComponents.RABBIT_VARIANT, Rabbit.Variant.GOLD);
                }
            }
        }
    }

    private static void setFrogVariant(ServerLevel level, Frog frog, ResourceKey<FrogVariant> variantKey) {
        level.registryAccess()
            .lookup(Registries.FROG_VARIANT)
            .flatMap(reg -> reg.get(variantKey))
            .ifPresent(holder -> frog.setComponent(DataComponents.FROG_VARIANT, holder));
    }

    private static void setWolfVariant(ServerLevel level, Wolf wolf, ResourceKey<WolfVariant> variantKey) {
        level.registryAccess()
            .lookup(Registries.WOLF_VARIANT)
            .flatMap(reg -> reg.get(variantKey))
            .ifPresent(holder -> wolf.setComponent(DataComponents.WOLF_VARIANT, holder));
    }
}
