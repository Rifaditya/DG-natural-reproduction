<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <a href="https://modrinth.com/mod/dasik-library"><img src="https://img.shields.io/badge/Requires-Dasik_Library-purple?style=for-the-badge" alt="Requires Dasik Library"></a>
    <img src="https://img.shields.io/badge/Language-Java_25-orange?style=for-the-badge&logo=java" alt="Java 25">
    <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License">
    <img src="https://img.shields.io/badge/Minecraft-26.2+-brightgreen?style=for-the-badge" alt="Minecraft 26.2+">
</p>

# 🐑 Natural Reproduction - Autonomous Livestock Husbandry & Ecological Genetics

> **"Nature does not wait for player intervention to blossom."**

In vanilla Minecraft, animals stand frozen in cramped pens, completely helpless until a player right-clicks them with wheat. In real ecosystems, animals graze, form structured herds led by alpha animals, gestate over time, and thrive in rich pastures while suffering severe degradation in overcrowded conditions.

**Natural Reproduction** transforms Minecraft's passive fauna into a dynamic, living ecosystem. Wild animals autonomously mate when their environmental habitat needs are met, while pastoral farmers are rewarded for spacious pastures, rotational grazing, and careful genetic stewardship.

Part of the **Delayed Gratification Collection** — mods that reward long-term planning, multi-generational investment, and strategic animal husbandry.

---

## ✨ Comprehensive Feature Catalog

### 🌿 Autonomous Ecosystem Breeding & Food Blocks
Wild animals no longer require manual player hand-feeding to reproduce. When animals are healthy and surrounded by species-appropriate environmental blocks, they autonomously enter love mode:
- **Cows, Sheep & Goats**: Require nearby grass blocks, tall grass, or hay bales.
- **Pigs**: Seek mud blocks, farmland crops, or carrots/potatoes.
- **Chickens**: Require seed crops, hay bales, or grassy turf.
- **Wolves & Foxes**: Seek biome-specific den blocks (snow/ice for Snowy variants, podzol/spruce for Taigas).
- **Frogs & Turtles**: Require water bodies, lily pads, sand, or mangrove mud.
- *Supports all 27+ vanilla Minecraft animal species!*

### 👑 Herd Social Dynamics & Alpha Leadership
Animals no longer scatter randomly. They form organic pastoral herds:
- **Alpha Leader Election**: The herd dynamically elects the largest physical animal (by scale genetics) as their Alpha leader.
- **Pastoral Flocking**: Herd members stay within 5–16 blocks of their leader, grazing together during daylight hours and seeking sheltered pens at night.
- **Herd Distress Stampede Panic**: If a predator or player attacks any herd member, all nearby animals panic in a synchronized 5-second stampede away from danger!

### ⏳ Pregnancy Gestation Timers & Prenatal Vitality
No more instant baby pops. Mating initiates a natural pregnancy countdown:
- **Autonomous Gestation**: Default duration of 24,000 ticks (1 in-game Minecraft Day).
- **Prenatal Pasture Vitality**: Mothers kept in enriched, spacious pastures during pregnancy bestow **Prenatal Vitality** onto their offspring (+15% max health, +10% speed, and +10% baseline scale)!

### 🥚 Dedicated Chicken Reproduction & Fertilized Eggs
Chickens feature an authentic, separate reproduction cycle:
- **Autonomous Mating**: When chickens breed autonomously, they roll a 50/50 chance to either hatch an immediate baby chick or lay a **Fertilized Egg**.
- **Guaranteed Hatch Rate**: Fertilized Eggs have a **100% hatch rate** when thrown by players, and a **75% hatch rate** when fired from automated dispensers!
- **Infertile Regular Eggs**: Ordinary unfertilized eggs have a reduced 1/64 miracle hatch chance, isolating egg farms from runaway entity lag.

### 🛑 Cramped Factory Farm Penalty vs. Spacious Pasture Recovery
Factory farming has realistic consequences:
- **Cramped Space Stunting**: Confining breeding animals to 1x1 pit holes or tight, overcrowded pens gradually stunts offspring genetics down to **0.25x scale** (yielding minimal meat and drops).
- **Spacious Pasture Recovery**: Moving stunted animals into open, spacious green pastures grants **+10% to +30% genetic size recovery per generation**, gradually restoring their pedigree back to full potential!

### 🧬 Lineage Inbreeding Degradation & Hybrid Vigor
Closed-herd inbreeding carries authentic biological penalties:
- **Lineage Tracking**: Every animal tracks parental lineage across generations.
- **Inbreeding Tiers (T0 to T4)**: Repeated sibling and parent-offspring mating causes severe genetic collapse. At Tier 3/Tier 4, animals yield **Rotten Flesh & Bones** instead of prime meat, and Tier 4 animals suffer lethal health decay.
- **Hybrid Vigor**: Outcrossing unrelated bloodlines grants a **+15% scale boost** and superior genetic vitality!

### 🏡 Pasture Enrichment, Rotational Grazing & Shelter
Invest in your pastures to reap rich agricultural rewards:
- **Pasture Enrichment**: Providing water cauldrons, composters, hay bales, and barn roof shelters grants animals the **Well-Nourished** state (+25% faster breeding checks, +10% offspring scale, golden sparkle particles).
- **Overgrazing Terrain Wear**: Overcrowding too many animals on a single pasture causes grass blocks to gradually degrade into dirt and coarse dirt, encouraging rotational grazing.

### 🌍 Biome Variant Adaptation & Climate Fertility
- **Native Biome Fertility**: Breeding animals in their native climate biomes (Taigas for Wolves, Deserts for Camels, Swamps for Frogs) grants **2x faster breeding frequency** and **+15% genetics quality**.
- **Variant Skin Adaptation**: Offspring born in specialized biomes naturally adapt their visual variant skins (e.g. Snowy Wolves, Warm/Cold Frogs, Desert Rabbits).

### 📏 Physical Scale-Based Harvest Drops
Size directly dictates harvest yields:
- Scaled animal drops dynamically scale from **0.50x to 1.30x** meat, leather, and wool. Larger livestock reward thoughtful breeding!

### 🧩 Compatibility & HUD Integrations
- **Server-Side Compatible**: Works seamlessly on dedicated servers — vanilla clients can connect without installing the mod.
- **In-Game GUI**: Full **YetAnotherConfigLib (YACL v3)** and **ModMenu** configuration screen.
- **Data-Driven Genetics**: Powered by **DasikLibrary API** and `EntityGeneticsRegistry`.

---

## 📊 Quick Reference & Mechanics Matrix

### 🐾 Species Environmental Habitat Triggers (27 Species)

| Species Category | Example Animals | Required Habitat Blocks |
| :--- | :--- | :--- |
| **Bovines & Equines** | Cows, Mooshrooms, Horses, Donkeys, Mules | `GRASS_BLOCK`, `SHORT_GRASS`, `TALL_GRASS`, `HAY_BLOCK` |
| **Ovine & Caprine** | Sheep, Goats, Llamas, Camels | `GRASS_BLOCK`, `SHORT_GRASS`, `HAY_BLOCK`, `SAND` |
| **Porcines** | Pigs, Hoglins | `MUD`, `FARMLAND`, `CARROTS`, `POTATOES`, `CRIMSON_NYLIUM` |
| **Avians & Small Fauna** | Chickens, Rabbits, Bees | `WHEAT_SEEDS`, `HAY_BLOCK`, `CARROTS`, `FLOWERS` |
| **Canines & Felines** | Wolves, Foxes, Cats, Ocelots | `PODZOL`, `SPRUCE_LEAVES`, `SNOW_BLOCK`, `GRASS_BLOCK` |
| **Aquatics & Amphibians**| Frogs, Turtles, Axolotls | `WATER`, `LILY_PAD`, `MUD`, `SAND`, `SEAGRASS` |
| **Nether & Exotic** | Striders, Sniffers, Armadillos, Polar Bears | `LAVA`, `WARPED_NYLIUM`, `MOSS_BLOCK`, `ICE`, `PACKED_ICE` |

### 🧬 Inbreeding Degradation Matrix

| Tier | Generation Status | Genetic Effects | Harvest Drop Penalties |
| :---: | :--- | :--- | :--- |
| **T0** | Clean Bloodline / Wild | Full genetic baseline (`1.00x` scale) | Normal drops |
| **T1** | First Inbred Generation | `-10%` Max HP, `-5%` Movement Speed | Normal drops |
| **T2** | Second Inbred Generation | `-20%` Max HP, `-10%` Movement Speed, Stunted Scale | `-25%` Item Yield |
| **T3** | Severe Lineage Inbreeding | `-35%` Max HP, Genetic Defects, Severe Stunting | **Meat turns into Rotten Flesh & Bones** |
| **T4** | Lethal Lineage Collapse | **Lethal Health Decay (1 dmg/sec)** | **100% Rotten Flesh & Bones Only** |
| **HV** | **Hybrid Vigor (Outcross)** | **+15% Scale, +20% Max HP, +10% Speed** | **+25% Bonus Prime Drops** |

---

## 💻 In-Game Commands

The mod includes a full `/naturalreproduction` Brigadier command suite with tab-completion (requires permission level 2 / cheat access):

```text
/naturalreproduction help                           - Displays all available commands
/naturalreproduction status                         - Prints active breeding settings and density limits
/naturalreproduction get <gamerule>                 - Inspects the live value of a specific setting
/naturalreproduction set <gamerule> <val>           - Adjusts a setting live (e.g. /naturalreproduction set min_scale 60)
/naturalreproduction reset                          - Restores all GameRules to default settings
/naturalreproduction reload                         - Dynamically refreshes scale and modifiers on all loaded mobs
/naturalreproduction trackerlogs list               - Views recent autonomous wild reproduction events
/naturalreproduction trackerlogs enable/disable     - Toggles autonomous reproduction event logging
/naturalreproduction trackerlogs clear              - Clears the breeding tracker log history
```

---

## ⚙️ Native Namespaced GameRules

> [!WARNING]
> **Important: Global Config vs. In-Game GameRules**  
> The global configuration file only defines **default values for brand-new worlds** at creation time.  
> If you have **already created or opened a world**, changing the config file will have no effect. You must adjust settings in-game using the **Edit Game Rules** UI screen or the `/naturalreproduction set` / `/gamerule` commands.

| GameRule | Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Master toggle for autonomous wild animal reproduction. |
| `natural-reproduction:density_cap` | Integer | `10` | Maximum same-species animals within 16 blocks for breeding. |
| `natural-reproduction:rate` | Integer | `24000` | Average tick interval between breeding attempts (24000 = 1 MC Day). |
| `natural-reproduction:min_scale` | Integer | `50` | Minimum animal scale percentage (`50` = `0.50x` scale). |
| `natural-reproduction:max_scale` | Integer | `130` | Maximum animal scale percentage (`130` = `1.30x` scale). |
| `natural-reproduction:scale_drops` | Boolean | `true` | Multiplies item drops based on physical body scale. |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | Enables size stunting in tight pens and spacious pasture recovery. |
| `natural-reproduction:inbreeding_degradation` | Boolean | `true` | Enables multi-generational inbreeding genetic collapse. |
| `natural-reproduction:pasture_enrichment` | Boolean | `true` | Enables bonuses from feeding troughs, hay, water, and shelter. |
| `natural-reproduction:overgrazing` | Boolean | `true` | High-density herds convert grass blocks to dirt/coarse dirt. |
| `natural-reproduction:gestation_period` | Boolean | `true` | Enables pregnancy countdown timer for autonomous breeding. |
| `natural-reproduction:manual_gestation` | Boolean | `true` | Applies pregnancy gestation timers to manual player feeding. |
| `natural-reproduction:gestation_duration` | Integer | `24000` | Duration of pregnancy countdown in ticks (24000 = 1 MC Day). |
| `natural-reproduction:fertilized_chicken_eggs`| Boolean | `true` | Chickens lay 100% hatch Fertilized Eggs during wild breeding. |
| `natural-reproduction:chicken_infertile_regular_eggs` | Boolean | `true` | Reduces unfertilized regular egg hatch chance to 1/64. |
| `natural-reproduction:dispenser_egg_hatch_chance` | Integer | `75` | Hatch chance percentage for dispenser-fired Fertilized Eggs. |
| `natural-reproduction:herd_dynamics` | Boolean | `true` | Animals elect Alpha leaders and maintain pastoral flocking. |
| `natural-reproduction:herd_stampede` | Boolean | `true` | Herds panic in synchronized stampedes when attacked. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | Animals in native biomes gain 2x breeding speed and +15% quality. |
| `natural-reproduction:biome_variants` | Boolean | `true` | Offspring adapt visual variant skins in specialized biomes. |
| `natural-reproduction:tracker_logs` | Boolean | `false` | Records autonomous reproduction events to console and log. |
| `natural-reproduction:allow_<species>` | Boolean | `true` | 27 individual toggles to enable/disable specific species. |

---

## 📖 In-Depth How-To & Gameplay Playbook

Mastering animal husbandry in Natural Reproduction requires thoughtful ecosystem management:

### 1. Designing an Enriched Pasture
- Build pastures with at least **8x8 to 16x16 blocks of open grass**.
- Place **Hay Bales**, **Water Cauldrons**, and **Composters** near grazing areas.
- Build a roofed shelter (wood/stone roof with open fence gates). Animals grazing in enriched pastures gain the **Well-Nourished** state (+25% faster breeding checks, +10% offspring scale, golden sparkles).

### 2. Establishing Herds & Alpha Leadership
- Introduce at least 3–4 animals of the same species into your pasture.
- The largest animal automatically becomes the **Alpha leader**. Surrounding animals will follow the leader during daytime grazing and retreat to shelter at dusk.

### 3. Managing Gestation & Prenatal Care
- When animals mate, the mother enters pregnancy gestation (1 Minecraft Day).
- Ensure pregnant mothers have access to enriched pastures to guarantee their offspring receive **Prenatal Vitality** (+15% HP, +10% Speed, +10% Scale).

### 4. Preventing Inbreeding Collapse
- Never breed consecutive generations of the same closed family.
- If you notice animals shrinking or dropping **Rotten Flesh**, capture a wild animal from a distant biome and introduce it to your herd. Outcrossing triggers **Hybrid Vigor** (+15% scale boost and complete pedigree recovery).

### 5. Managing Chicken Flocks
- Collect **Fertilized Eggs** laid by breeding chickens. Throw them manually for a guaranteed 100% baby chick hatch, or load them into Dispensers (75% hatch chance) for automated hatchery systems.

### 6. Live In-Game Configuration
- Adjust scale limits on the fly using `/naturalreproduction set min_scale 50` and `/naturalreproduction set max_scale 130`.
- Run `/naturalreproduction reload` to immediately clamp and update all active animals across loaded chunks!

---

## ☕ Support My Work

If you enjoy the **Delayed Gratification Collection** and want to support ongoing updates:

<p align="center">
    <a href="https://ko-fi.com/dasikigaijin/tip"><img src="https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Ko-fi"></a>
    <a href="https://sociabuzz.com/dasikigaijin/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge" alt="SocioBuzz"></a>
    <a href="https://saweria.co/DasikIgaijinn"><img src="https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge" alt="Saweria"></a>
</p>

> **Indonesian Users:** SocioBuzz and Saweria support local payment methods (GoPay, OVO, DANA, QRIS) if you wish to support me locally!

---

## 📦 Modpack Permissions & Licensing

> **Modpack Distribution Policy:**  
> You are free to include this mod in any modpack, provided that the modpack is hosted on the same platform where you obtained this mod (e.g. CurseForge modpacks on CurseForge, Modrinth modpacks on Modrinth). Cross-platform redistribution is strictly prohibited to support the creator and ensure legitimate downloads.

---

## 📜 Credits

| Role | Author |
| :--- | :--- |
| **Creator & Developer** | **Dasik** (Rifaditya) |
| **Collection** | Delayed Gratification Collection |
| **License** | GPLv3 |

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Delayed Gratification Collection*

</div>
