# ⚙️ Namespaced GameRules & Configuration

> **"Full control at your fingertips — tune ecosystem balance live without restarting the server."**

**Natural Reproduction** exposes its entire simulation engine through native, namespaced **GameRules** and an optional in-game configuration GUI. All mechanics can be tuned on the fly per world, and settings persist safely inside world save data.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Configuration Architecture** | Native Namespaced GameRules & JSON Fallback |
| **In-Game GUI Library** | **YetAnotherConfigLib (YACL v3)** & **ModMenu** |
| **Config File Path** | `config/naturalreproduction.json` |
| **World Save Persistence** | Stored natively in `level.dat` GameRules |
| **Total Registered GameRules**| **35+ GameRules** |
| **Hot Reload Support** | Full `/naturalreproduction reload` support |

---

## ⚠️ Important: Global Config vs. In-Game GameRules

> [!WARNING]
> **Understanding Global Config vs. World GameRules**  
> The file `config/naturalreproduction.json` only sets **initial default values for brand-new worlds** at the moment of world creation.  
> Once a world has been created, changing the JSON config file will **have no effect on existing saves**. To adjust settings in an existing world, use the **Edit Game Rules** UI menu or the `/naturalreproduction set` / `/gamerule` commands.

---

## 📊 Complete Namespaced GameRules Master Table

### 1. Core Simulation & Density Controls

| GameRule | Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Master toggle for all autonomous wild breeding checks. |
| `natural-reproduction:density_cap` | Integer | `10` | Maximum same-species entities within 16 blocks permitted for breeding. |
| `natural-reproduction:rate` | Integer | `24000` | Average interval in ticks between autonomous breeding attempts (24000 = 1 MC Day). |
| `natural-reproduction:min_scale` | Integer | `50` | Minimum physical body scale percentage (`50` = `0.50x`). |
| `natural-reproduction:max_scale` | Integer | `130` | Maximum physical body scale percentage (`130` = `1.30x`). |
| `natural-reproduction:scale_drops` | Boolean | `true` | Enables loot drops scaling proportionally with physical body size. |

### 2. Advanced Husbandry & Environmental Mechanics

| GameRule | Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | Enables size stunting in tight pens and spacious pasture recovery. |
| `natural-reproduction:inbreeding_degradation` | Boolean | `true` | Enables multi-generational lineage tracking and inbreeding collapse. |
| `natural-reproduction:pasture_enrichment` | Boolean | `true` | Enables Well-Nourished bonuses from hay, water, and shelter. |
| `natural-reproduction:overgrazing` | Boolean | `true` | Enables grass block wear into dirt/coarse dirt under dense herds. |
| `natural-reproduction:gestation_period` | Boolean | `true` | Enables pregnancy gestation countdown timers for autonomous breeding. |
| `natural-reproduction:manual_gestation` | Boolean | `true` | Applies pregnancy gestation countdowns to manual player hand-feeding. |
| `natural-reproduction:gestation_duration` | Integer | `24000` | Duration of pregnancy countdown in ticks (24000 = 1 MC Day). |
| `natural-reproduction:fertilized_chicken_eggs` | Boolean | `true` | Chickens lay 100% hatch Fertilized Eggs during wild breeding. |
| `natural-reproduction:chicken_infertile_regular_eggs` | Boolean | `true` | Reduces unfertilized regular egg hatch chance to 1/64. |
| `natural-reproduction:dispenser_egg_hatch_chance` | Integer | `75` | Hatch percentage for dispenser-fired Fertilized Eggs. |
| `natural-reproduction:herd_dynamics` | Boolean | `true` | Animals elect Alpha leaders and follow them in pastoral flocking. |
| `natural-reproduction:herd_stampede` | Boolean | `true` | Herds panic in synchronized stampedes when attacked. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | Animals in native biomes gain 2x breeding speed and +15% quality. |
| `natural-reproduction:biome_variants` | Boolean | `true` | Offspring adapt visual skin variants based on local climate. |
| `natural-reproduction:tracker_logs` | Boolean | `false` | Records autonomous reproduction events to console and log files. |

### 3. Individual Species Toggles (27 Species)

| GameRule | Type | Default | Species Target |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:allow_cow` | Boolean | `true` | Cows |
| `natural-reproduction:allow_mooshroom` | Boolean | `true` | Mooshrooms |
| `natural-reproduction:allow_sheep` | Boolean | `true` | Sheep |
| `natural-reproduction:allow_pig` | Boolean | `true` | Pigs |
| `natural-reproduction:allow_chicken` | Boolean | `true` | Chickens |
| `natural-reproduction:allow_wolf` | Boolean | `true` | Wolves (all 9 variants) |
| `natural-reproduction:allow_fox` | Boolean | `true` | Foxes (Red & Snow) |
| `natural-reproduction:allow_cat` | Boolean | `true` | Cats |
| `natural-reproduction:allow_ocelot` | Boolean | `true` | Ocelots |
| `natural-reproduction:allow_horse` | Boolean | `true` | Horses |
| `natural-reproduction:allow_donkey` | Boolean | `true` | Donkeys |
| `natural-reproduction:allow_mule` | Boolean | `true` | Mules |
| `natural-reproduction:allow_llama` | Boolean | `true` | Llamas |
| `natural-reproduction:allow_trader_llama` | Boolean | `true` | Trader Llamas |
| `natural-reproduction:allow_camel` | Boolean | `true` | Camels |
| `natural-reproduction:allow_goat` | Boolean | `true` | Goats |
| `natural-reproduction:allow_rabbit` | Boolean | `true` | Rabbits |
| `natural-reproduction:allow_frog` | Boolean | `true` | Frogs (Warm, Cold, Temperate) |
| `natural-reproduction:allow_turtle` | Boolean | `true` | Sea Turtles |
| `natural-reproduction:allow_axolotl` | Boolean | `true` | Axolotls |
| `natural-reproduction:allow_bee` | Boolean | `true` | Honey Bees |
| `natural-reproduction:allow_panda` | Boolean | `true` | Pandas |
| `natural-reproduction:allow_polar_bear` | Boolean | `true` | Polar Bears |
| `natural-reproduction:allow_armadillo` | Boolean | `true` | Armadillos |
| `natural-reproduction:allow_sniffer` | Boolean | `true` | Sniffers |
| `natural-reproduction:allow_strider` | Boolean | `true` | Striders |
| `natural-reproduction:allow_hoglin` | Boolean | `true` | Hoglins |

---

## 🖥️ Optional GUI Integration (YACL v3 & ModMenu)

When installed on the client alongside **YetAnotherConfigLib (YACL)** and **ModMenu**, Natural Reproduction provides a clean visual configuration screen accessible directly from the ModMenu interface:
* **Sliders**: Intuitive range sliders for breeding rates, density caps, and scale limits.
* **Toggles**: Quick switches for individual species and environmental features.
* **Server Compatibility**: Purely optional — clients without YACL can connect to servers seamlessly.

---

## 🔗 Related Documentation
* [[In-Game Commands & Breeding Tracker Logs|Commands-and-Diagnostics]]
* [[Autonomous Wild Breeding & Species Habitats|Autonomous-Breeding-and-Habitats]]
* [[Technical Architecture & Mixin Integration|Architecture-and-Mixins]]
