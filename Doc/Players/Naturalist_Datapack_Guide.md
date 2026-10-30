# 🌿 Naturalist Compatibility & Breeding Control Datapack Guide

Welcome to the official compatibility and configuration guide for running **Naturalist** alongside **Natural Reproduction**.

If you are playing with both mods on current public releases, this universal vanilla datapack provides immediate, fine-grained control over autonomous breeding for all 47 Naturalist species without requiring a mod binary update.

---

## ⚙️ How the Datapack Works

On current public releases of **Natural Reproduction**, any modded entity extending Minecraft's vanilla `Animal` class can trigger autonomous reproduction checks if nearby adult partners exist. Because ambient and wild creatures in **Naturalist** (such as snails, fireflies, caterpillars, butterflies, ducks, and bears) inherit vanilla animal behaviors without native habitat restrictions, they can breed unchecked in high numbers, leading to entity crowding and FPS drops.

This datapack solves the issue immediately through two complementary layers:

1. **Active `Fast-Fail 1` Cooldown Loop (Current Releases)**:
   The datapack runs a lightweight scheduled function loop every 100 ticks (5 seconds). Whenever an adult Naturalist mob belonging to a disabled category is ready to breed (`nbt={Age:0}`), the loop applies `Age: 600` (a 30-second breeding cooldown).
   Under **Natural Reproduction**'s core engine, `AnimalBreedingMixin` evaluates **Fast-Fail 1**:
   ```java
   if (self.getAge() != 0 || self.isInLove()) {
       return;
   }
   ```
   Because `Age != 0`, the entity instantly aborts out of the autonomous breeding tick before performing spatial searches, block scanning, or mate pairing.
   - **Zero Baby Interference**: Growing juveniles (`Age < 0`) are ignored and mature normally.
   - **Negligible Server Load**: Runs only once every 5 seconds via vanilla command scheduling.

2. **Forward-Compatibility with Native Entity Tags (Upcoming Releases)**:
   The datapack includes pre-populated `#natural-reproduction:excluded_animals` and `#natural-reproduction:included_animals` entity type tags across both MC 1.20.1 (`tags/entity_types/`) and MC 1.21.1+ (`tags/entity_type/`) paths. When the upcoming mod update arrives with native tag exclusion, these tags will be recognized natively by the engine with zero additional configuration needed.

---

## 📥 Installation Instructions

The datapack is packaged as a ready-to-drop universal `.zip` archive:
`Doc/Players/Datapacks/Natural-Reproduction-Naturalist-Control.zip`

> [!IMPORTANT]
> ⚠️ **Must be placed inside your World Save folder!**
> Do **NOT** put this `.zip` into your main launcher or profile root folder (e.g. `ModrinthApp/profiles/<profile>/datapacks` or `.minecraft/datapacks`). In Minecraft, the game engine **only** loads datapacks from inside each specific world's `saves/<WorldName>/datapacks/` folder.

### Singleplayer Worlds (Vanilla, Modrinth App, CurseForge, Prism)

#### Option 1: Directly from the Minecraft Title Screen (Easiest)
1. Open Minecraft and click **Singleplayer**.
2. Select your world in the list and click **Edit** (at the bottom).
3. Click **Open World Folder**.
4. Open the **`datapacks`** folder inside.
5. Drop `Natural-Reproduction-Naturalist-Control.zip` straight into this folder.
6. Open your world (or type `/reload` if you are already in-game).

#### Option 2: Via File Explorer
- **Vanilla Launcher**: `%appdata%\.minecraft\saves\<YourWorld>\datapacks\`
- **Modrinth App**: `%appdata%\ModrinthApp\profiles\<ProfileName>\saves\<YourWorld>\datapacks\`
- **CurseForge App**: `%userprofile%\curseforge\minecraft\Instances\<ProfileName>\saves\<YourWorld>\datapacks\`
- **Prism / MultiMC**: `<InstanceFolder>\.minecraft\saves\<YourWorld>\datapacks\`

Type `/reload` in chat to activate. You will see a golden welcome message confirming **Natural Reproduction - Naturalist Compatibility Datapack** is active.

### Dedicated Multiplayer Servers
1. Navigate to your Minecraft server root directory.
2. Open your active world's datapack folder: `<server_root>/world/datapacks/` (or `<server_root>/<YourWorldName>/datapacks/`).
3. Place `Natural-Reproduction-Naturalist-Control.zip` into that `datapacks/` folder.
4. Run `/reload` from the in-game chat (if OP) or `reload` directly in the server console.

---

## 🎮 In-Game Interactive Menu Reference

Once installed, open the interactive control menu at any time by running:

```mcfunction
/function nr_naturalist:menu
```

The menu prints a clickable dashboard directly into chat:

```text
=== [ Naturalist Breeding Control ] ===
Configure autonomous breeding for 47 Naturalist mobs:

Quick Actions:
 * [DISABLE ALL (47 MOBS)]  [ENABLE ALL]
 * [DISABLE AMBIENT]  [THIN OUT EXCESS MOBS (FPS SAVER)]

Category Presets:
 * Ambient (7 mobs):   [OFF] [ON] (snails, butterflies, fireflies, etc.)
 * Forest (8 mobs):    [OFF] [ON] (deer, bears, boars, etc.)
 * Savanna (16 mobs):  [OFF] [ON] (elephants, lions, zebras, etc.)
 * Aquatic (16 mobs):  [OFF] [ON] (alligators, ducks, sharks, etc.)
=======================================
```

### Quick Actions
- **`[DISABLE ALL (47 MOBS)]`**: Instantly disables autonomous reproduction for all 47 Naturalist creatures across all biomes.
- **`[ENABLE ALL]`**: Re-enables natural reproduction for all Naturalist creatures.
- **`[DISABLE AMBIENT]`**: Specifically disables reproduction for rapid-multiplying ambient creatures (snails, butterflies, moths, dragonflies, fireflies, caterpillars, and blue snails).
- **`[THIN OUT EXCESS MOBS (FPS SAVER)]`**: Runs `/function nr_naturalist:cull_excess` to thin out wild populations and restore FPS immediately.

### Category Presets
Click **`[OFF]`** or **`[ON]`** next to any category to toggle breeding for that specific ecological niche:
- **Ambient (7 species)**: Snails, butterflies, moths, dragonflies, fireflies, caterpillars, and blue snails.
- **Forest (8 species)**: Deer, bears, boars, birds, cardinals, bluejays, robins, and canaries.
- **Savanna (16 species)**: Elephants, lions, giraffes, zebras, rhinos, vultures, ostriches, hyenas, hippos, termites, cheetahs, and gazelles.
- **Aquatic & Semi-Aquatic (16 species)**: Ducks, alligators, tortoises, lizards, frogs, sharks, bass, catfish, and marine wildlife.

---

## 🧹 Emergency FPS Recovery (`/function nr_naturalist:cull_excess`)

If your world has already suffered severe lag from runaway animal populations before installing this datapack, use:

```mcfunction
/function nr_naturalist:cull_excess
```

### Safety Features
- **Strict Custom Name Protection**: Any animal named with a Name Tag is completely ignored (`unless data entity @s CustomName`).
- **Strict Tamed Pet Protection**: Tamed pets and animals are not targeted.
- **Adult-Only Culling**: Only targets wild adult entities currently marked as disabled (`Age: 600`).
- **Batch Capped (Max 25)**: Safely limits removal to a maximum of 25 random wild mobs per command execution to avoid game freeze or sudden world empties. If you have hundreds of runaway mobs, simply run the command a few times until your FPS recovers.

---

## 🛠️ Manual JSON Customization

If you prefer editing configuration files directly instead of using in-game commands:

1. Unzip `Natural-Reproduction-Naturalist-Control.zip` (or edit the files inside the archive).
2. Open `data/nr_naturalist/tags/entity_types/disabled_mobs.json` (or `tags/entity_type/disabled_mobs.json` on 1.21+).
3. Add or remove any entity ID from the list. Every entry uses `"required": false` so removing or modifying entries will never cause missing entity crashes:
   ```json
   {
     "replace": false,
     "values": [
       { "id": "naturalist:snail", "required": false },
       { "id": "naturalist:butterfly", "required": false }
     ]
   }
   ```
4. Save the file and run `/reload` in your Minecraft world.

---

## 💬 Ready-to-Send Discord Reply for Muqington

Copy and paste the exact message below to reply to Muqington's issue report on Discord:

***

Hey Muqington! Thanks so much for bringing this to my attention.

I investigated what was going on under the hood: Naturalist creatures extend vanilla's `Animal` class, so on current public releases of Natural Reproduction, they participate in autonomous breeding checks. Because ambient creatures like snails, butterflies, and fireflies don't have natural habitat constraints, they can multiply rapidly and hit your FPS pretty hard.

I've put together a universal, zero-mod compatibility datapack that you can drop straight into your world right now to solve this without having to wait for the next mod release:

1. Download **`Natural-Reproduction-Naturalist-Control.zip`** (attached below).
2. Drop the `.zip` file into your specific world's **`datapacks`** folder:
   - *Easiest way*: In Minecraft, click **Singleplayer** ➔ click your world ➔ click **Edit** (bottom) ➔ click **Open World Folder** ➔ open the **`datapacks`** folder inside and drop the `.zip` right there.
   *(Make sure it's inside your world's `saves/<YourWorld>/datapacks/` folder and not the root launcher folder, since Minecraft only loads datapacks per world).*
3. In-game, type `/reload`.
4. Run `/function nr_naturalist:menu` in chat. It opens a clickable menu where you can disable breeding for all Naturalist mobs or toggle specific categories (like Ambient, Forest, Savanna, or Aquatic).
5. If your world already has hundreds of extra mobs causing lag, click **`[THIN OUT EXCESS MOBS (FPS SAVER)]`** in the menu (or run `/function nr_naturalist:cull_excess`). It safely clears up to 25 wild disabled adult mobs per click while strictly protecting your named animals and tamed pets.

I've also already implemented native dynamic GameRules and `#natural-reproduction:excluded_animals` tag support into the mod codebase for the upcoming release, so in the future you'll have native in-game toggles built directly into the mod as well.

Give the datapack a spin and let me know if it brings your framerates right back to normal!

***
