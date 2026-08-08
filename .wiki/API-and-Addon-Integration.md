# API & Addon Integration Reference

| Library / API | Compatible Version | Integration Purpose |
| :--- | :--- | :--- |
| **DasikLibrary** | `>= 1.8.15` | `DasikAnimalGeneticsAPI`, `DynamicGameRuleManager`, `ModVersionGuard` |
| **Fabric Loader** | `>= 0.19.1` | Mod entrypoints and client/server gating |
| **YACL v3** | `3.8.2+1.21.4-fabric` | Optional client configuration GUI builder |

---

## 🧬 DasikAnimalGeneticsAPI Integration

Natural Reproduction registers universal data-driven `GeneticsConfig` rules across all 27 animal species using `EntityGeneticsRegistry` and `GeneticsLimitRegistry`:

```java
import net.dasik.social.api.genetics.EntityGeneticsRegistry;
import net.dasik.social.api.genetics.GeneticsConfig;
import net.dasik.social.api.genetics.GeneticsLimitRegistry;
import net.dasik.social.api.genetics.TraitConfig;
import net.dasik.social.api.genetics.MutationRule;

// Register Data-Driven Traits across all 27 Animal Species
Map<String, TraitConfig> animalTraits = Map.of(
    "scale", new TraitConfig("scale", "minecraft:scale", "ADD_VALUE", 0.0f, 1.0f, 0.50f, 1.30f),
    "max_health", new TraitConfig("max_health", "minecraft:generic.max_health", "ADD_VALUE", 2.0f, 0.5f, -4.0f, 12.0f),
    "movement_speed", new TraitConfig("movement_speed", "minecraft:generic.movement_speed", "ADD_MULTIPLIED_BASE", 0.05f, 0.5f, -0.04f, 0.08f)
);

GeneticsConfig config = new GeneticsConfig(animalTraits, animalMutations);

for (EntityType<?> type : allAnimals) {
    EntityGeneticsRegistry.register(type, config);

    // Register Dynamic Limit Callbacks tied to server MIN_SCALE & MAX_SCALE GameRules
    GeneticsLimitRegistry.registerMin(type, "scale", (entity, defaultMin) -> {
        if (entity.level() instanceof ServerLevel sl) {
            return DynamicGameRuleManager.getInt(sl, MIN_SCALE) / 100.0f;
        }
        return 0.50f;
    });

    GeneticsLimitRegistry.registerMax(type, "scale", (entity, defaultMax) -> {
        if (entity.level() instanceof ServerLevel sl) {
            return DynamicGameRuleManager.getInt(sl, MAX_SCALE) / 100.0f;
        }
        return 1.30f;
    });
}
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
