# 🌍 Biome Variant Adaptation & Climate Fertility

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **Haftungsausschluss zum Repository-Quellcode-Status**: Die Dokumentation in diesem Wiki spiegelt den **aktuellen Quellcode-Status im Repository** wider (`v1.3.4+26.2`), der neuere unveröffentlichte Commits oder Entwicklungsfunktionen vor den öffentlichen Releases auf CurseForge und Modrinth enthalten kann.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Feature Name** | Biome Adaptation & Climate Fertility |
| **Biome Fertility Toggle** | `natural-reproduction:biome_fertility` (Default: `true`) |
| **Biome Variants Toggle** | `natural-reproduction:biome_variants` (Default: `true`) |
| **Native Biome Speed Boost** | `2.0x` Faster Breeding Evaluation Frequency |
| **Native Biome Quality Buff**| `+15%` Baseline Genetics & Health Quality |
| **Supported Adaptive Species**| Wolves (9 variants), Frogs (3 variants), Rabbits (6 variants), Foxes (2 variants) |
| **Primary Helper** | `AnimalBiomeHelper.java` |

---

## 🎮 Player & Survival Workflow

1. **Native Climate Fertility**:
   - Animals kept in their native climate biomes (e.g. Wolves in Taigas, Camels in Deserts, Frogs in Mangrove Swamps, Polar Bears in Ice Plains) breed twice as often.
2. **Adaptive Offspring Skin Variants**:
   - When animals deliver offspring in a new biome, the baby dynamically adapts its visual skin variant based on the local temperature and climate:
     - **Wolves**: Born in Snowy Taigas &rarr; *Snowy Wolf*; born in Old Growth Pine &rarr; *Black Wolf*; born in Savannas &rarr; *Ashen Wolf*.
     - **Frogs**: Warm biomes (Desert, Jungle) &rarr; *Warm (Orange) Frog*; Cold biomes (Snowy, Deep Ocean) &rarr; *Cold (Green) Frog*; Temperate &rarr; *Temperate (Grey) Frog*.
     - **Rabbits**: Deserts &rarr; *Desert Coat*; Snowy Plains &rarr; *White Coat*; Forests &rarr; *Brown Coat*.
     - **Foxes**: Cold & Snowy biomes &rarr; *Snow Fox*; Temperate biomes &rarr; *Red Fox*.

---

## 🧮 Climate Fertility & Variant Matrix

### 1. Climate Fertility Multipliers

| Species | Native Biome Categories | Breeding Rate Multiplier | Genetics Quality Bonus |
| :--- | :--- | :---: | :---: |
| **Cow, Sheep, Horse** | Plains, Meadow, Savanna | **2.0x** | `+15%` Scale / HP |
| **Pig** | Swamps, Forests, Farmlands | **2.0x** | `+15%` Scale / HP |
| **Wolf** | Taigas, Forests, Groves | **2.0x** | `+15%` Attack / HP |
| **Camel** | Deserts, Badlands | **2.0x** | `+15%` Speed / Scale |
| **Frog** | Swamps, Mangrove Swamps | **2.0x** | `+15%` Jump / HP |
| **Goat** | Jagged Peaks, Snowy Slopes | **2.0x** | `+15%` Jump / HP |

### 2. Algorithmic Variant Resolution
```java
// Determines the visual skin variant based on delivery biome
Holder<Biome> currentBiome = level.getBiome(offspring.blockPosition());
AnimalBiomeHelper.applyBiomeVariant(offspring, currentBiome);
```

---

## 💻 Developer & Mixin Hooks

`AnimalBiomeHelper` hooks into offspring spawning logic:

```java
public static void applyAdaptiveVariant(Animal offspring, ServerLevel level) {
    if (!level.getGameRules().getBoolean(NaturalReproductionFabric.RULE_BIOME_VARIANTS)) {
        return;
    }
    // Resolves Wolf, Frog, Fox, or Rabbit variant registries dynamically
    VariantRegistryHelper.adaptToBiome(offspring, level.getBiome(offspring.blockPosition()));
}
```

---

> ☕ **Entwicklung unterstützen**: Wenn dir diese Mod gefällt, unterstütze den Autor auf [Ko-fi](https://ko-fi.com/rifaditya) für Vorab-Testversionen!

---

## 🔗 Related Documentation
* [[Autonomous Wild Breeding & Species Habitats|de_de-Autonomous-Breeding-and-Habitats]]
* [[Herd Dynamics & Leadership|de_de-Herd-Dynamics-and-Alpha-Leadership]]
* [[Physical Scale & Dynamic Harvest Drops|de_de-Physical-Scale-and-Harvest-Drops]]
* Return to [[Home Portal|de_de-Home]]
