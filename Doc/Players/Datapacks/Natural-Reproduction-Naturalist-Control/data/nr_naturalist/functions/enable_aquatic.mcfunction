scoreboard players set #aquatic nr_naturalist 0
execute as @e[type=#nr_naturalist:aquatic_mobs,tag=nr_breeding_blocked] run data merge entity @s {Age:0}
execute as @e[type=#nr_naturalist:aquatic_mobs,tag=nr_breeding_blocked] run tag @s remove nr_breeding_blocked
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1.2
title @a actionbar {"text":"[Natural Reproduction] Breeding ENABLED for Aquatic Mobs","color":"green"}
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Aquatic & swamp breeding: ","color":"yellow"},{"text":"ENABLED","color":"green","bold":true}]
function nr_naturalist:menu
