# 🏡 Pasture Enrichment & Overgrazing Terrain Wear

> **"Invest in the land, and the land pays dividends in fertile livestock."**

In vanilla Minecraft, grass never wears out regardless of herd size, and placing hay bales or water troughs is purely decorative. **Natural Reproduction** creates an interactive agricultural cycle: farmers who invest in **Pasture Enrichment** (water cauldrons, composters, hay bales, and roof shelters) gain the powerful **Well-Nourished** state, while overstocking leads to **Overgrazing** terrain wear.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Feature Name** | Pasture Enrichment & Terrain Wear |
| **Enrichment Toggle** | `natural-reproduction:pasture_enrichment` (Default: `true`) |
| **Overgrazing Toggle** | `natural-reproduction:overgrazing` (Default: `true`) |
| **Well-Nourished Speed Buff** | `+25%` Faster Breeding Evaluation Frequency |
| **Well-Nourished Scale Buff** | `+10%` Offspring Scale Bonus |
| **Visual Particles** | Golden `WAX_ON` Sparkles |
| **Terrain Wear Steps** | `GRASS_BLOCK` &rarr; `DIRT` &rarr; `COARSE_DIRT` |
| **Primary Helper** | `AnimalPastureHelper.java` |

---

## 🎮 Player & Survival Workflow

1. **Building an Enriched Pasture**:
   - Establish a fenced perimeter around an $8\times 8$ or larger grass pasture.
   - Install **Pasture Enrichment Structures**:
     - **Water Cauldrons**: Hydration source for grazing mobs.
     - **Hay Bales**: Supplemental foraging supply.
     - **Composters**: Soil enrichment and pasture health.
     - **Barn Roof Shelters**: Overhead solid block coverage protecting from rain/sun.
2. **The Well-Nourished State**:
   - Animals living in pastures containing at least **3 enrichment structures** enter the **Well-Nourished** state.
   - Shimmering golden `WAX_ON` sparkles appear around animals.
   - Breeding evaluations occur 25% faster, and offspring receive +10% bonus physical scale!
3. **Managing Overgrazing**:
   - If more than 5 animals graze in a concentrated area for extended periods, grass blocks slowly convert into plain **Dirt**.
   - If overgrazing continues ($\ge 8$ entities), dirt degrades into **Coarse Dirt**, preventing grass spread until rotated.

---

## 🧮 Enrichment Scoring & Terrain Wear Formulas

### 1. Pasture Enrichment Score Calculation
Every 200 ticks, animals evaluate a 6-block radius:

$$\text{Score} = (\text{HayBales} \ge 1 ? 1 : 0) + (\text{WaterCauldron} \ge 1 ? 1 : 0) + (\text{Composter} \ge 1 ? 1 : 0) + (\text{RoofShelter} ? 1 : 0)$$

$$\text{isWellNourished} \iff \text{Score} \ge 3$$

### 2. Overgrazing Degradation Logic
On animal grazing tick:

$$\text{WearChance} = \begin{cases}
0\% & \text{if } N_{\text{local}} < 5 \\
10\% & \text{if } 5 \le N_{\text{local}} < 8 \implies \text{Grass Block} \to \text{Dirt} \\
25\% & \text{if } N_{\text{local}} \ge 8 \implies \text{Dirt} \to \text{Coarse Dirt}
\end{cases}$$

```
[Pasture Environment Scan (6-Block Radius)]
                     │
         ┌───────────┴───────────┐
         ▼                       ▼
[Count Enrichment Blocks]  [Count Local Herd Density]
 Hay, Water, Composter,      Density >= 5: Grass -> Dirt
 Barn Roof Shelter           Density >= 8: Dirt -> Coarse Dirt
         │
         ▼
Score >= 3? ──Yes──► [Well-Nourished (+25% Speed, +10% Scale, Golden Sparkles)]
```

---

## 💻 Developer & Pasture API

```java
// Query enrichment score for a location
int score = AnimalPastureHelper.calculatePastureScore(level, entity.blockPosition());

// Check if an entity qualifies as well-nourished
boolean wellNourished = AnimalPastureHelper.isWellNourished(entity, level);
```

---

## 🔗 Related Documentation
* [[Autonomous Wild Breeding & Species Habitats|Autonomous-Breeding-and-Habitats]]
* [[Gestation & Prenatal Care|Gestation-and-Prenatal-Care]]
* [[Cramped Pen Stunting & Spacious Pasture Recovery|Cramped-Pen-Penalties-and-Pasture-Recovery]]
