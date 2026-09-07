# Version Compatibility Matrix

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **Pernyataan Sumber Kode Repositori**: Dokumentasi dalam Wiki ini mencerminkan **status kode sumber saat ini di repositori** (`v1.3.4+26.2`), yang mungkin mencakup komit terbaru yang belum dirilis atau fitur pengembangan mendahului rilis publik di CurseForge dan Modrinth.

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

> ☕ **Dukungan Pengembangan**: Jika Anda menikmati mod ini, dukung kreator di [Ko-fi](https://ko-fi.com/rifaditya) untuk akses build pengembangan awal!

---

## 🔙 Navigation

- [[Developer Setup & Building|id_id-Developer-Setup-and-Building]]
- [[Technical Architecture & Mixins|id_id-Architecture-and-Mixins]]
- Return to [[Home Portal|id_id-Home]]
