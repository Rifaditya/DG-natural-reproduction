# Changelog - Natural Reproduction (MC 1.20.1)

All notable changes to **Natural Reproduction** for Minecraft 1.20.1 are documented in this file.

## [1.4.46+1.20.1] - 2026-10-29

### Added & Verified
- **Universal Brigadier Command Framework Migration**:
  - Refactored NaturalReproductionCommand to adopt DasikLibrary's ModCommandDispatcher, UniversalCommandRegistry, and GameRuleCommandBridge.
  - Registered all 19 core GameRules and 27 species toggles via unified GameRuleCommandBridge with $O(1)$ key lookup and zero-allocation tab completion.
  - Enforced Freedom Over Anti-Crash saturated bounds arithmetic across all numeric integer GameRules (e.g. density_cap, rate, scale percentages).
  - Maintained 100% subcommand parity (help, status, stats, get, set, reset, reload, purge, trackerlogs, logs) across both /naturalreproduction and /nr root literals.
  - Preempted ERR-20261015-002 using lambda permission predicates (`source -> source.hasPermission(level)`).
  - Wired single registration authority via NaturalReproductionCommand.init() in NaturalReproductionFabric.onInitialize(), completely eliminating manual callback registration.
  - Updated zero-mock headless JUnit test suite (NaturalReproductionCommandTest) asserting UniversalCommandRegistry AST nodes, dynamic suggestion providers, saturated warning status codes, and role-based permission gating.

## [1.4.45+1.20.1] - 2026-10-29

### Added & Verified
- **Pillar 5 Sidedness & Mixin Contract Verification Suite**:
  - Implemented compile-time headless test suite (`NaturalReproductionSidednessContractTest`) verifying strict physical isolation between client and server logic.
  - Asserted zero client imports (`net.minecraft.client.*`, `com.mojang.blaze3d.*`) across all common source files.
  - Asserted zero client constant pool references in common class bytecode (`build/classes/java/main`).
  - Enforced single-line GNU GPLv3 license header verification across all Java source and test files.
  - Validated purged ModVersionGuard reflection hygiene and zero unremapped Class.forName invocations in entrypoints.
  - Verified `natural-reproduction.mixins.json` descriptor integrity and physical class presence.
  - Validated universal environment (`"environment": "*"`) and required entrypoints in `fabric.mod.json`.

## [1.4.44+1.20.1] - 2026-10-12

### Fixed
- Fixed runtime crash caused by redundant reflection checks during mod initialization on dedicated servers and clients.

## [1.4.43+1.20.1] - 2026-09-29

### Added & Verified
- **Pillar 4 In-Engine Fabric GameTest Suite**:
  - Implemented headless in-world GameTest suite (`NaturalReproductionGameTests`) verifying live server reproductive behaviors inside headless server structures.
  - Added `testDensityCapSuppression`: asserts that local populations exceeding `reproductionDensityCap` cleanly suppress natural reproduction.
  - Added `testCrowdedPenStunting`: asserts that offspring in overcrowded pens experience stunting down to `0.10x` physical scale floor.
  - Added `testSpaciousPastureRecovery`: asserts that breeding in enriched pastures (hay bales and water cauldrons) grants scale recovery boosts up to `1.20x`.
- **Loom GameTest Automation Infrastructure**:
  - Configured `loom.runs.gametest` and helper task `task gametest(dependsOn: 'runGametest')` in `build.gradle` using Gradle 9.3 layout API.
  - Registered `"fabric-gametest"` entrypoint in `fabric.mod.json`.

## [1.4.41+1.20.1] - 2026-09-28

### Added & Verified
- **Zero-Mock Headless Brigadier Command Suite**:
  - Implemented comprehensive headless unit test suite (`NaturalReproductionCommandTest`) asserting full Brigadier syntax hierarchy, alias parity, tab-completion suggestions, out-of-range bounds rejection, and OP permission gating.
- **Short Command Alias `/nr` with 100% Tree Parity**:
  - Registered `/nr` as a 1:1 alias for `/naturalreproduction` using modular subtree builders, ensuring identical command capabilities and auto-completion behavior.
- **Role-Based Command Permission Scoping**:
  - Ungated root command to Level 0, enabling unprivileged players to access read-only queries (`/nr help`, `/nr status`, `/nr stats`, `/nr get <rule>`, `/nr logs list`).
  - Enforced strict Level 2 OP gating on modifying and administrative commands (`set`, `reset`, `reload`, `purge`, `trackerlogs enable/disable/clear`).
- **Memory & Cache Diagnostic Commands**:
  - Added `/nr stats` subcommand providing instant visibility into active GameRules, spatial density cache entries, pasture cache entries, and recorded reproduction event counts.
  - Added `/nr purge [caches|logs|all]` subcommand for clearing spatial caches and event logs.

## [1.4.40+1.20.1] - 2026-09-28

### Added & Verified
- **50-Thread Spatial Cache Concurrency Load Simulator**:
  - Implemented headless multi-threaded unit test suite (`SpatialCacheConcurrencyTest`) dispatching 50 concurrent worker threads via dual-latch synchronization (`CountDownLatch`).
  - Executed 50,000 concurrent operations under `CountDownLatch` barrier with zero deadlocks and zero exceptions.
- **Concurrent TTL Eviction & Purge Race Condition Stress Test**:
  - Implemented 3-way asymmetric stress testing pitting 20 readers, 20 writers, and 10 eviction threads against concurrent `removeIf` sweeps with zero `ConcurrentModificationException`.
  - Asserted exact boundary condition behavior where `currentTime == expiryTime` across 500 pre-populated entries with clean eviction of expired keys while preserving future keys.
- **Multi-Era Parity Lockstep**:
  - Synchronized across all studio anchors.

## [1.4.37+1.20.1] - 2026-09-28

### Added & Verified
- **Continuous Stunting Curve & Pasture Recovery Headless Test Suite**:
  - Implemented automated headless fuzz and unit assertions for continuous overcrowding stunting penalties, monotonic non-increasing curve within [0.20f, 0.95f], and severe overcrowding saturation floor (0.20f).
  - Verified spacious pasture recovery scale boost (+15%) with strict ceiling clamping at 1.20f without overshoot.
  - Added negative density exploit protection assertions ensuring negative counts clamp safely to count 0.
  - Asserted scale clamping across confinement boundaries down to the 0.10f minimum allowed limit.
- **10,000-Iteration Chaos Property Fuzzing Engine**:
  - Automated property-based fuzzing injecting NaN, +/-Infinity, astronomical floats, and negative densities with zero exception leaks and saturated clamping.
  - Verified non-crashing fallback resilience and zero NaN/Infinity leaks across raw scale inputs, entity density counts (-1000 to 200), and dynamic bounds.
  - Validated saturated scale clamping within technical bounds [0.05f, 2.0f] for astronomical floats (`±1e38f`, `Float.MAX_VALUE`), subnormal floats (`Float.MIN_VALUE`, `±0.0f`), and arithmetic overflows.
  - Added targeted assertions for direct NaN injection, infinite scale injection (+/-Infinity), and astronomical overflow protection.
- **Multi-Era Parity Lockstep**:
  - Synchronized across all studio anchors.

## [1.4.34+1.20.1] - 2026-09-28

### Added & Verified
- **Lineage & Genetics Headless Verification Suite**:
  - Implemented automated test assertions for 3-generation pedigree kinship matching, inbreeding progression (Tiers 0-4), gradual generational dilution, and hybrid vigor scaling (+15%).
- **Drop Degradation & Codec Serialization**:
  - Verified Tier 3/4 drop degradation, secondary yield suppression (75% cut), rotten flesh/bone conversion, and deterministic pedigree codec roundtrip.
- **Multi-Era Parity Lockstep**:
  - Synchronized across all studio anchors with full Java 17 bytecode compatibility.

## [1.4.31+1.20.1] - 2026-09-28

### Added & Multi-Era Compatibility
- **Minecraft 1.20.1 Legacy Era Port**:
  - Full compatibility and dedicated standalone JAR for Minecraft 1.20.1.
  - Native support for 1.20.1 animal genetics, size scaling, and environmental breeding triggers.
  - Legacy item lore and NBT data support for Fertilized Chicken Eggs.
  - Java 17 bytecode, Fabric Loom 1.10.2 tooling, and Mojang mappings.
- **DasikLibrary 1.1.0+1.20.1 Integration**:
  - Powered by the dedicated DasikLibrary 1.1.0+1.20.1 runtime for autonomous breeding, genetics, and dynamic GameRules.
- **Autonomous Reproduction Core Mechanics**:
  - Autonomous breeding across passive livestock with density cap gating.
  - Multi-generational inbreeding degradation and hybrid vigor outcrossing.
  - Pasture enrichment, feeding troughs, and rotational grazing wear.
  - Pregnancy gestation timers and prenatal pasture nourishment.
  - Herd social cohesion, alpha leadership election, and coordinated stampede flight.
  - Zero-allocation spatial partitioning and staggered tick modulo throttling.
