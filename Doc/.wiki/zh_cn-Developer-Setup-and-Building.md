# Developer Setup & Building from Source

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **代码仓库源码状态免责声明**：本 Wiki 文档反映了代码仓库中的**当前源码状态**（`v1.3.4+26.2`）。可能包含领先于 CurseForge 与 Modrinth 公开发布版本的最新未发布提交或开发特性。

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

> ☕ **支持模组开发**：如果您喜欢本模组，欢迎前往 [Ko-fi](https://ko-fi.com/rifaditya) 支持作者获取最新开发测试构建！

---

## 🔙 Navigation

- [[Version Compatibility Matrix|zh_cn-Version-Compatibility]]
- [[Technical Architecture & Mixins|zh_cn-Architecture-and-Mixins]]
- Return to [[Home Portal|zh_cn-Home]]
