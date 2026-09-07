# Version Compatibility Matrix

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **Avis de non-responsabilité relatif au code source du dépôt** : La documentation de ce Wiki reflète **l'état actuel du code source dans le dépôt** (`v1.3.4+26.2`), qui peut inclure des commits récents non publiés ou des fonctionnalités de développement en avance sur les versions publiques de CurseForge et Modrinth.

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

> ☕ **Soutenir le développement** : Si vous appréciez ce mod, soutenez l'auteur sur [Ko-fi](https://ko-fi.com/rifaditya) pour des versions de test préliminaires !

---

## 🔙 Navigation

- [[Developer Setup & Building|fr_fr-Developer-Setup-and-Building]]
- [[Technical Architecture & Mixins|fr_fr-Architecture-and-Mixins]]
- Return to [[Home Portal|fr_fr-Home]]
