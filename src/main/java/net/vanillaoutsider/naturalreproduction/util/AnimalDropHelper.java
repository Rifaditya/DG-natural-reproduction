// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;

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
}
