# Complete Dynamic GameRules Reference

| Category | Key Count | Storage Data File | Administration |
| :--- | :--- | :--- | :--- |
| **Core GameRules** | 7 GameRules | `game_rules.dat` | `/gamerule` & `/naturalreproduction set` |
| **Species Toggles** | 27 GameRules | `game_rules.dat` | `§lNatural Reproduction - Species Toggles` |

---

## ⚙️ Core Dynamic GameRules Table

| GameRule Key | Data Type | Default | Description |
| :--- | :---: | :---: | :--- |
| `natural-reproduction:enabled` | Boolean | `true` | Master switch for autonomous wild animal breeding. |
| `natural-reproduction:density_cap` | Integer | `10` | Maximum same-species animals permitted within a 16-block radius. |
| `natural-reproduction:rate` | Integer | `24000` | Average tick interval between breeding checks ($24000\text{ ticks} = 1\text{ MC Day}$). |
| `natural-reproduction:scale_drops` | Boolean | `true` | Item drop yield scales proportionally with physical body scale attribute. |
| `natural-reproduction:cramped_space_penalty` | Boolean | `true` | Confined pen stunting vs spacious pasture size recovery. |
| `natural-reproduction:biome_fertility` | Boolean | `true` | $2\times$ breeding frequency and $+15\%$ genetics quality in native biomes. |
| `natural-reproduction:biome_variants` | Boolean | `true` | Dynamic biome variant skin adaptation for offspring. |

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

For Brigadier command syntax, see [[Commands|Commands]].
