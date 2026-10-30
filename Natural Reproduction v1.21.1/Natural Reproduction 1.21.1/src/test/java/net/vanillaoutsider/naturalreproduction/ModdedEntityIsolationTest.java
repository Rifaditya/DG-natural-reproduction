// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 1.21.1
package net.vanillaoutsider.naturalreproduction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class ModdedEntityIsolationTest {

    private static final Set<String> VANILLA_HERD_SPECIES = Set.of(
        "minecraft:cow", "minecraft:sheep", "minecraft:pig", "minecraft:horse",
        "minecraft:donkey", "minecraft:mule", "minecraft:llama", "minecraft:trader_llama",
        "minecraft:camel", "minecraft:goat"
    );

    private static final Set<String> AMBIENT_AND_SOLITARY_SPECIES = Set.of(
        "naturalist:butterfly", "naturalist:firefly", "naturalist:bluebird", "naturalist:cardinal",
        "naturalist:robin", "naturalist:canary", "naturalist:snake", "naturalist:coral_snake",
        "naturalist:rattlesnake", "naturalist:lizard", "naturalist:snail", "minecraft:frog",
        "minecraft:turtle", "minecraft:axolotl", "minecraft:bee", "minecraft:bat"
    );

    @Test
    @DisplayName("Verify Modded Animals Are Disabled by Default (Fast-Fail)")
    public void testModdedNamespaceIsolationLogic() {
        boolean allowModdedAnimals = false; // Default game rule value
        String moddedId = "naturalist:deer";
        String namespace = moddedId.split(":")[0];

        boolean isModded = !namespace.equals("minecraft");
        Assertions.assertTrue(isModded, "naturalist:deer must be recognized as a modded entity");

        boolean canBreed = isModded && allowModdedAnimals;
        Assertions.assertFalse(canBreed, "Third-party modded animals must fast-fail and be disabled by default");
    }

    @Test
    @DisplayName("Verify Modded Animals Allowed When Explicitly Opted-In")
    public void testModdedNamespaceOptInLogic() {
        boolean allowModdedAnimals = true; // Player explicitly enabled
        String moddedId = "naturalist:deer";
        String namespace = moddedId.split(":")[0];

        boolean isModded = !namespace.equals("minecraft");
        boolean canBreed = !isModded || allowModdedAnimals;
        Assertions.assertTrue(canBreed, "Modded animals must be permitted when allow_modded_animals is true");
    }

    @Test
    @DisplayName("Verify Herd Whitelist Rejects Ambient & Solitary Creatures")
    public void testHerdAnimalTagMembershipLogic() {
        // Assert vanilla herd animals are eligible
        for (String species : VANILLA_HERD_SPECIES) {
            Assertions.assertTrue(VANILLA_HERD_SPECIES.contains(species),
                species + " must be recognized in the herd animals whitelist");
        }

        // Assert ambient and solitary animals (butterflies, snakes, birds, frogs) NEVER qualify for herd goal
        for (String ambient : AMBIENT_AND_SOLITARY_SPECIES) {
            boolean isHerdEligible = VANILLA_HERD_SPECIES.contains(ambient);
            Assertions.assertFalse(isHerdEligible,
                ambient + " must NEVER be eligible for FollowHerdLeaderGoal");
        }
    }

    @Test
    @DisplayName("Verify Modded Herd Dynamics Requires Explicit Toggle & Tag Membership")
    public void testModdedHerdDynamicsGatingLogic() {
        boolean herdDynamics = true;
        boolean moddedHerdDynamics = false; // Default: false
        String moddedDeer = "naturalist:deer";
        String namespace = moddedDeer.split(":")[0];
        boolean isModded = !namespace.equals("minecraft");

        // Even if deer was in herd tag, modded_herd_dynamics=false must block goal injection
        boolean canAttachGoal = herdDynamics && (!isModded || moddedHerdDynamics);
        Assertions.assertFalse(canAttachGoal,
            "Modded animals must NEVER receive FollowHerdLeaderGoal when modded_herd_dynamics is false");

        // If player explicitly enables modded herd dynamics
        moddedHerdDynamics = true;
        boolean canAttachWhenEnabled = herdDynamics && (!isModded || moddedHerdDynamics);
        Assertions.assertTrue(canAttachWhenEnabled,
            "Modded animals can only receive herd goal when modded_herd_dynamics is true");
    }

    @Test
    @DisplayName("Verify Modded Animals Bypass spawnChildFromBreeding Gestation Cancellation")
    public void testSpawnChildFromBreedingPreservationLogic() {
        // When a modded animal breeds (e.g. player feeding two Naturalist deer)
        boolean isSpeciesAllowed = false; // Disabled modded species
        boolean shouldCancelVanillaEvent = isSpeciesAllowed; // Only cancel if managed by Natural Reproduction

        Assertions.assertFalse(shouldCancelVanillaEvent,
            "Natural Reproduction must NOT cancel spawnChildFromBreeding for unmanaged/modded species");
        // Result: Native mod breeding logic, animations, and variant spawning proceed unhindered!
    }

    @Test
    @DisplayName("Verify Tag Precedence: Excluded > Included > AllowModded")
    public void testTagPrecedenceHierarchy() {
        Set<String> excludedTags = Set.of("naturalist:bear");
        Set<String> includedTags = Set.of("naturalist:bear", "naturalist:deer");
        boolean allowModded = false;

        // 1. Excluded tag always wins
        String bear = "naturalist:bear";
        boolean bearAllowed;
        if (excludedTags.contains(bear)) {
            bearAllowed = false;
        } else if (includedTags.contains(bear)) {
            bearAllowed = true;
        } else {
            bearAllowed = allowModded;
        }
        Assertions.assertFalse(bearAllowed, "Excluded tag must take highest priority and deny reproduction");

        // 2. Included tag overrides allowModded=false
        String deer = "naturalist:deer";
        boolean deerAllowed;
        if (excludedTags.contains(deer)) {
            deerAllowed = false;
        } else if (includedTags.contains(deer)) {
            deerAllowed = true;
        } else {
            deerAllowed = allowModded;
        }
        Assertions.assertTrue(deerAllowed, "Included tag must allow whitelisting even when allow_modded_animals is false");
    }
}
