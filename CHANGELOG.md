# Changelog

All notable changes to this project will be documented in this file.

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
