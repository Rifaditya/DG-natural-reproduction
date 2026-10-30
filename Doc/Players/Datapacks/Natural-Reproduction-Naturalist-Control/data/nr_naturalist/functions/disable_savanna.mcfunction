scoreboard players set #global nr_naturalist 0
scoreboard players set #savanna nr_naturalist 1
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Savanna & tropics breeding: ","color":"yellow"},{"text":"DISABLED","color":"red","bold":true}]
