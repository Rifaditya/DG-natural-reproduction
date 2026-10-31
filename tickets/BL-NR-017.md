# 🎫 [BL-NR-017] Jade & WTHIT HUD Tooltip Gestation & Pregnancy Display Integration

- **Ticket ID**: `[BL-NR-017]`
- **Category**: `[FEATURE]` / `[REFINEMENT]`
- **Priority**: `[HIGH]`
- **Scope**: `All Anchors (1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3, 26.x)`
- **Status**: `📌 DEFERRED`
- **Target Repository / Root**: `Delayed Gratification Collection/Natural Reproduction`
- **Design Philosophy**: Delayed Gratification (DG) — High-clarity agricultural telemetry through standard community HUD tooltips

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Integrate `Natural Reproduction` with popular entity tooltip HUD mods (**Jade** and **WTHIT**) across all 7 version anchors to display real-time pregnancy status and gestation countdowns directly on the entity inspection overlay (matching the format of vanilla's `Breeding CD: Xm Ys`).
2. **In-Game Experience (Before vs. After)**:
   - *Before*: Inspecting an animal with Jade or WTHIT only shows its health and vanilla breeding cooldown (`Breeding CD: 3m 54s`). Even if an animal is carrying offspring under the mod's gestation system, the tooltip provides zero pregnancy information.
   - *After*: When aiming at a pregnant animal, the tooltip dynamically appends a clear, localized indicator line:
     ```text
     Cow
     ❤❤❤❤❤
     Breeding CD: 3m 54s
     Pregnant: 18m 20s
     Minecraft
     ```
     Showing the exact remaining gestation time until delivery.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Do not make Jade or WTHIT hard/mandatory dependencies; integration must be 100% optional and soft-gated via Fabric entrypoints.
   - ❌ Do not crash dedicated servers or headless clients when Jade/WTHIT are absent.
   - ❌ Do not send heavy redundant packets; utilize Jade/WTHIT server data synchronization channels (`IServerDataProvider`).
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [ ] Dedicated Jade plugin (`NaturalReproductionJadePlugin`) implements `IEntityComponentProvider` and `IServerDataProvider<Entity>`.
   - [ ] Dedicated WTHIT plugin (`NaturalReproductionWthitPlugin`) implements `IEntityComponentProvider` and `IServerDataProvider<Entity>`.
   - [ ] When `AnimalGestationHelper.isPregnant(animal) == true`, tooltip appends `Pregnant: <time>` (formatted mm:ss or hh:mm:ss based on remaining ticks).
   - [ ] Declared as optional entrypoints in `fabric.mod.json` (`"jade": [...]`, `"waila": [...]`).
   - [ ] Game boots cleanly and runs without errors in environments without Jade or WTHIT installed.
   - [ ] Localized translation keys added: `tooltip.naturalreproduction.pregnant`.

---

## ❓ 2. Problem Statement & Player Impact

As reported by players and captured in diagnostic screenshots:
1. Community players widely rely on HUD overlay mods like **Jade** and **WTHIT (What The Hell Is That)** to monitor livestock cooldowns and farming metrics.
2. Currently, Jade only inspects vanilla's `Animal.getAge()`:
   - When an animal breeds, vanilla sets `age = 6000` (5 minutes of breeding cooldown), which Jade renders as `Breeding CD: 3m 54s`.
   - However, in `Natural Reproduction`, breeding initiates a **gestation timer** (`gestation_ticks`) on the mother.
3. Because the mod does not supply a tooltip provider, players see the breeding cooldown ticking down, but have no way to know whether the animal is pregnant, how much gestation time remains, or when delivery will happen.

Adding official Jade and WTHIT providers bridges this gap seamlessly, giving players instant HUD clarity.

---

## 🔬 3. Architectural Blueprint & Target Classes

### Server Data Sync (`NaturalReproductionJadePlugin.java`)
```java
public class NaturalReproductionJadePlugin implements IWailaPlugin, IEntityComponentProvider, IServerDataProvider<Entity> {

    public static final ResourceLocation PREGNANCY_DATA = new ResourceLocation("natural-reproduction", "pregnancy");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(this, Animal.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(this, Animal.class);
    }

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, Entity entity, boolean showDetails) {
        if (entity instanceof Animal animal && AnimalGestationHelper.isPregnant(animal)) {
            float ticks = DasikAnimalGeneticsAPI.getTrait(animal, "gestation_ticks", 0.0f);
            data.putFloat("NR_GestationTicks", ticks);
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (data.contains("NR_GestationTicks")) {
            float ticks = data.getFloat("NR_GestationTicks");
            int totalSeconds = (int) (ticks / 20.0f);
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            
            String timeStr = minutes > 60 
                ? String.format("%dh %dm", minutes / 60, minutes % 60)
                : String.format("%dm %02ds", minutes, seconds);

            tooltip.add(Component.translatable("tooltip.naturalreproduction.pregnant", timeStr)
                .withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return PREGNANCY_DATA;
    }
}
```

### Soft-Gated Registration (`fabric.mod.json`)
```json
"entrypoints": {
  "main": [
    "net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric"
  ],
  "jade": [
    "net.vanillaoutsider.naturalreproduction.compat.jade.NaturalReproductionJadePlugin"
  ],
  "waila": [
    "net.vanillaoutsider.naturalreproduction.compat.wthit.NaturalReproductionWthitPlugin"
  ]
}
```

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

- **MC 1.20.1, 1.21.1, 1.21.11**: Jade API v11/v12 (`snownee.jade.api.*`), WTHIT API v8/v10.
- **MC 26.1.2, 26.2, 26.3, 26.x**: Modern Jade API reflection / compatibility layer.
- **Graceful Fallback**: If HUD mod is not loaded, entrypoint is ignored by Fabric Loader without overhead or crashes.

---

## 🛡️ 5. Edge Cases & Failure Modes

- **Missing Mod Safety**: Compiling against optional APIs or reflection ensures standalone environments without Jade/WTHIT load 100% cleanly.
- **Client/Server Desync**: Using `IServerDataProvider` guarantees that the client always displays the server's authoritative remaining gestation ticks, even if tick speed changes.
- **Localization Fallback**: English default `tooltip.naturalreproduction.pregnant`: `"Pregnant: %s"`.

---

## 🚀 6. Implementation Steps (Code-Level Plan)

1. Pre-step SemVer bump across targeted anchors.
2. Add optional Jade / WTHIT plugin classes under `net.vanillaoutsider.naturalreproduction.compat`.
3. Register `"jade"` and `"waila"` entrypoints in `fabric.mod.json`.
4. Add translation keys to `en_us.json` and studio language catalog.
5. Verify in test environment: inspect pregnant animal with Jade active, verify tooltip shows countdown.
6. Verify headless test suite (`./gradlew test`) passes with zero missing class crashes.

---

## 🏁 7. 5-Pillar Verification Checklist

- [ ] **Pillar 1 (Tooltip String Formatting)**: Assert seconds, minutes, and hours formatting produces clean text without index out of bounds.
- [ ] **Pillar 2 (Reflection & Optional Classloading)**: Confirm standalone game client boots without Jade present.
- [ ] **Pillar 3 (Server Data Payload Security)**: Verify NBT payload size is minimal ($< 16$ bytes per sync).
- [ ] **Pillar 4 (Dynamic Countdown Assertion)**: Assert tooltip timer decreases in lockstep with in-game ticks.
- [ ] **Pillar 5 (Sidedness & Licensing)**: Zero server-side classloader leaks; GPLv3 headers preserved.
