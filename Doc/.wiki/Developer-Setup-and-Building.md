# Developer Setup & Building from Source

| Requirement | Value |
| :--- | :--- |
| **Java JDK** | JDK **25** (Strictly required for MC 26.2 build toolchains) |
| **Gradle** | **9.3+** (`--no-daemon`) |
| **Fabric Loom** | **1.15.5+** |
| **Target Minecraft** | Stable **MC 26.2** |
| **Automated Testing** | `./gradlew test` (JUnit & Loom GameTest framework) |

---

## 🛠️ Environment Prerequisites

Ensure JDK 25 is installed and configured in your environment variables or Gradle configuration (`org.gradle.java.home=E:/JDK25`).

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

## 🔌 IDE Import & Project Setup

### IntelliJ IDEA (Recommended)
1. Open IntelliJ IDEA -> **File** -> **Open** -> select project root directory.
2. Select **Import as Gradle Project**.
3. Set **Project SDK** to **JDK 25**.
4. Generate Loom sources:
   ```bash
   ./gradlew genSources
   ```

For architecture details, see [[Architecture & Mixins|Architecture-and-Mixins]].
