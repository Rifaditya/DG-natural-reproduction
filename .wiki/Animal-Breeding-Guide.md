# Autonomous Animal Breeding Guide

In **Natural Reproduction**, animals do not require wheat or carrots from players to reproduce. When wild animals have full health and ideal environmental surroundings, they enter **Love Mode** naturally.

For full details on size genetics, see [[Genetics & Size Scaling|Genetics-and-Size-Scaling]]. For configuration rules, see [[GameRules & Commands|GameRules-and-Commands]].

---

## 🌾 Core Breeding Check Rules

During an animal's autonomous AI check cycle (configurable via `natural-reproduction:rate`), it verifies:

1. **Full Health & Maturity**: The animal must be at **100% max health** and must be a fully grown adult (`isBaby() == false`).
2. **Partner Proximity**: A compatible mate of the **same species** must be within an **8-block radius**.
3. **Density Cap**: The surrounding area (16-block radius) must not exceed **10 animals of the same species** (`natural-reproduction:density_cap`).

---

## 🐾 Complete 27 Animal Species Reference Table

Animals scan a **5x3x5 area** (`offset(-2, -1, -2)` to `offset(2, 1, 2)`) around themselves for required habitat blocks. Breeding in their **Native Climate Biome** grants **2x faster breeding speed** and **+15% genetics quality boost**.

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
