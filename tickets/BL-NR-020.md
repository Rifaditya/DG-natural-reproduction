# 🎫 [BL-NR-020] Fix `DasikSupportHelper.openKofi(Screen)` ABI Mismatch Across All Anchors (`YaclScreenHelper`)

- **Ticket ID**: `[BL-NR-020]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: `All Active Anchors (1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3)`
- **Scope**: `YaclScreenHelper.java` (lines `638`, `649` across all anchors) + Client Sidedness/Bytecode ABI verification test
- **Status**: `📌 DEFERRED`
- **Grilling Status**: `✅ PASSED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`
- **Umbrella Reference**: [`BL-WS-051`](file:///E:/Minecraft%20Project/tickets/BL-WS-051.md)
- **Date Added**: `2026-11-25`

---

## 🎯 1. Problem Statement & Player Impact

`code_auditor.py --check-profiles` and hub archive linkage scanning detected that compiled JARs of **Natural Reproduction** (`1.4.48+26.1`, `1.4.48+26.2`, `1.4.48+26.3`) still invoke the obsolete bytecode descriptor `Lnet/dasik/social/api/config/DasikSupportHelper;openKofi(Lnet/minecraft/client/gui/screens/Screen;)V`, which was replaced in `DasikLibrary 1.13.x` (`BL-DASIK-008`) by `DasikSupportHelper.openKofi(Object)` and `ClientSupportScreenHelper.openKofi(Screen)` to prevent Dedicated Server `ClassTweaker` crashes.

Clicking the YACL Ko-fi button in `Natural Reproduction` on `26.1.2`, `26.2`, or `26.3` against `DasikLibrary 1.13.x` throws `NoSuchMethodError` at runtime.

---

## 🔬 2. Architectural Blueprint

1. **Target Files (`1.20.1`, `1.21.1`, `1.21.11`, `26.1`, `26.2`, `26.3`)**:
   - `src/main/java/net/vanillaoutsider/naturalreproduction/client/YaclScreenHelper.java` (lines `638` and `649`).
   - Replace `net.dasik.social.api.config.DasikSupportHelper.openKofi(screen)` with `net.dasik.social.api.client.gui.ClientSupportScreenHelper.openKofi(screen)` (or `DasikSupportHelper.openKofi((Object) screen)` if `ClientSupportScreenHelper` is absent on an older anchor — Stage 1 verifies exact `dasik-library` version per anchor).
2. **ABI Bytecode Verification**:
   - Add pure-JDK `DataInputStream` constant-pool check in test suite asserting `YaclScreenHelper.class` contains **ZERO** `DasikSupportHelper;openKofi(Lnet/minecraft/client/gui/screens/Screen;)V` references.
3. **Rebuild & 5-Point Deploy**:
   - Bump `mod_version` by 1 patch step across active anchors, run `./gradlew.bat test build --no-daemon --console=plain`, deploy via 5-Point distribution (including Modrinth profiles), and verify `code_auditor.py --check-profiles` clears all `natural-reproduction` errors.

---

## 🛡️ 3. Blast Radius & Multi-Era Parity Table

| Dimension | Risk / Failure Mode | Blast Radius | Mitigation |
| :--- | :--- | :--- | :--- |
| **Dedicated Server Sidedness** | Referencing `ClientSupportScreenHelper` in common code | Server startup crash | `YaclScreenHelper` resides strictly in `.client.` package with `@Environment(EnvType.CLIENT)` |
| **World Saves / Breeding AI** | None | Zero | Touches only Ko-fi button click lambda in `YaclScreenHelper.java` |
| **Older Anchors (`1.20.1`–`1.21.11`)** | `ClientSupportScreenHelper` availability check | Compile error if version pin lags | Stage 1 checks each anchor's `dasik_library_version`; fall back to `DasikSupportHelper.openKofi((Object) screen)` or bump pin |

---

## 🚫 4. Anti-Goals (Creep Shield)

1. ❌ **Do NOT migrate `YaclScreenHelper` to `UniversalConfigScreen`** — keep config screen migration isolated to its own ticket.
2. ❌ **Do NOT modify gestation, genetics, breeding AI, or GameRule logic**.
3. ❌ **Do NOT bundle `BL-NR-016`, `BL-NR-017`, `BL-NR-019`, or Anchor 7 (`BL-NR-014`)**.

---

## 🏁 5. 5-Pillar Verification Checklist

- [ ] **Pillar 1 (Fuzz)**: Existing Natural Reproduction fuzz & math tests pass without regression.
- [ ] **Pillar 2 (Concurrency)**: Existing concurrent tests pass without regression.
- [ ] **Pillar 3 (GUI & Translation)**: `YaclScreenHelper` Ko-fi button click lambdas verified against `ClientSupportScreenHelper` / `Object` overload.
- [ ] **Pillar 4 (State / Bytecode Contract)**: `.class` constant-pool test asserts `0` occurrences of `DasikSupportHelper;openKofi(Screen)V`.
- [ ] **Pillar 5 (Sidedness & Profile Audit)**: `code_auditor.py --check-profiles` reports `0` `natural-reproduction` ABI findings across all Modrinth profiles.
