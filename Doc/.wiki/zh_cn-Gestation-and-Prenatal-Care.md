# ⏳ Pregnancy Gestation & Prenatal Pasture Care

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **代码仓库源码状态免责声明**：本 Wiki 文档反映了代码仓库中的**当前源码状态**（`v1.3.4+26.2`）。可能包含领先于 CurseForge 与 Modrinth 公开发布版本的最新未发布提交或开发特性。

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Feature Name** | Autonomous & Manual Gestation Timers |
| **Autonomous Gestation Toggle** | `natural-reproduction:gestation_period` (Default: `true`) |
| **Manual Breeding Gestation** | `natural-reproduction:manual_gestation` (Default: `true`) |
| **Default Gestation Duration** | `natural-reproduction:gestation_duration` (`24000` ticks / 1 MC Day) |
| **Prenatal Health Bonus** | `+15%` Max Health |
| **Prenatal Speed Bonus** | `+10%` Movement Speed |
| **Prenatal Scale Bonus** | `+10%` Baseline Physical Scale |
| **Primary Helper** | `AnimalGestationHelper.java` |

---

## 🎮 Player & Survival Gameplay Workflow

1. **Mating & Conception**: When two animals mate (either autonomously in the wild or via player manual feeding), the female parent enters pregnancy gestation.
2. **Gestation Countdown**: A 24,000-tick timer (1 in-game Minecraft Day) begins ticking down in the mother's persistent data.
3. **Pasture Care During Pregnancy**:
   - If the mother spends her pregnancy in an **Enriched Pasture** with hay bales, water cauldrons, and shelter, she continuously accumulates **Prenatal Vitality Points**.
   - If the mother is starved, trapped in a 1x1 pit, or overcrowded, prenatal vitality points decay.
4. **Delivery**: When the countdown reaches `0`, the mother delivers her offspring. If prenatal points exceed the threshold, the baby is born with shimmering green `HAPPY_VILLAGER` sparkles and receives **Prenatal Vitality**.

---

## 🧮 Mathematical Formulas & Stat Inheritance

### 1. Gestation Countdown Progression
On each server tick ($20\text{ ticks} = 1\text{s}$):

$$T_{\text{remaining}} = T_{\text{remaining}} - 1$$

When $T_{\text{remaining}} \le 0$, `deliverOffspring(mother, fatherLineage)` executes.

### 2. Prenatal Vitality Buff Calculation
When born from a well-nourished mother ($\text{vitalityScore} \ge 80$):

$$\text{MaxHealth}_{\text{offspring}} = \text{BaseHealth} \times 1.15$$

$$\text{MovementSpeed}_{\text{offspring}} = \text{BaseSpeed} \times 1.10$$

$$\text{Scale}_{\text{offspring}} = \min(\text{InheritedScale} \times 1.10, \text{max\_scale})$$

```
[Conception: 24,000 Ticks]
        │
        ▼ (Daylight Grazing)
Enriched Pasture Active? ──Yes──► [+1 Vitality Point / 200 ticks]
        │ No
Cramped Pen / Starved?   ──Yes──► [-1 Vitality Point / 200 ticks]
        │
        ▼ (Delivery at 0 Ticks)
Vitality >= 80 Points?
   ├── Yes ──► [Offspring receives +15% HP, +10% Speed, +10% Scale]
   └── No  ──► [Standard Base Offspring]
```

---

## 💻 Developer & NBT / CustomData Schema

### Persistent Pregnancy Attachment
Gestation data is stored in the entity's persistent custom data:

```json
{
  "NaturalReproduction": {
    "IsPregnant": true,
    "GestationTicks": 14200,
    "PrenatalVitality": 92,
    "FatherUUID": "c044d03e-8302-4212-9214-7d52ef7ca241"
  }
}
```

### Programmatic Gestation API
```java
// Check if an animal is pregnant
boolean pregnant = AnimalGestationHelper.isPregnant(animal);

// Query remaining gestation time
int remainingTicks = AnimalGestationHelper.getRemainingGestation(animal);
```

---

> ☕ **支持模组开发**：如果您喜欢本模组，欢迎前往 [Ko-fi](https://ko-fi.com/rifaditya) 支持作者获取最新开发测试构建！

---

## 🔗 Related Documentation
* [[Autonomous Wild Breeding & Species Habitats|zh_cn-Autonomous-Breeding-and-Habitats]]
* [[Pasture Enrichment & Overgrazing Terrain Wear|zh_cn-Pasture-Enrichment-and-Overgrazing]]
* [[Lineage Inbreeding Degradation & Hybrid Vigor|zh_cn-Lineage-Tracking-and-Inbreeding-Degradation]]
* Return to [[Home Portal|zh_cn-Home]]
