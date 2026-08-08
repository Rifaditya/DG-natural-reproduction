# Livestock Genetics & Physical Size Scaling

| Attribute | Value |
| :--- | :--- |
| **Entity Scale Attribute** | `minecraft:scale` |
| **Normal Scale Range** | $0.75x \le \text{scale} \le 1.30x$ |
| **Absolute Minimum Scale Floor** | $0.25x$ |
| **Item Drop Scaling** | Proportional to scale multiplier ($\text{yield} \propto \text{scale}$) |
| **Spacious Pasture Recovery Boost** | $+15\%\text{ per generation}$ (up to $1.30x$) |

---

## 🧬 Physical Size Attribute (`minecraft:scale`)

Natural Reproduction registers 3 data-driven genetic traits across all 27 animal species using `DasikAnimalGeneticsAPI`:

| Trait Key | Target Entity Attribute | Attribute Operation | Base Range | Dynamic Bounds |
| :--- | :--- | :--- | :---: | :--- |
| **`scale`** | `minecraft:scale` | `ADD_VALUE` | $0.50x \le \text{scale} \le 1.30x$ | Configured live via `MIN_SCALE` ($50\%$) & `MAX_SCALE` ($130\%$) GameRules. |
| **`max_health`** | `minecraft:generic.max_health` | `ADD_VALUE` | $+2.0\text{ HP Base}$ | Bounds: $-4.0\text{ HP}$ to $+12.0\text{ HP}$ bonus max health. |
| **`movement_speed`** | `minecraft:generic.movement_speed` | `ADD_MULTIPLIED_BASE` | $+5\%\text{ Speed Base}$ | Bounds: $-4\%$ to $+8\%$ bonus movement speed. |

### Physical Scale Inheritance Math
$$\text{baseScale} = \frac{\text{parent}_1.\text{scale} + \text{parent}_2.\text{scale}}{2.0}$$
- In native climate biomes, offspring receive a $+15\%$ genetics quality boost:
  $$\text{boostedScale} = \text{baseScale} \times 1.15$$

---

## 🍖 Dynamic Scale-Based Item Drop Yields

When animals are slain or harvested:
$$\text{Drop Yield} = \lfloor \text{Vanilla Drop Count} \times \text{scale} \rfloor$$
- **Giant Mobs ($\text{scale} = 1.30x$)**: Yield $+30\%$ bonus meat, leather, and wool.
- **Stunted Runts ($\text{scale} = 0.25x$)**: Yield minimal drop counts.

---

## 🏚️ Cramped Space Penalty vs. 🌾 Spacious Size Recovery

The crowding evaluation runs both for **structural confinement** (pits, 2x2 pens) AND **large open pastures (e.g. 100x100)** whenever local mob crowding is high:

$$\text{isOvercrowded} = \text{isConfinedPen} \lor (\text{extraLocalCount} \ge 3)$$

### Continuous Density Penalty Formula
When $\text{isOvercrowded}$ is true, the penalty multiplier is calculated continuously:
$$\text{penaltyMultiplier} = \max(0.95 - (\text{extraLocalCount} \times 0.05), 0.40)$$

$$\text{newScale} = \text{clamp}(\text{currentScale} \times \text{penaltyMultiplier}, 0.25, 2.0)$$

| Crowding Density | Penalty Multiplier | Generation Scale Reduction |
| :--- | :---: | :--- |
| **1 Extra Local Mob** | `0.90` | $-10\%$ scale reduction |
| **2 Extra Local Mobs** | `0.85` | $-15\%$ scale reduction |
| **3 Extra Local Mobs** | `0.80` | $-20\%$ scale reduction |
| **4 Extra Local Mobs** | `0.75` | $-25\%$ scale reduction |
| **5 Extra Local Mobs** | `0.70` | $-30\%$ scale reduction |
| **8 Extra Local Mobs** | `0.55` | $-45\%$ scale reduction |
| **10+ Extra Local Mobs** | `0.40` | $-60\%$ scale reduction (Floor) |

### 🌾 Spacious Pasture Recovery
When animals have space to roam ($\text{isOvercrowded} == \text{false}$ and $\text{extraLocalCount} < 3$):
$$\text{recoveryScale} = \text{clamp}(\text{currentScale} \times 1.15, 0.25, 1.30)$$

For GameRule toggles, see [[GameRules|GameRules]].
