// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.util;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.Vec3;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HerdSocialHelper {

    public static final double DEFAULT_HERD_RADIUS = 24.0;
    public static final double STAMPEDE_ALARM_RADIUS = 16.0;
    public static final int MIN_HERD_SIZE = 3;

    private static final Map<String, CachedLeader> LEADER_CACHE = new ConcurrentHashMap<>();

    private static class CachedLeader {
        final Animal leader;
        final long expiryTime;

        CachedLeader(Animal leader, long expiryTime) {
            this.leader = leader;
            this.expiryTime = expiryTime;
        }
    }

    private HerdSocialHelper() {
    }

    public static Animal findOrElectLeader(ServerLevel level, Animal mob, double radius) {
        if (level == null || mob == null || !mob.isAlive() || mob.isBaby() || mob instanceof TamableAnimal) {
            return null;
        }

        long currentTime = level.getGameTime();
        String cacheKey = mob.getType().toString() + "@" + (mob.blockPosition().getX() >> 4) + "," + (mob.blockPosition().getZ() >> 4);

        CachedLeader cached = LEADER_CACHE.get(cacheKey);
        if (cached != null && currentTime < cached.expiryTime && cached.leader != null && cached.leader.isAlive()) {
            return cached.leader;
        }

        List<Animal> members = level.getEntitiesOfClass(
            Animal.class,
            mob.getBoundingBox().inflate(radius),
            e -> e.getType() == mob.getType() && e.isAlive() && !e.isBaby() && !(e instanceof TamableAnimal)
        );

        if (members.size() < MIN_HERD_SIZE) {
            LEADER_CACHE.put(cacheKey, new CachedLeader(null, currentTime + 100));
            return null;
        }

        Animal leader = null;
        float maxScale = -1.0f;

        for (Animal candidate : members) {
            float scale = DasikAnimalGeneticsAPI.hasGenetics(candidate)
                ? DasikAnimalGeneticsAPI.getScale(candidate)
                : 1.0f;

            if (scale > maxScale || (scale == maxScale && leader != null && candidate.getId() < leader.getId())) {
                maxScale = scale;
                leader = candidate;
            }
        }

        LEADER_CACHE.put(cacheKey, new CachedLeader(leader, currentTime + 200));
        return leader;
    }

    public static boolean isLeader(ServerLevel level, Animal mob) {
        if (mob == null || mob.isBaby() || mob instanceof TamableAnimal) {
            return false;
        }
        Animal leader = findOrElectLeader(level, mob, DEFAULT_HERD_RADIUS);
        return leader == mob;
    }

    public static void triggerHerdDistressAlarm(ServerLevel level, Animal victim, LivingEntity attacker) {
        if (level == null || victim == null || !victim.isAlive() || victim instanceof TamableAnimal) {
            return;
        }

        boolean useStampede = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.HERD_STAMPEDE);
        if (!useStampede) {
            return;
        }

        Vec3 threatPos = attacker != null ? attacker.position() : victim.position();

        List<Animal> herdMembers = level.getEntitiesOfClass(
            Animal.class,
            victim.getBoundingBox().inflate(STAMPEDE_ALARM_RADIUS),
            e -> e.getType() == victim.getType() && e.isAlive() && !(e instanceof TamableAnimal)
        );

        for (Animal member : herdMembers) {
            Vec3 fleeTarget = DefaultRandomPos.getPosAway(member, 16, 7, threatPos);
            if (fleeTarget != null) {
                member.getNavigation().moveTo(fleeTarget.x, fleeTarget.y, fleeTarget.z, 1.35D);
            }
        }
    }

    public static BlockPos getDiurnalTarget(ServerLevel level, Animal leader) {
        if (level == null || leader == null) {
            return null;
        }

        long timeOfDay = level.getGameTime() % 24000L;
        BlockPos currentPos = leader.blockPosition();

        if (timeOfDay >= 6000L && timeOfDay <= 9000L) {
            return currentPos.offset(leader.getRandom().nextInt(5) - 2, 0, leader.getRandom().nextInt(5) - 2);
        }

        if (timeOfDay >= 12000L && timeOfDay <= 23000L) {
            return currentPos;
        }

        return null;
    }
}
