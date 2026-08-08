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

For diagnostic logs, see [[HUD & Diagnostics|HUD-and-Diagnostics]].
