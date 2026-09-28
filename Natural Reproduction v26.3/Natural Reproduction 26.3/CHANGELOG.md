# Changelog - Natural Reproduction (MC 26.3)

All notable changes to **Natural Reproduction** for Minecraft 26.3 are documented in this file.

## [1.4.33+26.3] - 2026-09-28

### Added & Verified
- **Drop Degradation & Pedigree Codec Serialization Determinism**:
  - Full automated headless test assertions for Tier 3 and 4 prime meat conversion to Rotten Flesh and Bones (50/50 ratio, preserving volume).
  - Verified 75% secondary yield cuts for leather, wool, feathers, and rabbit hides on degraded livestock.
  - Validated deterministic roundtrip serialization for `nr_father:<UUID>` pedigree entity tags with malformed/null resilience.
  - Implemented deterministic codec encode/decode assertions for `MockGeneticsRecord` pedigree states across inbred and wild livestock.

## [1.4.32+26.3] - 2026-09-28

### Added & Verified
- **Lineage & Pedigree Kinship Architecture Verification**:
  - Full automated headless test coverage for 3-generation pedigree kinship matching and inbreeding progression across Tiers 0 through 4.
  - Verified gradual generational dilution (-1 tier per outcross) and hybrid vigor recovery scaling (+15% scale boost for clean outcrosses from degraded stock).
  - Validated multi-generational scale stunting, movement speed penalties, and cyclic pedigree graph recursion guards.

## [1.4.31+26.3] - 2026-09-26

### Added & Modernized
- **Minecraft 26.3 Release Compatibility**:
  - Full compatibility and stability upgrades for the official Minecraft 26.3 release.
  - Seamlessly integrates with the modernized Fabric loader and tooling ecosystems.
- **DasikLibrary 1.9.2 Integration**:
  - Powered by the latest DasikLibrary 1.9.2 runtime for animal genetics tracking, dynamic GameRule synchronizations, and lifecycle management.
- **Enhanced Runtime Guard**:
  - Upgraded built-in version protection to verify clean classloading and prevent world save incompatibilities on newer Minecraft drops.
- **Autonomous Reproduction Core Mechanics**:
  - Autonomous breeding across passive livestock with density cap gating.
  - Multi-generational inbreeding degradation and hybrid vigor outcrossing.
  - Pasture enrichment, feeding troughs, and rotational grazing wear.
  - Pregnancy gestation timers and prenatal pasture nourishment.
  - Herd social cohesion, alpha leadership election, and coordinated stampede flight.
  - Zero-allocation spatial partitioning and staggered tick modulo throttling.
