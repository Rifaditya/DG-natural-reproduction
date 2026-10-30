scoreboard players set #global nr_naturalist 0
scoreboard players set #ambient nr_naturalist 1
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Ambient bugs & birds breeding: ","color":"yellow"},{"text":"DISABLED","color":"red","bold":true}]
