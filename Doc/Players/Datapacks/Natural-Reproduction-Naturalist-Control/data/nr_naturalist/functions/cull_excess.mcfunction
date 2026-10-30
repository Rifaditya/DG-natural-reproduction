# Safely cull up to 25 wild disabled adult Naturalist mobs (protecting named animals and pets)
execute as @e[type=#nr_naturalist:disabled_mobs,nbt={Age:600},limit=25,sort=random] unless data entity @s CustomName run kill @s
tellraw @s [{"text":"[Natural Reproduction] ","color":"gold","bold":true},{"text":"Safely thinned out up to 25 wild Naturalist mobs. Tamed pets and named animals were protected.","color":"green"}]
