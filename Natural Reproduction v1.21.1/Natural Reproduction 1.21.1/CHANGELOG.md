# Changelog - Natural Reproduction (MC 1.21.1)

All notable changes to **Natural Reproduction** for Minecraft 1.21.1 are documented in this file.

## [1.4.34+1.21.1] - 2026-09-28

### Added & Verified
- **Lineage & Genetics Headless Verification Suite**:
  - Implemented automated test assertions for 3-generation pedigree kinship matching, inbreeding progression (Tiers 0-4), gradual generational dilution, and hybrid vigor scaling (+15%).
- **Drop Degradation & Codec Serialization**:
  - Verified Tier 3/4 drop degradation, secondary yield suppression (75% cut), rotten flesh/bone conversion, and deterministic pedigree codec roundtrip.
- **Multi-Era Parity Lockstep**:
  - Synchronized across all studio anchors with full Java 21 bytecode compatibility.

## [1.4.31+1.21.1] - 2026-09-27

### Added & Multi-Era Compatibility
- **Minecraft 1.21.1 Transitional Early Port**:
  - Full compatibility and dedicated standalone JAR for Minecraft 1.21.1.
  - Native support for 1.21.1 animal genetics, size scaling, and environmental breeding triggers.
  - Integration with Minecraft 1.21.1 DataComponents and native Attributes.SCALE.
  - Java 21 bytecode and Fabric Loom 1.10.2 tooling.
- **DasikLibrary 1.1.0+1.21.1 Integration**:
  - Powered by the dedicated DasikLibrary 1.1.0+1.21.1 runtime for autonomous breeding, genetics, and dynamic GameRules.
- **Autonomous Reproduction Core Mechanics**:
  - Autonomous breeding across passive livestock with density cap gating.
  - Multi-generational inbreeding degradation and hybrid vigor outcrossing.
  - Pasture enrichment, feeding troughs, and rotational grazing wear.
  - Pregnancy gestation timers and prenatal pasture nourishment.
  - Herd social cohesion, alpha leadership election, and coordinated stampede flight.
  - Zero-allocation spatial partitioning and staggered tick modulo throttling.
