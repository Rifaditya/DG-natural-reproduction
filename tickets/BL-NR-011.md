# 🎫 [BL-NR-011] Standalone Naturalist Compatibility Datapack & Player Support Package

- **Ticket ID**: `[BL-NR-011]`
- **Category**: `[REFINEMENT]`
- **Priority**: `[HIGH]`
- **Scope**: `Datapack & Player Support (MC 1.20.1 & 1.21.1+)`
- **Status**: `📌 DEFERRED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Create a ready-to-drop universal vanilla datapack (`Doc/Players/Datapacks/Natural-Reproduction-Naturalist-Control.zip`) and player support guide that immediately lets players toggle off autonomous reproduction for any of the 47 **Naturalist** (`naturalist:*`) creatures on current public releases without waiting for a mod binary update, while remaining forward-compatible with upcoming native entity tags.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: Players using **Naturalist** alongside current public releases of **Natural Reproduction** cannot disable autonomous breeding for high-density ambient modded species (e.g., snails, fireflies, caterpillars, butterflies), leading to rapid population growth and severe FPS drops.
   - *After*: Players can drop a single universal `.zip` datapack into their world's `datapacks/` folder and immediately disable reproduction per species via an interactive clickable chat menu (`/function nr_naturalist:menu`), edit entity tags (`disabled_mobs.json`), or safely thin excess populations (`/function nr_naturalist:cull_excess`) to recover FPS immediately.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Do not modify Java source code or bump `mod_version` in `gradle.properties` within this ticket.
   - ❌ Do not require players to install extra dependency mods to run the compatibility datapack.
   - ❌ Do not use corporate "we" voice in the player support package or Discord reply; enforce the Solo Indie Voice ("I") invariant.
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [ ] Ready-to-drop datapack archive created at `Doc/Players/Datapacks/Natural-Reproduction-Naturalist-Control.zip`.
   - [ ] Active `Fast-Fail 1` 100-tick (`5s`) function loop sets `Age: 600` on adult (`nbt={Age:0}`) disabled Naturalist species.
   - [ ] Clickable chat toggle menu (`/function nr_naturalist:menu`) and safe population culling function (`/function nr_naturalist:cull_excess`) included.
   - [ ] Pre-populated entity type tags (`disabled_mobs.json`, plus forward-compatible `excluded_animals.json` and `included_animals.json` across `tags/entity_types/` for 1.20.1 and `tags/entity_type/` for 1.21.1+) with `"required": false` entries for all 47 Naturalist creatures.
   - [ ] Player support guide (`Doc/Players/Naturalist_Datapack_Guide.md`) and authentic solo-indie (`"I"` voice) Discord reply for Muqington authored.

---

## ❓ 2. Problem Statement & Player Impact

Community member **Muqington** reported on Discord that **Naturalist** creatures (`naturalist:*`) reproduce rapidly under **Natural Reproduction**, causing severe entity crowding and framerate drops without a built-in per-species toggle for modded mobs in current public releases. Because public releases follow the 7-day automated release queue cadence, providing an immediate zero-code vanilla datapack that hooks directly into `AnimalBreedingMixin` line 92 (`Fast-Fail 1: if (self.getAge() != 0 ...) return;`) resolves player lag today while seamlessly bridging into native `#natural-reproduction:excluded_animals` tag support in [`BL-NR-012`](./BL-NR-012.md).

---

## 🔬 3. Architectural Blueprint & Target Files

- **Target Files**:
  - `Doc/Players/Datapacks/Natural-Reproduction-Naturalist-Control/` (extracted datapack source tree)
  - `Doc/Players/Datapacks/Natural-Reproduction-Naturalist-Control.zip` (ready-to-drop universal archive)
  - `Doc/Players/Naturalist_Datapack_Guide.md` (player installation, chat menu, tag editing, and Discord support reply)
- **Core Mechanisms**:
  1. **Active `Fast-Fail 1` Bypass for Current Public Releases**: Schedules a lightweight 100-tick (`5s`) function (`nr_naturalist:tick_100t`) that sets `Age: 600` (a 30-second adult breeding cooldown) on adult (`nbt={Age:0}`) entities of disabled Naturalist species so they immediately exit at `AnimalBreedingMixin` line 92 (`if (self.getAge() != 0 ...) return;`) before spatial density or mate scans run.
  2. **Interactive In-Game Chat Menu & Safe Culling**: Provides `/function nr_naturalist:menu` using scoreboard toggles (`nr_naturalist`) so players can click `[ON]` / `[OFF]` in chat per species, plus `/function nr_naturalist:cull_excess` to safely thin out overcrowded Naturalist mobs (skipping named and tamed pets) and recover FPS.
  3. **Forward-Compatible Dual-Era Entity Type Tags**: Includes `disabled_mobs.json` alongside `data/natural-reproduction/tags/entity_types/excluded_animals.json` (MC 1.20.1 plural folder) and `data/natural-reproduction/tags/entity_type/excluded_animals.json` + `included_animals.json` (MC 1.21.1+ singular folder) with `"required": false` entries for all 47 Naturalist mobs.
  4. **Solo Indie Player Support Reply**: Includes a ready-to-paste first-person (`"I"`) Discord reply for Muqington explaining how to drop the `.zip` into `datapacks/` and run `/function nr_naturalist:menu`.

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

- **MC 1.20.1 (`pack_format: 15`)**: Uses `data/<namespace>/tags/entity_types/` and `data/<namespace>/functions/` via multi-overlay `pack.mcmeta` (`supported_formats: [15, 99]`).
- **MC 1.21.1 – 26.3+ (`pack_format: 48+`)**: Uses `data/<namespace>/tags/entity_type/` and `data/<namespace>/function/` via `overlays` directory in the same universal `.zip` archive.

---

## 🛡️ 5. Edge Cases, Collisions & Failure Modes

- **Missing Mod Safety (`"required": false`)**: Every `naturalist:*` entry in entity tags must use `{"id": "naturalist:<mob>", "required": false}` so the datapack never fails to load on worlds with different Naturalist versions or partial entity registries.
- **Baby & Named Pet Protection**: `Age: 600` is applied strictly to `nbt={Age:0}` (ready-to-breed adults) so growing babies (`Age < 0`) are never prematurely aged into adults, and `cull_excess` skips named/tamed mobs.
- **Zero Tick Lag**: The cooldown reset runs once every 100 ticks (`5s`) via `schedule function`, incurring negligible server overhead.

---

## 🚀 6. Implementation Steps (Execution Plan)

1. Author `pack.mcmeta` with multi-version compatibility (`pack_format: 15` base + `supported_formats: [15, 99]` + `overlays` for `1.21+` singular `function`/`tags/entity_type` directories).
2. Create `load.mcfunction`, `tick_100t.mcfunction`, `menu.mcfunction`, `cull_excess.mcfunction`, and per-species toggle functions covering all 47 Naturalist species.
3. Populate `disabled_mobs.json`, `excluded_animals.json`, and `included_animals.json` across both `1.20.1` (`entity_types`) and `1.21.1+` (`entity_type`) tag paths with `"required": false`.
4. Package the datapack into `Doc/Players/Datapacks/Natural-Reproduction-Naturalist-Control.zip` and document installation, usage, and the Discord reply for Muqington in `Doc/Players/Naturalist_Datapack_Guide.md`.

---

## 🏁 7. 5-Pillar Verification Checklist

- [ ] **Pillar 1 (Fuzzing & JSON Schema Validity)**: All tag JSON files and `pack.mcmeta` parse cleanly without syntax errors; all modded IDs include `"required": false`.
- [ ] **Pillar 2 (Concurrency & Tick Safety)**: 100-tick scheduled loop resets `Age: 600` only on `nbt={Age:0}` adults without affecting babies (`Age < 0`).
- [ ] **Pillar 3 (Brigadier & Function Syntax)**: `/function nr_naturalist:menu` and `/function nr_naturalist:cull_excess` use valid command syntax across 1.20.1 and 1.21.1+ overlays.
- [ ] **Pillar 4 (GameTest & Fast-Fail Alignment)**: Setting `Age: 600` verified against `AnimalBreedingMixin.java` line 92 (`if (self.getAge() != 0 ...) return;`).
- [ ] **Pillar 5 (Sidedness & Support Hygiene)**: Universal `.zip` structure verified at root level (`pack.mcmeta` + `data/`); Discord reply uses authentic Solo Indie (`"I"`) voice.
