# Version Compatibility Matrix

> [!NOTE]
> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository** (`v1.3.4+26.2`), which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

This document outlines version compatibility, dependency bounds, and lifecycle policies for **Natural Reproduction**.

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

For developer setup details, see [[Developer Setup & Building|Developer-Setup-and-Building]].
