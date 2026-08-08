# Technical Architecture & Mixins Breakdown

| Component | Specification |
| :--- | :--- |
| **Architectural Law** | 1 File, 1 Function Law (Single-purpose classes) |
| **Mixin Framework** | SpongePowered Mixin for Fabric Loom 1.15.5 |
| **Java Platform** | Java 25 |
| **Thread Safety** | Non-blocking main thread execution |

---

## 📂 ASCII Package Architecture

```text
net.vanillaoutsider.naturalreproduction
│
├── NaturalReproductionFabric.java       # Mod initialize entrypoint & GameRules registration
├── ModVersionGuard.java                 # Zero-dependency startup class guard (Knot ClassLoader)
│
├── client/
│   ├── ModMenuIntegration.java          # ModMenu API entrypoint
│   └── YaclScreenHelper.java            # YACL v3 config screen builder
│
├── command/
│   └── NaturalReproductionCommand.java  # Brigadier command suite (/naturalreproduction)
│
├── mixin/
│   ├── AnimalBreedingMixin.java         # Autonomous love mode tick injection & genetics inheritance
│   └── AnimalDropScaleMixin.java        # Scale-proportional item drop yield injection
│
└── util/
    ├── AnimalBiomeHelper.java           # Native climate biomes & skin variant triggers
    ├── AnimalCrampedSpaceHelper.java    # Dual structural confinement & density crowding penalty
    ├── AnimalDropHelper.java            # Scale-based item drop yield multiplier logic
    ├── AnimalHabitatHelper.java         # 27 species environmental habitat block scanning
    ├── BreedingLogEntry.java            # Event tracking data structure
    └── BreedingTrackerLogger.java       # In-memory breeding event logger
```

---

## ⚙️ Java Mixins Target Breakdown

| Mixin Class | Target Minecraft Class | Target Method / Injection Point | Description |
| :--- | :--- | :--- | :--- |
| `AnimalBreedingMixin` | `net.minecraft.world.entity.animal.Animal` | `@Inject` into `customServerAiStep` (HEAD) & `spawnChildFromBreeding` (TAIL) | Evaluates autonomous Love Mode conditions (health, partner proximity, overcrowding density cap, habitat blocks, species toggles) and handles baby scale inheritance/cramped stunting. |
| `AnimalDropScaleMixin` | `net.minecraft.world.entity.Entity` | `@Inject` into `spawnAtLocation` (HEAD) | Intercepts entity item drop spawning and delegates to `AnimalDropHelper.applyScaleDropMultiplier` when `SCALE_DROPS` GameRule is enabled. |

---

## 🛠️ Helper Utilities Architecture

### 1. `AnimalHabitatHelper.java`
- Scans a 5x3x5 cuboid (`offset(-2, -1, -2)` to `offset(2, 1, 2)`) around the entity for required environmental habitat blocks across all 27 animal species.
- Evaluates `isSpeciesReproductionAllowed(level, self)` against the `natural_reproduction_species` GameRules.

### 2. `AnimalCrampedSpaceHelper.java`
- **Dual Confinement Check**:
  - `isConfinedArea(level, pos)`: Detects horizontal 1x1 / 2x2 pit holes, fences, walls, and solid block enclosures (3+ blocked sides).
  - `isOvercrowded = isConfinedPen || extraLocalCount >= 3`: Applies crowding penalty both in tight structural pens AND in large open pastures (e.g. 100x100) when animals are crammed together.
- **Smooth Continuous Penalty Formula**:
  $$\text{penaltyMultiplier} = \max(0.95 - (\text{extraLocalCount} \times 0.05), 0.40)$$

For API integration, see [[API & Addon Integration|API-and-Addon-Integration]].
