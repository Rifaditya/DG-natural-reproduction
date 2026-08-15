// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AnimalDropHelper {

    private AnimalDropHelper() {
    }

    public static void applyScaleDropMultiplier(Animal animal, ItemStack stack) {
        if (animal == null || stack == null || stack.isEmpty()) {
            return;
        }

        float scale = DasikAnimalGeneticsAPI.getScale(animal);
        if (scale > 1.0f) {
            int extraCount = Math.round(stack.getCount() * (scale - 1.0f));
            if (extraCount > 0) {
                stack.grow(extraCount);
            }
        } else if (scale < 1.0f) {
            int newCount = Math.max(1, Math.round(stack.getCount() * scale));
            stack.setCount(newCount);
        }
    }

    public static boolean shouldConvertInbreedingDrop(Animal animal, ItemStack stack) {
        if (animal == null || stack == null || stack.isEmpty()) {
            return false;
        }
        int tier = AnimalLineageHelper.getInbreedingTier(animal);
        return tier >= 3;
    }

    public static ItemStack convertInbreedingDrop(Animal animal, ItemStack stack) {
        if (animal == null || stack == null || stack.isEmpty()) {
            return stack;
        }

        int tier = AnimalLineageHelper.getInbreedingTier(animal);
        if (tier < 3) {
            return stack;
        }

        int count = stack.getCount();

        if (isMeatItem(stack)) {
            // Tier 3/4 Prime Meat Conversion -> Rotten Flesh and Bones
            if (animal.getRandom().nextBoolean()) {
                return new ItemStack(Items.ROTTEN_FLESH, Math.max(1, count));
            } else {
                return new ItemStack(Items.BONE, Math.max(1, count));
            }
        }

        if (isSecondaryDrop(stack)) {
            // Reduce leather, wool, feathers by 75%
            int reducedCount = Math.max(1, count / 4);
            ItemStack reducedStack = stack.copy();
            reducedStack.setCount(reducedCount);
            return reducedStack;
        }

        return stack;
    }

    public static boolean isMeatItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return stack.is(ItemTags.MEAT)
            || stack.is(Items.BEEF)
            || stack.is(Items.COOKED_BEEF)
            || stack.is(Items.PORKCHOP)
            || stack.is(Items.COOKED_PORKCHOP)
            || stack.is(Items.MUTTON)
            || stack.is(Items.COOKED_MUTTON)
            || stack.is(Items.CHICKEN)
            || stack.is(Items.COOKED_CHICKEN)
            || stack.is(Items.RABBIT)
            || stack.is(Items.COOKED_RABBIT);
    }

    public static boolean isSecondaryDrop(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return stack.is(ItemTags.WOOL)
            || stack.is(Items.LEATHER)
            || stack.is(Items.FEATHER)
            || stack.is(Items.RABBIT_HIDE)
            || stack.is(Items.RABBIT_FOOT);
    }
}
