# Natural Reproduction - Autonomous Wild Animal Breeding

**Delayed Gratification Collection** | *Pasture Management & Multi-Generational Investment*

Animals in the wild should not depend solely on players for survival and growth. **Natural Reproduction** allows all passive mobs in Minecraft to autonomously breed and multiply when environmental conditions are ideal, rewarding thoughtful pasture management and genetics investment.

## Key Features

- **Universal 25+ Animal Species Support**: Autonomous wild breeding and environmental habitat block triggers across cattle, equines, canines, felines, amphibians, aquatic mobs, nether fauna, and wild animals.
- **Scale-Based Item Drop Yield**: Larger animals (up to 1.30x scale) yield bonus item drops on death/harvest, while smaller animals yield reduced drops.
- **Cramped Space Penalty vs. Spacious Pasture Recovery**: Breeding in cramped 4x4 factory farm pens stunts offspring scale down to 0.25x (yielding minimal drops). Moving animals to open pastures (+30% recovery per generation) restores their genetic size potential!
- **Data-Driven Genetics Integration**: Powered by `DasikAnimalGeneticsAPI` and `EntityGeneticsRegistry` modifying entity `minecraft:scale`.
- **In-Game Command Suite**: Complete `/naturalreproduction` Brigadier command suite with `help`, `status`, `get`, `set`, `reset`, and `reload` supporting all 5 GameRules.
- **GameRules & Optional Config Screen**: Dynamic namespaced GameRules (`natural-reproduction:enabled`, `rate`, `density_cap`, `scale_drops`, `cramped_space_penalty`) with ModMenu & YACL v3 support.

## Installation & Requirements

- **Fabric Loader** >= 0.19.1
- **Minecraft** 26.2+
- **Fabric API**
- **DasikLibrary** >= 1.8.0
