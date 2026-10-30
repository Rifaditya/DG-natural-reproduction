scoreboard players set #global nr_naturalist 1
scoreboard players set #ambient nr_naturalist 1
scoreboard players set #forest nr_naturalist 1
scoreboard players set #savanna nr_naturalist 1
scoreboard players set #aquatic nr_naturalist 1
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Breeding DISABLED for ALL 47 Naturalist mobs.","color":"red","bold":true}]
