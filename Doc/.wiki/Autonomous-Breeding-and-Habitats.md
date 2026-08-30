# 🌿 Autonomous Wild Breeding & Species Habitats

> **"A thriving ecosystem does not rely on hand-fed wheat."**

In vanilla Minecraft, passive animals are inert entities that only breed when directly interacted with by a player. **Natural Reproduction** introduces an autonomous ecological simulation where animals in the wild graze, evaluate their surrounding biome and food flora, and reproduce when environmental conditions are optimal.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Namespace** | `natural-reproduction` |
| **Master Toggle** | `natural-reproduction:enabled` (Default: `true`) |
| **Base Breeding Rate** | `natural-reproduction:rate` (Default: `24000` ticks / 1 MC Day) |
| **Density Check Radius** | `16` blocks horizontal |
| **Population Density Cap** | `natural-reproduction:density_cap` (Default: `10` entities) |
| **Staggered Evaluation Cycle** | `(entityId + gameTime) % 100 == 0` |
| **Supported Animal Species** | **27 vanilla species** (All passive livestock, wild beasts, amphibians) |
| **Primary Helper Class** | `AnimalHabitatHelper.java`, `SpatialBreedingCacheHelper.java` |

---

## 🎮 Player & Survival Gameplay Workflow

1. **Environmental Flora Alignment**: Animals will only enter Love Mode autonomously if suitable food or habitat blocks exist within a **4-block radius**.
2. **Health & Age Check**: Both potential parents must be fully grown adults (`age == 0`) and possess healthy vitality (`health > maxHealth * 0.5`).
3. **Density Cap Safety**: If the number of same-species animals within **16 blocks** reaches or exceeds `density_cap` (default: 10), wild breeding is suppressed to prevent runaway server lag.
4. **Autonomous Love State**: When all checks pass, both parents enter Love Mode concurrently with heart particle feedback (`HEART`), seeking each other out to mate naturally.

---

## 🧮 Mathematical Formalization & Algorithmic Flow

### 1. Staggered Cycle Distribution
To eliminate tick spikes, autonomous checks do NOT run on every tick. Instead, evaluations are distributed across 100-tick time slices:

$$\text{isEvaluationTick} \iff (\text{entityId} + \text{level.getGameTime()}) \pmod{100} = 0$$

### 2. Autonomous Breeding Probability
On every evaluation tick ($5\text{ seconds}$), the probability $P_{\text{breed}}$ of an individual animal attempting reproduction is governed by:

$$P_{\text{breed}} = \frac{100}{\text{rate}} \times \text{biomeMultiplier} \times \text{nourishmentMultiplier}$$

* Base rate $= 24,000\text{ ticks} \implies P_{\text{breed}} \approx 0.416\%$ per 100 ticks.
* Native Biome bonus $= 2.0\text{x} \implies P_{\text{breed}} \approx 0.833\%$.
* Well-Nourished bonus $= 1.25\text{x} \implies P_{\text{breed}} \approx 1.042\%$.

```
[Server Tick Loop]
       │
       ▼
(id + time) % 100 == 0? ──No──► [Fast-Fail Return]
       │ Yes
       ▼
Is Master Enabled & Species Allowed? ──No──► [Abort]
       │ Yes
Local Same-Species Density < density_cap (16 blocks)? ──No──► [Abort]
       │ Yes
Nearby Food/Habitat Blocks Present (4 blocks)? ──No──► [Abort]
       │ Yes
Roll Breeding Probability (P_breed)? ──Fail──► [Abort]
       │ Pass
Partner in Range? ──Yes──► [Both Enter Love Mode + Emit Hearts]
```

---

## 🐾 Species Habitat Block Trigger Matrix (27 Species)

| Species | Primary Habitat Blocks | Special Environmental Triggers |
| :--- | :--- | :--- |
| **Cow & Mooshroom** | `GRASS_BLOCK`, `SHORT_GRASS`, `TALL_GRASS`, `HAY_BLOCK` | Mooshrooms also accept `MYCELIUM` |
| **Sheep** | `GRASS_BLOCK`, `SHORT_GRASS`, `TALL_GRASS`, `HAY_BLOCK` | Prefers un-sheared wool state |
| **Pig** | `MUD`, `FARMLAND`, `CARROTS`, `POTATOES`, `BEETROOTS` | Mud blocks grant +10% love speed |
| **Chicken** | `WHEAT_SEEDS`, `HAY_BLOCK`, `SHORT_GRASS`, `FARMLAND` | Rolls for Fertilized Eggs |
| **Horse, Donkey, Mule** | `GRASS_BLOCK`, `SHORT_GRASS`, `HAY_BLOCK` | Requires open sky clearance |
| **Llama & Trader Llama** | `GRASS_BLOCK`, `HAY_BLOCK`, `SAND`, `GRAVEL` | Mountain biomes grant native boost |
| **Camel** | `SAND`, `RED_SAND`, `CACTUS`, `DEAD_BUSH` | Desert climate native |
| **Wolf** | `PODZOL`, `SPRUCE_LEAVES`, `SNOW_BLOCK`, `COARSE_DIRT` | Variant-specific biome checks |
| **Cat & Ocelot** | `GRASS_BLOCK`, `JUNGLE_LEAVES`, `MOSS_BLOCK` | Village or Jungle proximity |
| **Fox** | `SWEET_BERRY_BUSH`, `PODZOL`, `SNOW_BLOCK` | Taiga / Snowy Taiga flora |
| **Rabbit** | `CARROTS`, `DANDELION`, `SAND`, `SNOW` | Desert & Tundra adapted |
| **Frog** | `WATER`, `LILY_PAD`, `MUD`, `MANGROVE_ROOTS`, `DRIPLEAF` | Temperature controls variant offspring |
| **Turtle** | `SAND`, `WATER`, `SEAGRASS` | Beach biome nesting |
| **Axolotl** | `WATER`, `CLAY`, `DRIPLEAF` | Lush Caves / dark water |
| **Goat** | `STONE`, `SNOW_BLOCK`, `PACKED_ICE`, `POWDER_SNOW` | High altitude ($Y \ge 90$) |
| **Panda** | `BAMBOO`, `BAMBOO_SAPLING` | Bamboo Jungle native |
| **Polar Bear** | `ICE`, `PACKED_ICE`, `BLUE_ICE`, `SNOW_BLOCK` | Frozen Oceans & Snowy Plains |
| **Bee** | `BEE_NEST`, `BEEHIVE`, `FLOWERS` | Requires active pollination |
| **Armadillo** | `SAND`, `RED_SAND`, `TERRACOTTA`, `DEAD_BUSH` | Savanna & Badlands flora |
| **Sniffer** | `MOSS_BLOCK`, `TORCHFLOWER`, `PITCHER_PLANT` | Ancient flora enrichment |
| **Strider** | `LAVA`, `WARPED_NYLIUM`, `CRIMSON_NYLIUM` | Nether lava seas |
| **Hoglin** | `CRIMSON_NYLIUM`, `CRIMSON_FUNGUS` | Crimson Forest native |

---

## 💻 Developer & Addon Extension Hooks

### Checking Environmental Conditions Programmatically
```java
// Check if an entity has valid habitat conditions
boolean canBreed = AnimalHabitatHelper.hasEnvironmentalBreedingConditions(animal, level);
```

### Spatial Cache Optimization
```java
// SpatialBreedingCacheHelper caches chunk density counts for 100 ticks
int localCount = SpatialBreedingCacheHelper.getDensity(level, animal.blockPosition(), animal.getType());
```

---

## 🔗 Related Documentation
* [[Herd Dynamics, Alpha Leadership & Panic Stampedes|Herd-Dynamics-and-Alpha-Leadership]]
* [[Pregnancy Gestation & Prenatal Pasture Care|Gestation-and-Prenatal-Care]]
* [[Namespaced GameRules & Configuration|GameRules-and-Configuration]]
