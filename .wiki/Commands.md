# Brigadier Command Suite Reference

| Command Root | Default Permission Level | Tab Completion |
| :--- | :--- | :--- |
| `/naturalreproduction` | `LEVEL_GAMEMASTERS` (OP Level 2) | Supported across all subcommands and parameters |

---

## 🌳 Command Tree Hierarchy

```text
/naturalreproduction
├── help
├── status
├── get <gamerule>
├── set <gamerule> <value>
├── reset
├── reload
└── trackerlogs
    ├── list
    ├── clear
    ├── enable
    └── disable
```

---

## 📜 Subcommand Breakdown

- `/naturalreproduction help`: Displays full command usage and GameRules overview.
- `/naturalreproduction status`: Displays active core rules and the count of enabled species toggles.
- `/naturalreproduction get <gamerule>`: Queries current value of any rule or species toggle.
- `/naturalreproduction set <gamerule> <val>`: Updates any core rule or species toggle in real-time.
- `/naturalreproduction trackerlogs list`: Displays recent autonomous breeding events with timestamp and entity coordinates.
- `/naturalreproduction trackerlogs clear`: Flushes in-memory breeding tracker log history.
- `/naturalreproduction trackerlogs enable|disable`: Toggles event logging on or off.
- `/naturalreproduction reset`: Resets all 7 core rules and 27 species toggles to factory defaults.
- `/naturalreproduction reload`: Reloads configuration files.

For GUI configuration, see [[Configuration|Configuration]].
