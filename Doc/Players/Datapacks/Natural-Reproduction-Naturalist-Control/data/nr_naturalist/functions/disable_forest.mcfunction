scoreboard players set #forest nr_naturalist 1
execute as @e[type=#nr_naturalist:forest_mobs,nbt={Age:0}] run tag @s add nr_breeding_blocked
execute as @e[type=#nr_naturalist:forest_mobs,tag=nr_breeding_blocked] run data merge entity @s {Age:600}
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1.0
title @a actionbar {"text":"[Natural Reproduction] Breeding DISABLED for Forest Mobs","color":"red"}
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Forest & woodland breeding: ","color":"yellow"},{"text":"DISABLED","color":"red","bold":true}]
function nr_naturalist:menu
