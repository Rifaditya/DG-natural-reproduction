// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.ai;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;
import net.vanillaoutsider.naturalreproduction.util.HerdSocialHelper;

import java.util.EnumSet;

public class FollowHerdLeaderGoal extends Goal {

    private final Animal mob;
    private Animal leader;
    private final double speedModifier;
    private int nextStartTick;

    public FollowHerdLeaderGoal(Animal mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob == null || !this.mob.isAlive() || this.mob.isBaby() || this.mob.isInLove() || this.mob.isLeashed() || this.mob.isPanicking()) {
            return false;
        }

        if (!(this.mob.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        if (!DynamicGameRuleManager.getBoolean(serverLevel, NaturalReproductionFabric.HERD_DYNAMICS)) {
            return false;
        }

        if (this.nextStartTick > 0) {
            this.nextStartTick--;
            return false;
        }
        this.nextStartTick = 40 + this.mob.getRandom().nextInt(40);

        this.leader = HerdSocialHelper.findOrElectLeader(serverLevel, this.mob, HerdSocialHelper.DEFAULT_HERD_RADIUS);
        if (this.leader == null || this.leader == this.mob || !this.leader.isAlive()) {
            return false;
        }

        double distSqr = this.mob.distanceToSqr(this.leader);
        return distSqr >= 36.0 && distSqr <= 576.0; // Between 6 and 24 blocks away
    }

    @Override
    public boolean canContinueToUse() {
        if (this.leader == null || !this.leader.isAlive() || this.mob.isInLove() || this.mob.isLeashed() || this.mob.isPanicking()) {
            return false;
        }

        double distSqr = this.mob.distanceToSqr(this.leader);
        return distSqr > 25.0 && distSqr <= 625.0 && !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        if (this.leader != null) {
            this.mob.getNavigation().moveTo(this.leader, this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.leader = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.leader != null && this.mob.tickCount % 20 == 0) {
            this.mob.getNavigation().moveTo(this.leader, this.speedModifier);
        }
    }
}
