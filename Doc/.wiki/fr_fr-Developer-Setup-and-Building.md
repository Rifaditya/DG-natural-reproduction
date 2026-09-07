# Developer Setup & Building from Source

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **Avis de non-responsabilité relatif au code source du dépôt** : La documentation de ce Wiki reflète **l'état actuel du code source dans le dépôt** (`v1.3.4+26.2`), qui peut inclure des commits récents non publiés ou des fonctionnalités de développement en avance sur les versions publiques de CurseForge et Modrinth.

---

## 🛠️ Toolchain & Environment Specifications

| Requirement | Value |
| :--- | :--- |
| **Java JDK** | JDK **25** (Strictly required for MC 26.2 build toolchains) |
| **Gradle** | **9.3+** (`--no-daemon`) |
| **Fabric Loom** | **1.15.5+** |
| **Target Minecraft** | Stable **MC 26.2** |
| **Automated Testing** | `./gradlew test` (JUnit & Loom GameTest framework) |

---

## 💻 Cloning & Building Tagged JARs

### 1. Clone the Repository
```bash
git clone https://github.com/Dasik/Natural-Reproduction.git
cd Natural-Reproduction
```

### 2. Build via Gradle Wrapper
Execute `./gradlew build --no-daemon` to compile, test, and package the mod:
```bash
# On Linux / macOS / Git Bash
./gradlew build --no-daemon

# On Windows PowerShell
.\gradlew.bat build --no-daemon
```

### 3. Automated GameTest Verification
Run automated headless tests using JUnit and Fabric Loom GameTest:
```bash
./gradlew test --no-daemon
```

### 4. Output Artifact Location
The compiled build output JAR file will be generated in:
```text
build/libs/natural-reproduction-<version>+26.2.jar
```

---

> ☕ **Soutenir le développement** : Si vous appréciez ce mod, soutenez l'auteur sur [Ko-fi](https://ko-fi.com/rifaditya) pour des versions de test préliminaires !

---

## 🔙 Navigation

- [[Version Compatibility Matrix|fr_fr-Version-Compatibility]]
- [[Technical Architecture & Mixins|fr_fr-Architecture-and-Mixins]]
- Return to [[Home Portal|fr_fr-Home]]
