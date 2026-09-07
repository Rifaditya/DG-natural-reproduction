# 💻 In-Game Commands & Breeding Tracker Logs

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **リポジトリソースコード免責事項**: このWikiのドキュメントは、**リポジトリ内の現在のソースコード状態**（`v1.3.4+26.2`）を反映しています。CurseForgeやModrinthの公式公開ビルドに先行する開発中の最新コミットや機能が含まれる場合があります。

---

## 📋 Command Infobox

| Property | Value |
| :--- | :--- |
| **Root Command** | `/naturalreproduction` |
| **Permission Level Required** | Level 2 (OP / Cheats Enabled) |
| **Command Engine** | Vanilla Brigadier Command Node System |
| **Primary Class** | `NaturalReproductionCommand.java` |
| **Diagnostics Logger** | `BreedingTrackerLogger.java` |

---

## 🌳 Brigadier Command Syntax Tree

```text
/naturalreproduction
  ├── help
  ├── status
  ├── get <gamerule>
  ├── set <gamerule> <value>
  ├── reset
  ├── reload
  └── trackerlogs
        ├── list
        ├── enable
        ├── disable
        └── clear
```

---

## 📖 Command Reference & Operational Examples

### 1. General Administration & Status

#### `/naturalreproduction help`
* **Description**: Lists all available commands and explains their basic usage.
* **Example Output**: Prints formatted help list to chat.

#### `/naturalreproduction status`
* **Description**: Prints a real-time diagnostic summary of active simulation settings, active density caps, scale bounds, and master toggle states.
* **Example**:
  ```text
  [NaturalReproduction] === Simulation Status ===
  • Master Enabled: true
  • Breeding Rate: 24000 ticks (1 MC Day)
  • Density Cap: 10 entities / 16 blocks
  • Scale Bounds: 0.50x - 1.30x (Loot Scaling: ON)
  • Inbreeding Degradation: ON | Pasture Enrichment: ON
  ```

#### `/naturalreproduction reset`
* **Description**: Restores all `natural-reproduction:*` GameRules to default baseline values for the current world.

#### `/naturalreproduction reload`
* **Description**: Dynamically re-scans all loaded chunks, updates scale attributes on existing animals, and reapplies active modifier clamps without requiring a server reboot.

---

### 2. Live GameRule Get / Set

#### `/naturalreproduction get <gamerule>`
* **Description**: Inspects the live runtime value of any Natural Reproduction setting.
* **Example**: `/naturalreproduction get min_scale` &rarr; `min_scale = 50 (0.50x)`

#### `/naturalreproduction set <gamerule> <value>`
* **Description**: Updates a setting live in the current world with instant effect.
* **Examples**:
  - `/naturalreproduction set min_scale 60` &rarr; Sets minimum scale floor to 0.60x.
  - `/naturalreproduction set density_cap 15` &rarr; Expands density limit to 15 mobs.
  - `/naturalreproduction set inbreeding_degradation false` &rarr; Disables inbreeding decay.

---

### 3. Breeding Tracker Logger Commands

The **Breeding Tracker Logger** records autonomous reproduction events in memory, capturing the in-game day, species, world coordinates, biome, offspring scale, and pasture enrichment status.

#### `/naturalreproduction trackerlogs list`
* **Description**: Displays the most recent 10 autonomous reproduction events recorded on the server.
* **Example Output**:
  ```text
  [TrackerLog #14] Day 42 (11:30) | Cow @ [X: 124, Y: 68, Z: -310] (Plains) | Scale: 1.15x | Well-Nourished: YES
  [TrackerLog #15] Day 42 (14:15) | Wolf @ [X: -450, Y: 72, Z: 890] (Taiga) | Scale: 1.00x | Variant: Black Wolf
  ```

#### `/naturalreproduction trackerlogs enable` / `disable`
* **Description**: Toggles real-time autonomous event tracking and console logging.

#### `/naturalreproduction trackerlogs clear`
* **Description**: Clears the historical event tracker buffer.

---

> ☕ **開発サポート**: このMODを気に入っていただけましたら、ぜひ [Ko-fi](https://ko-fi.com/rifaditya) で作者をご支援いただき、最新テストビルドをいち早くお試しください！

---

## 🔗 Related Documentation
* [[Namespaced GameRules & Configuration|ja_jp-GameRules-and-Configuration]]
* [[Technical Architecture & Mixin Integration|ja_jp-Architecture-and-Mixins]]
* Return to [[Home Portal|ja_jp-Home]]
