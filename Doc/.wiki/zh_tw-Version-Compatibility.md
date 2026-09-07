# Version Compatibility Matrix

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **代碼倉庫源碼狀態免責聲明**：本 Wiki 文件反映了代碼倉庫中的**當前源碼狀態**（`v1.3.4+26.2`）。可能包含領先於 CurseForge 與 Modrinth 公開發布版本的最新未發布提交或開發特性。

---

## 📊 Compatibility Matrix

| Minecraft Version | Mod Release Tag | DasikLibrary Bound | Fabric Loader Bound | Support Status |
| :--- | :--- | :--- | :--- | :--- |
| **MC 26.2** | `v1.3.4+26.2` | `>= 1.8.15` | `>= 0.19.1` | **Active Mainline** |

---

## 🔒 1 Jar 1 Version Policy

Natural Reproduction strictly enforces the **1 Jar 1 Version** architectural law:
- Every compiled release artifact is built against a dedicated single target directory (e.g. `Natural Reproduction 26.2`).
- Cross-version reflection hacks and runtime version branching inside the main jar are strictly prohibited.
- Dependency bounds in `fabric.mod.json` use open-ended lower bounds (`"minecraft": ">=26.2-"`) to prevent loader locks during minor point releases.

---

> ☕ **支持模組開發**：如果您喜歡本模組，歡迎前往 [Ko-fi](https://ko-fi.com/rifaditya) 支持作者獲取最新開發測試組建！

---

## 🔙 Navigation

- [[Developer Setup & Building|zh_tw-Developer-Setup-and-Building]]
- [[Technical Architecture & Mixins|zh_tw-Architecture-and-Mixins]]
- Return to [[Home Portal|zh_tw-Home]]
