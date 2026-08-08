# Natural Reproduction: Player Guide & Animal Breeding Reference

Welcome to **Natural Reproduction**, a Delayed Gratification mod that enables wild animals to breed autonomously when their environment, health, and space conditions are ideal.

---

## 🌾 Core Autonomous Breeding Requirements

For an animal to enter **Love Mode** naturally without player feeding, it must satisfy **all** of the following conditions during its AI tick check:

### 1. Health & Maturity
- **Full Health**: The animal must be at **100% max health**. Injured animals will not enter Love Mode.
- **Adult Status**: The animal must be a fully grown adult (`isBaby() == false`). Growing juveniles cannot reproduce.
- **Not Currently Breeding**: The animal must not already be in Love Mode or on breeding cooldown.

### 2. Partner Proximity
- A compatible mate of the **same species** must be located within an **8-block radius**.
- The potential mate must also be an adult, alive, and at full health.

### 3. Population Density Cap (Overcrowding Protection)
- To prevent entity cramming and lag, autonomous breeding pauses if there are already more than **10 animals** of the same species within a **16-block radius** (configurable via `natural-reproduction:density_cap`).

---

## 🐾 Complete 27 Animal Species Breeding Reference

All 27 supported animal species scan a **5x3x5 area** around themselves for specific habitat blocks. Breeding in their **Native Climate Biome** grants **2x faster breeding frequency** and **+15% offspring genetics quality boost**.

| Animal Species | Required Habitat Blocks (within 2 blocks) | Native Climate Biomes (2x Speed & +15% Quality) | Skin Variant Triggers & Notes |
| :--- | :--- | :--- | :--- |
| **🐮 Cow** | Grass Block, Short Grass, Tall Grass, Wheat | Plains, Meadow, Sunflower Plains, Savanna | Standard livestock habitat |
| **🐑 Sheep** | Grass Block, Short Grass, Tall Grass, Wheat | Plains, Meadow, Sunflower Plains, Savanna | Standard livestock habitat |
| **🍄 Mooshroom** | Mycelium, Brown/Red Mushrooms, Mushroom Blocks | Mushroom Fields | Requires fungal mycelium base |
| **🐷 Pig** | Mud, Farmland, Carrots, Potatoes, Beetroots | Plains, Meadow, Sunflower Plains, Savanna | Mud & crop pasture triggers |
| **🐔 Chicken** | Hay Bale, Short/Tall Grass, Wheat | Plains, Meadow, Sunflower Plains, Savanna | Nesting hay & grass triggers |
| **🐴 Horse** | Grass Block, Hay Bale, Dandelion, Sugar Cane | Plains, Savanna | Open grass & hay pastures |
| **🫏 Donkey** | Grass Block, Hay Bale, Dandelion, Sugar Cane | Plains, Savanna | Open grass & hay pastures |
| **🫏 Mule** | Grass Block, Hay Bale, Dandelion, Sugar Cane | Plains, Savanna | Open grass & hay pastures |
| **🦙 Llama** | Hay Bale, Grass Block, Fern | Windswept Hills, Savanna | Mountain fern & grass pastures |
| **🦙 Trader Llama** | Hay Bale, Grass Block, Fern | Windswept Hills, Savanna | Mountain fern & grass pastures |
| **🐫 Camel** | Cactus, Sand, Dead Bush | Desert | Arid desert flora triggers |
| **🐺 Wolf** | *Pale/Woods*: Coarse Dirt, Podzol, Grass, Berry Bushes<br>*Snowy*: Snow Block, Powder Snow, Snow, Packed Ice<br>*Black/Chestnut*: Podzol, Coarse Dirt, Moss, Spruce Leaves<br>*Striped/Spotted/Rusty*: Red Sand, Terracotta, Dead Bush | Taiga, Snowy Taiga, Old Growth Pine/Spruce Taiga | Variant skin adapted triggers; offspring adapt skin to local biome |
| **🦊 Fox** | *Red*: Sweet Berry Bush, Cave Vines, Coarse Dirt, Spruce Leaves<br>*Snow*: Snow layer, Powder Snow, Snow Block, Packed Ice | Taiga, Old Growth Taiga, Snowy Taiga | Variant skin adapted triggers |
| **🐱 Cat** | Water, Seagrass, Kelp, Chest, Wooden Planks | Villages, Jungles | Domestic chest & plank triggers |
| **🐆 Ocelot** | Water, Seagrass, Kelp, Chest, Wooden Planks | Jungle, Sparse Jungle | Tropical water & foliage triggers |
| **🐰 Rabbit** | *Standard*: Carrots, Dandelion, Short Grass, Fern, Sand<br>*White*: Snow layer, Powder Snow, Snow Block, Packed Ice<br>*Gold*: Sand, Red Sand, Dead Bush, Cactus | Desert, Snowy Plains, Flower Forest | Variant skin adapted triggers |
| **🐢 Turtle** | Sand, Seagrass, Water | Beach | Coastal sand & seagrass triggers |
| **🐸 Frog** | *Temperate*: Lily Pad, Frogspawn, Mud, Water, Dripleaf<br>*Warm*: Sand, Big/Small Dripleaf, Mangrove Roots, Mud, Water<br>*Cold*: Ice, Packed Ice, Snow, Powder Snow, Water | Swamp, Mangrove Swamp | Climate variant skin adaptation |
| **🦎 Axolotl** | Clay, Water, Seagrass | Lush Caves | Subterranean clay & aquatic flora |
| **🐻‍❄️ Polar Bear** | Ice, Packed Ice, Blue Ice, Snow layer, Water | Snowy Plains, Ice Spikes, Frozen Ocean | Arctic ice & snow environment |
| **🐼 Panda** | Bamboo, Bamboo Sapling, Sugar Cane | Jungle, Bamboo Jungle | Bamboo grove environment |
| **🐝 Bee** | Flowers (`#minecraft:flowers`), Flowering Azalea, Bee Nest/Hive | Flower Forest, Meadow, Plains | Floral pollination triggers |
| **👾 Strider** | Warped Fungus, Warped Nylium, Lava, Netherrack | Nether (Warped Forest, Lava Oceans) | Nether warped fungus environment |
| **🐗 Hoglin** | Crimson Fungus, Crimson Nylium, Netherrack | Nether (Crimson Forest) | Nether crimson fungus environment |
| **🛡️ Armadillo** | Red Sand, Terracotta, Dead Bush, Short Grass | Savanna, Badlands | Savanna terracotta environment |
| **🐐 Goat** | Snow layer, Powder Snow, Stone, Packed Ice, Wheat | Jagged Peaks, Frozen Peaks, Snowy Slopes | Alpine cliff & snow environment |
| **👃 Sniffer** | Torchflower, Pitcher Plant, Moss Block, Podzol | Warm Ocean, Lush biomes | Ancient flora & moss environment |

---

## 🧬 Pasture Management & Genetics

### 🏚️ Cramped Space Penalty vs. 🌾 Spacious Size Recovery
- **Confined Pens & Overcrowded Pastures**: Breeding in tight enclosed pens/pits OR in overcrowded sections of large pastures (>= 3 local crowding mobs) causes offspring to suffer smooth, gradual size stunting (-5% scale per extra crowding entity down to `0.25x` floor).
- **Spacious Pasture Recovery (<= 2 Mobs in 4x4 Area)**: Moving stunted animals to open pastures boosts offspring scale genetics (+30% recovery per generation) up to **1.30x max scale**.

---

## ⚙️ Configuration & Dynamic GameRules

All settings can be configured via `/gamerule`, `/naturalreproduction set`, or the ModMenu / YACL v3 GUI:

### Core Configuration Rules
| GameRule | Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Enables or disables autonomous natural breeding globally. |
| `natural-reproduction:density_cap` | Integer | `10` | Maximum same-species animals permitted in a 16-block radius. |
| `natural-reproduction:rate` | Integer | `24000` | Average breeding attempt frequency per mob (24000 ticks = 1 MC Day). |
| `natural-reproduction:scale_drops` | Boolean | `true` | Item drop yield scales proportionally with physical body scale attribute. |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | Confined pen stunting vs spacious pasture size recovery. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | 2x breeding frequency & +15% genetics quality in native biomes. |
| `natural-reproduction:biome_variants` | Boolean | `true` | Offspring born in specific biomes adapt visual entity variant skins. |

### 🐾 27 Per-Species Toggles (`Natural Reproduction - Species Toggles`)
Each animal species has a dedicated boolean GameRule (`natural-reproduction:allow_<species>`, default `true`):
`allow_cow`, `allow_pig`, `allow_sheep`, `allow_chicken`, `allow_mooshroom`, `allow_horse`, `allow_donkey`, `allow_mule`, `allow_llama`, `allow_trader_llama`, `allow_camel`, `allow_wolf`, `allow_cat`, `allow_fox`, `allow_ocelot`, `allow_turtle`, `allow_frog`, `allow_axolotl`, `allow_polar_bear`, `allow_panda`, `allow_rabbit`, `allow_goat`, `allow_armadillo`, `allow_sniffer`, `allow_bee`, `allow_strider`, `allow_hoglin`.

---

## 💬 In-Game Commands

- `/naturalreproduction status` - Displays current rule states and number of enabled species toggles.
- `/naturalreproduction get <rule>` - Queries the current value of any rule or species toggle.
- `/naturalreproduction set <rule> <value>` - Updates any rule or species toggle in real-time.
- `/naturalreproduction trackerlogs list|clear|enable|disable` - View and manage autonomous breeding event logs.
- `/naturalreproduction reset` - Resets all rules and 27 species toggles to factory defaults.
