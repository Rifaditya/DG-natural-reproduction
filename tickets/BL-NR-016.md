# 🎫 [BL-NR-016] Visual Pregnancy Particle Effect Indicator

- **Ticket ID**: `[BL-NR-016]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Scope**: `All Anchors (1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3, 26.x)`
- **Status**: `📌 DEFERRED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`
- **Design Philosophy**: Delayed Gratification (DG) — Clear, immersive biological feedback without breaking vanilla visual balance

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Provide an intuitive, vanilla-friendly in-world visual indicator for pregnant animals across all version anchors of `Natural Reproduction` by emitting subtle ambient particle effects (e.g. gentle heart drift or villager happy sparkles) periodically from pregnant female animals.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: Once initial mating hearts disappear, pregnant animals look completely indistinguishable from non-breeding animals. Players cannot tell which cows, sheep, or pigs are carrying offspring without external mod tools or memory guesswork.
   - *After*: Pregnant animals emit subtle, periodic ambient particles (e.g. 1–2 small heart or sparkle particles every 40–80 ticks), giving farmers clear visual feedback that gestation is active in their pastures.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Do not flood the client with particle spam that causes FPS drops or obscures entity hitboxes in dense livestock pens.
   - ❌ Do not introduce client-only particle classes into common server tick loops.
   - ❌ Do not alter gestation countdown math or genetics inheritance logic.
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [ ] Pregnant animals (`AnimalGestationHelper.isPregnant(animal) == true`) periodically emit ambient particles during their gestation duration.
   - [ ] Ambient particle emission interval is staggered/modulo-checked to prevent synchronized particle bursts in grouped animals.
   - [ ] Controlled by dynamic GameRule `natural-reproduction:pregnancy_particles` (boolean, default `true`).
   - [ ] Particles immediately cease once birth or egg laying occurs (`gestation_ticks <= 0`).
   - [ ] Sidedness safe: server uses `ServerLevel.sendParticles` or syncs state cleanly to client ambient ticking without dedicated server crashes.
   - [ ] Verified across all active version anchors under `./gradlew test`.

---

## ❓ 2. Problem Statement & Player Impact

In `Natural Reproduction`, realistic gestation replaces vanilla's instantaneous baby popping. While this adds realistic farming depth, animals remain pregnant for 1 full in-game day (24,000 ticks) by default.

Currently:
1. When two animals mate, standard vanilla love hearts appear briefly and fade away.
2. For the subsequent 20 real minutes of gestation, there is zero visual difference between a pregnant cow and an ordinary cow.
3. Players cannot determine if their animals successfully conceived, if gestation stalled, or which animals in a pasture are expecting offspring, leading to confusion and bug reports (e.g. thinking animals are failing to breed).

Adding a clean, subtle ambient particle indicator solves this communication gap directly in vanilla gameplay without requiring third-party HUD mods.

---

## 🔬 3. Architectural Blueprint & Target Classes

### Particle Emission Mechanics (`AnimalGestationHelper.java` / `AnimalBreedingMixin.java`)
```java
// ServerLevel ambient particle pulse inside gestation tick loop
if (DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.PREGNANCY_PARTICLES)) {
    // Stagger particle pulses per entity ID every 60 ticks (3 seconds)
    if ((mother.getId() + level.getGameTime()) % 60 == 0) {
        level.sendParticles(
            ParticleTypes.HEART, // Or subtle custom / happy villager particle
            mother.getX(),
            mother.getY() + mother.getBbHeight() * 0.5,
            mother.getZ(),
            1, // Count: subtle single particle
            mother.getBbWidth() * 0.25,
            0.15,
            mother.getBbWidth() * 0.25,
            0.02
        );
    }
}
```

### GameRule Registration (`NaturalReproductionFabric.java`)
```java
public static DynamicGameRule<Boolean> PREGNANCY_PARTICLES;

// Inside registerGameRules:
PREGNANCY_PARTICLES = DynamicGameRuleManager.booleanRule("natural-reproduction:pregnancy_particles", CATEGORY, true)
    .name("Pregnancy Ambient Particles")
    .description("When true, pregnant animals periodically emit subtle particles indicating gestation status.")
    .register();
```

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

- **MC 1.20.1, 1.21.1, 1.21.11**: `ServerLevel.sendParticles(ParticleOptions, double, double, double, int, double, double, double, double)`
- **MC 26.1.2, 26.2, 26.3, 26.x**: Particle API parity verified with modern sovereign Mojang mappings.

---

## 🛡️ 5. Edge Cases & Failure Modes

- **High-Density Farms**: In pens with 100+ animals, particle counts must stay negligible ($O(1)$ per animal every 3–4 seconds) to avoid FPS drops.
- **Save File & State Resilience**: Particle check reads existing `gestation_ticks` trait directly; zero schema mutations or NBT versioning hazards.
- **Headless Server Safety**: `ServerLevel.sendParticles` sends standard network packets to tracking players; zero client imports in common codebase.

---

## 🚀 6. Implementation Steps (Code-Level Plan)

1. Pre-step SemVer bump across targeted anchors.
2. Add `PREGNANCY_PARTICLES` dynamic GameRule to `NaturalReproductionFabric.java`.
3. Wire periodic staggered particle emission in `AnimalGestationHelper.tickGestation(...)`.
4. Add English and global translation entries in `assets/naturalreproduction/lang/*.json`.
5. Execute `./gradlew test` across all anchors to verify 5 pillars.

---

## 🏁 7. 5-Pillar Verification Checklist

- [ ] **Pillar 1 (Gestation Particle Fuzzing)**: Verify particles only emit when `isPregnant(animal) == true` and stop immediately at 0 ticks.
- [ ] **Pillar 2 (Performance Stress)**: 50 pregnant animals in a chunk produce $< 1\%$ client/server frame time overhead.
- [ ] **Pillar 3 (Brigadier Command Sync)**: Toggling `/gamerule natural-reproduction:pregnancy_particles false` immediately halts particles.
- [ ] **Pillar 4 (Save/Reload Parity)**: Reloading world preserves pregnancy state and particle emission resumes.
- [ ] **Pillar 5 (Sidedness & Licensing)**: Zero client class imports; GPLv3 headers preserved.
