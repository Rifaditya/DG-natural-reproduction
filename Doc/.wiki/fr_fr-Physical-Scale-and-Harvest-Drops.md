# 📏 Physical Scale & Dynamic Harvest Drops

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **Avis de non-responsabilité relatif au code source du dépôt** : La documentation de ce Wiki reflète **l'état actuel du code source dans le dépôt** (`v1.3.4+26.2`), qui peut inclure des commits récents non publiés ou des fonctionnalités de développement en avance sur les versions publiques de CurseForge et Modrinth.

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

> ☕ **Soutenir le développement** : Si vous appréciez ce mod, soutenez l'auteur sur [Ko-fi](https://ko-fi.com/rifaditya) pour des versions de test préliminaires !

---

## 🔗 Related Documentation
* [[Cramped Pen Stunting & Spacious Pasture Recovery|fr_fr-Cramped-Pen-Penalties-and-Pasture-Recovery]]
* [[Lineage Inbreeding Degradation & Hybrid Vigor|fr_fr-Lineage-Tracking-and-Inbreeding-Degradation]]
* [[Namespaced GameRules & Configuration|fr_fr-GameRules-and-Configuration]]
* Return to [[Home Portal|fr_fr-Home]]
