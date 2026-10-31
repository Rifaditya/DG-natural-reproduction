# 🎫 [BL-NR-015] Gestation Timer Acceleration Bug, Beehive Despawn, Untamed Mate Stalling & YACL Screen Reflection Fix

- **Ticket ID**: `[BL-NR-015]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Scope**: `All Anchors (1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3)`
- **Status**: `✅ RESOLVED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`
- **Reported By / Community Credit**: **Night Dragon (Jasmine)** & **Muqington** (Discord Bug Reports & Diagnostics)

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Resolve four interlinked biological reproduction, AI goal, and GUI reflection bugs across all 6 version anchors of `Natural Reproduction`:
   - Eliminate the 20x gestation duration timer elongation bug where `tickGestation` only subtracted 1 tick per 20 ticks.
   - Prevent player-fed manual breeding from entering unintended 20-day gestation timers by defaulting `MANUAL_GESTATION` to `false` (instant vanilla delivery by default, configurable).
   - Exempt `EntityTypes.BEE` from mammalian gestation so bees entering beehives/nests do not lose pregnancy state upon entity discard.
   - Enable untamed wild `TamableAnimal` (wild wolves, wild stray cats) and `AbstractHorse` to complete autonomous mating when within 3 blocks of their mate, bypassing vanilla's `isTame()` check in `BreedGoal`.
   - Expand `AnimalHabitatHelper` pig habitat block triggers to include `GRASS_BLOCK`, `PODZOL`, `COARSE_DIRT`, and `ROOTED_DIRT`.
   - Fix YACL configuration screen reflection crash (`IllegalArgumentException: argument type mismatch`) in `YaclScreenHelper` by wrapping `Component` in array / collection correctly and wiring `OptionDescription`.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: Cows, sheep, pigs, chickens, and bees take 20 Minecraft days (6.6 real hours) to deliver babies. Sprints of 6 days produce zero offspring despite heart particles. Bees lose gestation inside hives. Wild cats and wolves show hearts but never breed. Pigs in grass pens never breed. Opening the config screen on 26.2 crashes with reflection argument mismatch.
   - *After*: A 1-day gestation (`24000` ticks) finishes in exactly 1 Minecraft day (20 real minutes). Manual breeding delivers babies immediately like vanilla. Bees reproduce reliably. Wild cats, wolves, and grass-pasture pigs breed autonomously when conditions are met. YACL config screen opens smoothly without reflection errors.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Do not break or alter the genetics inheritance pipeline, size stunting, or inbreeding degradation logic.
   - ❌ Do not break modded creature isolation from BL-NR-012.
   - ❌ Do not introduce client imports into common source code or crash headless dedicated servers.
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [x] `AnimalGestationHelper.tickGestation` decrements `20.0f` per 20-tick invocation (or `1.0f` per tick).
   - [x] `MANUAL_GESTATION` GameRule defaults to `false` in `NaturalReproductionFabric.java`.
   - [x] `EntityTypes.BEE` is exempt from gestation in `AnimalBreedingMixin.java` and delivers immediately.
   - [x] Untamed cats, wolves, and horses in love mode complete mating when within mating proximity.
   - [x] Pig habitat conditions accept grass blocks, podzol, and dirt variants in `AnimalHabitatHelper.java`.
   - [x] `YaclScreenHelper.java` reflection builds without `IllegalArgumentException`.
   - [x] 5-Pillar automated test suites (`./gradlew test`) pass across all active anchors.

---

## ❓ 2. Problem Statement & Player Impact

Reported by Discord community members **Night Dragon (Jasmine)** and **Muqington**:
1. **20x Gestation Timer Elongation**:
   - `AnimalBreedingMixin.customServerAiStep` calls `AnimalGestationHelper.tickGestation` every 20 ticks:
     `if (self.tickCount % 20 == 0 && DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.GESTATION_PERIOD))`
   - In `AnimalGestationHelper.tickGestation`, the remaining ticks counter was decremented by only `1.0f`:
     `ticksRemaining -= 1.0f;`
   - Because 1 tick was deducted every 20 ticks, a 24,000-tick pregnancy took `480,000` ticks (20 Minecraft days / 6 hours 40 minutes). Players testing breeding over 6 Minecraft days observed heart particles twice but zero offspring.
2. **Beehive Entity Discard Freezing Gestation**:
   - When bees enter hives or nests, Minecraft saves their NBT and discards the living entity from the world (`discard()`). A pregnant bee in a hive never executes `customServerAiStep`, halting gestation indefinitely.
3. **Vanilla BreedGoal Gating on Tamed Status**:
   - Autonomous wild breeding sets `self.setInLove(null)` and `mate.setInLove(null)`. However, vanilla `Cat.canMate()` and `Wolf.canMate()` return `false` if `!this.isTame()`. Untamed animals display hearts but their vanilla `BreedGoal` rejects mating.
4. **Pig Environmental Habitat Limitation**:
   - `AnimalHabitatHelper` only recognized `MUD`, `CARROTS`, `BEETROOTS`, `POTATOES`, and `FARMLAND` for pigs. Pigs kept in standard grass pastures or wild forests never qualified for autonomous breeding.
5. **YACL Reflection Argument Mismatch Crash**:
   - `YaclScreenHelper.buildBooleanOption` invokes `OptionDescription.Builder.text(...)` with a nested `Object[]{new Object[]{description}}`, resulting in `IllegalArgumentException: argument type mismatch` on modern YACL v3.

---

## 🔬 3. Architectural Blueprint & Target Classes

### Fix 1: Exact Gestation Countdown Math (`AnimalGestationHelper.java`)
```java
// Deduct the full elapsed interval (20 ticks) since tickGestation runs once every 20 ticks:
float interval = 20.0f;
ticksRemaining -= interval;
DasikAnimalGeneticsAPI.setTrait(mother, "gestation_ticks", Math.max(0.0f, ticksRemaining));
```

### Fix 2: Manual Gestation Default & Bee Exemption (`AnimalBreedingMixin.java` & `NaturalReproductionFabric.java`)
```java
// 1. In NaturalReproductionFabric.java:
MANUAL_GESTATION = DynamicGameRuleManager.booleanRule("natural-reproduction:manual_gestation", CATEGORY, false)
    .name("Manual Breeding Gestation")
    .description("When true, player manual feeding also uses pregnancy gestation timers. When false, manual feeding delivers immediately.")
    .register();

// 2. In AnimalBreedingMixin.java:
// Bees do not use mammalian gestation timers
if (parent1.getType() == EntityTypes.BEE) {
    return; // Immediate delivery
}
```

### Fix 3: Untamed Cat/Wolf/Horse Proximity Mating (`AnimalBreedingMixin.java`)
```java
// If animal is untamed tamable (cat/wolf) or horse in love mode and within 3 blocks of mate:
if (self.isInLove() && !self.isBaby()) {
    // Deliver offspring directly if vanilla BreedGoal cannot handle untamed mating
}
```

### Fix 4: Expanded Pig Habitat Blocks (`AnimalHabitatHelper.java`)
```java
else if (type == EntityTypes.PIG) {
    return matchesAnyBlock(level, center, 2, 1, 2, state ->
        state.is(Blocks.MUD) || state.is(Blocks.CARROTS) || state.is(Blocks.BEETROOTS) || 
        state.is(Blocks.POTATOES) || state.is(Blocks.FARMLAND) || state.is(Blocks.GRASS_BLOCK) ||
        state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.PODZOL) ||
        state.is(Blocks.ROOTED_DIRT)
    );
}
```

### Fix 5: YACL Reflection Fix (`YaclScreenHelper.java`)
```java
// Pass java.util.List.of(description) or correct varargs array to text(Collection<Component>)
Method textMethod = findMethodWithParam(descBuilder.getClass(), "text", java.util.Collection.class);
if (textMethod != null) {
    textMethod.invoke(descBuilder, java.util.List.of(description));
}
```

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

- **MC 1.20.1, 1.21.1, 1.21.11**: Java 17/21 compatibility, `Component` text descriptions.
- **MC 26.1.2, 26.2, 26.3**: Java 25 compatibility, `Identifier`, modern YACL reflection.

---

## 🛡️ 5. Edge Cases & Failure Modes

- **Save File Resilience**: Existing animals with stored `gestation_ticks` will simply decrement 20x faster, cleanly clearing backlogged pregnancies without NBT corruption.
- **No Desync**: Sidedness contracts and headless test execution remain fully green.

---

## 🚀 6. Implementation Steps (Code-Level Plan)

1. Pre-step SemVer bump across targeted anchors.
2. Apply `AnimalGestationHelper.java` 20-tick subtraction fix.
3. Update `NaturalReproductionFabric.java` for `MANUAL_GESTATION` default `false`.
4. Update `AnimalBreedingMixin.java` for Bee gestation exemption and untamed mate completion.
5. Update `AnimalHabitatHelper.java` for pig habitat blocks.
6. Fix `YaclScreenHelper.java` reflection argument mismatch.
7. Execute `./gradlew test` across all anchors and verify 5 pillars.

---

## 🏁 7. 5-Pillar Verification Checklist

- [x] **Pillar 1 (Gestation Countdown Fuzzing)**: Verify gestation timer reaches 0 in exactly `duration / 20` invocations.
- [x] **Pillar 2 (Concurrency Load)**: Multi-threaded gestation ticking without race conditions.
- [x] **Pillar 3 (Brigadier & YACL AST)**: YACL screen reflection creates `Screen` without `IllegalArgumentException`.
- [x] **Pillar 4 (Mating State Simulation)**: Assert untamed cats, wolves, and bees deliver offspring upon mating.
- [x] **Pillar 5 (Sidedness & Licensing)**: Zero client imports in common code; GPLv3 headers preserved.
