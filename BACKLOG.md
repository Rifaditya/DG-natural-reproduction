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

| ID | Category | Title | Priority | Target Version | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `[BL-NR-001]` | `[FEATURE]` | Multi-Generational Inbreeding Lineage Degradation & Hybrid Vigor System | `[HIGH]` | `26.2, 26.3` | `✅ RESOLVED` |
| `[BL-NR-002]` | `[FEATURE]` | Pasture Enrichment, Rotational Grazing & Feeding Trough Dynamics | `[HIGH]` | `26.2, 26.3` | `✅ RESOLVED` |
| `[BL-NR-003]` | `[FEATURE]` | Autonomous Gestation Timers & Prenatal Pasture Vitality | `[MEDIUM]` | `26.2, 26.3` | `✅ RESOLVED` |
| `[BL-NR-004]` | `[FEATURE]` | Herd Social Cohesion, Alpha Leadership & Flock Movement AI | `[MEDIUM]` | `26.2, 26.3` | `✅ RESOLVED` |
| `[BL-NR-005]` | `[PERF]` | Zero-Allocation Spatial Partitioning & High-Mob Density Throttling | `[HIGH]` | `26.2, 26.3` | `✅ RESOLVED` |
| `[BL-NR-008]` | `[BUGFIX]` | Fix Entity Scale Modifier Offset & Attribute Stacking Causing Ubiquitous Giant Mob Sizes | `[HIGH]` | `26.2, 26.3` | `✅ RESOLVED` |
| `[BL-NR-007]` | `[FEATURE]` | Multi-Era Anchor Porting: Natural Reproduction (Modern & Older Anchors) | `[HIGH]` | `All Anchors` | `✅ RESOLVED` |
| `[BL-NR-006a]` | `[TECH_DEBT]` | Lineage & Genetics JUnit Suite | `[MEDIUM]` | `All Anchors` | `✅ RESOLVED` |
| `[BL-NR-006b]` | `[TECH_DEBT]` | Continuous Stunting & Scale Clamping Chaos Fuzzing | `[LOW]` | `All Anchors` | `✅ RESOLVED` |
| `[BL-NR-006c]` | `[TECH_DEBT]` | Spatial Cache Concurrency Load Simulator | `[MEDIUM]` | `All Anchors` | `📌 DEFERRED` |
| `[BL-NR-006d]` | `[TECH_DEBT]` | Zero-Mock Headless Brigadier Command Suite | `[MEDIUM]` | `All Anchors` | `📌 DEFERRED` |
| `[BL-NR-006e]` | `[TECH_DEBT]` | Fabric Loom GameTest In-World Lifecycle | `[HIGH]` | `All Anchors` | `📌 DEFERRED` |

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

### [BL-NR-007] Multi-Era Anchor Porting: Natural Reproduction (Modern & Older Anchors)
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Status**: `✅ RESOLVED`
- **Target Version**: `All Anchors`
- **Target Component(s)**: Multi-subproject directories (`Natural Reproduction v26.3/`, `v26.2/`, `v26.1/`, `v1.21.11/`, `v1.21.1/`, `v1.20.1/`), `AnimalBreedingMixin.java`, `SpatialBreedingCacheHelper.java`, `AnimalGestationHelper.java`, `HerdLeaderGoal.java`, `NaturalReproductionFabric.java`, `build.gradle`, `fabric.mod.json`, `RELEASE_QUEUE.md`
- **Date Added**: 2026-08-15

#### ❓ Problem / Context
Natural Reproduction currently has its working baseline at `MC 26.3` (`1.4.31+26.3`), with prior builds on `MC 26.2` up to `1.4.30+26.2`. All other version anchors (`26.1 / 26.1.2`, `1.21.11`, `1.21.1`, `1.20.1`) are completely unported, leaving a significant cross-version parity gap. Furthermore, the repository currently lives in a single root project rather than dedicated version directories.

Per the **Multi-Era Anchor Parity Law** and **1 Jar 1 Version Policy**, all active anchors must be scaffolded as dedicated version directories (`Natural Reproduction v26.3/`, `Natural Reproduction v26.2/`, `Natural Reproduction v26.1/`, `Natural Reproduction v1.21.11/`, `Natural Reproduction v1.21.1/`, `Natural Reproduction v1.20.1/`), adapted to their respective toolchains and vanilla engine mappings, compiled, tested, and queued before advancing to downstream tech debt or further feature expansion.

#### 💡 Architectural Specifications & Toolchain Anchors
- **Phase 1: Modern Sovereign Anchors (Java 25+, Loom 1.15+, No Mappings Block)**:
  - `MC 26.3`: Java 25, Fabric Loom 1.15+, `minecraft_version=26.3`, `fabric_version=0.161.0+26.3`, DasikLibrary 1.9.2 (Lead Baseline).
  - `MC 26.2`: Java 25, Fabric Loom 1.15+, `Identifier.fromNamespaceAndPath`, `DynamicGameRuleManager` (Catch-up to 1.4.31).
  - `MC 26.1 / 26.1.2`: Java 25, Fabric Loom 1.15+, `Identifier.fromNamespaceAndPath`, `EntityTypes`, `dasik-library` 26.1.
- **Phase 2: Older Anchors (Mojang Mappings, Java 21 / 17)**:
  - `MC 1.21.11`: Java 21, Loom 1.15-SNAPSHOT (`fabric-loom-remap`), Mojang mappings, `Identifier.of`, `Optional<T>` CompoundTag, relocated entity packages.
  - `MC 1.21.1`: Java 21, Loom 1.10+, Mojang mappings, `Identifier.of`, `DataComponents`, native `Attributes.SCALE`.
  - `MC 1.20.1`: Java 17, Loom 1.4–1.10, Mojang mappings, `new Identifier`, primitive NBT CompoundTag, `FabricItemSettings`, `GameRules.Category` enum.

#### 🧪 Verification & Acceptance Criteria

##### Phase 1: Modern Sovereign Anchors (Priority 1)
- [x] **Anchor: MC 26.3 (Lead Baseline Segregation)**
  - [x] Dedicated subproject directory scaffolding (`Natural Reproduction v26.3/`) matching 1 Jar 1 Version Policy
  - [x] Headless unit & integration test suite pass (`./gradlew test --no-daemon`)
  - [x] Clean binary compilation (`./gradlew build --no-daemon`)
  - [x] Mandatory Universal 4-Point Distribution verified
- [x] **Anchor: MC 26.2 (Parity Catch-up & Directory Segregation)**
  - [x] Dedicated subproject directory scaffolding (`Natural Reproduction v26.2/`)
  - [x] Port `1.4.31` features/fixes and DasikLibrary alignment
  - [x] Headless unit & integration test suite pass (`./gradlew test --no-daemon`)
  - [x] Clean binary compilation (`./gradlew build --no-daemon`)
  - [x] Mandatory Universal 4-Point Distribution (Local Archive, Hub Archive, External Vault `D:\`, Launcher Test Profile)
  - [x] Release queue registration in `RELEASE_QUEUE.md` (`- [ ]`) and `CHANGELOG.md` entry
- [x] **Anchor: MC 26.1 / 26.1.2**
  - [x] Subproject directory & build script scaffolding (`Natural Reproduction v26.1/`)
  - [x] Source adaptation, API/mixin relocation, and dasik-library wiring for target version
  - [x] Headless unit & integration test suite pass (`./gradlew test --no-daemon`)
  - [x] Clean binary compilation (`./gradlew build --no-daemon`)
  - [x] Mandatory Universal 4-Point Distribution (Local Archive, Hub Archive, External Vault `D:\`, Launcher Test Profile)
  - [x] Release queue registration in `RELEASE_QUEUE.md` (`- [ ]`) and `CHANGELOG.md` entry

##### Phase 2: Older Anchors (Priority 2)
- [x] **Anchor: MC 1.21.11 (Java 21, Loom 1.15-SNAPSHOT `fabric-loom-remap`, Mojang mappings, `Identifier.of`, `Optional<T>` CompoundTag, relocated entity packages)**
  - [x] Subproject directory & build script scaffolding (`Natural Reproduction v1.21.11/`)
  - [x] Source adaptation, API/mixin relocation, and dasik-library wiring for target version
  - [x] Headless unit & integration test suite pass (`./gradlew test --no-daemon`)
  - [x] Clean binary compilation (`./gradlew build --no-daemon`)
  - [x] Mandatory Universal 4-Point Distribution (Local Archive, Hub Archive, External Vault `D:\`, Launcher Test Profile)
  - [x] Release queue registration in `RELEASE_QUEUE.md` (`- [ ]`) and `CHANGELOG.md` entry
- [x] **Anchor: MC 1.21.1 (Java 21, Loom 1.10+, Mojang mappings, `Identifier.of`, `DataComponents`, native `Attributes.SCALE`)**
  - [x] Subproject directory & build script scaffolding (`Natural Reproduction v1.21.1/`)
  - [x] Source adaptation, API/mixin relocation, and dasik-library wiring for target version
  - [x] Headless unit & integration test suite pass (`./gradlew test --no-daemon`)
  - [x] Clean binary compilation (`./gradlew build --no-daemon`)
  - [x] Mandatory Universal 4-Point Distribution (Local Archive, Hub Archive, External Vault `D:\`, Launcher Test Profile)
  - [x] Release queue registration in `RELEASE_QUEUE.md` (`- [ ]`) and `CHANGELOG.md` entry
- [x] **Anchor: MC 1.20.1 (Java 17, Loom 1.4-1.10, Mojang mappings, `new Identifier`, primitive NBT CompoundTag, `FabricItemSettings`, `GameRules.Category` enum)**
  - [x] Subproject directory & build script scaffolding (`Natural Reproduction v1.20.1/`)
  - [x] Source adaptation, API/mixin relocation, and dasik-library wiring for target version
  - [x] Headless unit & integration test suite pass (`./gradlew test --no-daemon`)
  - [x] Clean binary compilation (`./gradlew build --no-daemon`)
  - [x] Mandatory Universal 4-Point Distribution (Local Archive, Hub Archive, External Vault `D:\`, Launcher Test Profile)
  - [x] Release queue registration in `RELEASE_QUEUE.md` (`- [ ]`) and `CHANGELOG.md` entry

---

### [BL-NR-006a] Lineage & Genetics JUnit Suite
- **Category**: `[TECH_DEBT]`
- **Priority**: `[MEDIUM]`
- **Status**: `✅ RESOLVED`
- **Target Version**: `All Anchors`
- **Target Component(s)**: `src/test/java/net/vanillaoutsider/naturalreproduction/LineageGeneticsTest.java`
- **Roadmap Tracker**: [`lineage_genetics_test_roadmap.md`](file:///C:/Users/fmrif/.gemini/antigravity/brain/52edd868-f3b2-4dd5-a02d-9fec2886a698/lineage_genetics_test_roadmap.md)

#### 💡 Proposed Solution & Technical Specifications
- **Step 1 (`1.4.32+mc`)**: 3-Generation Pedigree Kinship & Inbreeding Tier Progression Engine Tests (`LineageGeneticsTest.java`).
  - Assert 3-generation pedigree ancestry matching across Tier 0 (Diverse), Tier 1 (Moderate), Tier 2 (Severe), and Tier 3 (Extreme).
  - Assert inbreeding tier progression: $\text{tier} = \text{clamp}(\max(p_1, p_2) + 1, 1, 4)$.
  - Assert gradual generational dilution: $\text{tier} = \max(0, \max(p_1, p_2) - 1)$.
  - Assert hybrid vigor $+15\%$ scale bonus for outcrossed lineages.
  - Assert stunting penalties (-10% T1, -25% T2, 0.20x T3, 0.10x floor T4; speed -20% T2, -30% T3, -50% T4).
- **Step 2 (`1.4.33+mc`)**: Drop Degradation & Pedigree Codec Serialization Determinism Tests.
  - Assert Tier 3 inbreeding rotten flesh & bone drop degradation criteria.
  - Assert secondary drop reduction (75% reduction on leather/wool/feathers).
  - Test pedigree NBT / Codec serialization and deserialization determinism.
- **Step 3 (`1.4.34+mc`)**: Multi-Era Anchor Porting, Parity Sync & Verification across all 6 anchors (`1.20.1`, `1.21.1`, `1.21.11`, `26.1`, `26.2`, `26.3`).

#### 🎯 Acceptance Criteria
- [x] Step 1: Pedigree kinship, tier progression, dilution, and hybrid vigor assertions pass 100%.
- [x] Step 2: Drop degradation and serialization determinism assertions pass 100%.
- [x] Step 3: `./gradlew test` passes 100% across all 6 version anchors.
- [x] Ancestry matching cleanly isolates generational depth without recursive stack overflow.

---

### [BL-NR-006b] Continuous Stunting & Scale Clamping Chaos Fuzzing
- **Category**: `[TECH_DEBT]`
- **Priority**: `[LOW]`
- **Status**: `✅ RESOLVED`
- **Target Version**: `All Anchors`
- **Target Component(s)**: `src/test/java/net/vanillaoutsider/naturalreproduction/StuntingFuzzTest.java`

#### 💡 Proposed Solution & Technical Specifications
- **Step 1 (`1.4.35+mc`)**: Continuous Overcrowding Stunting & Pasture Recovery Assertions (`StuntingFuzzTest.java`).
  - Assert continuous overcrowding stunting curve ($\max(0.95 - \text{count} \times 0.05, 0.20)$) across density counts 0 through 100.
  - Assert spacious pasture recovery $+15\%$ ceiling clamping at `maxAllowed` (`1.20f`).
  - Assert severe overcrowding saturation floor ($0.20f$ for counts $\ge 15$).
- **Step 2 (`1.4.36+mc`)**: 10,000-Iteration Chaos Property Fuzzing Engine.
  - 10,000 randomized iterations with pseudo-random seed determinism (`Random(0xDA51C)`).
  - Injections: `Float.NaN`, `Float.POSITIVE_INFINITY`, `Float.NEGATIVE_INFINITY`, negative densities ($-1000 \dots -1$), astronomical floats ($\pm 10^{38}$), and subnormal floats.
  - Assert zero unhandled exceptions, zero `NaN` scale leaks, and strict confinement within $[0.10f, 1.20f]$.
- **Step 3 (`1.4.37+mc`)**: Multi-Era Anchor Porting, Parity Sync & Verification across all 6 anchors (`1.20.1`, `1.21.1`, `1.21.11`, `26.1`, `26.2`, `26.3`).

#### 🎯 Acceptance Criteria
- [x] Step 1: Continuous curve produces strictly monotonic values within $[0.20, 0.95]$.
- [x] Step 1: Spacious pasture recovery clamps cleanly at `1.20f` without overshoot.
- [x] Step 2: 10,000 chaos iterations produce zero unhandled exceptions and clamp safely within $[0.10f, 1.20f]$.
- [x] Step 3: `./gradlew test` passes 100% across all 6 version anchors.

---

### [BL-NR-006c] Spatial Cache Concurrency Load Simulator
- **Category**: `[TECH_DEBT]`
- **Priority**: `[MEDIUM]`
- **Status**: `📌 DEFERRED`
- **Target Version**: `All Anchors`
- **Target Component(s)**: `src/test/java/net/vanillaoutsider/naturalreproduction/SpatialCacheConcurrencyTest.java`

#### 💡 Proposed Solution & Technical Specifications
- Multi-threaded load test dispatching 50 concurrent threads with `CountDownLatch`.
- Simulate simultaneous cache queries, entry insertions, and TTL eviction sweeps.
- Assert zero `ConcurrentModificationException` and zero deadlocks.

#### 🎯 Acceptance Criteria
- [ ] 50-thread concurrent blast completes with zero lockups or exceptions.
- [ ] Thread safety verified for `SpatialBreedingCacheHelper` and `HerdSocialHelper`.

---

### [BL-NR-006d] Zero-Mock Headless Brigadier Command Suite
- **Category**: `[TECH_DEBT]`
- **Priority**: `[MEDIUM]`
- **Status**: `📌 DEFERRED`
- **Target Version**: `All Anchors`
- **Target Component(s)**: `src/test/java/net/vanillaoutsider/naturalreproduction/NaturalReproductionCommandTest.java`

#### 💡 Proposed Solution & Technical Specifications
- Inspect full Brigadier syntax tree for `/naturalreproduction` and `/nr`.
- Verify root and alias tree parity across all child nodes (`status`, `stats`, `purge`, `reload`).
- Assert tab-completion suggestions match registered nodes.
- Assert non-OP permission rejection (`PermissionSet.NO_PERMISSIONS`).

#### 🎯 Acceptance Criteria
- [ ] Dispatcher parse leaves 0 unread characters on valid commands.
- [ ] Non-OP callers cannot execute mutating admin commands.

---

### [BL-NR-006e] Fabric Loom GameTest In-World Lifecycle
- **Category**: `[TECH_DEBT]`
- **Priority**: `[HIGH]`
- **Status**: `📌 DEFERRED`
- **Target Version**: `All Anchors`
- **Target Component(s)**: `build.gradle`, `fabric.mod.json`, `src/test/java/net/vanillaoutsider/naturalreproduction/gametest/NaturalReproductionGameTests.java`

#### 💡 Proposed Solution & Technical Specifications
- Configure `loom.runs.gametest` with `-Dfabric-api.gametest`.
- Register `"gametest"` entrypoint in `fabric.mod.json`.
- Implement in-world GameTests: `testCrowdedPenStunting`, `testSpaciousPastureRecovery`, `testDensityCapSuppression`.

#### 🎯 Acceptance Criteria
- [ ] `./gradlew test gametest` executes headless server world GameTests cleanly.
- [ ] Real in-world breeding behaviors pass assertion checks.

---

### [BL-NR-008] Fix Entity Scale Modifier Offset & Attribute Stacking Causing Ubiquitous Giant Mob Sizes
- **Category**: `[BUGFIX]`
- **Priority**: `[HIGH]`
- **Status**: `✅ RESOLVED`
- **Target Version**: `26.2, 26.3`
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

