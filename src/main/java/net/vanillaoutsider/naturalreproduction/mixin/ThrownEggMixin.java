// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.phys.HitResult;
import net.vanillaoutsider.naturalreproduction.util.ChickenEggHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin {

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void naturalreproduction$onThrownEggHit(HitResult hitResult, CallbackInfo ci) {
        ThrownEgg self = (ThrownEgg)(Object)this;
        if (self.level() instanceof ServerLevel serverLevel) {
            boolean handled = ChickenEggHelper.handleEggImpact(self, serverLevel, hitResult);
            if (handled) {
                self.discard();
                ci.cancel();
            }
        }
    }
}
