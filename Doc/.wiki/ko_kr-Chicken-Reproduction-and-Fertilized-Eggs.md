# 🥚 Dedicated Chicken Reproduction & Fertilized Eggs

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **저장소 소스 코드 상태 고지**: 본 위키 문서는 저장소의 **현재 소스 코드 상태**(`v1.3.4+26.2`)를 반영하고 있습니다. CurseForge 및 Modrinth의 공개 릴리스 빌드보다 앞선 최신 미출시 커밋이나 개발 기능이 포함될 수 있습니다.

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

> ☕ **개발 후원**: 모드가 마음에 드셨다면, [Ko-fi](https://ko-fi.com/rifaditya)를 통해 개발자를 후원하고 최신 초기 테스트 빌드를 체험해 보세요!

---

## 🔗 Related Documentation
* [[Autonomous Wild Breeding & Species Habitats|ko_kr-Autonomous-Breeding-and-Habitats]]
* [[Cramped Pen Stunting & Spacious Pasture Recovery|ko_kr-Cramped-Pen-Penalties-and-Pasture-Recovery]]
* [[Namespaced GameRules & Configuration|ko_kr-GameRules-and-Configuration]]
* Return to [[Home Portal|ko_kr-Home]]
