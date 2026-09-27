<p align="center">
  <a href="https://discord.gg/EV99bgAFqb"><img src="https://img.shields.io/badge/Discord-Join_Community-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="Join Discord"></a>
  <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
  <a href="https://modrinth.com/mod/dasik-library"><img src="https://img.shields.io/badge/Requires-Dasik_Library-purple?style=for-the-badge" alt="Requires Dasik Library"></a>
  <img src="https://img.shields.io/badge/Environment-Server_&_Client-success?style=for-the-badge" alt="Server & Client">
  <img src="https://img.shields.io/badge/Language-Java_25-orange?style=for-the-badge&logo=java" alt="Java 25">
  <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License GPLv3">
  <img src="https://img.shields.io/badge/Minecraft-26.2+-brightgreen?style=for-the-badge" alt="Minecraft 26.2+">
</p>

# 🌿 Natural Reproduction

> **"Ecology in Motion. Autonomous Breeding, Herd Dynamics, and Pasture Ecosystems."**

---

## 📖 Introduction

In vanilla Minecraft, animal life is strangely artificial and static. Cows, sheep, pigs, and chickens stand frozen in cramped pens, completely incapable of breeding, thriving, or expanding unless a player manually walks up and hand-feeds them wheat or seeds. The moment you leave your farm, the ecosystem grinds to a dead halt. Wild animal populations in meadows and savannahs never grow, natural herd social structures do not exist, and farming devolves into industrial 1x1 breeding pits that ruin game immersion.

**Natural Reproduction** breathes vibrant, organic life into Minecraft's wildlife under the **Delayed Gratification** design philosophy. Animals become dynamic ecological beings: they form cohesive social herds, follow established herd leaders, graze on pasture flora, and reproduce autonomously when their environment satisfies natural biological requirements (spacious pastures, shelter, food abundance, and density caps). Built with strict spatial caching algorithms, high-performance tick budgeting, and zero-allocation entity schedulers, ecosystems flourish without lagging your world.

> [!NOTE]
> **1 Jar 1 Version Policy:** I build **1 dedicated JAR for each Minecraft version** (e.g. MC 26.2, MC 26.3). Please download the exact build that matches your Minecraft installation.
> 
> **Dependency Callout:** For modern Minecraft releases (26.x+), requires both **Fabric API** and **Dasik Library** (`v1.8.0+`) for high-performance spatial herd clustering.

Part of the **Delayed Gratification Collection** — mods that cultivate patient, deeply rewarding survival progression.

---

## ✨ Features

### 🐂 Autonomous Herd Dynamics & Leader Following
- **Social Herd Clustering:** Passive animals (Cows, Sheep, Pigs, Goats, Horses) dynamically organize into social herd clusters using `HerdSocialHelper` and `FollowHerdLeaderGoal`.
- **Alpha Herd Leaders:** The oldest, healthiest adult in a pasture naturally emerges as the herd leader, guiding the group across pastures to fresh grass, water sources, and shaded tree canopies.

### 🌱 Environmental Fertility & Gestation Cycles
- **Pasture Space Requirements:** Animals refuse to reproduce if crammed into cramped, inhumane feedlots. Autonomous mating requires a minimum pasture density threshold (`natural_reproduction:pasture_density_limit`), encouraging sprawling pastoral ranches over 1-chunk lag pens.
- **Natural Gestation Timers:** Once conception occurs, expectant mothers experience a realistic gestation period marked by subtle visual heart particles and protective instincts.
- **Biome & Seasonal Fertility Modifiers:** Temperate meadows, lush plains, and river valleys offer fertility bonuses, while frozen tundras and arid deserts naturally limit reproductive frequency.

### 🛡️ Lag-Free Spatial Pasture Density Caps
- **Intelligent Spatial Caching:** Employs `SpatialBreedingCacheHelper` to compute local animal counts in $O(1)$ constant time without triggering costly entity iterations across whole chunks.
- **Population Self-Regulation:** When a pasture reaches its natural ecological carrying capacity (e.g. 16 animals per 32-block radius), autonomous reproduction safely enters dormancy, preventing runaway entity counts and protecting server TPS.

---

## 📊 Ecological Farming Comparison

| Farming Aspect | Vanilla Minecraft | Natural Reproduction |
| :--- | :--- | :--- |
| **Breeding Trigger** | Player must hand-feed items manually | **Autonomous when well-fed in spacious pastures** |
| **Herd Social Behavior** | Random aimless wandering | **Cohesive herds following alpha leaders** |
| **Feedlot / Pen Cramming**| Exploited in 1x1 hopper grinders | **Cramped penalties; requires open pasture space** |
| **Entity Count Safety** | Can breed infinitely causing server lag | **Hard spatial carrying capacity limits** |
| **Immersion & Lore** | Gamey, artificial breeding mechanic | **Living, breathing wildlife ecosystem** |

---

## ⚙️ Native GameRules & Server Configuration

Configure ecology parameters dynamically in-game:

| GameRule Key | Type | Default | Valid Range | Description |
| :--- | :---: | :---: | :---: | :--- |
| `natural_reproduction:enable_autonomous_breeding` | `Boolean` | `true` | `true / false` | Global toggle for autonomous animal reproduction. |
| `natural_reproduction:gestation_duration_ticks` | `Integer` | `6000` | `1200 – 24000` | Duration of gestation from mating to birth (ticks). |
| `natural_reproduction:pasture_density_limit` | `Integer` | `16` | `4 – 64` | Maximum animals permitted within carrying capacity radius. |
| `natural_reproduction:require_open_sky` | `Boolean` | `true` | `true / false` | Requires animals to have access to natural daylight. |
| `natural_reproduction:maternal_protection` | `Boolean` | `true` | `true / false` | Mother animals defend offspring from predators. |

---

## 📖 In-Depth How-To & Gameplay Playbook

### Step 1: Installing for Singleplayer or Servers
1. Install **Fabric Loader**, **Fabric API**, and **Dasik Library** (`v1.8.0+`) for Minecraft 26.2+ / 26.3+.
2. Place `natural-reproduction-x.y.z+<version>.jar` into your `mods/` directory.
3. Launch Minecraft. Animals in your world will naturally begin social herd grouping!

### Step 2: Designing a Sustainable Ranch
- Build a spacious wooden fenced pasture (at least 20x20 blocks) with grass blocks, a water trough, and shady trees.
- Bring in a pair of cows or sheep: over time, as they graze and enjoy the open space, they will autonomously mate and birth calves.
- Watch your ranch flourish organically while you focus on building, mining, and exploring!

---

## ☕ Support & Creator Community

I am an independent solo developer creating lightweight, vanilla-enhancing mods that respect your time and game performance. If Natural Reproduction enriches your world, consider supporting future development:

<p align="center">
  <a href="https://ko-fi.com/rifaditya"><img src="https://img.shields.io/badge/Ko--fi-Support_on_Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Support on Ko-fi"></a>
  <a href="https://sociabuzz.com/rifaditya"><img src="https://img.shields.io/badge/SocioBuzz-Support_Creator-00A651?style=for-the-badge" alt="Support on SocioBuzz"></a>
  <a href="https://saweria.co/rifaditya"><img src="https://img.shields.io/badge/Saweria-Support_Local-FFA500?style=for-the-badge" alt="Support on Saweria"></a>
</p>

> [!TIP]
> **🇮🇩 Indonesian Local Payment Note:** Indonesian supporters can also support my development work directly using local payment options (**GoPay, OVO, Dana, QRIS, LinkAja**) via **Saweria** or **SocioBuzz**!

Join our official Discord community for live development updates, early test builds, and friendly support:
- 💬 **Discord Community:** [https://discord.gg/EV99bgAFqb](https://discord.gg/EV99bgAFqb)

---

## 📜 Metadata & Permissions

| Property | Value |
| :--- | :--- |
| **Mod Name** | Natural Reproduction |
| **Namespace / Mod ID** | `natural_reproduction` |
| **License** | GNU General Public License v3.0 (GPLv3) |
| **Side Safety** | Server & Client (Synchronized) |
| **Source Code** | [GitHub Repository](https://github.com/Rifaditya/DG-natural-reproduction) |
| **Issue Tracker** | [GitHub Issues](https://github.com/Rifaditya/DG-natural-reproduction/issues) |

> [!IMPORTANT]
> **📦 Modpack Permissions & Distribution:**<br>
> You are fully welcome to include this mod in any modpack on any platform! However, the mod file must be downloaded directly through official distribution channels (**Modrinth** or **CurseForge**). Re-uploading, mirroring, or redistributing the original mod JAR to third-party mirror sites, scraper portals, or unauthorized launchers is strictly prohibited.
> <br><br>
> **⚖️ License & Fork Guidelines (No Zero-Change Re-uploads):**<br>
> This project is open-source under the **GNU GPLv3**. You are fully encouraged to inspect the code, learn from it, and fork the repository to create genuine modifications, substantial feature expansions, or community ports—provided your project remains open-source under GPLv3 with proper attribution.<br>
> **However, straight 1:1 re-uploads, clone forks with no meaningful functional changes, or re-publishing identical builds under different project names (e.g. to farm downloads or rewards) are strictly forbidden.**

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Delayed Gratification Collection*

</div>
