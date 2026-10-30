scoreboard players set #ambient nr_naturalist 0
playsound minecraft:ui.button.click master @a ~ ~ ~ 1 1.2
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Ambient bugs & birds breeding: ","color":"yellow"},{"text":"ENABLED","color":"green","bold":true}]
