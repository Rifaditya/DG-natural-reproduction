# 👑 Herd Dynamics, Alpha Leadership & Panic Stampedes

> **"A scattered flock is vulnerable; a united herd thrives under its Alpha."**

In vanilla Minecraft, animal mobs wander aimlessly without social cohesion or hierarchy. **Natural Reproduction** introduces authentic social herd dynamics: groups of livestock autonomously organize around a dominant **Alpha leader**, graze in cohesive formations, bed down together at dusk, and react to predator attacks in synchronized panic stampedes.

---

## 📋 System Infobox

| Property | Value |
| :--- | :--- |
| **Feature Name** | Herd Social Dynamics & Leadership AI |
| **Master Toggle** | `natural-reproduction:herd_dynamics` (Default: `true`) |
| **Panic Stampede Toggle** | `natural-reproduction:herd_stampede` (Default: `true`) |
| **Leadership Metric** | Largest Physical Body Scale (`minecraft:scale`) |
| **Flocking Follow Distance** | `5` to `16` blocks |
| **Stampede Duration** | `100` ticks ($5.0\text{ seconds}$) |
| **Stampede Speed Multiplier** | `1.45x` baseline movement speed |
| **AI Goal Hook** | `FollowHerdLeaderGoal.java` (Priority 3) |
| **Primary Helper** | `HerdSocialHelper.java` |

---

## 🎮 Player & Survival Workflow

1. **Natural Alpha Emergence**: When 3 or more animals of the same species congregate in a pasture, the entity with the largest physical body scale automatically assumes Alpha status.
2. **Pastoral Flocking**: Subordinate herd members actively follow the Alpha, keeping within 5–16 blocks while grazing.
3. **Diurnal Grazing Schedule**: Herds naturally wander together across open grass during daylight hours ($0 \le \text{time} < 12000$) and migrate towards enclosed barn shelters as night approaches ($\text{time} \ge 12000$).
4. **Stampede Reaction**: If any herd member is damaged by a player, wolf, or predator, the entire herd emits startled snorts and stampedes in unison away from the damage source for 5 seconds.

---

## 🧮 Mathematical Formalization & Algorithmic Logic

### 1. Alpha Leader Selection Algorithm
Every 200 ticks (10s), animals scan nearby same-species entities within an 8-block sphere:

$$\text{Alpha} = \arg\max_{e \in \text{Herd}} \Big( \text{getScale}(e) \times 1000 + \text{getMaxHealth}(e) \Big)$$

If multiple animals share identical peak scale attributes, the older specimen (`UUID` comparison) retains leadership.

### 2. Follow Vector & Separation Math
Subordinate herd members compute a desired movement vector $\vec{v}_{\text{follow}}$ towards the Alpha leader $\vec{p}_{\text{alpha}}$:

$$\vec{d} = \vec{p}_{\text{alpha}} - \vec{p}_{\text{self}}$$

$$\vec{v}_{\text{follow}} = \begin{cases} 
\vec{0} & \text{if } \|\vec{d}\| < 5.0\text{ blocks (Too Close)} \\
\frac{\vec{d}}{\|\vec{d}\|} \times v_{\text{walk}} & \text{if } 5.0 \le \|\vec{d}\| \le 16.0\text{ blocks (Flocking)} \\
\frac{\vec{d}}{\|\vec{d}\|} \times (v_{\text{walk}} \times 1.25) & \text{if } \|\vec{d}\| > 16.0\text{ blocks (Catch-Up Sprint)}
\end{cases}$$

```
        [Alpha Leader (1.30x Scale)]
              ▲            ▲
             /              \
        5-16 blocks     5-16 blocks
           /                  \
   [Subordinate #1]     [Subordinate #2]
          │                    │
          └─── Flocking Radius ┘
```

### 3. Stampede Panic Trigger
When an entity takes damage:
1. `HerdSocialHelper.triggerHerdDistress(victim, attacker, radius = 12.0)` is invoked.
2. All herd members receive a `stampedeTicks = 100` timer.
3. Animals pathfind directly away from $\vec{p}_{\text{attacker}}$ at $1.45\text{x}$ sprint speed.

---

## 💻 Developer & Mixin Hooks

### Custom Goal Injection
`FollowHerdLeaderGoal` is attached to all `Animal` entities on server initialization:

```java
// Priority 3: Below BreedGoal and PanicGoal, above RandomStrollGoal
animal.goalSelector.addGoal(3, new FollowHerdLeaderGoal(animal, 1.15D));
```

### Programmatic Alpha Query
```java
LivingEntity leader = HerdSocialHelper.getHerdLeader(animal);
boolean isAlpha = HerdSocialHelper.isAlphaLeader(animal);
```

---

## 🔗 Related Documentation
* [[Autonomous Wild Breeding & Species Habitats|Autonomous-Breeding-and-Habitats]]
* [[Physical Scale & Dynamic Harvest Drops|Physical-Scale-and-Harvest-Drops]]
* [[Pasture Enrichment & Overgrazing Terrain Wear|Pasture-Enrichment-and-Overgrazing]]
