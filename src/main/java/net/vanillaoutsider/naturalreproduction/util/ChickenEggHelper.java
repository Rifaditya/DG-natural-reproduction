// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.phys.HitResult;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

import java.util.List;

public final class ChickenEggHelper {

    public static final String FERTILIZED_KEY = "natural_reproduction:fertilized";
    public static final String FATHER_TIER_KEY = "natural_reproduction:father_inbreeding_tier";

    private ChickenEggHelper() {
    }

    public static ItemStack createFertilizedEgg(ServerLevel level, Animal mother, Animal father) {
        ItemStack eggStack = new ItemStack(Items.EGG);

        CompoundTag tag = new CompoundTag();
        tag.putBoolean(FERTILIZED_KEY, true);

        if (father != null) {
            tag.putInt(FATHER_TIER_KEY, AnimalLineageHelper.getInbreedingTier(father));
        }

        eggStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        eggStack.set(DataComponents.ITEM_NAME, Component.translatable("item.natural-reproduction.fertilized_egg"));

        ItemLore lore = new ItemLore(List.of(
            Component.translatable("item.natural-reproduction.fertilized_egg.desc")
        ));
        eggStack.set(DataComponents.LORE, lore);

        return eggStack;
    }

    public static boolean isFertilized(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.is(Items.EGG)) {
            return false;
        }

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return false;
        }

        return customData.copyTag().getBoolean(FERTILIZED_KEY).orElse(false);
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
            new ItemParticleOption(ParticleTypes.ITEM, Items.EGG),
            egg.getX(), egg.getY(), egg.getZ(),
            8, 0.1, 0.1, 0.1, 0.05
        );

        if (isFertilized && useFertilizedEggs) {
            boolean isPlayerThrown = egg.getOwner() != null;
            int hatchChance = isPlayerThrown ? 100 : DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE);

            if (egg.getRandom().nextInt(100) < hatchChance) {
                int chicksToSpawn = 1;
                // Vanilla Quadruplet Roll (1/256)
                if (egg.getRandom().nextInt(256) == 0) {
                    chicksToSpawn = 4;
                }

                for (int i = 0; i < chicksToSpawn; i++) {
                    AgeableMob chick = (AgeableMob)EntityTypes.CHICKEN.create(level, EntitySpawnReason.BREEDING);
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
            if (egg.getRandom().nextInt(64) == 0) {
                AgeableMob chick = (AgeableMob)EntityTypes.CHICKEN.create(level, EntitySpawnReason.BREEDING);
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
