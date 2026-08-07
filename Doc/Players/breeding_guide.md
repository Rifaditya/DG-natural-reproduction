# Natural Reproduction: Player Guide & Breeding Requirements

Welcome to **Natural Reproduction**, a Vanilla Outsider mod that enables wild animals to breed autonomously when their environment and health conditions are ideal.

---

## 🌾 Core Breeding Requirements

For a mob to enter **Love Mode** naturally without player intervention, it must satisfy **all** of the following requirements during its AI check cycle:

### 1. Health & Age
- **Full Health**: The animal must be at **100% max health**. Injured or hungry animals will not breed.
- **Adult Status**: The mob must be a fully grown adult (`age == 0`). Baby animals and growing juveniles cannot enter Love Mode.
- **Not Currently Breeding**: The mob must not already be in Love Mode.

### 2. Partner Proximity
- A compatible mate of the **same species** (`e.getType() == self.getType()`) must be located within **8 blocks**.
- The potential mate must also be an adult (`age == 0`) and alive.

### 3. Population Density Cap (Overcrowding Protection)
- To prevent entity cramming, lag, and unnatural population explosions, natural breeding is **paused** if there are already more than **10 animals** of the same species within a **16-block radius**.
- You can adjust or disable this limit using GameRules (see below).

---

## 🏡 Universal Species Environmental Habitat Triggers

All Minecraft animal species rely on nearby environmental resources to trigger their natural breeding urge. The mob scans a **5x3x5 area** (`offset(-2, -1, -2)` to `offset(2, 1, 2)`) around itself for specific blocks:

| Animal Category & Species | Required Environmental Blocks | Scan Area |
| :--- | :--- | :--- |
| **🐮 Cow & 🐑 Sheep** | Grass Block, Short Grass, Tall Grass, or Wheat | 5x3x5 around mob |
| **🍄 Mooshroom** | Mycelium, Brown/Red Mushrooms, or Mushroom Blocks | 5x3x5 around mob |
| **🐷 Pig** | Mud, Farmland, Carrots, Beetroots, or Potatoes | 5x3x5 around mob |
| **🐔 Chicken** | Hay Block, Short/Tall Grass, or Wheat | 5x3x5 around mob |
| **🐴 Horse, Donkey & Mule** | Grass Block, Hay Block, Dandelion, or Sugar Cane | 5x3x5 around mob |
| **🦙 Llama & Trader Llama** | Hay Block, Grass Block, or Fern | 5x3x5 around mob |
| **🐪 Camel** | Cactus, Sand, or Dead Bush | 5x3x5 around mob |
| **Canines & Foxes** | Wolves / Dogs, Foxes | `COARSE_DIRT`, `PODZOL`, `GRASS_BLOCK`, `SWEET_BERRY_BUSH` *(Snowy Wolves/Foxes require `SNOW_BLOCK` / `POWDER_SNOW` / `PACKED_ICE`; Black Wolves require `PODZOL` / `MOSS_BLOCK` / `SPRUCE_LEAVES`)* |
| **Felines** | Cats, Ocelots | `WATER`, `SEAGRASS`, `KELP`, `CHEST`, Any Planks |
| **Rabbits** | Rabbits | `CARROTS`, `DANDELION`, `SHORT_GRASS`, `FERN`, `SAND` *(White Rabbits require `SNOW`/`ICE`; Gold Rabbits require `SAND`/`CACTUS`)* |
| **Amphibians & Aquatics** | Turtles, Frogs, Axolotls | `WATER`, `SEAGRASS`, `MUD`, `LILY_PAD`, `FROGSPAWN`, `CLAY`, `DRIPLEAF` *(Warm/White Frogs require `SAND`/`DRIPLEAF`/`MUD`; Cold Frogs require `ICE`/`SNOW`)* |
| **🐻 Polar Bear** | Ice, Packed Ice, Blue Ice, Snow, or Water | 5x3x5 around mob |
| **🐼 Panda** | Bamboo, Bamboo Sapling, or Sugar Cane | 5x3x5 around mob |
| **🐝 Bee** | Flowers (`#minecraft:flowers`), Flowering Azalea, Bee Hive/Nest | 5x3x5 around mob |
| **👾 Strider** | Warped Fungus, Warped Nylium, Lava, or Netherrack | 5x3x5 around mob |
| **🐗 Hoglin** | Crimson Fungus, Crimson Nylium, or Netherrack | 5x3x5 around mob |
| **🛡️ Armadillo** | Red Sand, Terracotta, Dead Bush, or Short Grass | 5x3x5 around mob |
| **🐐 Goat** | Snow, Powder Snow, Stone, Packed Ice, or Wheat | 5x3x5 around mob |
| **👃 Sniffer** | Torchflower, Pitcher Plant, Moss Block, or Podzol | 5x3x5 around mob |
| **🐾 Modded / Custom** | Generic fallback (unrestricted habitat check) | N/A |

---

## 🧬 Livestock Genetics & Scale-Based Item Drop Yield

**Natural Reproduction** integrates with `DasikAnimalGeneticsAPI` to provide data-driven genetics:
- **🧬 Data-Driven Universal Genetics Architecture**:
  - Registered data-driven `GeneticsConfig` via `DasikAnimalGeneticsAPI` across **all 25+ vanilla animal species**.
  - Dynamically modifies the official Minecraft 26.2 entity scale attribute (`minecraft:scale`).

- **Physical Size Scaling**: Animals vary in size from `0.75x` (runt) to `1.30x` (giant).
- **Attribute Modifiers**: Movement speed and max health scale dynamically based on genetic traits.
- **Pedigree & Inheritance**: Offspring inherit size, speed, and health traits from parent animals.
- **🍖 Dynamic Scale-Based Item Drop Yield**:
  - **Giant Animals (`scale > 1.0x`)**: Item drops increase proportionally with body size (e.g., a `1.30x` scale cow yields +30% meat and leather).
  - **Runt Animals (`scale < 1.0x`)**: Item drops decrease proportionally (e.g., a `0.75x` scale chicken yields reduced drop counts).
- **🏚️ Cramped Space Penalty vs. 🌾 Spacious Size Recovery**:
  - **Confined Factory Farming (>= 4 Mobs in 4x4 Area)**: Breeding animals in overcrowded pens causes offspring to suffer gradual size stunting down to `0.25x` scale (severely reducing item drops).
  - **Spacious Pasture Recovery (<= 2 Mobs in 4x4 Area)**: Moving stunted animals to open pastures gradually recovers offspring size genetics (+30% scale boost per generation up to `1.00x` - `1.30x`).
  - Can be toggled via `natural-reproduction:cramped_space_penalty` GameRule or YACL config screen.
- **🏞️ Biome Climate Fertility & Biome Variant Adaptation**:
  - **Native Biome Boost (2x Rate & +15% Quality)**: Breeding animals in their native biome (e.g. Wolves in Taigas, Frogs in Swamps, Camels in Deserts, Polar Bears in Ice Plains) grants 2x faster breeding frequency and +15% genetics quality.
  - **Visual Variant Skin Adaptation**: Offspring born in specific biomes dynamically adapt their visual entity variant skin (e.g. Snowy Wolves, Desert Frogs, White Rabbits) or inherit parent variants.
  - Controlled via `natural-reproduction:biome_fertility` and `natural-reproduction:biome_variants` GameRules.

---

## ⚙️ Configuration & GameRules

Server operators and singleplayer worlds can customize breeding mechanics via native namespaced **GameRules** or the optional **ModMenu / YACL v3** GUI screen:

| GameRule | Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Enables or disables autonomous natural breeding globally. |
| `natural-reproduction:rate` | Integer | `24000` | Average breeding attempt frequency per mob (in ticks). `24000` ticks = 1 Minecraft Day. |
| `natural-reproduction:density_cap` | Integer | `10` | Maximum number of same-species animals permitted in a 16-block radius before breeding stops. |
| `natural-reproduction:scale_drops` | Boolean | `true` | When true, animal item drop yield scales proportionally with physical body scale. |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | When true, breeding in cramped 4x4 pens stunts offspring scale down to 0.25x; breeding in spacious pastures recovers size genetics. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | When true, animals in native biomes get 2x faster breeding frequency and +15% offspring genetics quality. |
| `natural-reproduction:biome_variants` | Boolean | `true` | When true, offspring born in specific biomes adapt their visual entity variant skin (e.g. Snowy Wolves, Desert Frogs). |

---

## 💻 In-Game Commands

Access the complete Brigadier command suite with permission level `LEVEL_GAMEMASTERS` (OP level 2):

- `/naturalreproduction help` - Displays command help and usage guide.
- `/naturalreproduction status` - Displays current GameRule settings and mod status.
- `/naturalreproduction get <gamerule>` - Queries the current value of a specific GameRule.
- `/naturalreproduction set <gamerule> <value>` - Updates a GameRule value in real-time.
- `/naturalreproduction reset` - Resets all Natural Reproduction GameRules to factory defaults.
- `/naturalreproduction reload` - Reloads configuration settings.
