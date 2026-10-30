execute as @e[type=#nr_naturalist:disabled_mobs,nbt={Age:600},limit=25,sort=random] unless data entity @s CustomName run kill @s
playsound minecraft:entity.experience_orb.pickup master @a ~ ~ ~ 1 1
tellraw @a [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Safely thinned out up to 25 wild disabled Naturalist mobs to recover FPS.","color":"green"}]
