# 📏 Physical Scale & Dynamic Harvest Drops

> **"A champion ox feeds a village; a stunted runt barely fills a stew."**

In vanilla Minecraft, an animal's drop yields are static — whether an entity is tiny or gigantic, it always drops 1–3 raw beef. **Natural Reproduction** links physical visual scale (`minecraft:scale`) directly to agricultural harvest yields, rewarding careful selective breeding with multiplied meat, leather, and wool drops.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Feature Name** | Physical Scale & Drop Multiplier System |
| **Master Drop Toggle** | `natural-reproduction:scale_drops` (Default: `true`) |
| **Minimum Scale Limit** | `natural-reproduction:min_scale` (Default: `50` $\implies 0.50\text{x}$) |
| **Maximum Scale Limit** | `natural-reproduction:max_scale` (Default: `130` $\implies 1.30\text{x}$) |
| **Affected Resources** | Raw Beef, Porkchops, Mutton, Chicken, Leather, Wool, Feathers, Bones |
| **Mixin Injection Point** | `AnimalDropScaleMixin.java` (`LivingEntity#dropFromLootTable`) |
| **Primary Helper** | `AnimalDropHelper.java` |

---

## 🎮 Player & Survival Workflow

1. **Visualizing Livestock Size**:
   - Animals naturally vary in physical height and width based on genetics, parentage, and pasture health.
   - Large prize animals stand visibly taller than vanilla livestock.
2. **Selective Breeding**:
   - Pairing the largest specimens over several generations elevates herd baseline genetics toward the **$1.30\text{x}$ maximum cap**.
3. **Harvesting Rewards**:
   - Harvesting a $1.30\text{x}$ heavyweight cow yields up to **$130\%$ to $160\%$ meat and leather** compared to vanilla.
   - Harvesting a $0.25\text{x}$ confined runt yields severely diminished drops (often single meat items).

---

## 🧮 Mathematical Formulas & Drop Yield Curve

### 1. Drop Scaling Multiplier
When an animal entity drops loot items:

$$\text{ScaleFactor} = \frac{\text{getPhysicalScale}(entity)}{1.00}$$

$$\text{FinalDropCount} = \max\Big(1,\, \text{round}(\text{BaseLootCount} \times \text{ScaleFactor})\Big)$$

### 2. Physical Scale & Harvest Comparison Matrix

| Scale Attribute | Physical Descriptor | Meat Drop Yield | Leather / Wool Yield | Visual Footprint |
| :---: | :--- | :---: | :---: | :--- |
| **$0.25\text{x}$** | Extreme Confinement Runt | $1\text{x}$ (Minimum) | $0 - 1\text{x}$ | Tiny miniature runt |
| **$0.50\text{x}$** | Subordinate / Inbred | $1 - 2\text{x}$ | $1\text{x}$ | Half vanilla size |
| **$1.00\text{x}$** | Standard Baseline | $1 - 3\text{x}$ | $0 - 2\text{x}$ | Standard Minecraft size |
| **$1.15\text{x}$** | Well-Nourished Pasture | $2 - 4\text{x}$ | $1 - 3\text{x}$ | Large, healthy livestock |
| **$1.30\text{x}$** | **Alpha Champion / Hybrid Vigor** | **$3 - 6\text{x}$** | **$2 - 4\text{x}$** | **Massive prize specimen** |

```
[Entity Death / Loot Drop Trigger]
                │
                ▼
Is scale_drops Enabled? ──No──► [Vanilla Unscaled Loot]
                │ Yes
Get Entity Scale Attribute (S)
                │
                ▼
Multiply Each Item Drop: Count = round(BaseCount * S)
                │
                ▼
Spawn Scaled Item Drops in World!
```

---

## 💻 Developer & Mixin Implementation

`AnimalDropScaleMixin` dynamically intercepts entity loot generation:

```java
@Inject(method = "dropCustomDeathLoot", at = @At("HEAD"))
private void naturalreproduction$scaleLootDrops(ServerLevel level, DamageSource damageSource, boolean recentlyHit, CallbackInfo ci) {
    if (level.getGameRules().getBoolean(NaturalReproductionFabric.RULE_SCALE_DROPS)) {
        AnimalDropHelper.applyScaleToLoot(this, level);
    }
}
```

---

## 🔗 Related Documentation
* [[Cramped Pen Stunting & Spacious Pasture Recovery|Cramped-Pen-Penalties-and-Pasture-Recovery]]
* [[Lineage Inbreeding Degradation & Hybrid Vigor|Lineage-Tracking-and-Inbreeding-Degradation]]
* [[GameRules & Configuration|GameRules-and-Configuration]]
