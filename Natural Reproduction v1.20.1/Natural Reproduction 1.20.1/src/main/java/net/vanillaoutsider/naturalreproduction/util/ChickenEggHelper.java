// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 1.20.1
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.HitResult;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

public final class ChickenEggHelper {

    public static final String FERTILIZED_KEY = "natural_reproduction:fertilized";
    public static final String FATHER_TIER_KEY = "natural_reproduction:father_inbreeding_tier";

    private ChickenEggHelper() {
    }

    public static ItemStack createFertilizedEgg(ServerLevel level, Animal mother, Animal father) {
        ItemStack eggStack = new ItemStack(Items.EGG);

        CompoundTag tag = eggStack.getOrCreateTag();
        tag.putBoolean(FERTILIZED_KEY, true);

        if (father != null) {
            tag.putInt(FATHER_TIER_KEY, AnimalLineageHelper.getInbreedingTier(father));
        }

        eggStack.setHoverName(Component.translatable("item.natural-reproduction.fertilized_egg"));

        CompoundTag display = eggStack.getOrCreateTagElement("display");
        ListTag loreList = new ListTag();
        loreList.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.translatable("item.natural-reproduction.fertilized_egg.desc")
        )));
        display.put("Lore", loreList);

        return eggStack;
    }

    public static boolean isFertilized(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.is(Items.EGG)) {
            return false;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return false;
        }

        return tag.getBoolean(FERTILIZED_KEY);
    }

    public static boolean handleEggImpact(ThrownEgg egg, ServerLevel level, HitResult hitResult) {
        if (egg == null || level == null) {
            return false;
        }

        ItemStack itemStack = egg.getItem();
        boolean isFertilized = isFertilized(itemStack);
        boolean useFertilizedEggs = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS);
        boolean infertileRegular = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS);

        // Visual break particles on impact
        level.sendParticles(
            new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)),
            egg.getX(), egg.getY(), egg.getZ(),
            8, 0.1, 0.1, 0.1, 0.05
        );

        if (isFertilized && useFertilizedEggs) {
            boolean isPlayerThrown = egg.getOwner() != null;
            int hatchChance = isPlayerThrown ? 100 : DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE);

            if (level.getRandom().nextInt(100) < hatchChance) {
                int chicksToSpawn = 1;
                // Vanilla Quadruplet Roll (1/256)
                if (level.getRandom().nextInt(256) == 0) {
                    chicksToSpawn = 4;
                }

                for (int i = 0; i < chicksToSpawn; i++) {
                    AgeableMob chick = (AgeableMob)EntityType.CHICKEN.create(level);
                    if (chick != null) {
                        chick.setBaby(true);
                        chick.setPos(egg.getX(), egg.getY(), egg.getZ());

                        if (!DasikAnimalGeneticsAPI.hasGenetics(chick)) {
                            DasikAnimalGeneticsAPI.rollStats(chick, "default");
                            GeneticsEngine.applyGeneticsModifiers(chick);
                        }

                        level.addFreshEntity(chick);
                    }
                }

                level.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    egg.getX(), egg.getY() + 0.3, egg.getZ(),
                    6, 0.25, 0.25, 0.25, 0.02
                );
            }
            return true; // Mod handled fertilized egg impact
        }

        if (!isFertilized && infertileRegular) {
            // Rare 1/64 miracle hatch chance for regular unfertilized eggs
            if (level.getRandom().nextInt(64) == 0) {
                AgeableMob chick = (AgeableMob)EntityType.CHICKEN.create(level);
                if (chick != null) {
                    chick.setBaby(true);
                    chick.setPos(egg.getX(), egg.getY(), egg.getZ());
                    level.addFreshEntity(chick);
                }
            }
            return true; // Mod handled unfertilized egg impact (replacing vanilla 1/8)
        }

        return false; // Defer to vanilla logic
    }
}
