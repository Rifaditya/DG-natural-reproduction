# Natural Reproduction - Version History

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
