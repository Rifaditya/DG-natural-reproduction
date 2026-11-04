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

In vanilla Minecraft, animal life is strangely artificial and static. Cows, sheep, pigs, and chickens stand frozen in cramped pens, completely incapable of breeding, thriving, or expanding unless a player manually walks up and hand-feeds them. The moment you leave your farm, the ecosystem grinds to a dead halt. Wild animal populations never grow, natural herd structures do not exist, and farming devolves into industrial 1×1 breeding pits.

**Natural Reproduction** breathes vibrant, organic life into Minecraft's wildlife under the **Delayed Gratification** design philosophy. Animals become dynamic ecological beings — they form cohesive social herds, follow established herd leaders, and reproduce autonomously when their environment satisfies natural biological requirements. Built with strict spatial caching algorithms and high-performance tick budgeting, ecosystems flourish without lagging your world.

**1 Jar 1 Version Policy:** I build 1 dedicated JAR for each Minecraft version (MC 1.20.1, MC 1.21.1, MC 1.21.11, MC 26.1, MC 26.2, MC 26.3). Download the exact build matching your Minecraft installation.

**Requires:** Fabric API + Dasik Library (>=1.9.0).

Part of the **Delayed Gratification Collection** — mods that cultivate patient, deeply rewarding survival progression.

---

## ✨ Features

### Autonomous Animal Reproduction
- 27 animal species breed autonomously when environmental conditions are met. No player hand-feeding required.
- Per-species GameRule toggles allow enabling or disabling autonomous reproduction for each species independently.

### Data-Driven Animal Genetics
- Offspring inherit genetics across three traits: scale (body size), max_health (HP), and movement_speed.
- Scale-Based Item Drops: Animals with higher scale attributes drop proportionally more items.
- Cramped Space Penalty: Breeding in feedlots stunts offspring size (0.10x minimum); spacious pastures recover healthy genetics.
- Inbreeding Lineage Degradation: Closed-herd inbreeding degrades genetics across generations.
- Configurable minimum (default 10%), normal (default 95%), and maximum (default 120%) size bounds.

### Environmental Fertility & Gestation Cycles
- Population Density Cap: Autonomous mating halts once a species reaches its carrying capacity within 16 blocks (default: 10 animals).
- Gestation Timers: Expectant mothers enter a pregnancy countdown (default: 24,000 ticks = 1 MC day) before delivering offspring.
- Manual Breeding Gestation: Player hand-feeding also triggers the gestation cycle rather than instant birth.
- Pasture Enrichment: Pastures with feeding troughs, hay, water, and shelter grant well-nourished bonuses.
- Overgrazing Terrain Wear: Dense herds convert grass blocks into dirt and coarse dirt over time.

### Chicken Egg System
- Fertilized Chicken Eggs: Autonomous chicken breeding rolls 50/50 for immediate chicks or a guaranteed-hatch Fertilized Egg.
- Infertile Regular Eggs: Ordinary unfertilized eggs have a reduced 1/64 miracle hatch chance.
- Dispenser Egg Hatch Chance: Fertilized Eggs fired from dispensers have a configurable hatch chance (default 75%).

### Herd Dynamics & Social Behavior
- Social Herd Clustering: Animals dynamically organize into herds led by the largest Alpha animal with diurnal cohesion.
- Herd Stampede Panic: Damage from predators or players alerts nearby herd members to stampede away in unison.

### Biome Ecology
- Native Biome Fertility Boost: Animals in native biomes breed 2x faster with +15% offspring genetics quality.
- Biome Variant Skin Adaptation: Offspring born in specific biomes adapt their visual entity variant skin dynamically.

---

## ⚙️ Native GameRules & Server Configuration

Configure ecology parameters via `/gamerule` or the `/naturalreproduction` (`/nr`) command suite.

### Core Ecology Rules

| GameRule Key | Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Global toggle for autonomous animal reproduction. |
| `natural-reproduction:density_cap` | Integer | `10` | Max animals of same species within 16 blocks for autonomous breeding. |
| `natural-reproduction:rate` | Integer | `24000` | Average tick interval between breeding attempts (24000 = 1 MC Day). |
| `natural-reproduction:scale_drops` | Boolean | `true` | Animal drops scale with the animal's physical scale attribute. |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | Cramped breeding stunts offspring size; spacious pastures recover genetics. |
| `natural-reproduction:inbreeding_degradation` | Boolean | `true` | Closed-herd inbreeding degrades genetics over generations. |
| `natural-reproduction:pasture_enrichment` | Boolean | `true` | Pastures with troughs, hay, water, and shelter grant breeding bonuses. |
| `natural-reproduction:overgrazing` | Boolean | `true` | Dense herds convert grass blocks into dirt and coarse dirt. |
| `natural-reproduction:gestation_period` | Boolean | `true` | Autonomous breeding triggers gestation countdown before birth. |
| `natural-reproduction:manual_gestation` | Boolean | `true` | Player manual feeding also uses gestation timers. |
| `natural-reproduction:gestation_duration` | Integer | `24000` | Gestation duration in ticks (24000 = 1 MC Day). |
| `natural-reproduction:fertilized_chicken_eggs` | Boolean | `true` | Chicken breeding rolls 50/50 for chicks or guaranteed-hatch Fertilized Eggs. |
| `natural-reproduction:chicken_infertile_regular_eggs` | Boolean | `true` | Ordinary eggs have a reduced 1/64 miracle hatch chance. |
| `natural-reproduction:dispenser_egg_hatch_chance` | Integer | `75` | Percentage chance (0-100) for a dispenser-fired Fertilized Egg to hatch. |
| `natural-reproduction:herd_dynamics` | Boolean | `true` | Animals form herds led by the largest Alpha with diurnal schedule cohesion. |
| `natural-reproduction:herd_stampede` | Boolean | `true` | Damage alerts nearby herd members to stampede away in unison. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | Native biome animals breed 2x faster with +15% genetics quality. |
| `natural-reproduction:biome_variants` | Boolean | `true` | Offspring in specific biomes adapt their visual entity variant skin. |
| `natural-reproduction:min_scale` | Integer | `10` | Minimum animal scale percentage (10 = 0.10x size). |
| `natural-reproduction:normal_scale` | Integer | `95` | Standard baseline animal scale percentage (95 = 0.95x size). |
| `natural-reproduction:max_scale` | Integer | `120` | Maximum animal scale percentage (120 = 1.20x size). |
| `natural-reproduction:tracker_logs` | Boolean | `false` | Log autonomous reproduction events to server console and tracker. |

### Per-Species Toggle Rules (all default true)

Each of the 27 supported species has an individual toggle:

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

## ☕ Support & Creator Community

I am an independent solo developer creating lightweight, vanilla-enhancing mods. If Natural Reproduction enriches your world, consider supporting future development:

<p align="center">
  <a href="https://ko-fi.com/rifaditya"><img src="https://img.shields.io/badge/Ko--fi-Support_on_Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Support on Ko-fi"></a>
  <a href="https://sociabuzz.com/rifaditya"><img src="https://img.shields.io/badge/SocioBuzz-Support_Creator-00A651?style=for-the-badge" alt="Support on SocioBuzz"></a>
  <a href="https://saweria.co/rifaditya"><img src="https://img.shields.io/badge/Saweria-Support_Local-FFA500?style=for-the-badge" alt="Support on Saweria"></a>
</p>

**Indonesian supporters:** Local payment options (GoPay, OVO, Dana, QRIS, LinkAja) available via **Saweria** or **SocioBuzz**.

Join my official Discord community:
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

**Modpack Permissions:** You are welcome to include this mod in any modpack. However, the mod file must be downloaded directly through official distribution channels (Modrinth or CurseForge). Re-uploading, mirroring, or redistributing the mod JAR to third-party mirror sites is strictly prohibited.

**License & Fork Guidelines:** Open-source under GNU GPLv3. You may fork and modify with proper attribution and open-source compliance. Zero-change re-uploads or clone forks under different project names are strictly forbidden.

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Delayed Gratification Collection*

</div>
