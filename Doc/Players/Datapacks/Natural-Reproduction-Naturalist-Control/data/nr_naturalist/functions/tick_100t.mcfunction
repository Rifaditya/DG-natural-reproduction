# Fast-Fail 1 Cooldown Reset: If disabled, set Age:600 on ready-to-breed adults (Age:0)
# Global override
execute if score #global nr_naturalist matches 1 as @e[type=#nr_naturalist:disabled_mobs,nbt={Age:0}] run data merge entity @s {Age:600}

# Per-category control when global override is off (#global = 0)
execute if score #global nr_naturalist matches 0 if score #ambient nr_naturalist matches 1 as @e[type=#nr_naturalist:ambient_mobs,nbt={Age:0}] run data merge entity @s {Age:600}
execute if score #global nr_naturalist matches 0 if score #forest nr_naturalist matches 1 as @e[type=#nr_naturalist:forest_mobs,nbt={Age:0}] run data merge entity @s {Age:600}
execute if score #global nr_naturalist matches 0 if score #savanna nr_naturalist matches 1 as @e[type=#nr_naturalist:savanna_mobs,nbt={Age:0}] run data merge entity @s {Age:600}
execute if score #global nr_naturalist matches 0 if score #aquatic nr_naturalist matches 1 as @e[type=#nr_naturalist:aquatic_mobs,nbt={Age:0}] run data merge entity @s {Age:600}

# Re-schedule self every 100 ticks (5 seconds)
schedule function nr_naturalist:tick_100t 100t replace
