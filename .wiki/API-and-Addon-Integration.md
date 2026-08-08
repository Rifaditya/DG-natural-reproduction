# API & Addon Integration Reference

**Natural Reproduction** is built on top of **DasikLibrary**, utilizing data-driven genetics registries and dynamic GameRule managers. This guide covers integration APIs for addon developers and third-party mods.

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

### Scale Attribute Features in DasikLibrary 1.8.15+
- **Scale Offset Calculation**: Automatically subtracts `1.0f` for `ADD_VALUE` attribute modifiers, preventing 200% giant entity spawning bugs.
- **Stale Modifier Purging**: Actively purges legacy/duplicate `genetics_` attribute modifiers on entity spawn/tick.
- **Codec Bounds Crash Fix**: `DynamicGameRuleManager` integer builders handle `Integer.MIN_VALUE` without throwing `IllegalStateException` on world save.

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
