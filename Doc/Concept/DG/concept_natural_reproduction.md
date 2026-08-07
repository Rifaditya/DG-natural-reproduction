# Concept: Natural Reproduction

> **Philosophy**: Delayed Gratification (DG) - "Pasture Management & Multi-Generational Investment."
> **Core Identity**: Animals in the wild should not depend solely on players for survival and growth. This mod allows passive mobs to reproduce autonomously when environmental conditions are ideal.

## Core Mechanics

### 1. The Breeding Urge
- **Autonomous Love Mode**: Animals have a rare chance (weighted by GameRule) to enter "Love Mode" naturally.
- **Conditions for Success**:
    - **Health**: Must be at full health.
    - **Hunger**: Must have "eaten" recently (e.g., Sheep eating grass, Pigs digging up roots/beetroots).
    - **Proximity**: A compatible partner must be within 8 blocks.
    - **Density Cap**: Will NOT breed if there are more than 10 entities of the same species within a 16-block radius (prevents entity cramming/lag).

### 2. Species-Specific Triggers
- **Cows/Sheep**: Require Grass or Wheat blocks nearby.
- **Pigs**: Require mud or root-based crops.
- **Chickens**: (Handled by *Warm Nests* mod synergy).

## Technical Hooks (Snapshot 26.x)
- `AnimalEntity#mobTick()`: To inject the autonomous breeding logic.
- `DasikLibrary (GroupManager)`: To efficiently track population density and partner proximity.
- `Entity.setLoveTicks()`: To trigger vanilla breeding behavior.

## Configuration (GameRules)
- `naturalReproductionRate`: (Integer, default `24000` - once per MC day per mob).
- `naturalReproductionDensityCap`: (Integer, default `10`).
- `naturalReproductionEnabled`: (Boolean, default `true`).

## Assets Needed
- **Pure Logic**: Entity behavior Mixins.
