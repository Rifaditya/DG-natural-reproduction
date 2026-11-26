# 🎫 [BL-NR-020] Fix `DasikSupportHelper.openKofi(Screen)` ABI Mismatch Across `26.x` Anchors (`YaclScreenHelper`)

- **Ticket ID**: `[BL-NR-020]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: `Active 26.x Anchors (26.1 -> 1.4.49+26.1, 26.2 -> 1.4.49+26.2, 26.3 -> 1.4.49+26.3)`
- **Scope**: `YaclScreenHelper.java` (lines `638`, `649` in `v26.1`, `v26.2`, `v26.3`; also sync root `src/` `602,613`) + `gradle.properties` (`dasik_library_version=1.13.2`) + `fabric.mod.json` (`"dasik-library": ">=1.11.2"`) + Bytecode ABI contract test + Modrinth profile deploy
- **Status**: `✅ RESOLVED`
- **Grilling Status**: `✅ PASSED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`
- **Umbrella Reference**: [`BL-WS-051`](file:///E:/Minecraft%20Project/tickets/BL-WS-051.md)
- **Date Added**: `2026-11-25`

---

## 🎯 1. Problem Statement & Player Impact

`code_auditor.py --check-profiles` and hub archive linkage scanning detected that compiled JARs of **Natural Reproduction** (`1.4.48+26.1`, `1.4.48+26.2`, `1.4.48+26.3`) still invoke the obsolete bytecode descriptor `Lnet/dasik/social/api/config/DasikSupportHelper;openKofi(Lnet/minecraft/client/gui/screens/Screen;)V`, which was replaced in `DasikLibrary 1.13.x` (`BL-DASIK-008`) by `DasikSupportHelper.openKofi(Object)` and `ClientSupportScreenHelper.openKofi(Screen)` to prevent Dedicated Server `ClassTweaker` crashes. *(Stage 1 verified that `1.4.48+1.20.1`, `1.4.48+1.21.1`, and `1.4.48+1.21.11` ALREADY emit `DasikSupportHelper;openKofi(Ljava/lang/Object;)V` with 0 ABI errors; per Director decision, `1.20.1`–`1.21.11` remain untouched.)*

Clicking the YACL Ko-fi button in `Natural Reproduction` on `26.1.2`, `26.2`, or `26.3` against `DasikLibrary 1.13.x` throws `NoSuchMethodError` at runtime.

---

## 🔬 2. Architectural Blueprint

1. **Target Files (`v26.1`, `v26.2`, `v26.3`, plus root `src/` sync)**:
   - `src/main/java/net/vanillaoutsider/naturalreproduction/client/YaclScreenHelper.java` (lines `638` and `649`; `602` and `613` in root `src/`).
   - Replace `net.dasik.social.api.config.DasikSupportHelper.openKofi(screen)` with `net.dasik.social.api.client.gui.ClientSupportScreenHelper.openKofi(screen)` (client-only helper inside the `.client.` package).
   - Update `gradle.properties` in `v26.1`, `v26.2`, `v26.3` (and root) from `dasik_library_version=1.11.2` to `dasik_library_version=1.13.2` (already present in `mavenLocal` with `ClientSupportScreenHelper.openKofi(Screen)`).
   - Update `fabric.mod.json` `"dasik-library"` floor from `>=1.8.37` / `>=1.9.0` to `>=1.11.2` per `[PAT-20261022-001]` (`1.11.2` is published on Modrinth/CurseForge and exports `ClientSupportScreenHelper`).
2. **ABI Bytecode Verification**:
   - Add pure-JDK `DataInputStream` constant-pool check in test suite across `v26.1`, `v26.2`, `v26.3` asserting `YaclScreenHelper.class` contains **ZERO** `DasikSupportHelper;openKofi(Lnet/minecraft/client/gui/screens/Screen;)V` references and contains `ClientSupportScreenHelper;openKofi(Lnet/minecraft/client/gui/screens/Screen;)V`.
3. **Rebuild & 5-Point Deploy**:
   - Bump `mod_version` to `1.4.49+26.1`, `1.4.49+26.2`, `1.4.49+26.3`, run `./gradlew.bat test build --no-daemon --console=plain` on each anchor, deploy via 5-Point distribution including ALL Modrinth profiles (`Fabric 26.1.2`, `Fabric 26.2`, `Fabric 26.2 (1)`, `Fabric 26.2 (1) (1)`, `26.2`, `26.3`), and verify `code_auditor.py --check-profiles` clears all `natural-reproduction` errors.

---

## 🛡️ 3. Blast Radius & Multi-Era Parity Table

| Dimension | Risk / Failure Mode | Blast Radius | Mitigation |
| :--- | :--- | :--- | :--- |
| **Dedicated Server Sidedness** | Referencing `ClientSupportScreenHelper` in common code | Server startup crash | `YaclScreenHelper` resides strictly in `.client.` package and is skipped in `NaturalReproductionSidednessContractTest` |
| **World Saves / Breeding AI** | None | Zero | Touches only Ko-fi button click lambda in `YaclScreenHelper.java` |
| **Unpublished Library Floor (`[PAT-20261022-001]`)** | Pinning `fabric.mod.json` to unreleased `1.13.2` | FabricLoader crash on CurseForge/Modrinth users | Pin `>=1.11.2` (live public release that already exposes `ClientSupportScreenHelper`) while compiling against `mavenLocal` `1.13.2` |
| **Legacy Anchors (`1.20.1`–`1.21.11`)** | Unnecessary churn on already-clean anchors | Zero | Stage 1 verified `1.4.48+1.20.1/1.21.1/1.21.11` emit `openKofi(Object)V`; excluded per Director A2 |

---

## 🚫 4. Anti-Goals (Creep Shield)

1. ❌ **Do NOT migrate `YaclScreenHelper` to `UniversalConfigScreen`** — keep config screen migration isolated to its own ticket.
2. ❌ **Do NOT modify gestation, genetics, breeding AI, or GameRule logic**.
3. ❌ **Do NOT bundle `BL-NR-016`, `BL-NR-017`, `BL-NR-019`, or Anchor 7 (`BL-NR-014`)**.

---

## 🏁 5. 5-Pillar Verification Checklist

- [x] **Pillar 1 (Fuzz)**: Existing Natural Reproduction fuzz & math tests pass without regression.
- [x] **Pillar 2 (Concurrency)**: Existing concurrent tests pass without regression.
- [x] **Pillar 3 (GUI & Translation)**: `YaclScreenHelper` Ko-fi button click lambdas verified against `ClientSupportScreenHelper` / `Object` overload.
- [x] **Pillar 4 (State / Bytecode Contract)**: `.class` constant-pool test asserts `0` occurrences of `DasikSupportHelper;openKofi(Screen)V`.
- [x] **Pillar 5 (Sidedness & Profile Audit)**: `code_auditor.py --check-profiles` reports `0` `natural-reproduction` ABI findings across all Modrinth profiles.
