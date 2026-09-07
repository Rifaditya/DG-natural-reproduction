# Developer Setup & Building from Source

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> [!NOTE]
> 📌 **Отказ от ответственности за статус исходного кода**: Документация в этой Wiki отражает **текущее состояние исходного кода в репозитории** (`v1.3.4+26.2`), которое может содержать недавние неопубликованные коммиты или функции разработки, опережающие общедоступные релизы на CurseForge и Modrinth.

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

> ☕ **Поддержка разработки**: Если вам нравится мод, поддержите автора на [Ko-fi](https://ko-fi.com/rifaditya) для раннего доступа к тестовым сборкам!

---

## 🔙 Navigation

- [[Version Compatibility Matrix|ru_ru-Version-Compatibility]]
- [[Technical Architecture & Mixins|ru_ru-Architecture-and-Mixins]]
- Return to [[Home Portal|ru_ru-Home]]
