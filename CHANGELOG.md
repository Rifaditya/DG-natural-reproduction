# Changelog

All notable changes to this project will be documented in this file.

## [1.4.24+26.2] - 2026-09-03

### Changed & Refined
- **Tamed-Only Pet Exemption Filter (`AnimalBreedingMixin`)**:
  - Refined entity gating filter from blanket `self instanceof TamableAnimal` to `self instanceof TamableAnimal tamable && tamable.isTame()`.
  - Tamed player pets (wolves, cats) remain fully protected and exclusively handled by vanilla / Better Dogs mechanics.
  - Wild wolves and stray cats now actively participate in Natural Reproduction: autonomous breeding, alpha herd/pack leadership, gestation, and genetics.
  - Updated 8-block mate search to permit wild untamed canines and felines while continuing to exclude tamed pets.

## [1.4.23+26.2] - 2026-09-03

### Fixed & Refactored
- **Gestation Father Lineage Preservation & False Inbreeding Fix**:
  - Fixed critical bug where gestated offspring cloned only the mother (`inherit(baby, mother, mother)`), falsely flagging 100% of gestated newborns as inbred.
  - Persisted father UUID via entity tag (`nr_father:<uuid>`) and genetic trait snapshots (`father_scale`, `father_inbreeding_tier`) across the entire pregnancy duration.
  - Resolved living father entity on delivery (with fallback to genetic surrogate if unloaded or deceased) to ensure distinct parent pairing in inheritance.
  - Reconnected gestated births directly into `BreedingPipelineHelper.finalizeNewborn(...)`, restoring cramped stunting, spacious pasture recovery, inbreeding tiers, biome adaptations, and tracker logs to pregnant livestock deliveries.

## [1.4.22+26.2] - 2026-09-03

### Added & Refactored
- **Unified Post-Birth Lifecycle Pipeline (`BreedingPipelineHelper`)**:
  - Centralized all newborn post-birth subsystems into a cohesive helper (`BreedingPipelineHelper.finalizeNewborn(...)`).
  - Standardized the sequential execution of genetics inheritance, cramped space stunting vs. spacious pasture recovery, inbreeding lineage effects, pasture enrichment vitality boosts (+10% scale), and biome variant/climate adaptations.
  - Added native ambient birth audio cue on successful newborn delivery.
  - Refactored `AnimalBreedingMixin` (`onSpawnChildFromBreeding` at `TAIL`) to delegate directly to `BreedingPipelineHelper`, eliminating duplicate mixin code.

## [1.4.21+26.2] - 2026-09-03

### Changed
- **Documentation, Wiki & Player Guide Synchronization**: Completely updated and verified encyclopedic wiki pages, player guides, storefront descriptions, and configuration references across all channels:
  - **Scale Bounds & Genetics**: Documented the organic `0.80x`–`0.95x` wild spawn roll, `0.95x` normal baseline, `0.10x` minimum floor, and `1.20x` maximum ceiling.
  - **Dynamic Drop Rate Formulas**: Documented the continuous mathematical drop curve centered at `normal_scale` (+50% bonus at `1.20x`, 0% at `0.10x`, and empty item entity cancellation).
  - **Confinement & Pasture Recovery**: Updated mathematical degradation tables, continuous crowding penalty step, and spacious pasture recovery boost (+15% per generation).
  - **Platform Storefronts**: Synchronized Modrinth Markdown and CurseForge HTML description documents and GameRules master tables.

## [1.4.20+26.2] - 2026-09-03

### Changed
- **Harmonized Scale Bounds Synchronization**: Replaced legacy static scale bounds with dynamic queries to `natural-reproduction:min_scale` (`0.10x` default) and `natural-reproduction:max_scale` (`1.20x` default) across all breeding subsystems:
  - **Cramped Space Stunting**: Tight feedlots and pit holes now stunt runts down to the true floor (`0.10x`).
  - **Spacious Pasture Recovery**: Restores stunted genetics up to the live `max_scale` ceiling (`1.20x`).
  - **Pasture Enrichment & Prenatal Gestation**: Enriched feeding troughs and prenatal pasture vitality clamp between live `min_scale` and `max_scale`.
  - **Inbreeding Lineage Degradation**: Lethal collapse (Tier 4) drops to live `min_scale` (`0.10x`), and Hybrid Vigor outcrossing respects live `max_scale` (`1.20x`).
  - **Biome Genetics Quality Boost**: Native biome quality boost clamps dynamically between `min_scale` and `max_scale`.

## [1.4.19+26.2] - 2026-09-03

### Changed
- **Dynamic Gradual Drop Scaling**: Overhauled animal harvest drops to dynamically scale relative to active GameRules (`normal_scale`, `min_scale`, and `max_scale`).
  - Baseline animals (`0.95x` scale) yield standard 100% drops.
  - Large animals scale smoothly up to a **+50% bonus drop** at the highest scale (`1.20x`).
  - Small / stunted animals scale smoothly down to **0% drops** at the lowest scale (`0.10x`).
- **Zero-Drop Entity Cancellation**: Added empty item stack cancellation in `AnimalDropScaleMixin`, cleanly preventing empty drop entities when severely stunted runts die.

## [1.4.18+26.2] - 2026-09-03

### Changed
- **Wild Spawn Scale Rebalance**: Tightened initial wild animal scale rolls to `0.80x` – `0.95x`, preventing newly spawned wild animals from rolling excessively large sizes.
- **Scale GameRule Boundary Tuning**: Updated `natural-reproduction:min_scale` default from `50` to `10` (`0.10x`) and `natural-reproduction:max_scale` default from `130` to `120` (`1.20x`).

### Added
- **Normal Scale GameRule (`natural-reproduction:normal_scale`)**: Registered new integer GameRule defaulting to `95` (`0.95x`) defining the standard baseline animal scale.
- **YACL Configuration Sliders**: Added GUI slider for Normal Scale (50%–150%) and updated sliders for Min Scale (5%–100%) and Max Scale (100%–200%).
- **Brigadier Command Expansion**: Integrated `normal_scale` into `/naturalreproduction get`, `set`, `status`, and `reset`.
- **11-Language Localization**: Full translation coverage for `normal_scale` across all 11 supported languages.

## [1.4.17+26.2] - 2026-08-30

### Added
- **Korean (ko_kr) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.16+26.2] - 2026-08-30

### Added
- **Japanese (ja_jp) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.15+26.2] - 2026-08-30

### Added
- **Brazilian Portuguese (pt_br) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.14+26.2] - 2026-08-30

### Added
- **French (fr_fr) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.13+26.2] - 2026-08-30

### Added
- **German (de_de) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.12+26.2] - 2026-08-30

### Added
- **Spanish (es_es) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.11+26.2] - 2026-08-30

### Added
- **Russian (ru_ru) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.10+26.2] - 2026-08-30

### Added
- **Traditional Chinese (zh_tw) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.9+26.2] - 2026-08-30

### Added
- **Simplified Chinese (zh_cn) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.8+26.2] - 2026-08-30

### Added
- **Indonesian (id_id) Translation**: Complete localization across all 105 configuration strings, GameRules, item names, and species toggles.

## [1.4.7+26.2] - 2026-08-30

### Fixed & Enhanced
- **Scale Attribute Modifier Fix Integration (`DasikLibrary 1.8.35`)**: Updated dependency to DasikLibrary 1.8.35, resolving the issue where vanilla animals unintentionally doubled in size (2.0x scale) on entity spawn and initial genetics roll.
- **Dynamic Entity Scale Refresh on `/naturalreproduction reload`**: Enhanced `/naturalreproduction reload` command to iterate all loaded entities in world memory and dynamically re-apply and clamp genetics scale modifiers against updated `min_scale` and `max_scale` GameRules.

## [1.4.6+26.2] - 2026-08-17

### Added & Optimized
- **Zero-Allocation Spatial Partitioning & High-Mob Density Throttling (`[BL-NR-005]`)**:
  - *Staggered 100-Tick (5-second) Modulo*: Distributed autonomous livestock breeding checks across tick cycles (`(id + gameTime) % 100 == 0`) with proportional probability scaling (`rate / 100`), reducing server AI check overhead by over 95%.
  - *Fast-Fail Probability Ordering*: Random reproduction chance is rolled before any block or entity scans occur, skipping 99.9% of heavy world interactions in constant $O(1)$ time.
  - *SpatialBreedingCacheHelper*: Created single-purpose spatial cache managing sub-chunk entity density counts (100-tick TTL) and pasture enrichment evaluation (200-tick TTL), eliminating redundant bounding-box entity scans and block state iterations across neighboring livestock.
  - *Compact Pasture Scan Bounds*: Optimized pasture scanning bounding box from 25x9x25 down to a compact 17x7x17 enclosure radius ($2,023$ blocks).
- **Better Dogs Synergy & Tamed Pet Protection (`AnimalBreedingMixin`)**:
  - *Tamed Animals Autonomous Breeding Exemption*: Tamed dogs and pets never breed autonomously in the wild without explicit player interaction.
  - *Instant Vanilla Birth Delivery*: Manual breeding of tamed dogs immediately delivers vanilla/Better Dogs litters (1–4 pups, personality DNA, scale alleles) with zero forced gestation delays.
  - *Herd Flocking Scoping*: Restricted `FollowHerdLeaderGoal` strictly to pastoral livestock, allowing Better Dogs pack alpha leadership (`WildWolfFollowLeaderGoal`) to operate exclusively on wolves.
- **Cooperative Herd Leader Caching (`HerdSocialHelper`)**: Implemented spatial chunk caching (`LEADER_CACHE`) caching elected Alpha leaders for 10 seconds (200 ticks), eliminating hundreds of redundant 24-block bounding box queries per second across loaded chunks.

## [1.4.5+26.2] - 2026-08-17

### Fixed
- **Startup Mixin Transformation Crash (`AnimalBreedingMixin`)**: Fixed game startup crash (`InvalidMixinException`) caused by attempting to `@Shadow protected GoalSelector goalSelector` on `Animal.class`. Made `AnimalBreedingMixin` extend `AgeableMob` directly, allowing `goalSelector` to be resolved cleanly via native Java superclass inheritance without `@Shadow`.

## [1.4.4+26.2] - 2026-08-15 [BROKEN / CRASHED ON STARTUP]

> **Post-Mortem / Crash Notice**: Failed during game bootstrap with `InvalidMixinException: @Shadow field goalSelector was not located in the target class net.minecraft.world.entity.animal.Animal` due to shadowing inherited `Mob.class` field on subclass mixin. Superseded by `1.4.5+26.2`.

### Added
- **Herd Social Cohesion, Alpha Leadership & Flock Movement AI**: Introduced emergent group dynamics and synchronized pastoral routines:
  - *Dynamic Alpha Leader Election*: In pastures with 3+ same-species animals, the largest/oldest mature animal is dynamically elected as the Alpha Leader based on scale genetics.
  - *Follow Herd Leader AI Goal*: Herd followers maintain an organic 5–16 block soft grazing perimeter behind their Alpha, creating natural pastoral formations without unnatural clumping.
  - *Diurnal Schedule Cohesion*: Leaders steer grazing towards water troughs and shade at midday, and cluster the herd near barn shelter/fences at dusk to rest and protect calves.
  - *Predator Alarm & Synchronized Stampede*: When any herd member is damaged by predators or players, an alert distress sound triggers a 5-second coordinated stampede flight for all nearby herd members within 16 blocks.
- **HerdSocialHelper**: Created single-purpose helper class managing leader elections, flocking bounds, diurnal schedule checks, and stampede alarm broadcasts.
- **FollowHerdLeaderGoal**: Created custom AI goal attached to livestock entities for organic pastoral flocking.
- **Dedicated GameRules**: Registered `natural-reproduction:herd_dynamics` (Boolean, default `true`) and `natural-reproduction:herd_stampede` (Boolean, default `true`), integrated into `/gamerule`, `/naturalreproduction` command suite, and YACL screen.

## [1.4.3+26.2] - 2026-08-15

### Added
- **Dedicated Chicken Reproduction & Fertilized Egg System**: Completely separated chicken periodic unfertilized egg laying from active reproduction mechanics:
  - *Player Manual Breeding*: Delivering seeds manually instantly hatches a baby chick directly between parents.
  - *Autonomous Natural Breeding*: Rolls 50/50 between spawning an immediate baby chick or laying a guaranteed-hatch **Fertilized Egg** item.
  - *Fertilized Egg Mechanics*: Thrown Fertilized Eggs have a 100% guaranteed baby chick hatch rate for player throws and a configurable 75% hatch rate when fired from Dispensers, inheriting parental genetics. Fertilized eggs cleanly stack up to 16 with other fertilized eggs only.
  - *Infertile Regular Eggs*: Ordinary passive chicken eggs have their thrown hatch rate reduced to a 1-in-64 rare miracle chance.
- **ChickenEggHelper**: Created single-purpose helper class managing fertilized egg item creation, CustomData tagging, and impact hatching calculations.
- **ThrownEggMixin**: Intercepted egg projectile impact to apply guaranteed fertilized chick hatching and infertile egg reduction.
- **Dedicated GameRules**: Registered `natural-reproduction:fertilized_chicken_eggs` (Boolean, default `true`), `natural-reproduction:chicken_infertile_regular_eggs` (Boolean, default `true`), and `natural-reproduction:dispenser_egg_hatch_chance` (Integer, default `75`%), integrated into `/gamerule`, `/naturalreproduction` command suite, and YACL screen.

## [1.4.2+26.2] - 2026-08-15

### Added
- **Autonomous Gestation Timers & Prenatal Pasture Vitality**: Introduced dynamic pregnancy countdowns and prenatal nutrition mechanics:
  - *Pregnancy Countdown*: Breeding enters mothers into active gestation (default `24000` ticks = 1 in-game day), emitting occasional warmth particles (`HAPPY_VILLAGER` / `HEART`) and preventing immediate repeat breeding.
  - *Dynamic Prenatal Pasture Care*: Mothers spending their pregnancy in enriched pastures deliver calves with the **"Prenatal Vitality"** trait (+15% max HP, +10% movement speed, +10% scale recovery up to 1.30x).
  - *Native Egg Laying Synergy*: Oviparous species lay native egg blocks upon completing gestation (Frogs lay `Blocks.FROGSPAWN`, Turtles lay `Blocks.TURTLE_EGG`, Sniffers lay `Blocks.SNIFFER_EGG`, Chickens lay eggs).
- **AnimalGestationHelper**: Created single-purpose utility class managing gestation countdowns, prenatal enrichment checks, and egg delivery.
- **Dedicated GameRules**: Registered `natural-reproduction:gestation_period` (Boolean, default `true`), `natural-reproduction:manual_gestation` (Boolean, default `true`), and `natural-reproduction:gestation_duration` (Integer, default `24000` ticks), integrated into `/gamerule`, `/naturalreproduction` command suite, and YACL screen.

## [1.4.1+26.2] - 2026-08-15

### Added
- **Pasture Enrichment, Rotational Grazing & Feeding Trough Dynamics**: Implemented complete pasture enrichment and overgrazing mechanics using strictly vanilla interactive blocks and native particle effects:
  - *Multi-Structure Pasture Enrichment*: Automatically detects vanilla feeding troughs (water cauldrons, composters with compost/crops), hay reserves (`Blocks.HAY_BLOCK`), natural water sources, and barn ceiling weather shelters within 16 blocks.
  - *Well-Nourished Livestock State*: Living in enriched pastures grants herds +25% faster autonomous breeding check intervals, +10% offspring scale recovery (up to 1.30x max), and periodic golden `ParticleTypes.WAX_ON` sparkle particles.
  - *Dynamic Overgrazing Terrain Wear*: Dense herds (5+ animals in an 8-block area) grazing without pasture rotation dynamically convert grass blocks into `Blocks.DIRT` (or `Blocks.COARSE_DIRT` for 8+ crowding), requiring pasture rotation to keep foraging grounds fertile.
- **AnimalPastureHelper**: Created single-purpose helper class managing pasture enrichment evaluation, overhead barn shelter scanning, overgrazing wear, and well-nourished sparkle effects.
- **Dedicated GameRules**: Registered `natural-reproduction:pasture_enrichment` (Boolean, default `true`) and `natural-reproduction:overgrazing` (Boolean, default `true`), integrated into `/gamerule`, `/naturalreproduction` command suite, and YACL screen.

## [1.4.0+26.2] - 2026-08-15

### Added
- **Multi-Generational Inbreeding Lineage Degradation & Lethal Collapse**: Implemented full multi-generational inbreeding degradation system across 5 tiers (Tiers 0 to 4):
  - *Tier 0 (Wild / Diverse)*: Full genetic potential, normal breeding.
  - *Tier 1 (Mild Inbreeding)*: -10% scale stunting, -2 HP, birth smoke puff.
  - *Tier 2 (Moderate Inbreeding)*: -25% scale stunting, -20% movement speed, -4 HP, slower walking.
  - *Tier 3 (Severe Degradation)*: Miniature 0.35x scale, -50% HP, -30% speed, black squid ink particles. 100% prime meat drops (beef, pork, mutton, chicken, rabbit) convert to Rotten Flesh & Bones; secondary yields (leather, wool) reduced by 75%.
  - *Tier 4 (Lethal Genetic Collapse)*: Terminal inbreeding failure. Offspring suffers 1 damage per second continuously until death, emitting persistent distress smoke and squid ink particles.
- **Gradual Generational Dilution & Hybrid Vigor**: Outcrossing degraded livestock with unrelated wild stock steps down the inbreeding tier by 1 level per generation (Tier 4 -> 3 -> 2 -> 1 -> 0). Reaching Tier 0 via outcrossing awards +15% Hybrid Vigor to offspring scale and vitality.
- **AnimalLineageHelper**: Created dedicated single-purpose utility class managing lineage tier calculations, dilution step-down, attribute penalties, and lethal collapse damage ticks.
- **Inbreeding Degradation GameRule**: Registered `natural-reproduction:inbreeding_degradation` (Boolean, default `true`), integrated into `/gamerule`, `/naturalreproduction` command suite, and YACL GUI.

## [1.3.4+26.2] - 2026-08-08

### Enhanced & Added
- **Dual Structural Confinement & Local Density Crowding Penalty**: Updated `AnimalCrampedSpaceHelper` so the crowding penalty takes effect both in small structural pens/pits AND in large open pastures (e.g. 100x100 pens) whenever local mob crowding is high (`extraLocalCount >= 3`).

## [1.3.3+26.2] - 2026-08-08

### Changed & Refactored
- **Smooth Gradual Cramped Space Penalty**: Refactored `AnimalCrampedSpaceHelper` to replace discrete tier steps with a smooth continuous density formula (`penaltyMultiplier = Math.max(0.95 - extraLocalCount * 0.05, 0.40)`). Every extra crowding animal in a confined pen now causes a smooth, incremental 5% scale reduction per entity instead of sudden cliff drops.

## [1.3.2+26.2] - 2026-08-08

### Changed & Updated
- **Minimum DasikLibrary Requirement Bump**: Updated minimum `dasik-library` dependency bound in `fabric.mod.json` to `">=1.8.15"` and updated `gradle.properties` to `dasik_library_version=1.8.15`.
- **Platform Pages & Docs Sync**: Updated `README.md`, `description_curseforge.html`, and `description_modrinth.md` to reflect `DasikLibrary >= 1.8.15` minimum requirement and the 27 per-species GameRule toggles.

## [1.3.1+26.2] - 2026-08-08

### Added
- **27 Per-Species GameRule Toggles & Dedicated Category**: Added 27 individual per-species GameRules under a dedicated `natural_reproduction_species` category (`§lNatural Reproduction - Species Toggles`). All 27 toggles default to `true` (ON), allowing admins to granularly enable or disable natural reproduction for any specific animal species (e.g. Wolf, Hoglin, Cow, Pig) via `/gamerule`, `/naturalreproduction set`, or the YACL config screen.

## [1.3.0+26.2] - 2026-08-08

### Added & Fixed
- **Universal Confinement & Pit Hole Detection**: Introduced universal confinement checking (`isConfinedArea`) detecting 1x1 / 2x2 pit holes, fences, walls, trapdoors, and solid block enclosures.
- **Open Pasture Stunting Immunity**: Excluded breeding parents from local crowd counts and protected open pasture herds from false stunting penalties, granting size recovery (`Spacious Pasture`) up to `1.30x` scale in open worlds.

## [1.2.9+26.2] - 2026-08-08

### Fixed
- **Dual-Parent In-Love State Fix for Autonomous Breeding**: Fixed issue where only parent #1 was set into love mode during autonomous breeding. Now both `self` and `mate` enter love mode concurrently (`self.setInLove(null)` and `mate.setInLove(null)`), satisfying vanilla `BreedGoal` requirements and allowing animals to successfully mate, spawn offspring, and trigger tracker logs during tick sprints.

## [1.2.8+26.2] - 2026-08-08

### Added & Changed
- **Manual Toggle & Off-By-Default Tracker Logging**: Made Autonomous Breeding Tracker Logging disabled (`false`) by default. Added `natural-reproduction:tracker_logs` GameRule (default `false`), YACL toggle, and `/naturalreproduction trackerlogs enable/disable` command controls.

## [1.2.7+26.2] - 2026-08-08

### Added
- **Autonomous Breeding Tracker Log System & Command**: Added `BreedingTrackerLogger` and `/naturalreproduction trackerlogs` (and `/naturalreproduction logs`) command suite (`list`, `clear`) to track, format, and inspect autonomous animal reproduction events (day, species, coordinates, biome, offspring scale, habitat status).

## [1.2.6+26.2] - 2026-08-08

### Fixed
- **GameRule Text Formatting Refinement**: Removed bold formatting from individual GameRule display names so individual rules render in standard unbolded font, isolating bold formatting exclusively to category headers.

## [1.2.5+26.2] - 2026-08-07

### Added & Changed
- **Default Max Scale Update (1.3x)**: Updated default maximum animal scale bound from `1.50x` (150%) to `1.30x` (130%) across GameRules, genetics configs, YACL sliders, Brigadier commands, and localization.
- **Bold Category Header Formatting**: Added `§l` formatting prefix to GameRule category translation keys so category headers (e.g. `Natural Reproduction (9 rules)`) render bolded in GameRule menus.

## [1.2.4+26.2] - 2026-08-07

### Added
- **Bold GameRule Title Formatting**: Added `§l` formatting prefix across all GameRule display names in `en_us.json` and builder registrations so GameRule titles appear bolded in vanilla and modded GameRule menus.

## [1.2.3+26.2] - 2026-08-07

### Fixed
- **Stale Attribute Modifier Purging Integration**: Integrated with `DasikLibrary` 1.8.11 to purge all legacy/duplicate attribute modifiers on entity spawn and tick, permanently resolving the giant animal scale bug.

## [1.2.2+26.2] - 2026-08-07

### Added
- **Configurable Dynamic Size Range**: Default scale bounds set to `0.5x` (50% smallest) and `1.5x` (150% largest). Introduced dynamic `natural-reproduction:min_scale` and `natural-reproduction:max_scale` GameRules integrated with `GeneticsLimitRegistry`.
- **YACL v3 & Command Suite Sliders**: Added GUI sliders and `/naturalreproduction set min_scale <val>` / `max_scale <val>` command controls.

### Fixed
- **Scale Attribute Base Offset**: Integrated with `DasikLibrary` 1.8.10 to subtract `1.0f` from `scale` attribute modifiers (`ADD_VALUE`), fixing 200%+ oversized sheep.

## [1.2.1+26.2] - 2026-08-07

### Fixed
- **Startup Mixin Injection Crash**: Changed `AnimalDropScaleMixin` target to `@Mixin(Entity.class)` with `instanceof Animal` check. Resolves `InvalidInjectionException` caused by injecting into inherited `spawnAtLocation` method on `Animal.class` (`ERR-20260530-002`).

## [1.2.0+26.2] - 2026-08-02

### Added
- **Variant-Specific Environmental Habitat Triggers**: Updated `AnimalHabitatHelper` so animals with variant skins have unique habitat requirements:
  - *Snowy Wolves & Snow Foxes*: Require `SNOW_BLOCK`, `POWDER_SNOW`, `SNOW`, or `PACKED_ICE`.
  - *Black / Chestnut Wolves*: Require `PODZOL`, `COARSE_DIRT`, `MOSS_BLOCK`, or `SPRUCE_LEAVES`.
  - *Warm Frogs & Gold Rabbits*: Require `SAND`, `DEAD_BUSH`, `CACTUS`, `DRIPLEAF`, or `MUD`.
  - *Cold Frogs & White Rabbits*: Require `ICE`, `PACKED_ICE`, `SNOW`, or `SNOW_BLOCK`.

## [1.1.0+26.2] - 2026-08-02

### Added
- **Native Biome Climate Fertility Boost**: Breeding animals in native climate biomes (e.g. Taigas for Wolves, Swamps for Frogs, Deserts for Camels, Snowy Plains for Polar Bears) grants 2x faster breeding frequency and +15% genetics quality. Backed by `natural-reproduction:biome_fertility`.
- **Biome Variant Skin Adaptation**: Offspring born in specific biomes dynamically adapt their visual entity variant skin (e.g. Snowy Wolves, Desert/Warm Frogs, White Rabbits) or inherit parent variants. Backed by `natural-reproduction:biome_variants`.
- **AnimalBiomeHelper**: Created single-purpose utility class for native biome matching, fertility calculation, and entity variant skin setting.

## [1.0.9+26.2] - 2026-08-02

### Polished & Enhanced
- **Full 5-GameRule Command Suite**: Updated `/naturalreproduction` Brigadier command suite to support `get`, `set`, `status`, and `reset` for `scale_drops` and `cramped_space_penalty` GameRules.
- **Visual Breeding Particle Feedback**: Added particle indicators upon animal birth—spawning `ANGRY_VILLAGER` & `SMOKE` when cramped space stunting occurs, and green `HAPPY_VILLAGER` particles when spacious pasture size recovery occurs.
- **Platform Pages Update**: Refreshed CurseForge HTML and Modrinth markdown platform documentation.

## [1.0.8+26.2] - 2026-08-02

### Changed & Relocated
- **Collection Relocation to Delayed Gratification**: Relocated Natural Reproduction into the `Delayed Gratification Collection` folder (`E:\Minecraft Project\Delayed Gratification Collection\Natural Reproduction`), aligning its multi-generational pasture size recovery and cramped space stunting mechanics under the Delayed Gratification (DG) design track.
- **Concept Document Relocation**: Updated concept specification path to `Doc/Concept/DG/concept_natural_reproduction.md`.

## [1.0.7+26.2] - 2026-08-02

### Fixed & Refactored
- **Data-Driven Universal Genetics Registration**: Registered data-driven `GeneticsConfig` in `EntityGeneticsRegistry` for **all 25+ vanilla animal species** (Cows, Pigs, Sheep, Chickens, Mooshrooms, Horses, Donkeys, Mules, Llamas, Camels, Wolves, Cats, Foxes, Ocelots, Turtles, Frogs, Axolotls, Polar Bears, Pandas, Striders, Hoglins, Rabbits, Bees, Goats, Armadillos, Sniffers).
- **Minecraft 26.2 Scale Attribute Alignment**: Updated entity scale attribute identifier from legacy `minecraft:generic.scale` to official Minecraft 26.2 `minecraft:scale`, ensuring visual physical scaling, cramped stunting, and drop yield scaling function across all animal species.

## [1.0.6+26.2] - 2026-08-02

### Added
- **Cramped Space Penalty & Spacious Recovery**: Breeding animals in cramped 4x4 pens (high local density) gradually stunts offspring size genetics down to `0.25x` scale. Breeding stunted animals in spacious open pastures applies a +30% size recovery boost per generation back to full genetic potential.
- **Configurable Cramped Penalty GameRule**: Added `natural-reproduction:cramped_space_penalty` (Boolean, default `true`) and YACL configuration option.
- **AnimalCrampedSpaceHelper**: Created dedicated helper for confinement density checking and 2-way size stunting/recovery math.

## [1.0.5+26.2] - 2026-08-02

### Added
- **Scale-Based Item Drop Multiplier**: Animal item drop yield now scales dynamically with physical body scale (`DasikAnimalGeneticsAPI.getScale`). Larger animals drop more meat, leather, wool, etc., while smaller animals drop less.
- **Configurable Drop Scaling GameRule**: Added `natural-reproduction:scale_drops` (Boolean, default `true`) and YACL configuration toggle.
- **AnimalDropHelper & Mixin**: Implemented `AnimalDropHelper` and `AnimalDropScaleMixin` intercepting animal item entity drops.

## [1.0.4+26.2] - 2026-08-02

### Added
- **Universal Vanilla Animal Support**: Extended autonomous breeding and species-specific environmental habitat triggers to **all vanilla Minecraft animal species** (Wolves, Cats, Foxes, Rabbits, Turtles, Pandas, Bees, Striders, Hoglins, Armadillos, Camels, Goats, Frogs, Sniffers, Axolotls, Polar Bears, Equines, Mooshrooms, etc.).
- **AnimalHabitatHelper**: Extracted environmental condition validation into a clean, dedicated helper class adhering to 1 File 1 Function and Thin Mixin laws.

## [1.0.3+26.2] - 2026-08-02

### Fixed
- **Mixin Member Prefix Compliance**: Prefixed `AnimalBreedingMixin` helper method to `naturalreproduction$hasEnvironmentalBreedingConditions`.
- **JDK Path Configuration**: Corrected `org.gradle.java.home` to `E:/JDK25` in `gradle.properties`.
- **Code Standard Headers**: Added `// Verified against: Minecraft 26.2` headers across all Java source files.
- **Resource JSON Formatting**: Re-formatted `fabric.mod.json` and `natural-reproduction.mixins.json` to 2-space standard.
- **Platform Pages Documentation**: Generated raw HTML CurseForge and Markdown Modrinth platform pages.

## [1.0.2+26.2] - 2026-08-02

### Changed
- **Config GUI Migration to YACL**: Replaced legacy ClothConfig integration with **YetAnotherConfigLib (YACL v3)** + **ModMenu** in strict compliance with workspace rules (`Optional GUI Dependencies.md`).

## [1.0.1+26.2] - 2026-08-02

### Added
- **Configurable Breeding Rate GameRule**: Introduced `natural-reproduction:rate` (default `24000` ticks / 1 MC Day) to configure autonomous breeding attempt frequency per animal.
- **Species Environmental Food/Habitat Checks**: Mobs now require species-appropriate blocks nearby (Cows/Sheep require Grass/Wheat, Pigs require Mud/Crops, Chickens require Seeds/Hay) before entering Love Mode.
- **Brigadier Command Suite**: Full `/naturalreproduction` command tree (`help`, `status`, `get`, `set`, `reset`, `reload`) with tab completion and permission level gating.
- **ModVersionGuard Integration**: Added `ModVersionGuard` startup check with Knot ClassLoader safety.
- **Automated Unit Testing**: Added `NaturalReproductionTest` for breeding logic and configuration bounds verification.

## [1.0.0+26.2] - 2026-08-02

### Added
- **Autonomous Wild Breeding**: Animals (Cows, Pigs, Sheep, Chickens) autonomously enter love mode when healthy and in proximity to mates, bounded by population density cap GameRules.
- **Livestock Animal Genetics Integration**: Full `DasikAnimalGeneticsAPI` integration providing individual scale/size (`0.75x` - `1.30x`), health, and movement speed traits.
- **Breeding Inheritance & Pedigree**: Offspring inherit size, speed, and health traits from parent animals, with full parent UUID tracking and inbreeding penalty detection.
