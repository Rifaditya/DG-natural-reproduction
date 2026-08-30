# 🥚 Dedicated Chicken Reproduction & Fertilized Eggs

> **"Eggs are for food; Fertilized Eggs are for life."**

In vanilla Minecraft, throwing ordinary chicken eggs creates a bizarre entity duplication loop where kitchen eggs randomly hatch chicks, while industrial egg collection hoppers cause massive entity lag. **Natural Reproduction** separates food eggs from reproduction by introducing **Fertilized Eggs** and realistic avian husbandry.

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

## 🔗 Related Documentation
* [[Autonomous Wild Breeding & Species Habitats|Autonomous-Breeding-and-Habitats]]
* [[Cramped Pen Stunting & Spacious Pasture Recovery|Cramped-Pen-Penalties-and-Pasture-Recovery]]
* [[Namespaced GameRules & Configuration|GameRules-and-Configuration]]
