# 🎫 [BL-NR-013] Wiki & Web Portal Guide: How to Create a Datapack to Exclude or Add Custom Mobs

- **Ticket ID**: `[BL-NR-013]`
- **Category**: `[REFINEMENT]`
- **Priority**: `[MEDIUM]`
- **Scope**: `Wiki (.wiki/) & VitePress Web Portal`
- **Status**: `📌 DEFERRED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Author a comprehensive, beginner-friendly encyclopedic guide (`Datapack-and-Mod-Compatibility.md`) in `.wiki/` (and federate to `dasik-mc-mod-portal`) teaching players and modpack creators how to create a custom datapack to exclude unwanted mobs (`#natural-reproduction:excluded_animals`) or add/whitelist new modded mobs (`#natural-reproduction:included_animals`), alongside how to use the legacy `Age: 600` function pack.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: Players and modpack authors wanting to customize which modded animals breed with **Natural Reproduction** have no step-by-step documentation on entity tag paths, `pack.mcmeta` formats, or how to inspect modded entity IDs in-game.
   - *After*: Players and pack authors have a copy-paste-ready Wiki and Web Portal tutorial covering folder structure, `pack.mcmeta` across MC `1.20.1` to `26.3`, `#natural-reproduction:excluded_animals`, `#natural-reproduction:included_animals`, `"required": false` safety, in-game entity ID discovery (`F3+I` / `/summon`), and the legacy `Age: 600` datapack method.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Do not modify Java source code or bump mod SemVer within this ticket.
   - ❌ Do not advertise `#natural-reproduction:excluded_animals` or `#natural-reproduction:included_animals` on Modrinth/CurseForge storefronts until [`BL-NR-012`](./BL-NR-012.md) is published to live platforms (or clearly label version requirements on the Wiki).
   - ❌ Do not use corporate "we" voice; enforce the Solo Indie Voice ("I") invariant.
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [ ] `.wiki/Datapack-and-Mod-Compatibility.md` created with complete copy-paste examples and troubleshooting matrix.
   - [ ] `.wiki/Home.md` and `.wiki/_Sidebar.md` updated with navigation links to `Datapack-and-Mod-Compatibility`.
   - [ ] `pack_format` reference table and folder path differences (`tags/entity_types/` on 1.20.1 vs `tags/entity_type/` on 1.21.1–26.3) documented.
   - [ ] Synchronized with `.wiki.git` remote and federated to `dasik-mc-mod-portal` once [`BL-NR-012`](./BL-NR-012.md) ships.

---

## ❓ 2. Problem Statement & Player Impact

Modpack creators and players running large fauna mods (**Naturalist**, **Alex's Mobs**, **Critters & Companions**, **Cobblemon**, etc.) frequently need a declarative, world-portable way to blacklist ambient creatures or whitelist selective modded species across servers and modpacks. Without clear documentation showing exact folder paths (`entity_types` vs `entity_type`), `"required": false` JSON syntax, and how to look up modded entity IDs, users hit broken datapack loads or silent tag mismatches.

---

## 🔬 3. Architectural Blueprint & Target Documentation Files

- **Target Files**:
  - `.wiki/Datapack-and-Mod-Compatibility.md` (new wiki page)
  - `.wiki/Home.md` & `.wiki/_Sidebar.md` (navigation links)
- **Required Guide Sections**:
  1. **Overview & Compatibility Modes**: Dynamic GameRules (`/nr set`), Datapack Entity Tags (`#natural-reproduction:excluded_animals` & `#natural-reproduction:included_animals`), and the legacy `Age: 600` function pack for older releases.
  2. **Step-by-Step Folder Tree**: Visual directory diagram comparing MC `1.20.1` (`data/natural-reproduction/tags/entity_types/`) and MC `1.21.1 – 26.3+` (`data/natural-reproduction/tags/entity_type/`).
  3. **Copy-Paste `pack.mcmeta` Reference Table**: Exact `pack_format` numbers across `1.20.1` (`15`), `1.21.1` (`48`), `1.21.11` (`61`), and `26.1.2 – 26.3` (`71+`), plus multi-version `supported_formats` snippet.
  4. **Excluding Mobs (`excluded_animals.json`)**: Copy-paste JSON template using `{"id": "<namespace>:<mob>", "required": false}` with examples for **Naturalist** and **Alex's Mobs**.
  5. **Adding / Whitelisting Custom Mobs (`included_animals.json`)**: Workflow for setting `/gamerule allow_modded_animals false` (or `natural-reproduction:allow_modded_animals false`) and selectively whitelisting only chosen modded animals via `included_animals.json` (noting that the target mob must extend vanilla `Animal` in Java).
  6. **How to Find Modded Mob IDs In-Game**: Using `F3 + I` while looking at an entity to copy its `/summon` command and namespace ID to the clipboard, or using `/summon ` tab-completion.
  7. **Verification & Troubleshooting**: Running `/reload`, `/datapack list`, and checking tag membership.

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

- Explicitly documents the Mojang 1.21 plural-to-singular tag folder rename:
  - **MC 1.20.1**: `data/natural-reproduction/tags/entity_types/excluded_animals.json` & `included_animals.json`
  - **MC 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3**: `data/natural-reproduction/tags/entity_type/excluded_animals.json` & `included_animals.json`

---

## 🛡️ 5. Edge Cases, Collisions & Failure Modes

- **Missing `"required": false` Crash**: Warn users that omitting `"required": false` on modded entity IDs causes Minecraft to reject the entire tag if that mod is removed or changes an entity ID.
- **Non-`Animal` Custom Entities**: Clarify that `#natural-reproduction:included_animals` applies to modded mobs that extend Minecraft's `Animal` class; mobs built on custom non-`Animal` base classes (e.g., `PathfinderMob` without `Animal` inheritance) do not run vanilla animal breeding hooks.
- **Version Gate Clarity**: Clearly note which mod version introduces native `#excluded_animals` / `#included_animals` tags vs. using the [`BL-NR-011`](./BL-NR-011.md) function datapack on earlier versions.

---

## 🚀 6. Implementation Steps (Execution Plan)

1. Author `.wiki/Datapack-and-Mod-Compatibility.md` with all 7 guide sections, copy-paste JSON blocks, and `pack_format` table.
2. Add `[[Datapack & Mod Compatibility|Datapack-and-Mod-Compatibility]]` to `.wiki/Home.md` and `.wiki/_Sidebar.md`.
3. Verify UTF-8 encoding, LF (`\n`) line endings, and Solo Indie (`"I"`) voice across all modified wiki files.
4. Push `.wiki/` changes and federate to `dasik-mc-mod-portal` when [`BL-NR-012`](./BL-NR-012.md) is released.

---

## 🏁 7. 5-Pillar Verification Checklist

- [ ] **Pillar 1 (JSON & Schema Accuracy)**: All sample `pack.mcmeta`, `excluded_animals.json`, and `included_animals.json` snippets are valid JSON with `"required": false`.
- [ ] **Pillar 2 (Multi-Era Path Parity)**: Both `tags/entity_types/` (1.20.1) and `tags/entity_type/` (1.21.1–26.3) paths are clearly contrasted.
- [ ] **Pillar 3 (Command & Hotkey Verification)**: `F3+I`, `/summon`, `/reload`, and `/datapack list` instructions match vanilla behavior.
- [ ] **Pillar 4 (Code-to-Wiki Alignment)**: Tag resource locations (`natural-reproduction:excluded_animals` and `natural-reproduction:included_animals`) and precedence rules match [`BL-NR-012`](./BL-NR-012.md).
- [ ] **Pillar 5 (Navigation & Voice Hygiene)**: `.wiki/Home.md` and `.wiki/_Sidebar.md` links resolve cleanly; Solo Indie (`"I"`) voice verified.
