// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.mixin;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;
import net.vanillaoutsider.naturalreproduction.util.AnimalDropHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class AnimalDropScaleMixin {

    @Inject(
        method = "spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void naturalreproduction$onSpawnAtLocation(ServerLevel level, ItemStack stack, float offsetY, CallbackInfoReturnable<ItemEntity> cir) {
        if ((Object) this instanceof Animal self) {
            if (level != null && !level.isClientSide() && stack != null && !stack.isEmpty()) {
                boolean inbreedingActive = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.INBREEDING_DEGRADATION);
                if (inbreedingActive && AnimalDropHelper.shouldConvertInbreedingDrop(self, stack)) {
                    ItemStack converted = AnimalDropHelper.convertInbreedingDrop(self, stack);
                    if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.SCALE_DROPS)) {
                        AnimalDropHelper.applyScaleDropMultiplier(self, converted);
                    }
                    if (!converted.isEmpty()) {
                        ItemEntity itemEntity = new ItemEntity(level, self.getX(), self.getY() + (double) offsetY, self.getZ(), converted);
                        itemEntity.setDefaultPickUpDelay();
                        level.addFreshEntity(itemEntity);
                        cir.setReturnValue(itemEntity);
                    } else {
                        cir.setReturnValue(null);
                    }
                    return;
                }

                if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.SCALE_DROPS)) {
                    AnimalDropHelper.applyScaleDropMultiplier(self, stack);
                }
            }
        }
    }
}
