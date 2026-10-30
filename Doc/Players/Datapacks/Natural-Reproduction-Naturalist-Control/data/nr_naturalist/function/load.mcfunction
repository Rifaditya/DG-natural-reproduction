# Initialize Naturalist Control scoreboard objective
scoreboard objectives add nr_naturalist dummy "Naturalist Control"

# Set default scores (1 = Disabled, 0 = Enabled)
scoreboard players set #global nr_naturalist 1
scoreboard players set #ambient nr_naturalist 1
scoreboard players set #forest nr_naturalist 1
scoreboard players set #savanna nr_naturalist 1
scoreboard players set #aquatic nr_naturalist 1

# Start 100-tick loop
schedule function nr_naturalist:tick_100t 100t replace

# Broadcast load notification in chat with clickable /function nr_naturalist:menu
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Naturalist Compatibility Datapack loaded. Naturalist breeding is disabled by default. Type ","color":"yellow"},{"text":"/function nr_naturalist:menu","color":"aqua","underlined":true,"clickEvent":{"action":"run_command","value":"/function nr_naturalist:menu"},"hoverEvent":{"action":"show_text","contents":{"text":"Click to open Naturalist Control Menu","color":"gold"}}},{"text":" to configure species.","color":"yellow"}]
