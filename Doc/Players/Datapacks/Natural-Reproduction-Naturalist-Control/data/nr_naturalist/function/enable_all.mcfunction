scoreboard players set #global nr_naturalist 0
scoreboard players set #ambient nr_naturalist 0
scoreboard players set #forest nr_naturalist 0
scoreboard players set #savanna nr_naturalist 0
scoreboard players set #aquatic nr_naturalist 0
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1.2
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Breeding ENABLED for ALL Naturalist mobs.","color":"green","bold":true}]
