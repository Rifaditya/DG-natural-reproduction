# Biome Climate Fertility & Entity Skin Variants

| System Attribute | Value |
| :--- | :--- |
| **Fertility Boost GameRule** | `natural-reproduction:biome_fertility` (Default: `true`) |
| **Variants Adaptation GameRule** | `natural-reproduction:biome_variants` (Default: `true`) |
| **Breeding Speed Boost** | $2\times\text{ Faster}$ ($600\text{s} = 10\text{m}$) |
| **Genetics Scale Boost** | $+15\%\text{ Offspring Quality}$ |

---

## 🏞️ Native Biome Climate Alignment

When animals reproduce within their native climate biome, they receive enhanced breeding speed and genetics bonuses:

```text
[Animal Breeding Check]
       │
       ▼
Is In Native Biome? (e.g. Wolf in Taiga / Frog in Swamp)
       ├── No  -> Standard Rate (24000 ticks = 20 mins) & Standard Genetics
       └── Yes -> Native Rate Boost (12000 ticks = 10 mins) & +15% Scale Quality Boost
```

---

## 🎨 Biome Skin Variant Adaptation

Offspring born in specific biomes dynamically adapt visual entity skin variants to match their environment:

| Entity Type | Local Biome Condition | Adapted Visual Variant Skin |
| :--- | :--- | :--- |
| **Wolf** | Snowy Taiga / Ice Plains | **Snowy Wolf** (White Fur) |
| **Wolf** | Old Growth Pine/Spruce Taiga | **Black / Chestnut Wolf** |
| **Wolf** | Desert / Badlands | **Striped / Spotted / Rusty Wolf** |
| **Wolf** | Taiga (Standard) | **Pale / Woods Wolf** |
| **Fox** | Snowy Taiga / Snowy Plains | **Snow Fox** (White Fur) |
| **Fox** | Taiga (Standard) | **Red Fox** |
| **Frog** | Desert / Badlands / Savanna | **Warm Frog** (Yellow/Brown) |
| **Frog** | Snowy Taiga / Ice Plains | **Cold Frog** (Green/Dark) |
| **Frog** | Swamp / Mangrove Swamp | **Temperate Frog** (Green) |
| **Rabbit** | Snowy Plains / Ice Spikes | **White Rabbit** |
| **Rabbit** | Desert | **Gold Rabbit** |

For habitat block triggers, see [[Species Habitat Reference|Species-Habitat-Reference]].
