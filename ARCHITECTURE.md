# Architecture & Symbol Index: Natural Reproduction

## 1. Mod Metadata & Entrypoint
- **Mod ID**: `natural-reproduction`
- **Main Entrypoint**: `net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric` (`net.fabricmc.api.ModInitializer`)
- **Client Entrypoint**: `None`

## 2. Bytecode Mixin Target Registry
| Target Vanilla Class | Mixin Class | Purpose |
| :--- | :--- | :--- |
| `Vanilla Class` | `net.vanillaoutsider.naturalreproduction.mixin.AnimalBreedingMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.naturalreproduction.mixin.AnimalDropScaleMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.naturalreproduction.mixin.ThrownEggMixin` | Core mixin hook |

## 3. Core Mechanics & Subsystems
- **Source Root**: `src/main/java/`
- **Resource Root**: `src/main/resources/`

## 4. Dynamic GameRules & Commands
- **GameRules / Commands**: Configured dynamically via namespaced keys (`natural-reproduction:*`).

## 5. Configuration & Sidedness Isolation
- **Sidedness**: Server-safe logic in main, client isolated in `src/client/java` or client entrypoint.
