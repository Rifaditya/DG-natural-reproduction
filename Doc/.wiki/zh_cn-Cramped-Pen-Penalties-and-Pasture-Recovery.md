# 🛑 Cramped Pen Stunting & Spacious Pasture Recovery

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **代码仓库源码状态免责声明**：本 Wiki 文档反映了代码仓库中的**当前源码状态**（`v1.3.4+26.2`）。可能包含领先于 CurseForge 与 Modrinth 公开发布版本的最新未发布提交或开发特性。

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

> ☕ **支持模组开发**：如果您喜欢本模组，欢迎前往 [Ko-fi](https://ko-fi.com/rifaditya) 支持作者获取最新开发测试构建！

---

## 🔗 Related Documentation
* [[Physical Scale & Dynamic Harvest Drops|zh_cn-Physical-Scale-and-Harvest-Drops]]
* [[Pasture Enrichment & Overgrazing Terrain Wear|zh_cn-Pasture-Enrichment-and-Overgrazing]]
* [[Lineage Inbreeding Degradation & Hybrid Vigor|zh_cn-Lineage-Tracking-and-Inbreeding-Degradation]]
* Return to [[Home Portal|zh_cn-Home]]
