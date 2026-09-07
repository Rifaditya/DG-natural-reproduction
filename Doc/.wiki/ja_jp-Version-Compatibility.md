# Version Compatibility Matrix

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **リポジトリソースコード免責事項**: このWikiのドキュメントは、**リポジトリ内の現在のソースコード状態**（`v1.3.4+26.2`）を反映しています。CurseForgeやModrinthの公式公開ビルドに先行する開発中の最新コミットや機能が含まれる場合があります。

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

> ☕ **開発サポート**: このMODを気に入っていただけましたら、ぜひ [Ko-fi](https://ko-fi.com/rifaditya) で作者をご支援いただき、最新テストビルドをいち早くお試しください！

---

## 🔙 Navigation

- [[Developer Setup & Building|ja_jp-Developer-Setup-and-Building]]
- [[Technical Architecture & Mixins|ja_jp-Architecture-and-Mixins]]
- Return to [[Home Portal|ja_jp-Home]]
