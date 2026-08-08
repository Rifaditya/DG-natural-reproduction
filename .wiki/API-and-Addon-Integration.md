# API & Addon Integration Reference

| Library / API | Compatible Version | Integration Purpose |
| :--- | :--- | :--- |
| **DasikLibrary** | `>= 1.8.15` | `DasikAnimalGeneticsAPI`, `DynamicGameRuleManager`, `ModVersionGuard` |
| **Fabric Loader** | `>= 0.19.1` | Mod entrypoints and client/server gating |
| **YACL v3** | `3.8.2+1.21.4-fabric` | Optional client configuration GUI builder |

---

## 🧬 DasikAnimalGeneticsAPI Integration

Natural Reproduction registers data-driven `GeneticsConfig` rules using `DasikAnimalGeneticsAPI`:

```java
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.minecraft.world.entity.animal.Animal;

// Query current physical scale attribute (minecraft:scale)
float currentScale = DasikAnimalGeneticsAPI.getScale(animal);

// Update physical scale attribute
float newScale = Math.clamp(currentScale * 1.15f, 0.25f, 1.30f);
DasikAnimalGeneticsAPI.setScale(animal, newScale);
```

---

## ⚙️ DynamicGameRuleManager Integration

Query or modify dynamic namespaced GameRules programmatically:

```java
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.server.level.ServerLevel;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

// Query boolean GameRule
boolean isEnabled = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.ENABLED);

// Query integer GameRule
int densityCap = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DENSITY_CAP);

// Query per-species GameRule
boolean isCowAllowed = NaturalReproductionFabric.isSpeciesAllowed(level, EntityTypes.COW);
```

---

## 🛡️ Zero-Dependency Guard (`ModVersionGuard`)

Natural Reproduction incorporates zero-dependency version guards resolved via Knot ClassLoader:

```java
import net.dasik.social.api.util.ModVersionGuard;

// Verified against MC 26.2+
ModVersionGuard.checkClass("net.minecraft.world.entity.EntityTypes");
```

---

## 🔌 Addon Event Hooks

Addon mods can inspect autonomous reproduction events by querying `BreedingTrackerLogger`:

```java
import net.vanillaoutsider.naturalreproduction.util.BreedingTrackerLogger;

// Check if tracker logs are enabled
boolean loggingActive = BreedingTrackerLogger.isEnabled();
```

For setup and building, see [[Developer Setup & Building|Developer-Setup-and-Building]].
