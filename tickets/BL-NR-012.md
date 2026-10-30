# 🎫 [BL-NR-012] Modded Species Dynamic GameRules, Datapack Entity Tags (#excluded_animals / #included_animals) & Disabled-Species AI Fast-Fail

- **Ticket ID**: `[BL-NR-012]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Scope**: `All Anchors (1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3)`
- **Status**: `📌 DEFERRED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Upgrade `Natural Reproduction` across all 6 Minecraft version anchors so modded animals (e.g., Naturalist, Alex's Mobs, Critters & Companions) can be controlled natively via auto-generated dynamic GameRules, `/nr set` commands, YACL GUI toggles, and datapack `EntityType` tags (`#natural-reproduction:excluded_animals` and `#natural-reproduction:included_animals`), while skipping herd AI and tick overhead on disabled species.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: `AnimalHabitatHelper.isSpeciesReproductionAllowed` returns `true` unconditionally for any `Animal` not present in the hardcoded 22-entry vanilla `SPECIES_TOGGLES` map, and `AnimalBreedingMixin` attaches `FollowHerdLeaderGoal` and ticks gestation/lineage/overgrazing on all `Animal` instances regardless of whether the species is disabled.
   - *After*: Players and modpack authors can toggle modded animals globally (`allow_modded_animals`), per modded species (`natural-reproduction:allow_<namespace>_<path>`), or via datapack tags (`#natural-reproduction:excluded_animals` / `#natural-reproduction:included_animals`) through `/gamerule`, `/nr set`, or the YACL config screen, and disabled species immediately fast-fail with zero herd AI or tick cost.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Do not hardcode specific third-party mod IDs in Java logic; discover modded creature `EntityType` entries dynamically from `BuiltInRegistries.ENTITY_TYPE`.
   - ❌ Do not alter default breeding rates, gestation timers, or density caps for the 22 vanilla animal species.
   - ❌ Do not break server-side classloading when YACL or ModMenu is absent.
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [ ] `ALLOW_MODDED_ANIMALS` boolean GameRule (default `true`) and dynamic `natural-reproduction:allow_<namespace>_<path>` boolean GameRules registered across all 6 anchors.
   - [ ] `TagKey<EntityType<?>> EXCLUDED_ANIMALS` and `INCLUDED_ANIMALS` implemented in `AnimalHabitatHelper.isSpeciesReproductionAllowed`.
   - [ ] `FollowHerdLeaderGoal`, `tickGestation`, `tickLethalCollapse`, and `processOvergrazing` gated behind `AnimalHabitatHelper.isSpeciesReproductionAllowed(level, self)` in `AnimalBreedingMixin`.
   - [ ] `/nr set` Brigadier command and client YACL config screen dynamically expose registered modded species rules.
   - [ ] Default empty `excluded_animals.json` and `included_animals.json` bundled in resources across all 6 anchors (`tags/entity_types/` on 1.20.1; `tags/entity_type/` on 1.21.1–26.3).
   - [ ] 5-Pillar autonomous test suites (`./gradlew test`) pass across all 6 version anchors.

---

## ❓ 2. Problem Statement & Player Impact

In `AnimalHabitatHelper.java`, `isSpeciesReproductionAllowed(ServerLevel, Animal)` checks `NaturalReproductionFabric.SPECIES_TOGGLES.get(self.getType())` and returns `true` whenever `ruleKey == null`. Because only the 22 vanilla animals are registered in `SPECIES_TOGGLES`, every modded `Animal` subclass (such as the 47 creatures in **Naturalist** or dozens in **Alex's Mobs**) breeds autonomously with no in-game GameRule, `/nr` command, YACL GUI toggle, or datapack tag to exclude them. Furthermore, `AnimalBreedingMixin` injects `FollowHerdLeaderGoal` and executes gestation, lineage, and pasture checks even when a species is disabled.

---

## 🔬 3. Architectural Blueprint & Target Files

- **Target Files Across All 6 Anchors (`1.20.1`, `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3`)**:
  - `src/main/java/net/dasik/naturalreproduction/NaturalReproductionFabric.java`:
    - Add `ALLOW_MODDED_ANIMALS` boolean GameRule (`allow_modded_animals`, default `true`).
    - Scan `BuiltInRegistries.ENTITY_TYPE` (plus `RegistryEntryAddedCallback.event(BuiltInRegistries.ENTITY_TYPE)` for late-registered modded entities) for non-vanilla (`!id.getNamespace().equals("minecraft")`) creature entities (`MobCategory.CREATURE`, `AXOLOTLS`, `WATER_CREATURE`), dynamically registering `natural-reproduction:allow_<namespace>_<path>` boolean GameRules (default `true`) into `SPECIES_TOGGLES` and `RULE_NAME_MAP`.
  - `src/main/java/net/dasik/naturalreproduction/util/AnimalHabitatHelper.java`:
    - Define `TagKey<EntityType<?>> EXCLUDED_ANIMALS` (`natural-reproduction:excluded_animals`) and `INCLUDED_ANIMALS` (`natural-reproduction:included_animals`).
    - In `isSpeciesReproductionAllowed(ServerLevel, Animal)`:
      1. If `self.getType().is(EXCLUDED_ANIMALS)` -> return `false`.
      2. If `self.getType().is(INCLUDED_ANIMALS)` -> check per-species rule (if present), otherwise return `true` (allowing datapacks to explicitly add/whitelist custom mobs even when `allow_modded_animals` is `false`).
      3. For non-vanilla mobs, check `ALLOW_MODDED_ANIMALS` and per-species dynamic rule `SPECIES_TOGGLES.get(type)`.
  - `src/main/java/net/dasik/naturalreproduction/mixin/AnimalBreedingMixin.java`:
    - Gate `FollowHerdLeaderGoal` execution, `AnimalGestationHelper.tickGestation`, `AnimalLineageHelper.tickLethalCollapse`, and `AnimalPastureHelper.processOvergrazing` behind `AnimalHabitatHelper.isSpeciesReproductionAllowed(level, self)` so disabled/excluded species incur zero AI or tick cost.
  - `src/main/java/net/dasik/naturalreproduction/command/NaturalReproductionCommand.java` & client YACL config screen:
    - Dynamically expose registered modded species rules in `/nr set` and YACL GUI.
  - Default resource tags:
    - Bundle empty default `excluded_animals.json` and `included_animals.json` under `src/main/resources/data/natural-reproduction/tags/entity_types/` (1.20.1) and `src/main/resources/data/natural-reproduction/tags/entity_type/` (1.21.1–26.3).

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

- **MC 1.20.1**: Uses `ResourceLocation.fromNamespaceAndPath` / `new ResourceLocation("natural-reproduction", ...)`, `GameRules.RuleKey<GameRules.BooleanValue>`, and `data/natural-reproduction/tags/entity_types/`.
- **MC 1.21.1 & 1.21.11**: Uses `Identifier.fromNamespaceAndPath("natural-reproduction", ...)` / `ResourceLocation.fromNamespaceAndPath(...)` and `data/natural-reproduction/tags/entity_type/`.
- **MC 26.1.2, 26.2, 26.3**: Uses `GameRule<Boolean>` registry (`level.getGameRules().get(rule)`), `Identifier.fromNamespaceAndPath("natural-reproduction", ...)`, and `data/natural-reproduction/tags/entity_type/`.

---

## 🛡️ 5. Edge Cases, Collisions & Failure Modes

- **Mod Load Order**: Third-party mods may register `EntityType` entries before or after `NaturalReproductionFabric.onInitialize()`. Using both an initial pass over `BuiltInRegistries.ENTITY_TYPE` and `RegistryEntryAddedCallback.event(BuiltInRegistries.ENTITY_TYPE)` guarantees all modded creatures are captured regardless of entrypoint ordering (`putIfAbsent` prevents duplicate registration).
- **Precedence Hierarchy**: `#natural-reproduction:excluded_animals` always wins (`false`). `#natural-reproduction:included_animals` bypasses `allow_modded_animals = false` while still respecting the specific species GameRule if toggled off.
- **Zero Hot-Path Allocation**: `TagKey` instances (`EXCLUDED_ANIMALS`, `INCLUDED_ANIMALS`) are `public static final` constants; `self.getType().is(...)` is an $O(1)$ reference check.

---

## 🚀 6. Implementation Steps (Execution Plan)

1. Bump `mod_version` in `gradle.properties` across all 6 anchors per the SemVer Calibration & Ground-Zero Bump Invariant before compiling.
2. Implement `ALLOW_MODDED_ANIMALS` and dynamic modded creature GameRule registration in `NaturalReproductionFabric.java` across all 6 anchors.
3. Add `EXCLUDED_ANIMALS` and `INCLUDED_ANIMALS` `TagKey<EntityType<?>>` constants and precedence logic to `AnimalHabitatHelper.isSpeciesReproductionAllowed` across all 6 anchors.
4. Gate `FollowHerdLeaderGoal` and tick helpers in `AnimalBreedingMixin.java` behind `isSpeciesReproductionAllowed`.
5. Update `NaturalReproductionCommand.java`, client YACL config integration, and default tag JSON files across all 6 anchors.
6. Run `./gradlew test` and `./gradlew build` across all 6 anchors to verify 5-Pillar compliance.

---

## 🏁 7. 5-Pillar Verification Checklist

- [ ] **Pillar 1 (Boundary & Tag Fuzzing)**: Unit tests verify precedence of `EXCLUDED_ANIMALS`, `INCLUDED_ANIMALS`, `ALLOW_MODDED_ANIMALS`, and per-species dynamic rules across vanilla and synthetic modded `EntityType` keys.
- [ ] **Pillar 2 (Concurrency & Registry Idempotency)**: `putIfAbsent` guards prevent duplicate GameRule registration across initial registry sweep and `RegistryEntryAddedCallback`.
- [ ] **Pillar 3 (Brigadier AST Verification)**: `/nr set allow_modded_animals` and dynamic modded species rules resolve cleanly in Brigadier command tree tests.
- [ ] **Pillar 4 (State Mutation & AI Fast-Fail)**: Disabled or tag-excluded species skip `FollowHerdLeaderGoal`, gestation, lineage, and pasture tick processing.
- [ ] **Pillar 5 (Knot Sidedness & Descriptor Audit)**: Zero client-only classes loaded on dedicated server; Mixin descriptors verified across all 6 anchors.
