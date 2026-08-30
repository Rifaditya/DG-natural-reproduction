# 📌 Natural Reproduction Backlog

This file tracks planned features, technical refinements, performance optimizations, and deferred bug fixes for **Natural Reproduction** under the **Delayed Gratification (DG)** design track (*"Invest in the world, and the world pays dividends"*).

---

## 🛡 Code-Only & Vanilla-First Architectural Directive
In strict adherence to the project's **Code & Vanilla First Asset Rule** (`[DIR-20260626-001]`), all mechanics planned in this backlog are engineered **strictly using code and native vanilla resources**:
- **Visual & Physical States**: Handled purely via vanilla entity attributes (`minecraft:scale`, `generic.max_health`, `generic.movement_speed`), vanilla DataComponents, and dynamic vanilla particle emitters (`ParticleTypes.WAX_ON`, `SMOKE`, `ANGRY_VILLAGER`, `HAPPY_VILLAGER`, `SQUID_INK`).
- **World & Habitat Structures**: Utilizes existing interactive vanilla blocks (`Blocks.CAULDRON`, `Blocks.COMPOSTER`, `Blocks.HAY_BLOCK`, `Blocks.WATER`, `Blocks.GRASS_BLOCK`, `Blocks.DIRT`) without custom blocks or 3D block models.
- **Entity Identity & Breeding**: Powered entirely by vanilla entity models, vanilla egg entities/blocks (`Blocks.FROGSPAWN`, `Blocks.TURTLE_EGG`), vanilla sound events, and custom Java AI goals (`GoalSelector`). **Zero custom 3D models or external textures required.**

---

## 📊 Backlog Summary

| ID | Category | Title | Priority | Status |
| :--- | :--- | :--- | :--- | :--- |
| `[BL-NR-001]` | `[FEATURE]` | Multi-Generational Inbreeding Lineage Degradation & Hybrid Vigor System | `[HIGH]` | `✅ RESOLVED` |
| `[BL-NR-002]` | `[FEATURE]` | Pasture Enrichment, Rotational Grazing & Feeding Trough Dynamics | `[HIGH]` | `✅ RESOLVED` |
| `[BL-NR-003]` | `[FEATURE]` | Autonomous Gestation Timers & Prenatal Pasture Vitality | `[MEDIUM]` | `✅ RESOLVED` |
| `[BL-NR-004]` | `[FEATURE]` | Herd Social Cohesion, Alpha Leadership & Flock Movement AI | `[MEDIUM]` | `✅ RESOLVED` |
| `[BL-NR-005]` | `[PERF]` | Zero-Allocation Spatial Partitioning & High-Mob Density Throttling | `[HIGH]` | `✅ RESOLVED` |
| `[BL-NR-006]` | `[TECH_DEBT]` | Automated Headless JUnit & Fabric GameTest Verification Suite | `[MEDIUM]` | `📌 DEFERRED` |
| `[BL-NR-007]` | `[FEATURE]` | Multi-Era Anchor Porting & 1 Jar 1 Version Matrix (`1.20.1`, `1.21.1`, `1.21.11`, `26.3+`) | `[MEDIUM]` | `📌 DEFERRED` |
| `[BL-NR-008]` | `[BUGFIX]` | Fix Entity Scale Modifier Offset & Attribute Stacking Causing Ubiquitous Giant Mob Sizes | `[HIGH]` | `✅ RESOLVED` |

---

## 🏷 Legend & Status Tags
- **Categories**: `[FEATURE]`, `[REFINEMENT]`, `[BUGFIX]`, `[PERF]`, `[TECH_DEBT]`
- **Priorities**: `[HIGH]` (Critical logic fix/enhancement), `[MEDIUM]` (Quality of life / optimization), `[LOW]` (Minor polish)
- **Statuses**: `📌 DEFERRED` (Queued for future work), `🚧 IN_PROGRESS` (Active development), `✅ RESOLVED` (Implemented and verified)

---

## 📝 Detailed Backlog Entries

### [BL-NR-001] Multi-Generational Inbreeding Lineage Degradation & Hybrid Vigor System
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Status**: `✅ RESOLVED`
- **Asset Mode**: `Strictly Code-Only (Vanilla Attributes + Loot Override + Particle Effects)`
- **Target Component(s)**: `[AnimalLineageHelper.java](src/main/java/net/vanillaoutsider/naturalreproduction/util/AnimalLineageHelper.java)`, `[AnimalBreedingMixin.java](src/main/java/net/vanillaoutsider/naturalreproduction/mixin/AnimalBreedingMixin.java)`, `[AnimalDropHelper.java](src/main/java/net/vanillaoutsider/naturalreproduction/util/AnimalDropHelper.java)`
- **Date Added**: 2026-08-15

#### ❓ Problem / Context
In the **Delayed Gratification (DG)** design philosophy, the central *Deep Consequence* pillar dictates:
> *"Animals can reproduce autonomously, but leaving a closed herd to inbreed over generations severely degrades their genetics, eventually causing them to only drop rotten flesh."*

Currently in Natural Reproduction:
1. `AnimalBreedingMixin` invokes `DasikAnimalGeneticsAPI.inherit(baby, parent1, mate, "default")`, which records parent UUIDs into pedigree NBT, but no downstream degradation or inbreeding consequence is applied.
2. A player can trap 2–4 cows in a small isolated pen indefinitely, and they will autonomously reproduce healthy offspring forever without needing fresh genetic diversity or pasture management.
3. There is no gameplay incentive for players to venture into the wild to capture fresh breeding stock or maintain multi-lineage breeding programs.

#### 💡 Proposed Solution & Technical Specifications
1. **Lineage Coefficient Calculation in `AnimalLineageHelper.java`**:
   - Compare the ancestry records of `parent1` and `parent2` using `DasikAnimalGeneticsAPI` pedigree tracking.
   - Calculate the inbreeding tier:
     - **Tier 0 (Diverse / Wild)**: Parents share no common ancestors in the last 3 generations.
     - **Tier 1 (Mild Inbreeding)**: First-degree cousins or 1 shared grandparent.
     - **Tier 2 (Close Inbreeding)**: Half-siblings or parent-offspring mating.
     - **Tier 3 (Degraded Lineage)**: Full siblings or repeated closed-herd mating over 3+ consecutive generations.

2. **Code-Only Physiological & Loot Degradation Effects**:
   - **Scale & Health Stunting**: Tier 2+ inbreeding applies an additional `-20%` to `-40%` scaling penalty via native `minecraft:scale` and reduces `generic.max_health` down to `50%` of species base.
   - **Lethargic Speed**: Inbred animals receive `-30%` movement speed modifier (`generic.movement_speed`).
   - **Degraded Loot Table Interception (`AnimalDropHelper.java`)**:
     - At **Tier 3 (Degraded Lineage)**, when `natural-reproduction:inbreeding_degradation` is enabled:
       - Prime meat drops (e.g. `raw_beef`, `porkchop`, `mutton`, `chicken`) are converted via Java drop interception into `Items.ROTTEN_FLESH` and `Items.BONE` with `25%` drop volume.
       - Leather/wool drops drop at only `25%` normal rates.
   - **Visual Particles**: Inbred births emit vanilla `ParticleTypes.SQUID_INK` and `ParticleTypes.ANGRY_VILLAGER`.

3. **Hybrid Vigor (Heterosis) Recovery**:
   - Breeding a degraded animal with an unrelated wild animal (Tier 0) completely cleanses the degradation tier and awards a **+20% Hybrid Vigor boost** to offspring scale and vitality.

4. **GameRule Configuration**:
   - `natural-reproduction:inbreeding_degradation` (Boolean, default `true`).
   - Integrated into YACL GUI and `/naturalreproduction` command suite.

```java
public final class AnimalLineageHelper {
    public static int calculateInbreedingTier(Animal parent1, Animal parent2) {
        if (parent1 == null || parent2 == null) return 0;
        // Query ancestor UUIDs from DasikAnimalGeneticsAPI
        Set<UUID> ancestors1 = DasikAnimalGeneticsAPI.getAncestors(parent1, 3);
        Set<UUID> ancestors2 = DasikAnimalGeneticsAPI.getAncestors(parent2, 3);
        
        int sharedCount = 0;
        for (UUID u : ancestors1) {
            if (ancestors2.contains(u)) sharedCount++;
        }
        if (sharedCount >= 3) return 3; // Severe closed-loop
        if (sharedCount >= 2) return 2; // Close inbreeding
        if (sharedCount == 1) return 1; // Mild
        return 0; // Fresh blood / Heterosis
    }
}
```

#### 🎯 Acceptance Criteria
- [ ] Mating full siblings or closed-herd animals over 3 generations increases inbreeding tier to Tier 3.
- [ ] Tier 3 degraded animals drop Rotten Flesh and Bones instead of standard raw meat when killed.
- [ ] Mating a degraded animal with an unrelated wild animal triggers Hybrid Vigor (+20% size and health boost).
- [ ] `natural-reproduction:inbreeding_degradation` GameRule completely disables inbreeding penalties when set to `false`.
- [ ] Implemented purely via code with zero custom models or external textures.

---

### [BL-NR-002] Pasture Enrichment, Rotational Grazing & Feeding Trough Dynamics
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Status**: `✅ RESOLVED`
- **Asset Mode**: `Strictly Code-Only (Vanilla Interactive Blocks + Native Particles)`
- **Target Component(s)**: `[AnimalPastureHelper.java](src/main/java/net/vanillaoutsider/naturalreproduction/util/AnimalPastureHelper.java)`, `[PastureGrazingGoal.java](src/main/java/net/vanillaoutsider/naturalreproduction/ai/PastureGrazingGoal.java)`, `[AnimalBreedingMixin.java](src/main/java/net/vanillaoutsider/naturalreproduction/mixin/AnimalBreedingMixin.java)`
- **Date Added**: 2026-08-15

#### ❓ Problem / Context
1. In the current implementation, `AnimalHabitatHelper.hasEnvironmentalBreedingConditions` only does a static 1-block proximity lookup (e.g. checking if `Blocks.GRASS_BLOCK` is within 2 blocks) at the moment of breeding roll.
2. Animals do not consume grass or alter pasture blocks dynamically, allowing infinite herds to survive on a single 1x1 patch of grass surrounded by cobblestone.
3. Players have no incentive to construct spacious pastures with rotational grazing paddocks, hay feeders, or water ponds.

#### 💡 Proposed Solution & Technical Specifications
1. **Dynamic Pasture Grazing & Overgrazing Mechanic**:
   - Add `PastureGrazingGoal` to passive livestock.
   - When an animal grazes, if more than 6 animals graze in the same 8-block area within an in-game day:
     - `Blocks.GRASS_BLOCK` converts into `Blocks.DIRT` or `Blocks.COARSE_DIRT` (Overgrazing).
     - Overgrazed pastures lose their autonomous breeding eligibility until grass regrows naturally or spreads from adjacent fertile soil.

2. **Vanilla Pasture Enrichment & Feeding Troughs (Zero Custom Models)**:
   - Recognize player-constructed native enrichment blocks within a 16-block radius of the herd:
     - **Feeding Troughs**: Vanilla `Blocks.CAULDRON` or `Blocks.COMPOSTER` filled with Wheat, Carrots, Beetroot, or adjacent `Blocks.HAY_BLOCK`.
     - **Clean Water**: Access to vanilla water blocks (`Blocks.WATER`) within 8 blocks of grazing ground.
     - **Sheltered Barn**: Hay Bales situated under solid roof structures (protecting from rain/storms).
   - Animals living in Enriched Pastures gain the **"Well-Nourished"** state:
     - +25% faster autonomous breeding rate (half tick interval).
     - +10% maximum scale potential (allowing herds to reach up to `1.30x` scale reliably).
     - Golden sparkle particles (`ParticleTypes.WAX_ON`) emit periodically from well-nourished animals.

3. **GameRule Configuration**:
   - `natural-reproduction:pasture_enrichment` (Boolean, default `true`).
   - `natural-reproduction:overgrazing` (Boolean, default `true`).

#### 🎯 Acceptance Criteria
- [ ] High-density grazing dynamically converts overgrazed grass blocks into dirt/coarse dirt over time.
- [ ] Animals in pastures containing feeding troughs (vanilla cauldrons/composters/hay) and water gain the Well-Nourished status.
- [ ] Well-Nourished animals breed faster and produce higher quality offspring.
- [ ] Overgrazed pastures without fresh grass temporarily halt autonomous reproduction until recovery.
- [ ] Built purely from vanilla blocks and particle events.

---

### [BL-NR-003] Autonomous Gestation Timers & Prenatal Pasture Vitality
- **Category**: `[FEATURE]`
- **Priority**: `[MEDIUM]`
- **Status**: `✅ RESOLVED`
- **Asset Mode**: `Strictly Code-Only (NBT Attachments + Pacing AI + Vanilla Egg Blocks)`
- **Target Component(s)**: `[AnimalGestationHelper.java](src/main/java/net/vanillaoutsider/naturalreproduction/util/AnimalGestationHelper.java)`, `[AnimalBreedingMixin.java](src/main/java/net/vanillaoutsider/naturalreproduction/mixin/AnimalBreedingMixin.java)`
- **Date Added**: 2026-08-15

#### ❓ Problem / Context
In vanilla Minecraft, animal breeding is instantaneous: as soon as two animals touch in love mode, a baby mob pops out instantly. Under Delayed Gratification principles, instantaneous baby popping breaks immersion and bypasses the gameplay loop of caring for pregnant livestock during gestation.

#### 💡 Proposed Solution & Technical Specifications
1. **Gestation Period State**:
   - When autonomous breeding occurs, instead of calling `spawnChildFromBreeding` immediately:
     - The female/mother animal enters a **Gestation** state stored in NBT attachment (`GestationTicksRemaining`, default: `24000` ticks = 1 in-game day).
     - Both parents exit love mode normally.
     - Mother displays subtle pacing behavior (slightly reduced wander speed) and occasional warmth particles (`ParticleTypes.HEART` / `ParticleTypes.HAPPY_VILLAGER`). No custom model meshes or texture swaps required.

2. **Prenatal Pasture Care (Vitality Bonus)**:
   - If the mother spends her gestation period in a spacious, enriched pasture with abundant food:
     - Offspring is born with the **"Prenatal Vitality"** trait (+15% base max health, +10% movement speed).
   - If the mother is starved, injured, or confined in a cramped dark pit during gestation:
     - Offspring suffers birth stunting (-20% size and reduced health).

3. **Egg-Laying Species Synergy (Vanilla Egg Blocks/Entities)**:
   - For Oviparous animals (Chickens, Turtles, Frogs, Sniffers):
     - Gestation results in laying native egg items/blocks (`Blocks.FROGSPAWN`, `Blocks.TURTLE_EGG`, `Blocks.SNIFFER_EGG`, or dropped vanilla eggs) in suitable nesting blocks (Hay Bales, Sand, Water) with incubation timers before hatching.

4. **GameRule Configuration**:
   - `natural-reproduction:gestation_period` (Boolean, default `true`).
   - `natural-reproduction:gestation_duration` (Integer, default `24000` ticks).

#### 🎯 Acceptance Criteria
- [ ] Autonomous breeding triggers a configurable gestation timer instead of instantaneous offspring spawn.
- [ ] Mother animals display gestation state and retain pregnancy through world save/reload.
- [ ] Mothers kept in spacious enriched pastures deliver calves with the Prenatal Vitality trait.
- [ ] Setting `gestation_period` to `false` restores immediate vanilla birth behavior.
- [ ] Uses 100% native entity models, NBT state, and vanilla egg blocks.

---

### [BL-NR-004] Herd Social Cohesion, Alpha Leadership & Flock Movement AI
- **Category**: `[FEATURE]`
- **Priority**: `[MEDIUM]`
- **Status**: `✅ RESOLVED`
- **Asset Mode**: `Strictly Code-Only (Custom AI Goal + Vector Math + Vanilla Sounds)`
- **Target Component(s)**: `[HerdLeaderGoal.java](src/main/java/net/vanillaoutsider/naturalreproduction/ai/HerdLeaderGoal.java)`, `[HerdSocialHelper.java](src/main/java/net/vanillaoutsider/naturalreproduction/util/HerdSocialHelper.java)`
- **Date Added**: 2026-08-15

#### ❓ Problem / Context
Livestock in pastures wander completely independently as isolated entities without group dynamics or herd cohesion. In nature and sim-like pastoral environments, grazing animals move in synchronized herds led by an elder matriarch or alpha male.

#### 💡 Proposed Solution & Technical Specifications
1. **Dynamic Alpha Leader Election**:
   - When 4 or more animals of the same species share a pasture (within a 24-block radius), `HerdSocialHelper` automatically elects the animal with the highest physical scale (`minecraft:scale`) and age as the **Herd Leader** (Alpha).
   - Herd members assign a soft attraction vector towards the leader during wander and grazing routines.

2. **Coordinated Daily Schedule**:
   - **Morning (0–6000 ticks)**: The leader leads the herd to open grazing areas.
   - **Midday (6000–9000 ticks)**: The herd moves towards water sources or shaded tree canopies.
   - **Dusk/Night (12000–18000 ticks)**: The herd clusters together near barn shelters or enclosed fencing to sleep and protect calves.

3. **Predator Alarm & Stampede AI**:
   - If a predator (Wolf, Fox, player wielding a weapon) attacks any herd member:
     - The victim sounds a loud species distress call (`SoundEvents.COW_HURT`, `SoundEvents.SHEEP_HURT`, etc.).
     - Herd members enter a collective "Alert / Stampede" panic state, fleeing in unison away from the threat vector.

4. **GameRule Configuration**:
   - `natural-reproduction:herd_dynamics` (Boolean, default `true`).

#### 🎯 Acceptance Criteria
- [ ] Groups of 4+ animals loosely cluster and follow the highest-scale Alpha leader during grazing.
- [ ] Herds seek shelter together at dusk and water during midday.
- [ ] Predator attacks trigger coordinated herd flight behavior.
- [ ] Code-only implementation using vanilla GoalSelector and sound registries.

---

### [BL-NR-005] Zero-Allocation Spatial Partitioning & High-Mob Density Throttling
- **Category**: `[PERF]`
- **Priority**: `[HIGH]`
- **Status**: `✅ RESOLVED`
- **Asset Mode**: `Strictly Code-Only (Spatial Hash Grid + Object Pool)`
- **Target Component(s)**: `[SpatialBreedingCacheHelper.java](src/main/java/net/vanillaoutsider/naturalreproduction/util/SpatialBreedingCacheHelper.java)`, `[AnimalBreedingMixin.java](src/main/java/net/vanillaoutsider/naturalreproduction/mixin/AnimalBreedingMixin.java)`
- **Date Added**: 2026-08-15

#### ❓ Problem / Context
In `AnimalBreedingMixin.java`, `customServerAiStep` executes broad-phase bounding box queries (`level.getEntitiesOfClass(Animal.class, self.getBoundingBox().inflate(16.0))`) and habitat scans for every individual animal in loaded chunks every tick. On large-scale farms or servers with 100–300+ animals, repeated un-throttled bounding box queries allocate short-lived heap objects and introduce unnecessary server tick overhead.

#### 💡 Proposed Solution & Technical Specifications
1. **Distributed Tick-Modulo Staggering**:
   - Distribute animal breeding checks across tick cycles using entity ID modulo:
     ```java
     long tick = level.getGameTime();
     if ((self.getId() + tick) % 100 != 0) {
         return; // Only evaluate breeding urge once every 5 seconds per entity
     }
     ```

2. **Spatial Chunk-Level Density Caching**:
   - Cache local animal counts per sub-chunk (16x16 block column) in `SpatialBreedingCacheHelper`.
   - Update spatial density caches at a low frequency (once every 100 ticks = 5 seconds) rather than querying `level.getEntitiesOfClass` on every entity tick.
   - Reuse pre-allocated bounding box structures and spatial lookup tables to achieve zero heap allocations in the critical server AI loop.

3. **Performance Target**:
   - Reduce entity lookup overhead by `>80%` on servers with 200+ livestock entities.

#### 🎯 Acceptance Criteria
- [x] Server tick time (MSPT) remains constant with 200+ passive livestock in loaded chunks.
- [x] Zero object allocation in per-tick entity query paths during steady state.
- [x] Autonomous reproduction rate and density checks remain accurate and responsive.

---

### [BL-NR-006] Automated Headless JUnit & Fabric GameTest Verification Suite
- **Category**: `[TECH_DEBT]`
- **Priority**: `[MEDIUM]`
- **Status**: `📌 DEFERRED`
- **Asset Mode**: `Strictly Code-Only (JUnit + GameTest Framework)`
- **Target Component(s)**: `[NaturalReproductionTest.java](src/test/java/net/vanillaoutsider/naturalreproduction/NaturalReproductionTest.java)`, `[NaturalReproductionGameTests.java](src/test/java/net/vanillaoutsider/naturalreproduction/gametest/NaturalReproductionGameTests.java)`
- **Date Added**: 2026-08-15

#### ❓ Problem / Context
The current unit test suite only asserts basic GameRule constants. As complex Delayed Gratification mechanics (inbreeding degradation, continuous density stunting, pasture enrichment, gestation periods) expand, comprehensive automated testing is required to prevent regressions across version updates.

#### 💡 Proposed Solution & Technical Specifications
1. **Headless JUnit Assertions**:
   - **Stunting Math Test**: Assert continuous stunting curve (`penaltyMultiplier = Math.max(0.95 - count * 0.05, 0.40)`) across count inputs 1 through 20.
   - **Inbreeding Tier Test**: Assert pedigree ancestry matching across 3 generations.
   - **Drop Yield Test**: Assert item count scaling across entity scales `0.25x` to `1.50x`.
   - **Scale Bound Clamping**: Assert dynamic `min_scale` and `max_scale` limits.

2. **Fabric GameTest Headless Structures**:
   - `testCrowdedPenStunting`: Spawn 8 cows in a 3x3 fence pen, verify baby calf is stunted to `<= 0.60x` scale.
   - `testSpaciousPastureRecovery`: Spawn 2 stunted cows in a 32x32 open meadow, verify offspring recovers scale towards `1.30x`.
   - `testDensityCapSuppression`: Spawn 15 sheep in a 16-block radius, verify love mode is suppressed.

#### 🎯 Acceptance Criteria
- [ ] `./gradlew test` executes and passes 100% of headless unit tests.
- [ ] GameTests verify real in-world reproduction behaviors in headless environment.

---

### [BL-NR-007] Multi-Era Anchor Porting & 1 Jar 1 Version Matrix (`1.20.1`, `1.21.1`, `1.21.11`, `26.3+`)
- **Category**: `[FEATURE]`
- **Priority**: `[MEDIUM]`
- **Status**: `📌 DEFERRED`
- **Asset Mode**: `Strictly Code-Only (Multi-Version Gradle Subprojects)`
- **Target Component(s)**: Subprojects: `Natural Reproduction v1.20.1`, `Natural Reproduction v1.21.1`, `Natural Reproduction v1.21.11`, `Natural Reproduction v26.3`
- **Date Added**: 2026-08-15

#### ❓ Problem / Context
Natural Reproduction is currently built only for Minecraft 26.2. Under the workspace **Universal Multi-Era Version Architecture Law** and **1 Jar 1 Version Law**, flagship Delayed Gratification mods must provide native, zero-compromise builds for all targeted version anchors.

#### 💡 Proposed Solution & Technical Specifications
1. **Legacy Anchor (`MC 1.20.1`)**:
   - Directory: `Delayed Gratification Collection/Natural Reproduction/Natural Reproduction v1.20.1/natural-reproduction-v1201/`
   - Java 17, Loom 1.10, Mojang mappings, `new Identifier(modid, path)`, NBT `CompoundTag`, `MatrixStack` render scaling.

2. **Transitional Anchor Early (`MC 1.21.1`)**:
   - Directory: `Delayed Gratification Collection/Natural Reproduction/Natural Reproduction v1.21.1/natural-reproduction-v1211/`
   - Java 21, `Identifier.of(modid, path)`, `DataComponents`, native `Attributes.SCALE`.

3. **Transitional Anchor Late / Winter Drop (`MC 1.21.11`)**:
   - Directory: `Delayed Gratification Collection/Natural Reproduction/Natural Reproduction v1.21.11/natural-reproduction-v12111/`
   - Java 21, Loom 1.15 remapping plugin, `ValueOutput`/`ValueInput` serialization, relocated `net.minecraft.world.entity.animal.wolf.Wolf` package, `GameRuleCategory.register()`.

4. **Modern Sovereign Drops (`MC 26.3`, `MC 27.x`+)**:
   - Directory: `Delayed Gratification Collection/Natural Reproduction/Natural Reproduction v26.3/natural-reproduction-26.3/`
   - Java 25+, non-obfuscated runtime, `Identifier.fromNamespaceAndPath()`, `EntityTypes`, `SavedDataType` Codecs.

#### 🎯 Acceptance Criteria
- [ ] Standalone Gradle subproject for each version anchor compiles cleanly.
- [ ] Each subproject passes headless test suite.
- [ ] Release JARs archived into `Archive Jar of all versions/MC <Version>/`.

---

### [BL-NR-008] Fix Entity Scale Modifier Offset & Attribute Stacking Causing Ubiquitous Giant Mob Sizes
- **Category**: `[BUGFIX]`
- **Priority**: `[HIGH]`
- **Status**: `📌 DEFERRED`
- **Asset Mode**: `Strictly Code-Only (AttributeModifier Math & Trait Configuration)`
- **Target Component(s)**: `[NaturalReproductionFabric.java](src/main/java/net/vanillaoutsider/naturalreproduction/NaturalReproductionFabric.java)`, `[GeneticsEngine.java](../../DasikLibrary-Rebuilt/src/main/java/net/dasik/social/api/genetics/GeneticsEngine.java)`, `[DasikAnimalGeneticsAPI.java](../../DasikLibrary-Rebuilt/src/main/java/net/dasik/social/api/genetics/DasikAnimalGeneticsAPI.java)`
- **Date Added**: 2026-08-27

#### ❓ Problem / Context
As reported from live in-game testing (ref: screenshot `2026-08-27_08.28.52_4k.png`), all passive animals and reproduction offspring render at giant, oversized dimensions:
1. In Minecraft 26.2+, the native base value of the `minecraft:scale` (`Attributes.SCALE`) attribute is `1.0` (100% normal vanilla scale).
2. In `NaturalReproductionFabric.java`, the `"scale"` trait is registered as:
   ```java
   "scale", new TraitConfig("scale", "minecraft:scale", "ADD_VALUE", 0.0f, 1.0f, 0.50f, 1.30f)
   ```
   with mutation rule:
   ```java
   "scale", new MutationRule("uniform", 0.50f, 1.30f)
   ```
3. When `GeneticsEngine.applyGeneticsModifiers` applies the rolled trait value directly using `AttributeModifier.Operation.ADD_VALUE`:
   - The rolled value `val` (between `0.50` and `1.30`) is added **directly on top** of the base scale `1.0`.
   - **Baseline Roll (`1.00`)**: `1.0 + 1.00 = 2.00x` scale (200% size — twice as large as normal).
   - **Max Potential Roll (`1.30`)**: `1.0 + 1.30 = 2.30x` scale (230% size — massive giant).
   - **Minimum Stunted Roll (`0.50`)**: `1.0 + 0.50 = 1.50x` scale (150% size — still 50% larger than vanilla adults, instead of a small runt).
4. Consequently, 100% of animals spawn and grow into giant entities because `scale` was treated as an absolute scale multiplier rather than an attribute offset from base `1.0`.

#### 💡 Proposed Solution & Technical Specifications
1. **Scale Attribute Offset Computation (`GeneticsEngine.java` / `NaturalReproduction`)**:
   - For `minecraft:scale` (or any attribute where `1.0` is the default baseline), the applied `ADD_VALUE` modifier must be computed as the offset:
     $$\Delta \text{scale} = \text{val} - 1.0\text{f}$$
   - In `GeneticsEngine.applyGeneticsModifiers(LivingEntity entity)`:
     ```java
     float modifierVal = val;
     if ("minecraft:scale".equals(trait.attributeId())) {
         modifierVal = val - 1.0f; // Offset from vanilla 1.0 base
     }
     
     if (Math.abs(modifierVal) > 0.0001f) {
         attribute.addPermanentModifier(new AttributeModifier(modifierId, modifierVal, trait.getOperation()));
     }
     ```
   - **Mathematical Verification**:
     - $\text{Trait } 1.00 \implies \Delta = +0.00 \implies \text{Final Scale } = 1.00\text{x}$ (Exact vanilla size).
     - $\text{Trait } 1.30 \implies \Delta = +0.30 \implies \text{Final Scale } = 1.30\text{x}$ (+30% Alpha/Well-Nourished).
     - $\text{Trait } 0.50 \implies \Delta = -0.50 \implies \text{Final Scale } = 0.50\text{x}$ (-50% Stunted/Inbred Runt).

2. **Wild Spawn Baseline Distribution Refinement**:
   - Refine wild spawn mutation rule from `uniform(0.50, 1.30)` to a bell curve / triangular distribution centered closely around `1.00` (e.g., `triangular(0.90, 1.10, 1.00)` or `triangular(0.85, 1.15, 1.00)`).
   - Wild natural animals will spawn at authentic near-vanilla sizes (`0.95x`–`1.05x`), while extreme scale variations (`0.50x` runts or `1.30x` heavyweights) develop dynamically through player pasture enrichment, inbreeding degradation, and cramped pen conditions.

#### 🎯 Acceptance Criteria
- [x] Animals spawn at authentic vanilla scale ($\sim 1.00\text{x}$) by default instead of giant sizes ($2.0\text{x}-2.3\text{x}$).
- [x] `minecraft:scale` modifier correctly computes $(val - 1.0\text{f})$ offset so configured range `[0.50, 1.30]` maps accurately to $[0.50\text{x}, 1.30\text{x}]$ physical entity size.
- [x] Stunted animals scale down cleanly to $0.50\text{x}$ without giant base inflation.
- [x] Well-nourished alpha animals reach up to $1.30\text{x}$ maximum scale.
- [x] Headless unit tests assert exact $(val - 1.0\text{f})$ offset math.

---

