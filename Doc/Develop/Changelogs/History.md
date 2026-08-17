# Natural Reproduction - Version History

## [1.4.6+26.2] - 2026-08-17

### Added & Improved
- **Better Dogs Synergy & Tamed Pet Protection (`AnimalBreedingMixin`)**:
  - *Tamed Animals Autonomous Breeding Exemption*: Tamed dogs and pets never breed autonomously in the wild without explicit player interaction.
  - *Instant Vanilla Birth Delivery*: Manual breeding of tamed dogs immediately delivers vanilla/Better Dogs litters (1–4 pups, personality DNA, scale alleles) with zero forced gestation delays.
  - *Herd Flocking Scoping*: Restricted `FollowHerdLeaderGoal` strictly to pastoral livestock, allowing Better Dogs pack alpha leadership (`WildWolfFollowLeaderGoal`) to operate exclusively on wolves.
- **Cooperative Herd Leader Caching (`HerdSocialHelper`)**: Implemented spatial chunk caching (`LEADER_CACHE`) caching elected Alpha leaders for 10 seconds (200 ticks), eliminating hundreds of redundant 24-block bounding box queries per second across loaded chunks.
- **Staggered Autonomous Breeding & Fast-Fail Guards (`AnimalBreedingMixin`)**: Distributed livestock breeding checks across 80-tick cycles (`(id + gameTime) % 80 == 0`) with fast-fail health and pregnancy guards before querying density boxes.

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
- **Multi-Generational Inbreeding Lineage Degradation & Lethal Collapse**: Added 5-tier inbreeding degradation (Tiers 0 to 4):
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
- **27 Per-Species GameRule Toggles & Dedicated Category**: Registered 27 individual per-species GameRules under `natural_reproduction_species` category (`§lNatural Reproduction - Species Toggles`). Added O(1) runtime evaluation in `AnimalHabitatHelper`, updated `/naturalreproduction` command suite, and added a dedicated tab in YACL config screen.

## [1.2.0+26.2] - 2026-08-02

### Added
- **Variant-Specific Environmental Habitat Triggers**: Updated `AnimalHabitatHelper` to inspect entity variant skin data (DataComponents / getters) for Wolves, Frogs, Rabbits, and Foxes to require variant-tailored habitat blocks (e.g. Snowy Wolves need snow/ice; Gold Rabbits need sand/cactus).

## [1.1.0+26.2] - 2026-08-02

### Added
- **Biome Climate Fertility Boost & Variant Skin Adaptation**: Added `AnimalBiomeHelper` handling native biome detection (2x breeding speed & +15% quality) and entity variant skin setting (`WolfVariants`, `FrogVariant`, `Rabbit.Variant`).
- **GameRules**: Added `natural-reproduction:biome_fertility` and `natural-reproduction:biome_variants` GameRules.

## [1.0.9+26.2] - 2026-08-02

### Polished & Enhanced
- **Command Suite Sync**: Updated `NaturalReproductionCommand` for full 5 GameRule coverage (`enabled`, `density_cap`, `rate`, `scale_drops`, `cramped_space_penalty`).
- **Particle Feedback**: Added `ANGRY_VILLAGER`/`SMOKE` for cramped stunting and `HAPPY_VILLAGER` for spacious pasture recovery.

## [1.0.8+26.2] - 2026-08-02

### Changed & Relocated
- **Collection Relocation**: Moved repository to `E:\Minecraft Project\Delayed Gratification Collection\Natural Reproduction` and moved concept spec to `Doc/Concept/DG/concept_natural_reproduction.md`.

## [1.0.7+26.2] - 2026-08-02

### Fixed & Refactored
- **Data-Driven Universal Animal Registration**: Registered `GeneticsConfig` for all 25+ vanilla animal species in `EntityGeneticsRegistry`.
- **Attribute Identifier Shift**: Updated scale attribute ID from `minecraft:generic.scale` to `minecraft:scale`.

## [1.0.6+26.2] - 2026-08-02

### Added
- **Confinement & Recovery Loop**: Added `AnimalCrampedSpaceHelper` implementing cramped 4x4 pen size stunting (-75% scale down to 0.25x) and spacious pasture size recovery (+30% scale boost per generation).
- **GameRule**: Added `natural-reproduction:cramped_space_penalty` (default `true`) and YACL configuration toggle.

## [1.0.5+26.2] - 2026-08-02

### Added
- **Scale Drop Multiplier**: Implemented `AnimalDropHelper` and `AnimalDropScaleMixin` to dynamically scale item drop yield based on animal body scale.
- **GameRule**: Added `natural-reproduction:scale_drops` (default `true`) and YACL configuration option.

## [1.0.4+26.2] - 2026-08-02

### Added
- **Universal Species Triggers**: Added habitat validation for all vanilla animal species (Canines, Felines, Equines, Camelids, Amphibians, Bears, Bees, Nether animals, Sniffers, Armadillos, Goats).
- **AnimalHabitatHelper**: Extracted habitat scan predicate matching from `AnimalBreedingMixin` into `AnimalHabitatHelper`.

## [1.0.3+26.2] - 2026-08-02

### Fixed
- **Mixin Prefix**: Renamed `AnimalBreedingMixin` helper to `naturalreproduction$hasEnvironmentalBreedingConditions`.
- **JDK Path**: Fixed `org.gradle.java.home=E:/JDK25` in `gradle.properties`.
- **Verification Headers**: Added `// Verified against: Minecraft 26.2` to all Java files.
- **2-Space JSON Formatting**: Applied 2-space indentation to `fabric.mod.json` and `natural-reproduction.mixins.json`.
- **Platform Pages**: Added HTML CurseForge and Markdown Modrinth platform page docs.

## [1.0.2+26.2] - 2026-08-02

### Changed
- **GUI Migration**: Migrated optional client GUI from legacy ClothConfig to YACL (YetAnotherConfigLib v3) + ModMenu.

## [1.0.1+26.2] - 2026-08-02

### Added & Refined
- **Configurable Breeding Rate GameRule**: `natural-reproduction:rate` (default `24000` ticks).
- **Environmental Checks**: Habitat/food conditions (Grass/Wheat for Cows/Sheep, Mud/Crops for Pigs, Seeds/Hay for Chickens).
- **Brigadier Command Suite**: `/naturalreproduction` (`help`, `status`, `get`, `set`, `reset`, `reload`).
- **ModVersionGuard**: Knot ClassLoader safe guard check.
- **Automated Tests**: Unit testing suite `NaturalReproductionTest.java`.

## [1.0.0+26.2] - 2026-08-02

### Added
- **Initial Release**: Autonomous wild breeding, livestock genetics (`DasikAnimalGeneticsAPI`), size scaling, and breeding inheritance.
