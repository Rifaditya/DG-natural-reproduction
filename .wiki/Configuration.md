# Client & Server Configuration Guide

| Interface | Supported Environment | Entrypoint Mapping |
| :--- | :--- | :--- |
| **Server GameRules** | Server & Singleplayer | `game_rules.dat` (Server-authoritative) |
| **ModMenu Client GUI** | Client Side Only (`ENV_CLIENT`) | `net.fabricmc.api.ClientModInitializer` |
| **YACL v3 Integration** | Client Side Only (`ENV_CLIENT`) | `YaclScreenHelper.java` |

---

## 🎮 Server GameRule Supremacy

All gameplay mechanics are driven by server-authoritative GameRules:
- When playing on a multiplayer server, the server's GameRules dictate gameplay behavior for all connected clients.
- GameRules can be modified live in-game by server operators (OP level 2+) via `/gamerule` or `/naturalreproduction set <rule> <val>`.

---

## 🖥️ Client Config GUI (ModMenu & YACL v3 Integration)

In singleplayer or local host worlds, players can configure settings visually using **ModMenu** and **Yet Another Config Lib (YACL v3)**:

### Config Screen Layout
- **General Settings Tab**:
  - Master Switch (`natural-reproduction:enabled`)
  - Breeding Frequency Rate (`natural-reproduction:rate`)
  - Overcrowding Density Cap (`natural-reproduction:density_cap`)
  - Scale-Based Drop Yield (`natural-reproduction:scale_drops`)
  - Cramped Space Stunting Penalty (`natural-reproduction:cramped_space_penalty`)
  - Native Biome Fertility Boost (`natural-reproduction:biome_fertility`)
  - Biome Variant Adaptation (`natural-reproduction:biome_variants`)

- **Species Toggles Tab (`§lNatural Reproduction - Species Toggles`)**:
  - 27 individual toggle switches for each animal species (`allow_cow`, `allow_pig`, `allow_sheep`, `allow_chicken`, `allow_wolf`, `allow_frog`, `allow_hoglin`, etc.).

---

## 🔒 Server-Crash Security Architecture

The client GUI integration is safely gated in `YaclScreenHelper.java` and `ModMenuIntegration.java`:
- The GUI screen is registered strictly on the client side (`net.fabricmc.api.EnvType.CLIENT`).
- Dedicated `ModMenu` and `YACL` entrypoint mappings prevent server classloading crashes when installed on dedicated server environments.

---

## 📦 Uninstallation & World Save Lifecycle

Because this mod utilizes native vanilla GameRules for per-world configuration (rather than a static global config file), active rule values are saved directly inside each world's data file (`saves/<world>/level.dat` under `Data.GameRules`).

### What Happens If You Remove the Mod:
If you uninstall this mod and load an existing world that was previously played with it:
1. **Informational Log Warning:** Vanilla Minecraft will log warning lines in `latest.log`:
   `Unknown registry key in ResourceKey[minecraft:root / minecraft:game_rule]: natural-reproduction:...`
2. **`minecraft:root` is an in-memory Java registry, NOT a folder:** `ResourceKey[minecraft:root / ...]` is Mojang's internal code identifier for the game's in-memory registry hierarchy. It is strictly an in-memory data structure in Java, not a physical directory on your drive. Zero files in your `.minecraft` directory or system are modified or overwritten.
3. **Identical to Mojang's Engine Mechanics:** This operates **exactly like how Mojang handles removed, renamed, or changed GameRules between vanilla updates** (or disabled experimental data packs). Minecraft safely skips over unknown keys during gameplay.
4. **Automatic Vanilla Pruning on Save:** Vanilla Minecraft only serializes active, registered GameRules when saving. The moment you save the world (**"Save and Quit to Title"** or after an auto-save), vanilla writes a clean `level.dat` containing only recognized keys, permanently purging all orphaned rules. Subsequent world loads will produce zero warnings.
5. **Harmless:** The warning is completely safe to ignore.

---

For diagnostic logs, see [[HUD & Diagnostics|HUD-and-Diagnostics]].
