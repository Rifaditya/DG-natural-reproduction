<h1 align="center">Natural Reproduction Wiki Portal</h1>

<p align="center">
  <strong>Delayed Gratification Collection</strong> | <em>Pasture Management & Multi-Generational Investment</em>
</p>

<p align="center">
  <a href="https://minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg" alt="Minecraft"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/ModLoader-Fabric-blue.svg" alt="Fabric"></a>
  <a href="https://www.gnu.org/licenses/gpl-3.0"><img src="https://img.shields.io/badge/License-GPLv3-blue.svg" alt="License"></a>
  <a href="https://modrinth.com/mod/dasik-library"><img src="https://img.shields.io/badge/DasikLibrary-%3E%3D1.8.15-orange.svg" alt="DasikLibrary"></a>
</p>

> [!NOTE]
> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository** (`v1.3.4+26.2`), which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

Welcome to the central encyclopedic wiki documentation for **Natural Reproduction**. This mod overhauls animal husbandry across Minecraft, allowing all 27 passive mob species to autonomously breed in the wild when health, partner proximity, and environmental habitat conditions are satisfied.

---

## 🧭 Encyclopedic Wiki Directory

### 📦 Minecraft Version Guides
- **[[MC 26.2 Guide|Minecraft-26.2-Guide]]**: Dedicated Minecraft 26.2 installation, setup, dependency bounds, and environment guide.
- **[[Version Compatibility|Version-Compatibility]]**: Version lifecycle matrix, legacy 1.21.x -> 26.x migration history, and support policies.

### 🎮 Player & Feature Guides
- **[[Autonomous Wild Breeding|Autonomous-Wild-Breeding]]**: Learn how autonomous Love Mode AI checks work, 8-block partner proximity, 16-block overcrowding density caps ($10\text{ mobs}$), and tick math ($24000\text{ ticks} = 1\text{ MC Day}$).
- **[[Species Habitat Reference|Species-Habitat-Reference]]**: View the **complete 27 animal species reference table**, required 5x3x5 habitat blocks, skin variant triggers, and native climate biomes.
- **[[Genetics & Size Scaling|Genetics-and-Size-Scaling]]**: Master physical size attribute scaling (`0.25x` to `1.30x`), scale-proportional item drop yields, and the smooth gradual crowding penalty formula.
- **[[Biome Fertility & Variants|Biome-Fertility-and-Variants]]**: Explore native climate biome bonuses ($2\times\text{ breeding speed}, +15\%\text{ genetics quality}$) and visual entity skin variant adaptation.
- **[[GameRules|GameRules]]**: Complete namespaced GameRules reference table, including the 27 per-species toggles under `Natural Reproduction - Species Toggles`.
- **[[Commands|Commands]]**: Full `/naturalreproduction` Brigadier command tree, permission levels, and trackerlogs diagnostic subcommands.
- **[[Configuration|Configuration]]**: Configure mod settings via ModMenu and YACL v3 client GUI screens, client/server config sync, and server crash security gating.
- **[[HUD & Diagnostics|HUD-and-Diagnostics]]**: Breeding event trackerlogs, particle feedback (Smoke, Angry Villager, Happy Villager), and Debug Stick shortcuts.

### 💻 Developer Reference
- **[[Developer Setup & Building|Developer-Setup-and-Building]]**: Set up JDK 25 environment, run Gradle builds (`./gradlew build --no-daemon`), automated headless tests, and import project into IDEs.
- **[[Architecture & Mixins|Architecture-and-Mixins]]**: Technical architecture breakdown, package structure, Mixin injection targets (`AnimalBreedingMixin`, `AnimalDropScaleMixin`), and helper classes.
- **[[API & Addon Integration|API-and-Addon-Integration]]**: Integrate with `DasikAnimalGeneticsAPI`, dynamic GameRule registries (`DynamicGameRuleManager`), `ModVersionGuard`, and addon event hooks.

---

## 🌟 Core Design Philosophy

Natural Reproduction follows the **Delayed Gratification** philosophy:
- **No Free Handouts**: Animals will not reproduce if crammed into tiny 1x1 or 2x2 factory farming pits.
- **Rewarding Pasture Management**: Moving livestock to spacious pastures and native biomes rewards players with $2\times$ breeding speed, $+15\%$ genetics quality, and larger physical body sizes that yield bonus item drops!
