// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.mixin;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Animal;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;
import net.vanillaoutsider.naturalreproduction.util.AnimalBiomeHelper;
import net.vanillaoutsider.naturalreproduction.util.AnimalCrampedSpaceHelper;
import net.vanillaoutsider.naturalreproduction.util.AnimalGestationHelper;
import net.vanillaoutsider.naturalreproduction.util.AnimalHabitatHelper;
import net.vanillaoutsider.naturalreproduction.util.AnimalLineageHelper;
import net.vanillaoutsider.naturalreproduction.util.AnimalPastureHelper;
import net.vanillaoutsider.naturalreproduction.util.BreedingPipelineHelper;
import net.vanillaoutsider.naturalreproduction.util.BreedingTrackerLogger;
import net.vanillaoutsider.naturalreproduction.util.SpatialBreedingCacheHelper;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Animal.class)
public abstract class AnimalBreedingMixin extends AgeableMob {

    protected AnimalBreedingMixin() {
        super(null, null);
    }

    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    private void naturalreproduction$onCustomServerAiStep(ServerLevel level, CallbackInfo ci) {
        Animal self = (Animal)(Object)this;

        // Better Dogs & Pet Priority: completely exempt tamed player pets (wolves, cats, etc.)
        if (self instanceof net.minecraft.world.entity.TamableAnimal tamable && tamable.isTame()) {
            return;
        }

        // Ensure genetics stats are rolled and applied via DasikLibrary API
        if (!DasikAnimalGeneticsAPI.hasGenetics(self)) {
            DasikAnimalGeneticsAPI.rollStats(self, "default");
            GeneticsEngine.applyGeneticsModifiers(self);
        }

        // Attach Herd Social Follow Goal dynamically (world-reload safe)
        if (!naturalreproduction$hasHerdGoal()) {
            this.goalSelector.addGoal(6, new net.vanillaoutsider.naturalreproduction.ai.FollowHerdLeaderGoal(self, 1.0D));
        }

        // Herd Predator Distress Alarm Check
        if (self.hurtTime == 10 && self.getLastHurtByMob() != null) {
            net.vanillaoutsider.naturalreproduction.util.HerdSocialHelper.triggerHerdDistressAlarm(level, self, self.getLastHurtByMob());
        }

        // Active Pregnancy / Gestation Countdown Tick (staggered every 20 ticks)
        if (self.tickCount % 20 == 0 && DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.GESTATION_PERIOD)) {
            AnimalGestationHelper.tickGestation(level, self);
        }

        // Tier 4 Lethal Genetic Collapse Tick (every 20 ticks)
        if (self.tickCount % 20 == 0 && DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.INBREEDING_DEGRADATION)) {
            AnimalLineageHelper.tickLethalCollapse(level, self);
        }

        // Dynamic Overgrazing Wear Check (staggered every 100 ticks per entity)
        if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.OVERGRAZING)
            && (self.getId() + level.getGameTime()) % 100 == 0) {
            AnimalPastureHelper.processOvergrazing(level, self);
        }

        // Autonomous Wild Breeding Logic (Staggered 100-tick modulo = 5 seconds per entity)
        if (!level.isClientSide() && (self.getId() + level.getGameTime()) % 100 == 0) {
            // Fast-Fail 1: Health, love status, age, pregnancy
            if (self.getAge() != 0 || self.isInLove() || AnimalGestationHelper.isPregnant(self) || self.getHealth() < self.getMaxHealth()) {
                return;
            }

            // Fast-Fail 2: Global & Species GameRule toggles
            if (!DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.ENABLED) || !AnimalHabitatHelper.isSpeciesReproductionAllowed(level, self)) {
                return;
            }

            int rate = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.RATE);
            if (rate <= 0) {
                rate = 24000;
            }

            // Staggered probability: 1 check every 100 ticks
            int effectiveRate = Math.max(1, rate / 100);

            if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_FERTILITY) && AnimalBiomeHelper.isNativeBiome(level, self)) {
                effectiveRate = Math.max(1, effectiveRate / 2); // 2x faster breeding in native biomes
            }

            boolean isEnriched = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.PASTURE_ENRICHMENT)
                && AnimalPastureHelper.isPastureEnriched(level, self.blockPosition());

            if (isEnriched) {
                effectiveRate = Math.max(1, Math.round(effectiveRate * 0.75f)); // +25% faster breeding in enriched pastures
                AnimalPastureHelper.emitWellNourishedParticles(level, self);
            }

            // Fast-Fail 3: Probability check before expensive spatial & entity queries
            if (self.getRandom().nextInt(effectiveRate) != 0) {
                return;
            }

            // Only on passing the random check: verify habitat conditions
            if (!AnimalHabitatHelper.hasEnvironmentalBreedingConditions(level, self)) {
                return;
            }

            // Fast-Fail 4: Spatial entity density check via 100-tick cached density
            int densityCap = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DENSITY_CAP);
            int nearbyCount = SpatialBreedingCacheHelper.getNearbySameSpeciesCount(level, self, 16.0);
            if (nearbyCount > densityCap) {
                return;
            }

            // Targeted mate search in 8-block radius
            List<Animal> potentialMates = level.getEntitiesOfClass(
                Animal.class,
                self.getBoundingBox().inflate(8.0),
                e -> e != self && e.getType() == self.getType() && e.getAge() == 0 && e.isAlive() && !e.isInLove() && !AnimalGestationHelper.isPregnant(e) && !(e instanceof net.minecraft.world.entity.TamableAnimal tamable && tamable.isTame())
            );

            if (!potentialMates.isEmpty()) {
                Animal mate = naturalreproduction$selectBestMate(self, potentialMates);
                if (mate != null) {
                    self.setInLove(null);
                    mate.setInLove(null);
                }
            }
        }
    }

    @Inject(method = "spawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
    private void naturalreproduction$onSpawnChildGestationCheck(ServerLevel level, Animal mate, CallbackInfo ci) {
        Animal parent1 = (Animal)(Object)this;

        // Tamed dogs and pets use vanilla instant delivery (Better Dogs litters)
        if (parent1 instanceof net.minecraft.world.entity.TamableAnimal tamable && tamable.isTame()) {
            return;
        }

        boolean useGestation = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.GESTATION_PERIOD);
        boolean manualAllowed = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.MANUAL_GESTATION);
        boolean isManual = parent1.getLoveCause() != null || mate.getLoveCause() != null;

        if (useGestation && (!isManual || manualAllowed)) {
            int duration = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.GESTATION_DURATION);
            AnimalGestationHelper.startGestation(level, parent1, mate, duration);

            // Put parents on breeding cooldown
            parent1.setAge(6000);
            mate.setAge(6000);
            parent1.resetLove();
            mate.resetLove();

            ci.cancel();
            return;
        }

        // If not using gestation (or manual), check autonomous chicken 50/50 fertilized egg drop
        if (!isManual && parent1.getType() == net.minecraft.world.entity.EntityTypes.CHICKEN
            && DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS)
            && parent1.getRandom().nextBoolean()) {
            parent1.spawnAtLocation(level, net.vanillaoutsider.naturalreproduction.util.ChickenEggHelper.createFertilizedEgg(level, parent1, mate));
            parent1.setAge(6000);
            mate.setAge(6000);
            parent1.resetLove();
            mate.resetLove();
            ci.cancel();
        }
    }

    @Inject(method = "spawnChildFromBreeding", at = @At("TAIL"))
    private void naturalreproduction$onSpawnChildFromBreeding(ServerLevel level, Animal mate, CallbackInfo ci) {
        Animal parent1 = (Animal)(Object)this;
        // Find newest baby animal near parents
        List<AgeableMob> babies = level.getEntitiesOfClass(
            AgeableMob.class,
            parent1.getBoundingBox().inflate(3.0),
            e -> e.getType() == parent1.getType() && e.isBaby()
        );

        boolean isEnriched = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.PASTURE_ENRICHMENT)
            && AnimalPastureHelper.isPastureEnriched(level, parent1.blockPosition());

        for (AgeableMob baby : babies) {
            BreedingPipelineHelper.finalizeNewborn(level, parent1, mate, baby, isEnriched);
        }
    }

    @Unique
    private boolean naturalreproduction$hasHerdGoal() {
        for (WrappedGoal wrapped : this.goalSelector.getAvailableGoals()) {
            if (wrapped.getGoal() instanceof net.vanillaoutsider.naturalreproduction.ai.FollowHerdLeaderGoal) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private Animal naturalreproduction$selectBestMate(Animal self, List<Animal> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }

        Animal bestCandidate = null;
        double bestScore = -Double.MAX_VALUE;

        for (Animal candidate : candidates) {
            double score = 1000.0;

            // Inbreeding risk / kinship penalty
            boolean related = DasikAnimalGeneticsAPI.isRelated(self, candidate);
            int inbreedingRisk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(self, candidate);

            if (related || inbreedingRisk > 0) {
                score -= 500.0 + (inbreedingRisk * 3.0);
            }

            // Inbreeding tier penalty
            int candidateTier = AnimalLineageHelper.getInbreedingTier(candidate);
            score -= (candidateTier * 50.0);

            // Proximity preference (distance-squared)
            double distSq = self.distanceToSqr(candidate);
            score -= (distSq * 2.0);

            // Genetic scale bonus
            float candidateScale = DasikAnimalGeneticsAPI.getScale(candidate);
            score += (candidateScale * 10.0);

            if (score > bestScore) {
                bestScore = score;
                bestCandidate = candidate;
            }
        }

        return bestCandidate != null ? bestCandidate : candidates.get(0);
    }
}
