# 🧬 Lineage Inbreeding Degradation & Hybrid Vigor

> **"A closed bloodline deteriorates into dust; outcrossing sparks explosive vigor."**

In vanilla Minecraft, players can continuously breed a single mother cow with her own children and grandchildren indefinitely without penalty. **Natural Reproduction** introduces multi-generational **Lineage Pedigree Tracking**: repeated inbreeding within closed herds causes severe genetic degradation (ultimately resulting in rotten meat and lethal collapse), while introducing wild, unrelated animals unleashes powerful **Hybrid Vigor**.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Feature Name** | Multi-Generational Inbreeding Degradation |
| **Master Toggle** | `natural-reproduction:inbreeding_degradation` (Default: `true`) |
| **Inbreeding Tiers** | `5` Tiers (`T0` Clean, `T1` Mild, `T2` Moderate, `T3` Severe, `T4` Lethal) |
| **T3 Meat Penalty** | Converts Prime Meat &rarr; **Rotten Flesh & Bones** |
| **T4 Collapse Rate** | **1.0 Damage / second** lethal genetic decay |
| **Hybrid Vigor Bonus** | `+15%` Physical Scale, `+20%` Max HP, `+10%` Movement Speed |
| **Primary Helper** | `AnimalLineageHelper.java` |

---

## 🎮 Player & Survival Gameplay Workflow

1. **Lineage Pedigree Storage**: Every animal born stores its direct mother and father `UUID` identifiers.
2. **Generational Breeding Checks**:
   - Mating full siblings, parent-child pairs, or closely related lineages increments the offspring's **Inbreeding Tier**.
3. **Recognizing Genetic Degradation**:
   - **T1 / T2**: Offspring suffer slight health and speed reductions.
   - **T3**: Severe deformities. When harvested, the animal drops **Rotten Flesh and Bones** instead of beef/pork/mutton.
   - **T4**: Lethal genetic collapse. Newborns cannot sustain life and suffer continuous 1 damage/second until death.
4. **Outcrossing for Hybrid Vigor**:
   - To save an inbred herd, capture a **wild animal from a distant biome** and breed it with your herd.
   - The resulting outcross resets the inbreeding counter to **T0** and grants **Hybrid Vigor** (+15% scale, golden sparkles, +25% bonus item drops).

---

## 🧮 Inbreeding Degradation & Stat Matrix

| Tier | Lineage Relationship | Genetic Modifiers | Harvest Drop Results |
| :---: | :--- | :--- | :--- |
| **T0** | Clean Bloodline / Wild | Normal baseline stats ($1.00\text{x}$ scale) | Normal drops |
| **T1** | First Inbred Cross | `-10%` Max HP, `-5%` Movement Speed | Normal drops |
| **T2** | Sibling-Sibling Cross | `-20%` Max HP, `-10%` Speed, Stunted Body | `-25%` Harvest Yield |
| **T3** | Closed Multi-Gen Herd | `-35%` Max HP, Genetic Defects | **Meat converted to Rotten Flesh & Bones** |
| **T4** | Extreme Closed Loop | **Continuous Lethal Decay (1 dmg/sec)** | **100% Rotten Flesh & Bones Only** |
| **HV** | **Hybrid Vigor (Outcross)** | **+15% Scale, +20% Max HP, +10% Speed** | **+25% Bonus Prime Yield** |

```
[Mating Pair Evaluation]
           │
           ▼
Are Parents Related (UUID Ancestry Match)?
   ├── Yes ──► [Increment Inbreeding Tier -> T1, T2, T3, or T4]
   └── No  ──► [Outcross Detected! -> Reset to T0 + Grant Hybrid Vigor (+15% Scale)]
```

---

## 💻 Developer & Lineage Tracking API

### Pedigree Querying
```java
// Check if two animals are closely related
boolean related = AnimalLineageHelper.areCloselyRelated(parentA, parentB);

// Calculate resulting inbreeding tier
int inbreedingTier = AnimalLineageHelper.computeInbreedingTier(parentA, parentB);
```

### CustomData Lineage Structure
```json
{
  "NaturalReproduction": {
    "MotherUUID": "8a31e345-2012-4fa1-8231-54a8cf821102",
    "FatherUUID": "b719c841-8931-419b-b012-9842af910034",
    "InbreedingTier": 0,
    "HybridVigor": true
  }
}
```

---

## 🔗 Related Documentation
* [[Cramped Pen Stunting & Spacious Pasture Recovery|Cramped-Pen-Penalties-and-Pasture-Recovery]]
* [[Physical Scale & Dynamic Harvest Drops|Physical-Scale-and-Harvest-Drops]]
* [[Gestation & Prenatal Care|Gestation-and-Prenatal-Care]]
