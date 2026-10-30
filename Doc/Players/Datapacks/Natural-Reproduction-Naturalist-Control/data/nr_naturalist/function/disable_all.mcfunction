scoreboard players set #ambient nr_naturalist 1
scoreboard players set #forest nr_naturalist 1
scoreboard players set #savanna nr_naturalist 1
scoreboard players set #aquatic nr_naturalist 1
execute as @e[type=#nr_naturalist:disabled_mobs,nbt={Age:0}] run tag @s add nr_breeding_blocked
execute as @e[type=#nr_naturalist:disabled_mobs,tag=nr_breeding_blocked] run data merge entity @s {Age:600}
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1.0
title @a actionbar {"text":"[Natural Reproduction] Breeding DISABLED for ALL 47 Naturalist Mobs","color":"red"}
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Breeding DISABLED for ALL 47 Naturalist mobs.","color":"red","bold":true}]
function nr_naturalist:menu
