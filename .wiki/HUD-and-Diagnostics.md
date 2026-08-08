# HUD Tooltips & Visual Particle Diagnostics

| Feature | Visual Trigger | Description |
| :--- | :--- | :--- |
| **Cramped Stunting Particles** | Smoke & Angry Villager | Emitted when offspring suffers scale stunting in confined pens or overcrowded pastures. |
| **Spacious Pasture Particles** | Happy Villager (Hearts) | Emitted when offspring receives $+15\%$ scale recovery in open pastures. |
| **Tracker Logs** | `/naturalreproduction trackerlogs` | In-memory event tracking for server administrators. |

---

## 🌫️ Visual Particle Feedback

Natural Reproduction provides instant visual particle feedback upon offspring birth:

- **Cramped Space Stunting**:
  - **Smoke Particles**: 5 smoke particles (`ParticleTypes.SMOKE`) around baby entity.
  - **Angry Villager Particles**: 2 angry villager particles (`ParticleTypes.ANGRY_VILLAGER`) signaling overcrowded stunting.
- **Spacious Pasture Recovery**:
  - **Happy Villager Particles**: 7 happy villager green particles (`ParticleTypes.HAPPY_VILLAGER`) signaling size recovery boost up to $1.30x$.

---

## 📜 In-Memory Event Trackerlogs

Administrators can inspect real-time breeding event logs using `BreedingTrackerLogger`:

```text
[Tracker Log Output]
[14:22:10] SPECIES: minecraft:cow | PARENT1: [x=102, y=64, z=-45] | SCALE: 1.15x | PASTURE: OPEN (Recovery)
[14:25:34] SPECIES: minecraft:wolf | PARENT1: [x=-12, y=70, z=180] | SCALE: 0.85x | PASTURE: CONFINED (Stunted)
```

For developer setup details, see [[Developer Setup & Building|Developer-Setup-and-Building]].
