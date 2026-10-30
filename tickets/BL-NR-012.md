# 🎫 [BL-NR-012] Mod-Level Creature Mod Engine Isolation & AI Safeguards (Zero Datapack Requirement)

- **Ticket ID**: `[BL-NR-012]`
- **Category**: `[PERF]`
- **Priority**: `[CRITICAL]`
- **Scope**: `All Anchors (1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3)`
- **Status**: `✅ RESOLVED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`
- **Reported By / Community Credit**: **Muqington** (Discord Community Request: Naturalist Frame Drops & Mob Toggles)

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Re-engineer `Natural Reproduction`'s core Java engine and Mixin hooks across all 6 version anchors so that all third-party creature mods (e.g. Naturalist, Alex's Mobs, Critters & Companions) are automatically safely isolated and run their native behaviors with zero lag, zero AI goal pollution, and zero breeding hijacks out-of-the-box in the mod JAR itself, without requiring players to install, create, or configure any datapacks.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: Installing Naturalist causes severe game lag and TPS drops. The mod blindly attaches `FollowHerdLeaderGoal` to every animal (triggering 24-block bounding box scans every second on birds, butterflies, snakes), defaults modded breeding checks to `true` (scanning 16-block radiuses), cancels modded breeding in `spawnChildFromBreeding` (breaking custom mod breeding animations and variant logic), and requires external datapacks to stop.
   - *After*: Installing Naturalist or any creature mod works smoothly out-of-the-box with zero configuration. Third-party animals are ignored by herd goals, excluded from autonomous breeding scans, and allowed to breed using their own mod's native logic unhindered. Players who *do* want Natural Reproduction mechanics on modded mobs can toggle them right from the in-game YACL GUI or `/nr` commands.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Never require players or server owners to install or edit datapacks to resolve mod compatibility or lag. Datapack tags are purely an optional advanced override.
   - ❌ Do not hardcode specific third-party mod namespaces (e.g. `naturalist:`); detect external namespaces generically via `!id.getNamespace().equals("minecraft")` and registry metadata.
   - ❌ Do not break or alter vanilla livestock breeding, genetics, or scale limits for the 27 supported vanilla animals.
   - ❌ Do not break dedicated server classloading when YACL or ModMenu is absent.
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [x] **Mod-Level Isolation**: By default, `AnimalHabitatHelper.isSpeciesReproductionAllowed` returns `false` in Java for any entity with a non-`minecraft` namespace unless explicitly enabled by the player.
   - [x] **Zero Herd Pollution**: `FollowHerdLeaderGoal` is strictly gated in Java to a hardcoded whitelist of pastoral vanilla herd animals (Cows, Sheep, Pigs, Horses, Donkeys, Mules, Llamas, Goats, Camels). Third-party mobs, birds, butterflies, frogs, axolotls, and turtles NEVER receive the goal.
   - [x] **Native Breeding Preservation**: `AnimalBreedingMixin.spawnChildFromBreeding` only cancels and enters gestation for enabled vanilla livestock. Modded animals bypass gestation and execute their native mod's breeding logic, animations, and offspring delivery completely unhindered.
   - [x] **Genetics & Scale Guard**: DasikLibrary genetics attribute modifiers are only applied to registered species; arbitrary modded entities are not mutated or scaled.
   - [x] **Overgrazing & Drop Multiplier Isolation**: Modded animals never trigger overgrazing dirt conversion or modified scale drop conversions.
   - [x] **In-Game Toggles**: `ALLOW_MODDED_ANIMALS` (default `false`) and `MODDED_HERD_DYNAMICS` (default `false`) exposed in YACL config screen and `/nr set` Brigadier commands.
   - [x] 5-Pillar automated test suites (`./gradlew test`) pass across all 6 version anchors (`1.20.1`, `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3`).

---

## ❓ 2. Problem Statement & Player Impact

Reported by community member **Muqington** (*"I'm using Natural Reproduction with Naturalist and am massively losing frames because of the amount of Naturalist creatures in my world. I'd like to be able to just switch some of the added mobs off."*).

Previously, compatibility issues with creature mods were approached through external datapacks (`BL-NR-011` / `BL-NR-013`). This is fundamentally flawed because players running popular mods like **Naturalist** (47 animals, 66 variants) experience massive unplayable lag spikes immediately upon installing the mod without knowing why:
1. **Unchecked Mixin Scope**: `AnimalBreedingMixin` hooks directly into `net.minecraft.world.entity.animal.Animal`. Every single creature class from any mod that inherits from `Animal` inherits all of Natural Reproduction's background loops.
2. **AI Goal Spreading**: Every creature (even a butterfly, firefly, bird, or snake) has `FollowHerdLeaderGoal` injected. Every 40–80 ticks per entity, it calls `HerdSocialHelper.findOrElectLeader()`, executing a 24-block (`48x48x48`) `level.getEntitiesOfClass(Animal.class)` query.
3. **Breeding Hijack**: `spawnChildFromBreeding` cancels the event for *all* animals to start gestation. This corrupts other mods' custom mating behaviors, variant selections, or event hooks.
4. **Autonomous Breeding Scans**: Unknown species default to `true` in `isSpeciesAllowed` and `hasEnvironmentalBreedingConditions`, causing every wild modded animal to scan for mates every 5 seconds.

These issues must be resolved permanently inside the Java code at the mod engine level.

---

## 🔬 3. Architectural Blueprint & Target Classes (Mod-Level Fixes)

### Fix 1: Mod-Level Species Gating in Java (`AnimalHabitatHelper.java`)
```java
public static boolean isSpeciesReproductionAllowed(ServerLevel level, Animal self) {
    if (level == null || self == null) return false;
    
    EntityType<?> type = self.getType();
    ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
    
    // 1. Optional datapack exclusion tag check (if loaded)
    if (type.is(EXCLUDED_ANIMALS)) return false;
    
    // 2. Modded Animal Isolation Guard:
    // If mob is from a third-party mod, strictly require ALLOW_MODDED_ANIMALS GameRule
    boolean isModded = !id.getNamespace().equals("minecraft");
    if (isModded) {
        if (type.is(INCLUDED_ANIMALS)) return true; // Optional datapack whitelist override
        boolean allowModded = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.ALLOW_MODDED_ANIMALS);
        if (!allowModded) return false; // Immediate mod-level fast-fail!
    }
    
    // 3. Registered GameRule check (defaults to false for unknown species)
    return NaturalReproductionFabric.isSpeciesAllowed(level, type);
}
```

### Fix 2: Strict Herd Goal Whitelist (`AnimalBreedingMixin.java`)
Replace blind goal addition with an explicit herd animal check:
```java
// Mod-Level Herd Eligibility Check:
if (naturalreproduction$isEligibleHerdAnimal(level, self) && !naturalreproduction$hasHerdGoal()) {
    this.goalSelector.addGoal(6, new FollowHerdLeaderGoal(self, 1.0D));
}

@Unique
private boolean naturalreproduction$isEligibleHerdAnimal(ServerLevel level, Animal self) {
    if (!DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.HERD_DYNAMICS)) return false;
    
    ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(self.getType());
    boolean isModded = !id.getNamespace().equals("minecraft");
    
    if (isModded) {
        // Modded animals NEVER get herd goals unless MODDED_HERD_DYNAMICS is explicitly enabled
        if (!DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.MODDED_HERD_DYNAMICS)) {
            return false;
        }
    }
    
    // Only attach to genuine pastoral herd animals
    return self.getType().is(AnimalHabitatHelper.HERD_ANIMALS);
}
```

### Fix 3: Native Breeding Preservation (`AnimalBreedingMixin.java`)
Do not hijack breeding unless the species is a supported/enabled livestock animal:
```java
@Inject(method = "spawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
private void naturalreproduction$onSpawnChildGestationCheck(ServerLevel level, Animal mate, CallbackInfo ci) {
    Animal parent1 = (Animal)(Object)this;
    
    // Completely bypass modded / disabled animals — let third-party mods handle their own breeding!
    if (!AnimalHabitatHelper.isSpeciesReproductionAllowed(level, parent1)) {
        return; // Proceeds directly to vanilla / modded child delivery
    }
    
    // Existing gestation logic for enabled livestock...
}
```

### Fix 4: Fast-Fail at Top of `customServerAiStep` (`AnimalBreedingMixin.java`)
```java
@Inject(method = "customServerAiStep", at = @At("HEAD"))
private void naturalreproduction$onCustomServerAiStep(CallbackInfo ci) {
    Animal self = (Animal)(Object)this;
    if (!(self.level() instanceof ServerLevel level)) return;
    
    // Tamed pets bypass completely
    if (self instanceof net.minecraft.world.entity.TamableAnimal tamable && tamable.isTame()) return;
    
    // Mod-Level Check: If not an allowed species, only check herd dynamics if eligible, otherwise return immediately!
    boolean isAllowed = AnimalHabitatHelper.isSpeciesReproductionAllowed(level, self);
    boolean isHerdEligible = naturalreproduction$isEligibleHerdAnimal(level, self);
    
    if (!isAllowed && !isHerdEligible) {
        return; // Zero CPU cost for modded / disabled animals!
    }
    
    // Attach herd goal ONLY if eligible
    if (isHerdEligible && !naturalreproduction$hasHerdGoal()) {
        this.goalSelector.addGoal(6, new FollowHerdLeaderGoal(self, 1.0D));
    }
    
    if (!isAllowed) {
        return; // Skip genetics, gestation, inbreeding, and breeding checks!
    }
    
    // Proceed with livestock-only features (Genetics, Gestation, Lineage, Overgrazing, Wild Breeding)
}
```

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

Implemented identically across all 6 targeted version anchors:
- **MC 1.20.1**: Java 17, `BuiltInRegistries.ENTITY_TYPE.getKey(type)`, `new ResourceLocation("natural-reproduction", ...)`.
- **MC 1.21.1 & 1.21.11**: Java 21, `BuiltInRegistries.ENTITY_TYPE.getKey(type)`, `ResourceLocation.fromNamespaceAndPath(...)`.
- **MC 26.1.2, 26.2, 26.3**: Java 25, `BuiltInRegistries.ENTITY_TYPE.getKey(type)`, `Identifier.fromNamespaceAndPath(...)`.

---

## 🛡️ 5. Edge Cases & Failure Modes

- **Zero Third-Party Mod Crash Risk**: The mod does not reference any modded classes or mod IDs in imports or bytecode. All checks operate via vanilla `EntityType` keys and namespaces.
- **Save File & Entity Integrity**: Modded animals that already spawned in an existing world simply stop running Natural Reproduction's AI goals on their next tick; zero NBT corruption.
- **Manual Modded Breeding**: When a player breeds modded animals (e.g. feeding two Naturalist deer), Natural Reproduction steps aside completely, allowing the other mod's original code, animations, particles, and custom baby logic to execute normally.

---

## 🚀 6. Implementation Steps (Code-Level Plan)

1. Bump `mod_version` in `gradle.properties` across all 6 anchors (1 Step = 1 SemVer Bump).
2. Register `ALLOW_MODDED_ANIMALS` (default `false`) and `MODDED_HERD_DYNAMICS` (default `false`) in `NaturalReproductionFabric.java`.
3. Scaffold `#natural-reproduction:herd_animals` default tag in resources (Cow, Sheep, Pig, Horse, Donkey, Mule, Llama, Trader Llama, Camel, Goat).
4. Update `AnimalHabitatHelper.java` to default non-vanilla mobs to `false` unless explicitly opted in.
5. Update `AnimalBreedingMixin.java` with Fast-Fail top guard, strict herd whitelist, and `spawnChildFromBreeding` bypass for non-livestock mobs.
6. Update `YaclScreenHelper.java` and `NaturalReproductionCommand.java` to expose the new controls in GUI and chat.
7. Execute `./gradlew test` across all 6 anchors to verify 5-Pillar compliance.

---

## 🏁 7. 5-Pillar Verification Checklist

- [x] **Pillar 1 (Boundary & Modded Namespace Fuzzing)**: Fuzz `isSpeciesReproductionAllowed` with synthetic modded namespaces (`naturalist:deer`, `alexsmobs:elephant`, etc.) asserting immediate `false` return when `ALLOW_MODDED_ANIMALS` is `false`.
- [x] **Pillar 2 (Concurrency Load)**: 50 concurrent threads querying species permissions and herd leader elections without deadlock.
- [x] **Pillar 3 (Brigadier AST Verification)**: Commands `/nr set allow_modded_animals <bool>` and `/nr set modded_herd_dynamics <bool>` compile and validate in Brigadier dispatcher.
- [x] **Pillar 4 (AI Fast-Fail & Breeding Preservation)**: Assert that modded animal entities never receive `FollowHerdLeaderGoal`, skip gestation ticks, and that `spawnChildFromBreeding` is NOT cancelled when `isSpeciesReproductionAllowed` is false.
- [x] **Pillar 5 (Knot Sidedness & Classloading)**: Client config GUI reflection compiles headlessly without dedicated server crashes.
