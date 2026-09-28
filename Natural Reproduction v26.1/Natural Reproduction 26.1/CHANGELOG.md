# Changelog - Natural Reproduction (MC 26.1)

All notable changes to **Natural Reproduction** for Minecraft 26.1 are documented in this file.

## [1.4.37+26.1] - 2026-09-28

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

## [1.4.34+26.1] - 2026-09-28

### Added & Verified
- **Lineage & Genetics Headless Verification Suite**:
  - Implemented automated test assertions for 3-generation pedigree kinship matching, inbreeding progression (Tiers 0-4), gradual generational dilution, and hybrid vigor scaling (+15%).
- **Drop Degradation & Codec Serialization**:
  - Verified Tier 3/4 drop degradation, secondary yield suppression (75% cut), rotten flesh/bone conversion, and deterministic pedigree codec roundtrip.
- **Multi-Era Parity Lockstep**:
  - Synchronized across all studio anchors with full Java 25 bytecode compatibility.

## [1.4.31+26.1] - 2026-09-27

### Added & Modernized
- **Minecraft 26.1 Modern Sovereign Port**:
  - Full compatibility and dedicated standalone JAR for Minecraft 26.1 / 26.1.2.
  - Native support for Minecraft 26.1 entity types and dynamic GameRules.
  - Java 25 bytecode and Fabric Loom 1.15 tooling.
- **DasikLibrary 1.8.37 Integration**:
  - Powered by the dedicated DasikLibrary 1.8.37 runtime for autonomous breeding, genetics, and dynamic GameRules.
- **Autonomous Reproduction Core Mechanics**:
  - Autonomous breeding across passive livestock with density cap gating.
  - Multi-generational inbreeding degradation and hybrid vigor outcrossing.
  - Pasture enrichment, feeding troughs, and rotational grazing wear.
  - Pregnancy gestation timers and prenatal pasture nourishment.
  - Herd social cohesion, alpha leadership election, and coordinated stampede flight.
  - Zero-allocation spatial partitioning and staggered tick modulo throttling.
