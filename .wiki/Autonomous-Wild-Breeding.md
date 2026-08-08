# Autonomous Wild Breeding Mechanics

| System Attribute | Value |
| :--- | :--- |
| **System Name** | Autonomous Wild Breeding AI |
| **Default Check Frequency** | $24000\text{ ticks} = 1\text{ MC Day}$ (`natural-reproduction:rate`) |
| **Partner Scan Radius** | $8\text{ blocks}$ |
| **Density Cap Scan Radius** | $16\text{ blocks}$ |
| **Max Same-Species Density** | $10\text{ animals}$ (`natural-reproduction:density_cap`) |
| **Required Health** | $100\%\text{ Max Health}$ |

---

## 🌾 Autonomous Love Mode AI Check Workflow

During the `customServerAiStep` tick loop injected by `AnimalBreedingMixin`, every passive animal checks its surroundings:

```text
[Mob AI Tick Step]
       │
       ▼
Is Full Health? (health == maxHealth)
       │ No -> Cancel Check
       ▼ Yes
Is Adult? (isBaby() == false)
       │ No -> Cancel Check
       ▼ Yes
Is Master Switch Enabled? (natural-reproduction:enabled)
       │ No -> Cancel Check
       ▼ Yes
Is Species Allowed? (natural-reproduction:allow_<species>)
       │ No -> Cancel Check
       ▼ Yes
Is Density < Cap? (< 10 same-species within 16 blocks)
       │ No -> Cancel Check (Overcrowded)
       ▼ Yes
Partner Nearby? (same-species adult within 8 blocks)
       │ No -> Cancel Check
       ▼ Yes
Habitat Blocks Present? (scans 5x3x5 area)
       │ No -> Cancel Check
       ▼ Yes
[ENTER LOVE MODE & SPAWN OFFSPRING]
```

---

## 🕒 Breeding Tick Frequency Math

- Default check interval: $T = 24000\text{ ticks}$.
- Since Minecraft runs at $20\text{ ticks/second}$:
  $$\text{Interval (seconds)} = \frac{24000}{20} = 1200\text{ seconds} = 20\text{ minutes} = 1\text{ MC Day}$$
- In native climate biomes, breeding frequency receives a **$2\times$ speed boost**:
  $$\text{Native Interval} = \frac{24000}{2 \times 20} = 600\text{ seconds} = 10\text{ minutes} = 0.5\text{ MC Day}$$

For habitat block lists, see [[Species Habitat Reference|Species-Habitat-Reference]].
