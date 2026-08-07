# Natural Reproduction (DG-natural-reproduction)

**Delayed Gratification Collection** | *Pasture Management & Multi-Generational Investment*

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric](https://img.shields.io/badge/ModLoader-Fabric-blue.svg)](https://fabricmc.net)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Animals in the wild should not depend solely on players for survival and growth. **Natural Reproduction** allows all passive mobs in Minecraft to autonomously breed and multiply when environmental conditions are ideal, while rewarding thoughtful pasture management, native biome climate alignment, and genetics investment.

---

## 🌟 Key Features

- **🌾 Autonomous Wild Breeding**: Healthy, well-fed passive animals enter Love Mode naturally when surrounded by compatible mates.
- **🏞️ Native Biome Climate Fertility Boost**: Breeding animals in their native climate biome (e.g. Wolves in Taigas, Frogs in Swamps, Camels in Deserts, Polar Bears in Ice Plains) grants **2x faster breeding frequency** and **+15% genetics quality**.
- **🎨 Biome Variant Skin Adaptation**: Offspring born in specific biomes dynamically adapt their visual entity variant skin (e.g. Snowy Wolves, Desert Frogs, White Rabbits) or inherit parent variants.
- **🐺 Variant-Specific Environmental Habitat Triggers**: Animals with variant skins require tailored habitat block conditions (e.g. Snowy Wolves/Foxes need snow & ice blocks; Warm Frogs & Gold Rabbits need sand & cactus).
- **🏚️ Cramped Space Penalty vs. 🌾 Spacious Size Recovery**: Breeding in confined factory farm pens (>=4 mobs in 4x4 area) stunts offspring down to `0.25x` scale. Moving animals to open pastures recovers size genetics (+30% per generation up to `1.30x`).
- **⚖️ Scale-Based Item Drops**: Item drops on death/harvest scale proportionally with the animal's physical body scale.
- **⚙️ Dynamic GameRules & Config Screen**: Fully configurable via 7 dynamic GameRules (`natural-reproduction:enabled`, `density_cap`, `rate`, `scale_drops`, `cramped_space_penalty`, `biome_fertility`, `biome_variants`) and optional ModMenu / YACL v3 config screen.
- **💬 Brigadier Command Suite**: Full `/naturalreproduction` command suite (`status`, `get`, `set`, `reset`, `reload`) with tab completion.

---

## 📋 Dynamic GameRules

| GameRule | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Enable autonomous wild animal breeding. |
| `natural-reproduction:density_cap` | Integer | `10` | Maximum same-species animals permitted within a 16-block radius. |
| `natural-reproduction:rate` | Integer | `24000` | Average tick interval between breeding checks (24000 = 1 MC Day). |
| `natural-reproduction:scale_drops` | Boolean | `true` | Item drops scale proportionally with physical body scale attribute. |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | Confined breeding stunts scale down to 0.25x; spacious pastures recover size. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | Native biomes grant 2x breeding frequency and +15% genetics quality boost. |
| `natural-reproduction:biome_variants` | Boolean | `true` | Offspring born in specific biomes adapt visual entity variant skins. |

---

## 🛠️ Requirements

- **Fabric Loader**: `>= 0.19.1`
- **Minecraft**: `26.2+`
- **Fabric API**
- **DasikLibrary**: `>= 1.8.0`

---

## 📄 License

This mod is licensed under the **GNU General Public License v3.0 (GPLv3)**.
Copyright (C) 2026 Dasik (Rifaditya).
