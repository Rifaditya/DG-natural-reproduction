# 🎛️ Master Release Queue: Delayed Gratification — Natural Reproduction

> **Mod Project Master Ground-Truth Document**  
> *Last Synchronized: 2026-10-30*  
> **Modrinth ID**: `WT4xq2BM` (`dg-natural-reproduction`) | **CurseForge ID**: `1642683` (`dg-natural-reproduction`) | **Lead SemVer**: `1.4.47`

---

## 📊 Multi-Version Release Matrix & Queue Status

| Target MC | Generational Era | Live on Platforms | Next Queued Version | Status & Cadence Action | Feature Highlights / Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **MC 26.3** | Modern Lead | MR: `1.4.32+26.3` / CF: `1.4.32+26.3` | `1.4.33+26.3` | ⏳ **Modern Lead** | Lineage & Pedigree Kinship Architecture live. Followed by `1.4.33` drop degradation test suite. |
| **MC 26.2** | Modern Predecessor | MR: `1.4.31+26.2` / CF: `1.4.31+26.2` | `1.4.34+26.2` | 🔄 **Catch-Up Phase** | Modern parity catch-up live. Next: `1.4.34+26.2` Multi-Era Lineage & Genetics Parity. |
| **MC 26.1** | Modern Predecessor | MR: `1.4.31+26.1` / CF: `1.4.31+26.1` | `1.4.34+26.1` | 🔄 **Catch-Up Phase** | Modern Sovereign port live. Next: `1.4.34+26.1` Multi-Era Lineage & Genetics Parity. |
| **MC 1.21.11** | Winter Drop | — | `1.4.31+1.21.11` | ⏳ **Queued** | Winter Drop port: Java 21 bytecode, Loom 1.15-SNAPSHOT remap, DasikLibrary 1.1.0+1.21.11 wiring. Followed by `1.4.34+1.21.11`. |
| **MC 1.21.1** | Transitional Early | — | `1.4.31+1.21.1` | ⏳ **Queued** | Dedicated subproject segregation for MC 1.21.1, Java 21 bytecode, DataComponents, native Attributes.SCALE. Followed by `1.4.34+1.21.1`. |
| **MC 1.20.1** | Legacy Era | — | `1.4.31+1.20.1` | ⏳ **Queued** | Legacy era port: Java 17 bytecode, Fabric Loom 1.10.2, Mojang mappings, DasikLibrary 1.1.0+1.20.1 wiring. Followed by `1.4.34+1.20.1`. |

---

## 🏛️ Project Operating Rules & Architectural Invariants

1. **🔢 Universal Direct SemVer Inheritance**:
   - Modern subprojects share unified SemVer milestone lineage targeting `1.4.34+`.
   - Each Minecraft version anchor manages its own organic progression to ensure 100% clean, verified parity.
   - Dedicated `CHANGELOG.md` and `RELEASE_QUEUE.md` are maintained in each version subproject folder to ensure deterministic extraction by the automated platform publisher.

2. **📅 Daily Update Guard**:
   - Strict maximum of 1 release per day per targeted Minecraft version anchor across Modrinth and CurseForge.
