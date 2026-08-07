# Changelog

All notable changes to this project will be documented in this file.

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
