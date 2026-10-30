scoreboard objectives add nr_naturalist dummy "Naturalist Control"
execute unless score #ambient nr_naturalist matches 0..1 run scoreboard players set #ambient nr_naturalist 1
execute unless score #forest nr_naturalist matches 0..1 run scoreboard players set #forest nr_naturalist 1
execute unless score #savanna nr_naturalist matches 0..1 run scoreboard players set #savanna nr_naturalist 1
execute unless score #aquatic nr_naturalist matches 0..1 run scoreboard players set #aquatic nr_naturalist 1
schedule function nr_naturalist:tick_100t 100t replace
tellraw @a ["",{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Naturalist Compatibility Datapack loaded. Naturalist breeding is disabled by default. Type ","color":"yellow"},{"text":"/function nr_naturalist:menu","color":"aqua","underlined":true,"clickEvent":{"action":"run_command","value":"/function nr_naturalist:menu","command":"/function nr_naturalist:menu"},"click_event":{"action":"run_command","value":"/function nr_naturalist:menu","command":"/function nr_naturalist:menu"},"hoverEvent":{"action":"show_text","contents":"Click to open Naturalist Breeding Menu","value":"Click to open Naturalist Breeding Menu"},"hover_event":{"action":"show_text","contents":"Click to open Naturalist Breeding Menu","value":"Click to open Naturalist Breeding Menu"}},{"text":" to configure species.","color":"yellow"}]
