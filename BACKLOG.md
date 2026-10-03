# 📌 Natural Reproduction Backlog

This file tracks planned features, technical refinements, performance optimizations, and deferred bug fixes for **Natural Reproduction** under the **Delayed Gratification (DG)** design track (*"Invest in the world, and the world pays dividends"*).

---

## 🛡 Code-Only & Vanilla-First Architectural Directive
In strict adherence to the project's **Code & Vanilla First Asset Rule** (`[DIR-20260626-001]`), all mechanics planned in this backlog are engineered **strictly using code and native vanilla resources**:
- **Visual & Physical States**: Handled purely via vanilla entity attributes (`minecraft:scale`, `generic.max_health`, `generic.movement_speed`), vanilla DataComponents, and dynamic vanilla particle emitters (`ParticleTypes.WAX_ON`, `SMOKE`, `ANGRY_VILLAGER`, `HAPPY_VILLAGER`, `SQUID_INK`).
- **World & Habitat Structures**: Utilizes existing interactive vanilla blocks (`Blocks.CAULDRON`, `Blocks.COMPOSTER`, `Blocks.HAY_BLOCK`, `Blocks.WATER`, `Blocks.GRASS_BLOCK`, `Blocks.DIRT`) without custom blocks or 3D block models.
- **Entity Identity & Breeding**: Powered entirely by vanilla entity models, vanilla egg entities/blocks (`Blocks.FROGSPAWN`, `Blocks.TURTLE_EGG`), vanilla sound events, and custom Java AI goals (`GoalSelector`). **Zero custom 3D models or external textures required.**

---

## 📊 Backlog Summary

*No active backlog items*

---

## 🏷 Legend & Status Tags
- **Categories**: `[FEATURE]`, `[REFINEMENT]`, `[BUGFIX]`, `[PERF]`, `[TECH_DEBT]`
- **Priorities**: `[HIGH]` (Critical logic fix/enhancement), `[MEDIUM]` (Quality of life / optimization), `[LOW]` (Minor polish)
- **Statuses**: `📌 DEFERRED` (Queued for future work), `🚧 IN_PROGRESS` (Active development), `✅ RESOLVED` (Implemented and verified)

---

## 📝 Detailed Backlog Entries

*(All queued tasks resolved and archived in History.md and RELEASE_QUEUE.md)*
