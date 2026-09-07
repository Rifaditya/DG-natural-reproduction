# 🧬 Lineage Inbreeding Degradation & Hybrid Vigor

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **저장소 소스 코드 상태 고지**: 본 위키 문서는 저장소의 **현재 소스 코드 상태**(`v1.3.4+26.2`)를 반영하고 있습니다. CurseForge 및 Modrinth의 공개 릴리스 빌드보다 앞선 최신 미출시 커밋이나 개발 기능이 포함될 수 있습니다.

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

> ☕ **개발 후원**: 모드가 마음에 드셨다면, [Ko-fi](https://ko-fi.com/rifaditya)를 통해 개발자를 후원하고 최신 초기 테스트 빌드를 체험해 보세요!

---

## 🔗 Related Documentation
* [[Cramped Pen Stunting & Spacious Pasture Recovery|ko_kr-Cramped-Pen-Penalties-and-Pasture-Recovery]]
* [[Physical Scale & Dynamic Harvest Drops|ko_kr-Physical-Scale-and-Harvest-Drops]]
* [[Gestation & Prenatal Care|ko_kr-Gestation-and-Prenatal-Care]]
* Return to [[Home Portal|ko_kr-Home]]
