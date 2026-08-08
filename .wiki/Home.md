# Welcome to the Natural Reproduction Wiki!

**Natural Reproduction** is a **Delayed Gratification** Minecraft mod that enables passive animals to reproduce autonomously in the wild when environmental, health, and space conditions are satisfied.

Instead of relying solely on player feeding, animals naturally seek out environmental habitat blocks, thrive in their native climate biomes, adapt visual skin variants, and pass down physical size genetics across generations.

---

## 🧭 Wiki Topics Directory

### 🎮 Player & Administrator Guides
- **[[Animal Breeding Guide|Animal-Breeding-Guide]]**: Learn how autonomous wild breeding works, partner proximity, overcrowding density caps, and view the **complete 27 animal species habitat & biome reference table**.
- **[[Genetics & Size Scaling|Genetics-and-Size-Scaling]]**: Understand physical size attribute scaling (`0.25x` to `1.30x`), scale-proportional item drop yields, cramped pen stunting penalties, and spacious pasture recovery.
- **[[GameRules & Commands|GameRules-and-Commands]]**: Master server customization via dynamic GameRules (including the 27 per-species toggles under `Natural Reproduction - Species Toggles`) and the `/naturalreproduction` Brigadier command suite.
- **[[Configuration Guide|Configuration-Guide]]**: Configure mod settings via ModMenu and YACL v3 client GUI screens.

### 💻 Developer & Technical Reference
- **[[Developer Setup & Building|Developer-Setup-and-Building]]**: Set up JDK 25 environment, run Gradle builds (`./gradlew build`), and import project into IDEs.
- **[[Architecture & Mixins|Architecture-and-Mixins]]**: Technical architecture breakdown, Mixin injection points (`AnimalBreedingMixin`, `AnimalDropScaleMixin`), and helper classes (`AnimalCrampedSpaceHelper`, `AnimalHabitatHelper`).
- **[[API & Addon Integration|API-and-Addon-Integration]]**: Integrate with `DasikAnimalGeneticsAPI`, dynamic GameRule registries (`DynamicGameRuleManager`), `ModVersionGuard`, and addon event hooks.

---

## 🌟 Core Design Philosophy

Natural Reproduction follows the **Delayed Gratification** philosophy:
- **No Free Handouts**: Animals won't reproduce if crammed into tiny 1x1 or 2x2 factory farming pits.
- **Rewarding Pasture Management**: Moving livestock to spacious pastures and native biomes rewards players with 2x breeding speed, +15% genetics quality, and larger physical body sizes that yield bonus item drops!
