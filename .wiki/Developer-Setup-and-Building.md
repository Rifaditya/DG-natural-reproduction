# Developer Setup & Building from Source

This guide covers setting up the development environment, cloning the repository, building tagged JAR artifacts, and importing **Natural Reproduction** into your IDE.

---

## 🛠️ Prerequisites & Environment Requirements

- **Java Development Kit (JDK)**: JDK **25** (strictly required for Minecraft 26.2+ build toolchains).
- **Gradle**: **9.3+** (Gradle wrapper included in repository).
- **Fabric Loom**: **1.15.5+**
- **Minecraft Version**: Target stable **MC 26.2**.

---

## 💻 Cloning & Building Tagged JARs

### 1. Clone the Repository
```bash
git clone https://github.com/Dasik/Natural-Reproduction.git
cd Natural-Reproduction
```

### 2. Build via Gradle Wrapper
Execute `./gradlew build` without daemon to compile, test, and package the mod:
```bash
# On Linux / macOS / Git Bash
./gradlew build --no-daemon

# On Windows PowerShell
.\gradlew.bat build --no-daemon
```

### 3. Output Artifact Location
The compiled build output JAR file will be generated in:
```text
build/libs/natural-reproduction-<version>+26.2.jar
```

---

## 🔌 IDE Import & Project Setup

### IntelliJ IDEA (Recommended)
1. Open IntelliJ IDEA -> **File** -> **Open** -> select the project root directory.
2. Select **Import as Gradle Project**.
3. Ensure **Project SDK** is configured to **JDK 25** (`E:/JDK25` or installed JDK 25 path).
4. Run Loom task generation:
   ```bash
   ./gradlew genSources
   ```
5. Use IntelliJ Gradle tool window to run `Tasks -> fabric -> runClient` or `runServer` for debugging.

---

## 📜 Build Laws & Guidelines

- **1 Jar 1 Version Law**: The build strictly targets single Minecraft versions without cross-version reflection hacks.
- **Outer Archive Law**: Following every successful `./gradlew build`, the output JAR must be archived to `Archive Jar of all versions/`.
- **Zero-Dependency Guard**: `ModVersionGuard.checkClass` in `onInitialize` enforces environment class checks at startup using `Thread.currentThread().getContextClassLoader()`.

For architecture details, see [[Architecture & Mixins|Architecture-and-Mixins]].
