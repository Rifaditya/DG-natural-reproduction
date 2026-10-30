scoreboard players set #ambient nr_naturalist 0
scoreboard players set #forest nr_naturalist 0
scoreboard players set #savanna nr_naturalist 0
scoreboard players set #aquatic nr_naturalist 0
execute as @e[type=#nr_naturalist:disabled_mobs,tag=nr_breeding_blocked] run data merge entity @s {Age:0}
execute as @e[type=#nr_naturalist:disabled_mobs,tag=nr_breeding_blocked] run tag @s remove nr_breeding_blocked
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1.2
title @a actionbar {"text":"[Natural Reproduction] Breeding ENABLED for ALL Naturalist Mobs","color":"green"}
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Breeding ENABLED for ALL Naturalist mobs.","color":"green","bold":true}]
function nr_naturalist:menu
