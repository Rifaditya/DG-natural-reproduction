# Dynamic GameRules & Command Reference

Server administrators and singleplayer worlds can customize every aspect of **Natural Reproduction** in real time using native namespaced **GameRules**, the `/naturalreproduction` command suite, or the optional **ModMenu / YACL v3** GUI screen.

---

## ⚙️ Core Dynamic GameRules

| GameRule Key | Data Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Master switch for autonomous wild animal breeding. |
| `natural-reproduction:density_cap` | Integer | `10` | Maximum same-species animals permitted within a 16-block radius. |
| `natural-reproduction:rate` | Integer | `24000` | Average tick interval between breeding checks (24000 ticks = 1 MC Day). |
| `natural-reproduction:scale_drops` | Boolean | `true` | Item drop yield scales proportionally with physical body scale attribute. |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | Confined pen stunting vs spacious pasture size recovery. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | 2x breeding speed and +15% quality boost in native climate biomes. |
| `natural-reproduction:biome_variants` | Boolean | `true` | Offspring born in specific biomes adapt visual entity variant skins. |

---

## 🐾 27 Per-Species Toggles (`Natural Reproduction - Species Toggles`)

Administrators can enable or disable natural reproduction for any individual animal species under the dedicated `natural_reproduction_species` category (localized as **`§lNatural Reproduction - Species Toggles`**):

| GameRule Key | Target Species | Default | Description |
| :--- | :--- | :---: | :--- |
| `natural-reproduction:allow_cow` | Cow | `true` | Enable/disable autonomous breeding for Cows. |
| `natural-reproduction:allow_pig` | Pig | `true` | Enable/disable autonomous breeding for Pigs. |
| `natural-reproduction:allow_sheep` | Sheep | `true` | Enable/disable autonomous breeding for Sheep. |
| `natural-reproduction:allow_chicken` | Chicken | `true` | Enable/disable autonomous breeding for Chickens. |
| `natural-reproduction:allow_mooshroom` | Mooshroom | `true` | Enable/disable autonomous breeding for Mooshrooms. |
| `natural-reproduction:allow_horse` | Horse | `true` | Enable/disable autonomous breeding for Horses. |
| `natural-reproduction:allow_donkey` | Donkey | `true` | Enable/disable autonomous breeding for Donkeys. |
| `natural-reproduction:allow_mule` | Mule | `true` | Enable/disable autonomous breeding for Mules. |
| `natural-reproduction:allow_llama` | Llama | `true` | Enable/disable autonomous breeding for Llamas. |
| `natural-reproduction:allow_trader_llama` | Trader Llama | `true` | Enable/disable autonomous breeding for Trader Llamas. |
| `natural-reproduction:allow_camel` | Camel | `true` | Enable/disable autonomous breeding for Camels. |
| `natural-reproduction:allow_wolf` | Wolf | `true` | Enable/disable autonomous breeding for Wolves. |
| `natural-reproduction:allow_cat` | Cat | `true` | Enable/disable autonomous breeding for Cats. |
| `natural-reproduction:allow_fox` | Fox | `true` | Enable/disable autonomous breeding for Foxes. |
| `natural-reproduction:allow_ocelot` | Ocelot | `true` | Enable/disable autonomous breeding for Ocelots. |
| `natural-reproduction:allow_turtle` | Turtle | `true` | Enable/disable autonomous breeding for Turtles. |
| `natural-reproduction:allow_frog` | Frog | `true` | Enable/disable autonomous breeding for Frogs. |
| `natural-reproduction:allow_axolotl` | Axolotl | `true` | Enable/disable autonomous breeding for Axolotls. |
| `natural-reproduction:allow_polar_bear` | Polar Bear | `true` | Enable/disable autonomous breeding for Polar Bears. |
| `natural-reproduction:allow_panda` | Panda | `true` | Enable/disable autonomous breeding for Pandas. |
| `natural-reproduction:allow_rabbit` | Rabbit | `true` | Enable/disable autonomous breeding for Rabbits. |
| `natural-reproduction:allow_goat` | Goat | `true` | Enable/disable autonomous breeding for Goats. |
| `natural-reproduction:allow_armadillo` | Armadillo | `true` | Enable/disable autonomous breeding for Armadillos. |
| `natural-reproduction:allow_sniffer` | Sniffer | `true` | Enable/disable autonomous breeding for Sniffers. |
| `natural-reproduction:allow_bee` | Bee | `true` | Enable/disable autonomous breeding for Bees. |
| `natural-reproduction:allow_strider` | Strider | `true` | Enable/disable autonomous breeding for Striders. |
| `natural-reproduction:allow_hoglin` | Hoglin | `true` | Enable/disable autonomous breeding for Hoglins. |

---

## 💬 Brigadier Command Suite

Manage all mod settings in-game using `/naturalreproduction` (requires Gamemaster OP level 2):

- `/naturalreproduction status` &mdash; Displays active states of all core rules and 27 species toggles.
- `/naturalreproduction get <rule>` &mdash; Queries the current value of a specific rule.
- `/naturalreproduction set <rule> <value>` &mdash; Dynamically updates any core rule or species toggle.
- `/naturalreproduction trackerlogs list|clear|enable|disable` &mdash; Inspect and manage autonomous reproduction logs.
- `/naturalreproduction reset` &mdash; Resets all core rules and 27 species toggles to factory defaults.
- `/naturalreproduction reload` &mdash; Reloads configuration settings.

For full breeding details, see [[Animal Breeding Guide|Animal-Breeding-Guide]].
