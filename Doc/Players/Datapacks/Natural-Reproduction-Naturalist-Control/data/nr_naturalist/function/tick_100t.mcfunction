# Fast-Fail Cooldown Reset: If category is disabled (score 1), lock adult mobs to Age:600
# Ambient Mobs
execute if score #ambient nr_naturalist matches 1 as @e[type=#nr_naturalist:ambient_mobs,nbt={Age:0}] run tag @s add nr_breeding_blocked
execute if score #ambient nr_naturalist matches 1 as @e[type=#nr_naturalist:ambient_mobs,tag=nr_breeding_blocked] run data merge entity @s {Age:600}

# Forest Mobs
execute if score #forest nr_naturalist matches 1 as @e[type=#nr_naturalist:forest_mobs,nbt={Age:0}] run tag @s add nr_breeding_blocked
execute if score #forest nr_naturalist matches 1 as @e[type=#nr_naturalist:forest_mobs,tag=nr_breeding_blocked] run data merge entity @s {Age:600}

# Savanna Mobs
execute if score #savanna nr_naturalist matches 1 as @e[type=#nr_naturalist:savanna_mobs,nbt={Age:0}] run tag @s add nr_breeding_blocked
execute if score #savanna nr_naturalist matches 1 as @e[type=#nr_naturalist:savanna_mobs,tag=nr_breeding_blocked] run data merge entity @s {Age:600}

# Aquatic Mobs
execute if score #aquatic nr_naturalist matches 1 as @e[type=#nr_naturalist:aquatic_mobs,nbt={Age:0}] run tag @s add nr_breeding_blocked
execute if score #aquatic nr_naturalist matches 1 as @e[type=#nr_naturalist:aquatic_mobs,tag=nr_breeding_blocked] run data merge entity @s {Age:600}

# Re-schedule self every 100 ticks (5 seconds)
schedule function nr_naturalist:tick_100t 100t replace
