# 💻 Technical Architecture & Mixin Integration

> **"A clean, robust architecture powered by lightweight Mixins, zero tick overhead, and high-performance caching."**

This document serves as the comprehensive technical guide for developers, server administrators, and addon creators. It details the internal architecture, Mixin injection descriptors, DasikLibrary genetics APIs, and build pipelines of **Natural Reproduction**.

---

## 📋 Technical Infobox

| Technical Property | Specification |
| :--- | :--- |
| **Platform Target** | Fabric Mod Loader (`>=0.19.1`) |
| **Java Release Target** | **Java 25+** (`release = 25`) |
| **Primary Dependency** | `dasik-library` (`>=1.8.35`) |
| **Mixin Configuration** | `natural-reproduction.mixins.json` |
| **Compatibility Mode** | **Server-Side Compatible** (Vanilla Client Connect) |
| **Build Tooling** | Gradle 9.3+ / Fabric Loom 1.15+ |

---

## 🏗️ Architecture & Package Layout

```text
net.vanillaoutsider.naturalreproduction/
├── NaturalReproductionFabric.java           - Main entrypoint, GameRules initialization, event listeners
├── command/
│   └── NaturalReproductionCommand.java      - Brigadier command suite (/naturalreproduction)
├── config/
│   ├── NaturalReproductionConfig.java       - Data record schema for config persistence
│   └── ModMenuIntegration.java              - Client YACL v3 & ModMenu GUI bridge
├── helper/
│   ├── AnimalBiomeHelper.java               - Biome fertility multipliers and variant skin adaptation
│   ├── AnimalCrampedSpaceHelper.java        - Confinement detection and spacious recovery curves
│   ├── AnimalDropHelper.java                - Item drop scaling math based on physical scale
│   ├── AnimalGestationHelper.java           - Pregnancy countdown ticker and prenatal vitality
│   ├── AnimalHabitatHelper.java             - 27-species environmental block trigger evaluation
│   ├── AnimalLineageHelper.java             - Multi-generational pedigree and inbreeding degradation
│   ├── AnimalPastureHelper.java             - Pasture enrichment structure scores and overgrazing wear
│   ├── ChickenEggHelper.java                - Fertilized Egg delivery and hatch chance handling
│   ├── HerdSocialHelper.java                - Alpha leader election and panic stampede triggers
│   └── SpatialBreedingCacheHelper.java      - Fast-fail spatial density cache
├── ai/
│   └── FollowHerdLeaderGoal.java            - Pastoral flocking AI goal (Priority 3)
├── logging/
│   └── BreedingTrackerLogger.java           - In-memory circular buffer for reproduction diagnostics
└── mixin/
    ├── AnimalBreedingMixin.java             - Injects gestation countdown & wild breeding hooks
    ├── AnimalDropScaleMixin.java            - Intercepts loot table drops to apply scale multipliers
    └── ThrownEggMixin.java                  - Intercepts thrown egg impact for Fertilized Egg mechanics
```

---

## 💉 Complete Mixin Target Matrix

| Mixin Class | Target Minecraft Class | Injection Target | Purpose |
| :--- | :--- | :--- | :--- |
| `AnimalBreedingMixin` | `net.minecraft.world.entity.animal.Animal` | `@Inject` at `tick()` HEAD | Drives staggered autonomous breeding evaluations and gestation timers. |
| `AnimalBreedingMixin` | `net.minecraft.world.entity.animal.Animal` | `@Inject` at `spawnChildFromBreeding()` HEAD | Intercepts offspring birth to apply inbreeding, scale, and prenatal vitality. |
| `AnimalDropScaleMixin` | `net.minecraft.world.entity.LivingEntity` | `@Inject` at `dropFromLootTable()` | Multiplies item drop counts by the entity's physical `minecraft:scale`. |
| `ThrownEggMixin` | `net.minecraft.world.entity.projectile.ThrownEgg` | `@Inject` at `onHit()` HEAD | Handles guaranteed 100% hatch mechanics for Fertilized Egg items. |

---

## 🧬 DasikLibrary API Integration

Natural Reproduction leverages **DasikLibrary** for standardized genetics, data-driven registries, and dynamic GameRule management:

```java
// Query or mutate entity physical genetics via DasikAnimalGeneticsAPI
float currentScale = DasikAnimalGeneticsAPI.getScale(animal);
DasikAnimalGeneticsAPI.setScale(animal, 1.25f);

// Access universal genetics registry
EntityGeneticsRecord record = EntityGeneticsRegistry.getRecord(animal.getType());
```

---

## 🔨 Building & Testing from Source

### 1. Requirements
* JDK 25+ installed and configured on `JAVA_HOME`.
* Git repository cloned.

### 2. Gradle Build Commands
```bash
# Compile and run automated GameTests / JUnit assertions
./gradlew test --no-daemon

# Build production release JAR
./gradlew build --no-daemon
```

---

## 🔗 Related Documentation
* [[Namespaced GameRules & Configuration|GameRules-and-Configuration]]
* [[In-Game Commands & Breeding Tracker Logs|Commands-and-Diagnostics]]
* [[Physical Scale & Dynamic Harvest Drops|Physical-Scale-and-Harvest-Drops]]
