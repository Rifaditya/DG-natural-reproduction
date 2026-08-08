// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class AnimalHabitatHelper {

    private AnimalHabitatHelper() {
    }

    public static boolean isSpeciesReproductionAllowed(ServerLevel level, Animal self) {
        if (level == null || self == null) {
            return false;
        }
        return net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric.isSpeciesAllowed(level, self.getType());
    }

    public static boolean hasEnvironmentalBreedingConditions(ServerLevel level, Animal self) {
        BlockPos center = self.blockPosition();
        EntityType<?> type = self.getType();

        // 1. Cattle & Sheep (Cows, Sheep, Mooshrooms)
        if (type == EntityTypes.COW || type == EntityTypes.SHEEP) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.WHEAT)
            );
        } else if (type == EntityTypes.MOOSHROOM) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.MYCELIUM) || state.is(Blocks.BROWN_MUSHROOM) || state.is(Blocks.RED_MUSHROOM) || state.is(Blocks.BROWN_MUSHROOM_BLOCK) || state.is(Blocks.RED_MUSHROOM_BLOCK)
            );
        }
        // 2. Pigs
        else if (type == EntityTypes.PIG) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.MUD) || state.is(Blocks.CARROTS) || state.is(Blocks.BEETROOTS) || state.is(Blocks.POTATOES) || state.is(Blocks.FARMLAND)
            );
        }
        // 3. Chickens
        else if (type == EntityTypes.CHICKEN) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.HAY_BLOCK) || state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.WHEAT)
            );
        }
        // 4. Equines (Horses, Donkeys, Mules)
        else if (type == EntityTypes.HORSE || type == EntityTypes.DONKEY || type == EntityTypes.MULE) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.HAY_BLOCK) || state.is(Blocks.DANDELION) || state.is(Blocks.SUGAR_CANE)
            );
        }
        // 5. Camelids (Llamas, Trader Llamas, Camels)
        else if (type == EntityTypes.LLAMA || type == EntityTypes.TRADER_LLAMA) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.HAY_BLOCK) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.FERN)
            );
        } else if (type == EntityTypes.CAMEL) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.CACTUS) || state.is(Blocks.SAND) || state.is(Blocks.DEAD_BUSH)
            );
        }
        // 6. Canines (Wolves / Dogs) & Foxes with Variant-Specific Triggers
        else if (self instanceof Wolf wolf) {
            Holder<WolfVariant> wolfVariantHolder = wolf.get(DataComponents.WOLF_VARIANT);
            ResourceKey<WolfVariant> key = wolfVariantHolder != null ? wolfVariantHolder.unwrapKey().orElse(null) : null;

            if (WolfVariants.SNOWY.equals(key)) {
                return matchesAnyBlock(level, center, 2, 1, 2, state ->
                    state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.POWDER_SNOW) || state.is(Blocks.SNOW) || state.is(Blocks.PACKED_ICE)
                );
            } else if (WolfVariants.BLACK.equals(key) || WolfVariants.CHESTNUT.equals(key)) {
                return matchesAnyBlock(level, center, 2, 1, 2, state ->
                    state.is(Blocks.PODZOL) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.MOSS_BLOCK) || state.is(Blocks.SPRUCE_LEAVES)
                );
            } else if (WolfVariants.STRIPED.equals(key) || WolfVariants.SPOTTED.equals(key) || WolfVariants.RUSTY.equals(key)) {
                return matchesAnyBlock(level, center, 2, 1, 2, state ->
                    state.is(Blocks.RED_SAND) || state.is(Blocks.TERRACOTTA) || state.is(Blocks.DEAD_BUSH) || state.is(Blocks.COARSE_DIRT)
                );
            }

            // Default / Pale / Woods Wolf Trigger
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.COARSE_DIRT) || state.is(Blocks.PODZOL) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.SWEET_BERRY_BUSH)
            );
        } else if (self instanceof Fox fox) {
            Fox.Variant foxVariant = fox.getVariant();
            if (foxVariant == Fox.Variant.SNOW) {
                return matchesAnyBlock(level, center, 2, 1, 2, state ->
                    state.is(Blocks.SNOW) || state.is(Blocks.POWDER_SNOW) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.PACKED_ICE)
                );
            }

            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.CAVE_VINES) || state.is(Blocks.CAVE_VINES_PLANT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.SPRUCE_LEAVES)
            );
        }
        // 7. Felines (Cats & Ocelots)
        else if (type == EntityTypes.CAT || type == EntityTypes.OCELOT) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.WATER) || state.is(Blocks.SEAGRASS) || state.is(Blocks.KELP) || state.is(Blocks.CHEST) || state.is(BlockTags.PLANKS)
            );
        }
        // 8. Rabbits with Variant-Specific Triggers
        else if (self instanceof Rabbit rabbit) {
            Rabbit.Variant rabbitVariant = rabbit.get(DataComponents.RABBIT_VARIANT);
            if (rabbitVariant == Rabbit.Variant.WHITE) {
                return matchesAnyBlock(level, center, 2, 1, 2, state ->
                    state.is(Blocks.SNOW) || state.is(Blocks.POWDER_SNOW) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.PACKED_ICE)
                );
            } else if (rabbitVariant == Rabbit.Variant.GOLD) {
                return matchesAnyBlock(level, center, 2, 1, 2, state ->
                    state.is(Blocks.SAND) || state.is(Blocks.RED_SAND) || state.is(Blocks.DEAD_BUSH) || state.is(Blocks.CACTUS)
                );
            }

            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.CARROTS) || state.is(Blocks.DANDELION) || state.is(Blocks.SHORT_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.SAND)
            );
        }
        // 9. Amphibians & Aquatic Animals (Turtles, Frogs with Variant Triggers, Axolotls)
        else if (type == EntityTypes.TURTLE) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.SAND) || state.is(Blocks.SEAGRASS) || state.is(Blocks.WATER)
            );
        } else if (self instanceof Frog frog) {
            Holder<FrogVariant> frogVariantHolder = frog.get(DataComponents.FROG_VARIANT);
            ResourceKey<FrogVariant> key = frogVariantHolder != null ? frogVariantHolder.unwrapKey().orElse(null) : null;

            if (FrogVariants.WARM.equals(key)) {
                return matchesAnyBlock(level, center, 2, 1, 2, state ->
                    state.is(Blocks.SAND) || state.is(Blocks.BIG_DRIPLEAF) || state.is(Blocks.SMALL_DRIPLEAF) || state.is(Blocks.MANGROVE_ROOTS) || state.is(Blocks.MUD) || state.is(Blocks.WATER)
                );
            } else if (FrogVariants.COLD.equals(key)) {
                return matchesAnyBlock(level, center, 2, 1, 2, state ->
                    state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.SNOW) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.WATER)
                );
            }

            // Default / Temperate Frog Trigger
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.LILY_PAD) || state.is(Blocks.FROGSPAWN) || state.is(Blocks.MUD) || state.is(Blocks.WATER) || state.is(Blocks.BIG_DRIPLEAF) || state.is(Blocks.SMALL_DRIPLEAF)
            );
        } else if (type == EntityTypes.AXOLOTL) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.CLAY) || state.is(Blocks.WATER) || state.is(Blocks.SEAGRASS)
            );
        }
        // 10. Bears (Polar Bears, Pandas)
        else if (type == EntityTypes.POLAR_BEAR) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.BLUE_ICE) || state.is(Blocks.SNOW) || state.is(Blocks.WATER)
            );
        } else if (type == EntityTypes.PANDA) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.BAMBOO) || state.is(Blocks.BAMBOO_SAPLING) || state.is(Blocks.SUGAR_CANE)
            );
        }
        // 11. Bees
        else if (type == EntityTypes.BEE) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(BlockTags.FLOWERS) || state.is(Blocks.FLOWERING_AZALEA) || state.is(Blocks.BEE_NEST) || state.is(Blocks.BEEHIVE)
            );
        }
        // 12. Nether Animals (Striders, Hoglins)
        else if (type == EntityTypes.STRIDER) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.WARPED_FUNGUS) || state.is(Blocks.WARPED_NYLIUM) || state.is(Blocks.LAVA) || state.is(Blocks.NETHERRACK)
            );
        } else if (type == EntityTypes.HOGLIN) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.CRIMSON_FUNGUS) || state.is(Blocks.CRIMSON_NYLIUM) || state.is(Blocks.NETHERRACK)
            );
        }
        // 13. Armadillos, Goats, Sniffers
        else if (type == EntityTypes.ARMADILLO) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.RED_SAND) || state.is(Blocks.TERRACOTTA) || state.is(Blocks.DEAD_BUSH) || state.is(Blocks.SHORT_GRASS)
            );
        } else if (type == EntityTypes.GOAT) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.SNOW) || state.is(Blocks.POWDER_SNOW) || state.is(Blocks.STONE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.WHEAT)
            );
        } else if (type == EntityTypes.SNIFFER) {
            return matchesAnyBlock(level, center, 2, 1, 2, state ->
                state.is(Blocks.TORCHFLOWER) || state.is(Blocks.PITCHER_PLANT) || state.is(Blocks.MOSS_BLOCK) || state.is(Blocks.PODZOL)
            );
        }

        // Generic fallback for custom / modded animal species
        return true;
    }

    private static boolean matchesAnyBlock(ServerLevel level, BlockPos center, int radiusX, int radiusY, int radiusZ, java.util.function.Predicate<BlockState> predicate) {
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radiusX, -radiusY, -radiusZ), center.offset(radiusX, radiusY, radiusZ))) {
            if (predicate.test(level.getBlockState(pos))) {
                return true;
            }
        }
        return false;
    }
}
