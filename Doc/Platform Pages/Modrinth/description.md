<p align="center">
  <a href="https://discord.gg/EV99bgAFqb"><img src="https://img.shields.io/badge/Discord-Join_Community-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="Join Discord"></a>
  <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
  <a href="https://modrinth.com/mod/dasik-library"><img src="https://img.shields.io/badge/Requires-Dasik_Library-purple?style=for-the-badge" alt="Requires Dasik Library"></a>
  <img src="https://img.shields.io/badge/Environment-Server_%26_Client-success?style=for-the-badge" alt="Server & Client">
  <img src="https://img.shields.io/badge/Language-Java_25-orange?style=for-the-badge&logo=java" alt="Java 25">
  <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License GPLv3">
  <img src="https://img.shields.io/badge/Minecraft-1.20.1_|_1.21.x_|_26.x-brightgreen?style=for-the-badge" alt="Minecraft 1.20.1 | 1.21.x | 26.x">
</p>

# 🌿 Natural Reproduction

> **"Ecology in Motion. Autonomous Breeding, Herd Dynamics, and Pasture Ecosystems."**

---

## 📖 Introduction

In vanilla Minecraft, animal life is strangely artificial and static. Cows, sheep, pigs, and chickens stand frozen in cramped pens, completely incapable of breeding, thriving, or expanding unless a player manually walks up and hand-feeds them wheat or seeds. The moment you leave your farm, the ecosystem grinds to a dead halt. Wild animal populations in meadows and savannahs never grow, natural herd social structures do not exist, and farming devolves into industrial 1×1 breeding pits that ruin game immersion.

**Natural Reproduction** breathes vibrant, organic life into Minecraft's wildlife under the **Delayed Gratification** design philosophy. Animals become dynamic ecological beings: they form cohesive social herds, follow established herd leaders, graze on pasture flora, and reproduce autonomously when their environment satisfies natural biological requirements — spacious pastures, shelter, food abundance, and density caps. Built with strict spatial caching algorithms, high-performance tick budgeting, and zero-allocation entity schedulers, ecosystems flourish without lagging your world.

> [!NOTE]
> **1 Jar 1 Version Policy:** I build **1 dedicated JAR for each Minecraft version** (e.g. MC 1.20.1, MC 26.3). Please download the exact build that matches your Minecraft installation.
>
> **Dependency Callout:** Requires **Fabric API** and **Dasik Library** (`>=1.9.0`).

Part of the **Delayed Gratification Collection** — mods that cultivate patient, deeply rewarding survival progression.

---

## ✨ Features

### 🐂 Autonomous Animal Reproduction
- **Passive Autonomous Breeding:** 27 animal species (Cow, Pig, Sheep, Chicken, Mooshroom, Horse, Donkey, Mule, Llama, Trader Llama, Camel, Wolf, Cat, Fox, Ocelot, Turtle, Frog, Axolotl, Polar Bear, Panda, Rabbit, Goat, Armadillo, Sniffer, Bee, Strider, Hoglin) breed autonomously when environmental conditions are met. No player hand-feeding required.
- **Per-Species Toggles:** 27 individual GameRules let you enable or disable autonomous reproduction for each species independently (`natural-reproduction:allow_cow`, `allow_pig`, etc.).

### 🧬 Data-Driven Animal Genetics
- **Inherited Trait System:** Offspring inherit genetics across three traits — `scale` (body size), `max_health` (HP), and `movement_speed` — from parents via the DasikLibrary Genetics API.
- **Scale-Based Item Drops:** Animals with higher scale attributes drop proportionally more items when harvested (`natural-reproduction:scale_drops`).
- **Cramped Space Penalty:** Breeding in inhumane feedlots stunts offspring size down to 0.10×; breeding in spacious pastures recovers healthy genetics (`natural-reproduction:cramped_space_penalty`).
- **Inbreeding Lineage Degradation:** Closed-herd inbreeding degrades genetics over generations (`natural-reproduction:inbreeding_degradation`).
- **Size Bounds:** Configurable minimum, normal, and maximum scale percentages via GameRules (`min_scale` default 10%, `normal_scale` default 95%, `max_scale` default 120%).

### 🌱 Environmental Fertility & Gestation Cycles
- **Population Density Cap:** Autonomous mating halts once a species reaches its carrying capacity within 16 blocks (default: 10 animals, `natural-reproduction:density_cap`).
- **Autonomous Gestation Timers:** Expectant mothers enter a pregnancy countdown before delivering offspring (default: 24,000 ticks = 1 MC day, `natural-reproduction:gestation_period` + `natural-reproduction:gestation_duration`).
- **Manual Breeding Gestation:** Player hand-feeding also triggers the gestation cycle rather than instant birth (`natural-reproduction:manual_gestation`).
- **Pasture Enrichment:** Pastures with feeding troughs, hay, water, and shelter grant well-nourished bonuses to breeding frequency (`natural-reproduction:pasture_enrichment`).
- **Overgrazing Terrain Wear:** Dense herds convert grass blocks into dirt and coarse dirt over time (`natural-reproduction:overgrazing`).

### 🐣 Chicken Egg System
- **Fertilized Chicken Eggs:** Autonomous chicken breeding rolls 50/50 for immediate chicks or a guaranteed-hatch Fertilized Egg (`natural-reproduction:fertilized_chicken_eggs`).
- **Infertile Regular Eggs:** Ordinary unfertilized eggs have a reduced 1/64 miracle hatch chance (`natural-reproduction:chicken_infertile_regular_eggs`).
- **Dispenser Egg Hatch Chance:** Fertilized Eggs fired from dispensers have a configurable hatch chance (default 75%, `natural-reproduction:dispenser_egg_hatch_chance`).

### 🐑 Herd Dynamics & Social Behavior
- **Social Herd Clustering:** Animals dynamically organize into social herd clusters led by the largest Alpha animal, with diurnal cohesion schedules (`natural-reproduction:herd_dynamics`).
- **Herd Stampede Panic:** Damage from predators or players alerts nearby herd members to stampede away in unison (`natural-reproduction:herd_stampede`).

### 🌍 Biome Ecology
- **Native Biome Fertility Boost:** Animals in their native biomes breed 2× faster and offspring receive a +15% genetics quality bonus (`natural-reproduction:biome_fertility`).
- **Biome Variant Skin Adaptation:** Offspring born in specific biomes adapt their visual entity variant skin dynamically (e.g. Snowy Wolves, Desert Frogs) (`natural-reproduction:biome_variants`).

---

## 📊 Ecological Farming Comparison

| Farming Aspect | Vanilla Minecraft | Natural Reproduction |
| :--- | :--- | :--- |
| **Breeding Trigger** | Player must hand-feed items manually | **Autonomous when conditions are met** |
| **Offspring Genetics** | Fixed — babies always identical | **Inherited scale, health, and speed traits** |
| **Herd Social Behavior** | Random aimless wandering | **Cohesive herds following Alpha leaders** |
| **Feedlot Cramming** | Exploited in 1×1 hopper grinders | **Cramped space stunts genetics** |
| **Entity Count Safety** | Can breed infinitely causing server lag | **Hard spatial density cap per species** |
| **Terrain Impact** | Animals have no terrain effect | **Dense herds cause overgrazing** |

---

## ⚙️ Native GameRules & Server Configuration

Configure ecology parameters dynamically in-game via `/gamerule` or the `/naturalreproduction` (`/nr`) command suite.

### Core Ecology Rules

| GameRule Key | Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Global toggle for autonomous animal reproduction. |
| `natural-reproduction:density_cap` | Integer | `10` | Max animals of same species within 16 blocks for autonomous breeding. |
| `natural-reproduction:rate` | Integer | `24000` | Average tick interval between breeding attempts (24000 = 1 MC Day). |
| `natural-reproduction:scale_drops` | Boolean | `true` | Animal item drops scale with the animal's physical scale attribute. |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | Cramped breeding stunts offspring size; spacious pastures recover genetics. |
| `natural-reproduction:inbreeding_degradation` | Boolean | `true` | Closed-herd inbreeding degrades genetics over generations. |
| `natural-reproduction:pasture_enrichment` | Boolean | `true` | Pastures with troughs, hay, water, and shelter grant breeding bonuses. |
| `natural-reproduction:overgrazing` | Boolean | `true` | Dense herds convert grass blocks into dirt and coarse dirt. |
| `natural-reproduction:gestation_period` | Boolean | `true` | Autonomous breeding triggers gestation countdown before birth. |
| `natural-reproduction:manual_gestation` | Boolean | `true` | Player manual feeding also uses gestation timers. |
| `natural-reproduction:gestation_duration` | Integer | `24000` | Gestation duration in ticks (24000 = 1 MC Day). |
| `natural-reproduction:fertilized_chicken_eggs` | Boolean | `true` | Chicken breeding rolls 50/50 for chicks or guaranteed-hatch Fertilized Eggs. |
| `natural-reproduction:chicken_infertile_regular_eggs` | Boolean | `true` | Ordinary eggs have a reduced 1/64 miracle hatch chance. |
| `natural-reproduction:dispenser_egg_hatch_chance` | Integer | `75` | Percentage chance (0–100) for a dispenser-fired Fertilized Egg to hatch. |
| `natural-reproduction:herd_dynamics` | Boolean | `true` | Animals form herds led by the largest Alpha with diurnal schedule cohesion. |
| `natural-reproduction:herd_stampede` | Boolean | `true` | Damage alerts nearby herd members to stampede away in unison. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | Native biome animals breed 2× faster with +15% genetics quality. |
| `natural-reproduction:biome_variants` | Boolean | `true` | Offspring in specific biomes adapt their visual entity variant skin. |
| `natural-reproduction:min_scale` | Integer | `10` | Minimum animal scale percentage (10 = 0.10× size). |
| `natural-reproduction:normal_scale` | Integer | `95` | Standard baseline animal scale percentage (95 = 0.95× size). |
| `natural-reproduction:max_scale` | Integer | `120` | Maximum animal scale percentage (120 = 1.20× size). |
| `natural-reproduction:tracker_logs` | Boolean | `false` | Log autonomous reproduction events to server console and tracker. |

### Per-Species Toggle Rules (all default `true`)

Each of the 27 supported species has an individual toggle under the `natural-reproduction:` namespace:

`allow_cow` · `allow_pig` · `allow_sheep` · `allow_chicken` · `allow_mooshroom` · `allow_horse` · `allow_donkey` · `allow_mule` · `allow_llama` · `allow_trader_llama` · `allow_camel` · `allow_wolf` · `allow_cat` · `allow_fox` · `allow_ocelot` · `allow_turtle` · `allow_frog` · `allow_axolotl` · `allow_polar_bear` · `allow_panda` · `allow_rabbit` · `allow_goat` · `allow_armadillo` · `allow_sniffer` · `allow_bee` · `allow_strider` · `allow_hoglin`

---

## 🛠️ Command Suite

| Command | Permission | Description |
| :--- | :---: | :--- |
| `/naturalreproduction help` | All | Show help for all subcommands. |
| `/nr status` | All | Display current GameRule config status. |
| `/nr get <rule>` | All | Get the current value of a GameRule. |
| `/nr set <rule> <value>` | Operators | Set a GameRule value. |
| `/nr reset` | Operators | Reset all GameRules to defaults. |
| `/nr reload` | Operators | Refresh genetics and scale modifiers on all loaded animals. |
| `/nr stats` | All | Display spatial cache sizes and reproduction event count. |
| `/nr purge [caches\|logs\|all]` | Operators | Clear spatial caches and/or tracker logs. |
| `/nr trackerlogs [list\|enable\|disable\|clear]` | Mixed | Manage the reproduction tracker log. |
| `/nr logs` | All | List the 10 most recent autonomous breeding events. |

---

## 📖 Getting Started

### Installing

1. Install **Fabric Loader**, **Fabric API**, and **Dasik Library** (`>=1.9.0`) for your Minecraft version.
2. Place `natural-reproduction-x.y.z+<version>.jar` into your `mods/` folder.
3. Launch Minecraft. Animals will begin autonomous breeding and herd behavior immediately!

### Designing a Sustainable Ranch

- Build a spacious fenced pasture (at least 20×20 blocks) with grass, water, and shade.
- Bring in a pair of cows or sheep: over time they will autonomously mate and birth offspring.
- Watch the genetic traits — offspring from spacious ranches grow larger and healthier over generations.

---

## ☕ Support & Creator Community

I am an independent solo developer creating lightweight, vanilla-enhancing mods that respect your time and game performance. If Natural Reproduction enriches your world, consider supporting future development:

<p align="center">
  <a href="https://ko-fi.com/rifaditya"><img src="https://img.shields.io/badge/Ko--fi-Support_on_Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Support on Ko-fi"></a>
  <a href="https://sociabuzz.com/rifaditya"><img src="https://img.shields.io/badge/SocioBuzz-Support_Creator-00A651?style=for-the-badge" alt="Support on SocioBuzz"></a>
  <a href="https://saweria.co/rifaditya"><img src="https://img.shields.io/badge/Saweria-Support_Local-FFA500?style=for-the-badge" alt="Support on Saweria"></a>
</p>

> [!TIP]
> **🇮🇩 Indonesian Local Payment Note:** Indonesian supporters can support my development work directly using local payment options (**GoPay, OVO, Dana, QRIS, LinkAja**) via **Saweria** or **SocioBuzz**!

Join my official Discord community for live development updates, early test builds, and friendly support:
- 💬 **Discord Community:** [https://discord.gg/EV99bgAFqb](https://discord.gg/EV99bgAFqb)

---

## 📜 Metadata & Permissions

| Property | Value |
| :--- | :--- |
| **Mod Name** | Natural Reproduction |
| **Namespace / Mod ID** | `natural-reproduction` |
| **License** | GNU General Public License v3.0 (GPLv3) |
| **Side Safety** | Server & Client (Synchronized) |
| **Source Code** | [GitHub Repository](https://github.com/Rifaditya/DG-natural-reproduction) |
| **Issue Tracker** | [GitHub Issues](https://github.com/Rifaditya/DG-natural-reproduction/issues) |

> [!IMPORTANT]
> **📦 Modpack Permissions & Distribution:**
> You are fully welcome to include this mod in any modpack on any platform! However, the mod file must be downloaded directly through official distribution channels (**Modrinth** or **CurseForge**). Re-uploading, mirroring, or redistributing the original mod JAR to third-party mirror sites, scraper portals, or unauthorized launchers is strictly prohibited.
>
> **⚖️ License & Fork Guidelines (No Zero-Change Re-uploads):**
> This project is open-source under the **GNU GPLv3**. You are fully encouraged to inspect the code, learn from it, and fork the repository to create genuine modifications, substantial feature expansions, or community ports — provided your project remains open-source under GPLv3 with proper attribution. Straight 1:1 re-uploads, clone forks with no meaningful functional changes, or re-publishing identical builds under different project names are strictly forbidden.

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Delayed Gratification Collection*

</div>
