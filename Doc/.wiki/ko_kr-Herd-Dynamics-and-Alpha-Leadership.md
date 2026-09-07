# 👑 Herd Dynamics, Alpha Leadership & Panic Stampedes

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **저장소 소스 코드 상태 고지**: 본 위키 문서는 저장소의 **현재 소스 코드 상태**(`v1.3.4+26.2`)를 반영하고 있습니다. CurseForge 및 Modrinth의 공개 릴리스 빌드보다 앞선 최신 미출시 커밋이나 개발 기능이 포함될 수 있습니다.

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

> ☕ **개발 후원**: 모드가 마음에 드셨다면, [Ko-fi](https://ko-fi.com/rifaditya)를 통해 개발자를 후원하고 최신 초기 테스트 빌드를 체험해 보세요!

---

## 🔗 Related Documentation
* [[Autonomous Wild Breeding & Species Habitats|ko_kr-Autonomous-Breeding-and-Habitats]]
* [[Physical Scale & Dynamic Harvest Drops|ko_kr-Physical-Scale-and-Harvest-Drops]]
* [[Pasture Enrichment & Overgrazing Terrain Wear|ko_kr-Pasture-Enrichment-and-Overgrazing]]
* Return to [[Home Portal|ko_kr-Home]]
