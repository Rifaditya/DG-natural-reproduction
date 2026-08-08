# Livestock Genetics & Physical Size Scaling

**Natural Reproduction** integrates seamlessly with `DasikAnimalGeneticsAPI` to provide dynamic, multi-generational size genetics across all passive animal species in Minecraft 26.2.

---

## 🧬 Physical Size Attribute (`minecraft:scale`)

- **Natural Genetic Variation**: Animals naturally vary in physical body size from **0.75x** (runt) to **1.30x** (giant).
- **Inheritance & Pedigree**: Offspring inherit size traits from their parents, modified by environmental conditions and native biome quality bonuses (+15% genetics quality in native biomes).
- **Dynamic Scale-Based Item Drops**: When harvested or slain, animals yield item drop counts proportional to their physical body size:
  - **Giant Animals (`scale > 1.0x`)**: Yield bonus meat, leather, wool, and drops.
  - **Runt Animals (`scale < 1.0x`)**: Yield reduced drop counts.

---

## 🏚️ Cramped Space Penalty vs. 🌾 Spacious Size Recovery

To discourage cramped 1x1/2x2 factory farming pits, Natural Reproduction evaluates pasture density:

| Pasture Condition | Herd Density | Effect on Offspring Size | Item Drop Impact |
| :--- | :--- | :--- | :--- |
| **Confined Factory Pen** | Confined 1x1/2x2 pit or pen | **Smooth gradual stunting (-5% scale per extra crowding mob)** down to `0.25x` | Reduced drop yield |
| **Spacious Open Pasture** | `<= 2 mobs` in a 4x4 area | **Recovers size genetics (+30% per generation)** up to `1.30x` scale | Bonus item drop yield |

> **Note**: Confined stunting can be toggled on or off using the `natural-reproduction:cramped_space_penalty` GameRule or ModMenu / YACL config GUI.

For a full list of configuration rules, see [[GameRules & Commands|GameRules-and-Commands]].
