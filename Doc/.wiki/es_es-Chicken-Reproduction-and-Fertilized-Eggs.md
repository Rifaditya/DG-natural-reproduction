# 🥚 Dedicated Chicken Reproduction & Fertilized Eggs

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **Descargo de responsabilidad del código fuente del repositorio**: La documentación de esta Wiki refleja el **estado actual del código fuente en el repositorio** (`v1.3.4+26.2`), que puede incluir confirmaciones recientes no publicadas o características de desarrollo por delante de las versiones públicas en CurseForge y Modrinth.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Feature Name** | Dedicated Avian Reproduction System |
| **Fertilized Eggs Toggle** | `natural-reproduction:fertilized_chicken_eggs` (Default: `true`) |
| **Infertile Regular Eggs** | `natural-reproduction:chicken_infertile_regular_eggs` (Default: `true`) |
| **Autonomous Mating Split** | `50%` Immediate Chick / `50%` Fertilized Egg Item |
| **Player Throw Hatch Rate** | `100%` Guaranteed Hatch |
| **Dispenser Hatch Rate** | `natural-reproduction:dispenser_egg_hatch_chance` (Default: `75%`) |
| **Vanilla Regular Egg Hatch** | Reduced to `1/64` ($1.56\%$) Miracle Hatch |
| **Primary Helper** | `ChickenEggHelper.java`, `ThrownEggMixin.java` |

---

## 🎮 Player & Survival Gameplay Workflow

1. **Egg Collection vs. Breeding**:
   - Chickens laying regular eggs every 5–10 minutes produce **Unfertilized Eggs** (safe for baking cakes and pies without accidental chick explosions).
2. **Autonomous Chicken Mating**:
   - When adult chickens mate autonomously in grassy pastures or near seed crops, they roll a 50/50 chance to either hatch an immediate baby chick or drop a **Fertilized Egg** entity item.
3. **Manual Player Incubation**:
   - Right-clicking (throwing) a **Fertilized Egg** provides a **guaranteed 100% hatch rate** for a healthy chick.
4. **Automated Hatcheries (Dispensers)**:
   - Loading Fertilized Eggs into redstone Dispensers fires eggs with a **75% hatch rate**, allowing high-throughput automated farm designs.

---

## 🧮 Mathematical Probabilities & Dispersion Matrix

### 1. Autonomous Mating Distribution
When two chickens complete a breeding cycle:

$$R = \text{random.nextFloat}()$$

$$\text{Outcome} = \begin{cases}
\text{Instant Baby Chick} & \text{if } R < 0.50 \\
\text{Fertilized Egg Item Drop} & \text{if } R \ge 0.50
\end{cases}$$

### 2. Egg Hatch Comparison Table

| Egg Type | Delivery Method | Hatch Chance | Offspring Quality |
| :--- | :--- | :---: | :--- |
| **Fertilized Egg** | Player Throw | **100% (1/1)** | Inherits parent scale & genetics |
| **Fertilized Egg** | Dispenser Ejection | **75% (3/4)** | Inherits parent scale & genetics |
| **Regular Egg** | Player Throw / Dispenser | **1.56% (1/64)** | Random baseline vanilla stats |

```
              [Chicken Breeding Cycle]
                         │
           ┌─────────────┴─────────────┐
           ▼ (50% Chance)              ▼ (50% Chance)
  [Immediate Baby Chick]       [Fertilized Egg Item]
                                       │
                         ┌─────────────┴─────────────┐
                         ▼ Player Throw              ▼ Dispenser Ejection
                       100% Hatch                  75% Hatch Rate
```

---

## 💻 Developer & Mixin Hooks

### Mixin Interception on Egg Impact
`ThrownEggMixin` intercepts `onHitEntity` and `onHitBlock`:

```java
// Intercept thrown egg impact to check for Fertilized Egg CustomData
@Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
private void naturalreproduction$onEggImpact(HitResult hitResult, CallbackInfo ci) {
    if (ChickenEggHelper.isFertilizedEgg(this)) {
        ChickenEggHelper.handleFertilizedEggImpact(this, this.level(), hitResult);
        ci.cancel();
    }
}
```

---

> ☕ **Apoya el desarrollo**: Si disfrutas de este mod, considera apoyar al autor en [Ko-fi](https://ko-fi.com/rifaditya) para acceder a versiones anticipadas.

---

## 🔗 Related Documentation
* [[Autonomous Wild Breeding & Species Habitats|es_es-Autonomous-Breeding-and-Habitats]]
* [[Cramped Pen Stunting & Spacious Pasture Recovery|es_es-Cramped-Pen-Penalties-and-Pasture-Recovery]]
* [[Namespaced GameRules & Configuration|es_es-GameRules-and-Configuration]]
* Return to [[Home Portal|es_es-Home]]
