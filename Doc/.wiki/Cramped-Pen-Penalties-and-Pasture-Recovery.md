# 🛑 Cramped Pen Stunting & Spacious Pasture Recovery

> **"Crowded factory pens produce stunted runts; open green pastures nurture champions."**

In vanilla Minecraft, cramming dozens of cows into a 1x1 stone hole with a water source is an optimal farming meta. **Natural Reproduction** balances animal husbandry with authentic spatial mechanics: breeding in high-density or confined pens causes offspring to suffer severe genetic stunting, while breeding in open, spacious green pastures dynamically restores size and vitality.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Feature Name** | Confinement Stunting & Pasture Recovery |
| **Master Toggle** | `natural-reproduction:cramped_space_penalty` (Default: `true`) |
| **Minimum Stunted Scale** | `0.25x` physical scale (Runts) |
| **Maximum Pasture Scale** | Up to `1.30x` physical scale (Heavyweights) |
| **Crowding Penalty Step** | `-5%` scale per extra entity beyond local threshold |
| **Spacious Recovery Boost** | `+10%` to `+30%` genetic size recovery per generation |
| **Particle Feedback** | `ANGRY_VILLAGER` & `SMOKE` (Stunted) / `HAPPY_VILLAGER` (Recovered) |
| **Primary Helper** | `AnimalCrampedSpaceHelper.java` |

---

## 🎮 Player & Survival Gameplay Workflow

1. **Factory Farm Penalty**:
   - If animals breed inside a **1x1 or 2x2 pit hole**, or inside a fence pen with **too many crowding entities** ($N_{\text{local}} \ge 4$), the newborn is declared a **Confined Runt**.
   - The baby spawns with angry villager particles and receives a severe scale reduction (down to 0.25x).
2. **Reduced Harvest Yields**:
   - Stunted animals drop drastically fewer resources upon maturity ($0.25\text{x}$ meat and leather).
3. **Pasture Rehabilitation**:
   - Transfer stunted animals into an **open, spacious pasture** ($\ge 8\times 8$ grass blocks with $<3$ crowding neighbors).
   - When stunted parents breed in open pastures, their offspring receive **Spacious Pasture Recovery** (+30% size boost), gradually rehabilitating the lineage across generations back to normal (1.00x) and heavyweight (1.30x) sizes!

---

## 🧮 Mathematical Formulas & Degradation Curves

### 1. Confinement Penalty Curve
When an entity breeds in a confined or crowded area:

$$N_{\text{extra}} = \max(0, N_{\text{local}} - N_{\text{threshold}})$$

$$\text{PenaltyMultiplier} = \max\Big(0.95 - (N_{\text{extra}} \times 0.05),\, 0.25\Big)$$

$$\text{Scale}_{\text{newborn}} = \text{ParentScale} \times \text{PenaltyMultiplier}$$

* 1 extra crowding mob $\implies -5\%$ scale penalty ($0.95\text{x}$).
* 5 extra crowding mobs $\implies -25\%$ scale penalty ($0.75\text{x}$).
* Extreme 1x1 pit farming $\implies$ Hard floor clamp at **$0.25\text{x}$ scale**.

### 2. Spacious Pasture Recovery Formula
When breeding in an open pasture ($N_{\text{extra}} = 0$ and open sky clearance):

$$\text{RecoveryMultiplier} = 1.0 + \min(0.30,\, 0.10 \times \text{PastureEnrichmentScore})$$

$$\text{Scale}_{\text{newborn}} = \min\Big(\text{ParentScale} \times \text{RecoveryMultiplier},\, 1.30\Big)$$

```
     [Breeding Condition Evaluation]
                    │
      ┌─────────────┴─────────────┐
      ▼ Confined / Crowded        ▼ Open Spacious Pasture
 [Cramped Penalty: -5%/entity]  [Pasture Recovery: +10% to +30%]
      │                           │
      ▼                           ▼
 [Runt Scale: 0.25x - 0.75x]    [Full Potential: 1.00x - 1.30x]
```

---

## 💻 Developer & Helper API

### Calculating Scale Modifiers
```java
// Computes newborn scale based on parents and local confinement
float finalScale = AnimalCrampedSpaceHelper.calculateOffspringScale(
    mother, father, level, mother.blockPosition()
);
```

---

## 🔗 Related Documentation
* [[Physical Scale & Dynamic Harvest Drops|Physical-Scale-and-Harvest-Drops]]
* [[Pasture Enrichment & Overgrazing Terrain Wear|Pasture-Enrichment-and-Overgrazing]]
* [[Lineage Inbreeding Degradation & Hybrid Vigor|Lineage-Tracking-and-Inbreeding-Degradation]]
