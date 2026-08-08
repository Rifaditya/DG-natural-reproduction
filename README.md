<h1 align="center">Natural Reproduction</h1>

<p align="center">
  <strong>Delayed Gratification Collection</strong> | <em>Pasture Management & Multi-Generational Investment</em>
</p>

<p align="center">
  <a href="https://minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg" alt="Minecraft"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/ModLoader-Fabric-blue.svg" alt="Fabric"></a>
  <a href="https://www.gnu.org/licenses/gpl-3.0"><img src="https://img.shields.io/badge/License-GPLv3-blue.svg" alt="License"></a>
  <a href="https://modrinth.com/mod/dasik-library"><img src="https://img.shields.io/badge/DasikLibrary-%3E%3D1.8.15-orange.svg" alt="DasikLibrary"></a>
</p>

---

Animals in the wild should not depend solely on players for survival and growth. **Natural Reproduction** allows all passive mobs in Minecraft to autonomously breed and multiply when environmental conditions are ideal, rewarding thoughtful pasture management, native climate alignment, and multi-generational size genetics investment.

---

## 🌟 Key Features

- **🌾 Autonomous Wild Breeding**: Healthy, well-fed passive animals enter Love Mode naturally when surrounded by compatible mates and environmental habitat blocks.
- **🏞️ Native Biome Climate Fertility Boost**: Breeding animals in their native climate biome (e.g. Wolves in Taigas, Frogs in Swamps, Camels in Deserts, Polar Bears in Ice Plains) grants **2x faster breeding frequency** and **+15% genetics quality boost**.
- **🎨 Biome Variant Skin Adaptation**: Offspring born in specific biomes dynamically adapt visual entity variant skins (e.g. Desert Frogs become Warm variant, Taiga Wolves become Pale/Black variant, Snowy Rabbits become White variant).
- **🐺 Variant-Specific Environmental Habitat Triggers**: Animals with variant skins require tailored habitat block conditions (e.g. Snowy Wolves & Foxes require snow/ice blocks; Warm Frogs & Gold Rabbits require sand/cactus).
- **🏚️ Cramped Space Penalty vs. 🌾 Spacious Size Recovery**: Breeding in confined factory farm pens (4+ mobs in 4x4 area) stunts offspring scale down to `0.25x`. Moving animals to open pastures (+30% recovery per generation) restores physical size potential up to `1.30x`!
- **⚖️ Scale-Based Item Drops**: Item drop yield on death/harvest scales proportionally with physical body scale attribute.
- **⚙️ 27 Per-Species GameRule Toggles**: Full granular control over every single animal species under a dedicated `Natural Reproduction - Species Toggles` category (all enabled by default).

📖 **Complete Animal Breeding Reference**: For full habitat block requirements, skin variant triggers, and native biomes across all 27 animal species, consult the **[Player Breeding Guide](Doc/Players/breeding_guide.md)**.

---

## 💻 Developer Guide: Building from Source

### Prerequisites
- **Java JDK**: `25` or higher
- **Gradle**: `9.3+` (wrapper included)

### Clone & Build
```bash
# Clone the repository
git clone https://github.com/Dasik/Natural-Reproduction.git
cd Natural-Reproduction

# Build the tagged JAR artifact
./gradlew build --no-daemon
```
The compiled output JAR file will be saved in `build/libs/natural-reproduction-1.3.2+26.2.jar`.

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
| `natural-reproduction:allow_<species>` | Boolean | `true` | Individual toggles for all 27 animal species under *Natural Reproduction - Species Toggles*. |

---

## 💬 Brigadier Command Suite

Manage all mod settings in-game via `/naturalreproduction` with tab completion:

- `/naturalreproduction status` — Display active states of all core rules and 27 species toggles.
- `/naturalreproduction get <rule>` — Query current value of a specific rule.
- `/naturalreproduction set <rule> <val>` — Dynamically update any core rule or species toggle.
- `/naturalreproduction trackerlogs list|clear|enable|disable` — Inspect and manage autonomous reproduction logs.
- `/naturalreproduction reset` — Reset all rules and 27 species toggles to defaults.

---

## 🛠️ Installation Requirements

- **Fabric Loader**: `>= 0.19.1`
- **Minecraft**: `26.2+`
- **Fabric API**: Required
- **DasikLibrary**: `>= 1.8.15` *(Required)*
- **YetAnotherConfigLib (YACL v3)**: Optional *(Client GUI)*
- **ModMenu**: Optional *(Client GUI)*

---

## 👥 Credits

| Role | Contributor |
| :--- | :--- |
| **Lead Developer & Author** | **Dasik (Rifaditya)** |
| **Genetics & GameRule Engine** | **DasikLibrary Team** |

---

## 📄 License & Modpack Usage

> This mod is licensed under the **GNU General Public License v3.0 (GPLv3)**.  
> You are welcome to include **Natural Reproduction** in any modpack published on CurseForge or Modrinth!

<p align="center"><em>Made with ❤️ by Dasik (Rifaditya)</em></p>
