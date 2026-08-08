# 📋 Natural Reproduction Release Queue & Backlog

This file tracks built versions uploaded to Modrinth/CurseForge.

## 🚀 Published & Backlog Queue

- [x] **`1.3.3+26.2`** (2026-08-08) - **Smooth Gradual Cramped Space Penalty**: Replaced discrete tier steps with continuous density formula (`-5%` scale per extra crowding entity).
- [x] **`1.3.2+26.2`** (2026-08-08) - **DasikLibrary 1.8.15 Bump & Platform Pages Sync**: Bumped minimum `dasik-library` bound to `1.8.15` minimum and updated CurseForge, Modrinth, and README docs.
- [x] **`1.3.1+26.2`** (2026-08-08) - **27 Per-Species GameRule Toggles & Dedicated Category**: Registered 27 individual per-species GameRules under `natural_reproduction_species` category (`§lNatural Reproduction - Species Toggles`).
- [x] **`1.3.0+26.2`** (2026-08-08) - **Universal Confinement & Open Pasture Protection**: Detected 1x1 pit holes, fences, walls, trapdoors; protected open pasture herds from false stunting penalties.
- [x] **`1.2.9+26.2`** (2026-08-08) - **Dual-Parent In-Love State Fix**: Set both parents to love mode concurrently to allow mating during tick sprints.
- [x] **`1.2.8+26.2`** (2026-08-08) - **Manual Toggle & Off-By-Default Tracker Logging**: Made tracker logging off by default (`false`) with manual enable/disable controls.
- [x] **`1.2.7+26.2`** (2026-08-08) - **Autonomous Breeding Tracker Log System & Command Suite**: Added `BreedingTrackerLogger` and `/naturalreproduction trackerlogs` command suite (`list`, `clear`).
- [x] **`1.2.6+26.2`** (2026-08-08) - **Category-Only Bold Formatting Isolation**: Isolated bold formatting (`§l`) exclusively to category header titles.
- [x] **`1.2.5+26.2`** (2026-08-07) - **Default Max Scale 1.3x & Bold Category Header**: Changed default max scale to `1.30x` (130%) and bolded category header titles (`§l`).
- [x] **`1.2.4+26.2`** (2026-08-07) - **Bold GameRule Title Formatting**: Added `§l` formatting prefix across all GameRule display names and builder registrations.
- [x] **`1.2.3+26.2`** (2026-08-07) - **Stale Attribute Modifier Purge Integration**: Integrated with DasikLibrary 1.8.11 to purge legacy and duplicate genetics attribute modifiers on entity spawn/tick.
- [x] **`1.2.2+26.2`** (2026-08-07) - **Configurable 0.5x - 1.5x Scale Range & Base Offset Fix**: Default scale range set to `0.5x`–`1.5x`. Added `min_scale` and `max_scale` GameRules, YACL sliders, Brigadier commands, and DasikLibrary 1.8.10 scale offset integration.
- [x] **`1.2.1+26.2`** (2026-08-07) - **Startup Mixin Injection Crash Fix**: Fixed `AnimalDropScaleMixin` target to `@Mixin(Entity.class)` with `instanceof Animal` check (`ERR-20260530-002`).
- [x] **`1.2.0+26.2`** (2026-08-02) - **Variant-Specific Environmental Habitat Triggers**: Animals with variant skins (Wolves, Frogs, Rabbits, Foxes) have unique environmental habitat block requirements tailored to their variant skin type.
- [x] **`1.1.0+26.2`** (2026-08-02) - **Biome Climate & Variant Adaptation**: Animals in native biomes get 2x faster breeding & +15% genetics quality; offspring born in specific biomes adapt visual variant skins (Snowy Wolves, Desert Frogs, etc.).
- [x] **`1.0.9+26.2`** (2026-08-02) - **Polishing Update**: Synced full 5 GameRules into Brigadier command suite, added visual particle feedback for stunting vs. recovery, and refreshed platform pages.
- [x] **`1.0.8+26.2`** (2026-08-02) - **Delayed Gratification Relocation**: Relocated to `Delayed Gratification Collection` folder and aligned concept doc under `Doc/Concept/DG/`.
- [x] **`1.0.7+26.2`** (2026-08-02) - **Data-Driven Universal Genetics Fix**: Registered data-driven `GeneticsConfig` across all 25+ vanilla animal species & updated attribute ID to `minecraft:scale`.
- [x] **`1.0.6+26.2`** (2026-08-02) - **Cramped Penalty & Spacious Recovery**: Breeding in confined 4x4 pens stunts offspring to 0.25x scale; breeding in open pastures recovers size genetics over generations. Backed by `natural-reproduction:cramped_space_penalty`.
- [x] **`1.0.5+26.2`** (2026-08-02) - **Scale-Based Item Drops**: Animal drop yield scales dynamically with physical body scale attribute, backed by `natural-reproduction:scale_drops` GameRule.
- [x] **`1.0.4+26.2`** (2026-08-02) - **Universal Animal Support**: Extended autonomous breeding and habitat triggers to ALL vanilla Minecraft animal species via `AnimalHabitatHelper`.
- [x] **`1.0.3+26.2`** (2026-08-02) - **Code Quality Audit Fixes**: Mixin member prefixing, JDK path correction, verification headers, 2-space JSON formatting, and CurseForge/Modrinth platform pages.
- [x] **`1.0.2+26.2`** (2026-08-02) - **YACL Migration**: Replaced legacy ClothConfig integration with YACL (YetAnotherConfigLib v3) + ModMenu optional GUI screen builder.
- [x] **`1.0.1+26.2`** (2026-08-02) - **Maintenance & Core Integration**: Added `natural-reproduction:rate` GameRule, species environmental habitat checks, `/naturalreproduction` Brigadier command suite, `ModVersionGuard`, and automated unit test suite.
- [x] **`1.0.0+26.2`** (2026-08-02) - **Initial Release**: Autonomous wild animal breeding and livestock genetics (`DasikAnimalGeneticsAPI`) for Cows, Pigs, Sheep, and Chickens.
