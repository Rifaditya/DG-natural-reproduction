// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.EntityGeneticsRegistry;
import net.dasik.social.api.genetics.GeneticsConfig;
import net.dasik.social.api.genetics.GeneticsLimitRegistry;
import net.dasik.social.api.genetics.MutationRule;
import net.dasik.social.api.genetics.TraitConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.vanillaoutsider.naturalreproduction.command.NaturalReproductionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaturalReproductionFabric implements ModInitializer {
    public static final String MOD_ID = "natural-reproduction";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final GameRuleCategory CATEGORY = DynamicGameRuleManager.registerCategory(
        Identifier.fromNamespaceAndPath(MOD_ID, "natural_reproduction")
    );

    public static final GameRuleCategory SPECIES_CATEGORY = DynamicGameRuleManager.registerCategory(
        Identifier.fromNamespaceAndPath(MOD_ID, "species_toggles")
    );

    public static GameRule<Boolean> ENABLED;
    public static GameRule<Integer> DENSITY_CAP;
    public static GameRule<Integer> RATE;
    public static GameRule<Boolean> SCALE_DROPS;
    public static GameRule<Boolean> CRAMPED_SPACE_PENALTY;
    public static GameRule<Boolean> INBREEDING_DEGRADATION;
    public static GameRule<Boolean> PASTURE_ENRICHMENT;
    public static GameRule<Boolean> OVERGRAZING;
    public static GameRule<Boolean> GESTATION_PERIOD;
    public static GameRule<Boolean> MANUAL_GESTATION;
    public static GameRule<Integer> GESTATION_DURATION;
    public static GameRule<Boolean> FERTILIZED_CHICKEN_EGGS;
    public static GameRule<Boolean> CHICKEN_INFERTILE_REGULAR_EGGS;
    public static GameRule<Integer> DISPENSER_EGG_HATCH_CHANCE;
    public static GameRule<Boolean> HERD_DYNAMICS;
    public static GameRule<Boolean> HERD_STAMPEDE;
    public static GameRule<Boolean> BIOME_FERTILITY;
    public static GameRule<Boolean> BIOME_VARIANTS;
    public static GameRule<Integer> MIN_SCALE;
    public static GameRule<Integer> NORMAL_SCALE;
    public static GameRule<Integer> MAX_SCALE;
    public static GameRule<Boolean> TRACKER_LOGS;

    public static GameRule<Boolean> ALLOW_COW;
    public static GameRule<Boolean> ALLOW_PIG;
    public static GameRule<Boolean> ALLOW_SHEEP;
    public static GameRule<Boolean> ALLOW_CHICKEN;
    public static GameRule<Boolean> ALLOW_MOOSHROOM;
    public static GameRule<Boolean> ALLOW_HORSE;
    public static GameRule<Boolean> ALLOW_DONKEY;
    public static GameRule<Boolean> ALLOW_MULE;
    public static GameRule<Boolean> ALLOW_LLAMA;
    public static GameRule<Boolean> ALLOW_TRADER_LLAMA;
    public static GameRule<Boolean> ALLOW_CAMEL;
    public static GameRule<Boolean> ALLOW_WOLF;
    public static GameRule<Boolean> ALLOW_CAT;
    public static GameRule<Boolean> ALLOW_FOX;
    public static GameRule<Boolean> ALLOW_OCELOT;
    public static GameRule<Boolean> ALLOW_TURTLE;
    public static GameRule<Boolean> ALLOW_FROG;
    public static GameRule<Boolean> ALLOW_AXOLOTL;
    public static GameRule<Boolean> ALLOW_POLAR_BEAR;
    public static GameRule<Boolean> ALLOW_PANDA;
    public static GameRule<Boolean> ALLOW_RABBIT;
    public static GameRule<Boolean> ALLOW_GOAT;
    public static GameRule<Boolean> ALLOW_ARMADILLO;
    public static GameRule<Boolean> ALLOW_SNIFFER;
    public static GameRule<Boolean> ALLOW_BEE;
    public static GameRule<Boolean> ALLOW_STRIDER;
    public static GameRule<Boolean> ALLOW_HOGLIN;

    public static final Map<EntityType<?>, GameRule<Boolean>> SPECIES_TOGGLES = new HashMap<>();
    public static final Map<String, GameRule<Boolean>> RULE_NAME_MAP = new HashMap<>();

    @Override
    public void onInitialize() {
        ModVersionGuard.checkClass("Natural Reproduction", "net.minecraft.world.entity.animal.Animal");

        net.fabricmc.loader.api.FabricLoader.getInstance()
            .getModContainer(MOD_ID)
            .ifPresent(container -> LOGGER.info("Natural Reproduction v{} Initializing...", container.getMetadata().getVersion().getFriendlyString()));

        if (!net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("dasik-library")) {
            throw new RuntimeException("Natural Reproduction requires 'dasik-library' to be loaded!");
        }

        ENABLED = DynamicGameRuleManager.booleanRule("natural-reproduction:enabled", CATEGORY, true)
            .name("Enable Natural Reproduction")
            .description("When true, passive livestock breed autonomously when conditions are ideal.")
            .register();

        DENSITY_CAP = DynamicGameRuleManager.integerRule("natural-reproduction:density_cap", CATEGORY, 10)
            .name("Population Density Cap")
            .description("Maximum animals of same species within 16 blocks allowed for autonomous breeding.")
            .register();

        RATE = DynamicGameRuleManager.integerRule("natural-reproduction:rate", CATEGORY, 24000)
            .name("Autonomous Breeding Rate")
            .description("Average tick interval between autonomous breeding attempts (24000 = 1 MC Day).")
            .register();

        SCALE_DROPS = DynamicGameRuleManager.booleanRule("natural-reproduction:scale_drops", CATEGORY, true)
            .name("Scale-Based Item Drops")
            .description("When true, animal item drops scale with the animal's physical scale attribute.")
            .register();

        CRAMPED_SPACE_PENALTY = DynamicGameRuleManager.booleanRule("natural-reproduction:cramped_space_penalty", CATEGORY, true)
            .name("Cramped Space Penalty & Recovery")
            .description("When true, breeding in cramped spaces stunts offspring scale down to 0.10x; breeding in spacious pastures recovers size genetics.")
            .register();

        INBREEDING_DEGRADATION = DynamicGameRuleManager.booleanRule("natural-reproduction:inbreeding_degradation", CATEGORY, true)
            .name("Inbreeding Lineage Degradation")
            .description("When true, repeated closed-herd inbreeding degrades genetics down to lethal collapse.")
            .register();

        PASTURE_ENRICHMENT = DynamicGameRuleManager.booleanRule("natural-reproduction:pasture_enrichment", CATEGORY, true)
            .name("Pasture Enrichment Dynamics")
            .description("When true, pastures with feeding troughs, hay, water, and shelter grant well-nourished bonuses.")
            .register();

        OVERGRAZING = DynamicGameRuleManager.booleanRule("natural-reproduction:overgrazing", CATEGORY, true)
            .name("Overgrazing Terrain Wear")
            .description("When true, dense herds convert grass blocks into dirt/coarse dirt.")
            .register();

        GESTATION_PERIOD = DynamicGameRuleManager.booleanRule("natural-reproduction:gestation_period", CATEGORY, true)
            .name("Autonomous Gestation Timers")
            .description("When true, breeding enters mothers into a pregnancy countdown before delivering offspring.")
            .register();

        MANUAL_GESTATION = DynamicGameRuleManager.booleanRule("natural-reproduction:manual_gestation", CATEGORY, true)
            .name("Manual Breeding Gestation")
            .description("When true, player manual feeding also uses pregnancy gestation timers.")
            .register();

        GESTATION_DURATION = DynamicGameRuleManager.integerRule("natural-reproduction:gestation_duration", CATEGORY, 24000)
            .name("Gestation Duration")
            .description("Pregnancy gestation duration in ticks (24000 = 1 MC Day).")
            .register();

        FERTILIZED_CHICKEN_EGGS = DynamicGameRuleManager.booleanRule("natural-reproduction:fertilized_chicken_eggs", CATEGORY, true)
            .name("Fertilized Chicken Eggs")
            .description("When true, chicken autonomous breeding rolls 50/50 for immediate chicks or laying guaranteed-hatch Fertilized Eggs.")
            .register();

        CHICKEN_INFERTILE_REGULAR_EGGS = DynamicGameRuleManager.booleanRule("natural-reproduction:chicken_infertile_regular_eggs", CATEGORY, true)
            .name("Infertile Regular Eggs")
            .description("When true, ordinary unfertilized chicken eggs have a reduced 1/64 miracle hatch chance.")
            .register();

        DISPENSER_EGG_HATCH_CHANCE = DynamicGameRuleManager.integerRule("natural-reproduction:dispenser_egg_hatch_chance", CATEGORY, 75)
            .name("Dispenser Egg Hatch Chance")
            .description("Percentage chance (0-100%) for a dispenser-fired Fertilized Egg to hatch a baby chick.")
            .register();

        HERD_DYNAMICS = DynamicGameRuleManager.booleanRule("natural-reproduction:herd_dynamics", CATEGORY, true)
            .name("Herd Social Dynamics & Leadership")
            .description("When true, animals form pastoral herds led by the largest Alpha animal with diurnal schedule cohesion.")
            .register();

        HERD_STAMPEDE = DynamicGameRuleManager.booleanRule("natural-reproduction:herd_stampede", CATEGORY, true)
            .name("Herd Distress Stampede Panic")
            .description("When true, damage from predators or players alerts nearby herd members to stampede away in unison.")
            .register();

        BIOME_FERTILITY = DynamicGameRuleManager.booleanRule("natural-reproduction:biome_fertility", CATEGORY, true)
            .name("Native Biome Fertility Boost")
            .description("When true, animals in native biomes get 2x faster breeding frequency and +15% offspring genetics quality.")
            .register();

        BIOME_VARIANTS = DynamicGameRuleManager.booleanRule("natural-reproduction:biome_variants", CATEGORY, true)
            .name("Biome Variant Skin Adaptation")
            .description("When true, offspring born in specific biomes dynamically adapt their visual entity variant skin (e.g. Snowy Wolves, Desert Frogs).")
            .register();

        MIN_SCALE = DynamicGameRuleManager.integerRule("natural-reproduction:min_scale", CATEGORY, 10)
            .name("Minimum Size Percentage")
            .description("Minimum animal scale percentage (10 = 0.1x scale).")
            .register();

        NORMAL_SCALE = DynamicGameRuleManager.integerRule("natural-reproduction:normal_scale", CATEGORY, 95)
            .name("Normal Baseline Size Percentage")
            .description("Standard baseline animal scale percentage (95 = 0.95x scale).")
            .register();

        MAX_SCALE = DynamicGameRuleManager.integerRule("natural-reproduction:max_scale", CATEGORY, 120)
            .name("Maximum Size Percentage")
            .description("Maximum animal scale percentage (120 = 1.2x scale).")
            .register();

        TRACKER_LOGS = DynamicGameRuleManager.booleanRule("natural-reproduction:tracker_logs", CATEGORY, false)
            .name("Breeding Tracker Logging")
            .description("When true, autonomous animal reproduction events are recorded to the tracker log and server console.")
            .register();

        // 27 Per-Species GameRules under SPECIES_CATEGORY
        ALLOW_COW = registerSpeciesRule("allow_cow", EntityTypes.COW, "Cow");
        ALLOW_PIG = registerSpeciesRule("allow_pig", EntityTypes.PIG, "Pig");
        ALLOW_SHEEP = registerSpeciesRule("allow_sheep", EntityTypes.SHEEP, "Sheep");
        ALLOW_CHICKEN = registerSpeciesRule("allow_chicken", EntityTypes.CHICKEN, "Chicken");
        ALLOW_MOOSHROOM = registerSpeciesRule("allow_mooshroom", EntityTypes.MOOSHROOM, "Mooshroom");
        ALLOW_HORSE = registerSpeciesRule("allow_horse", EntityTypes.HORSE, "Horse");
        ALLOW_DONKEY = registerSpeciesRule("allow_donkey", EntityTypes.DONKEY, "Donkey");
        ALLOW_MULE = registerSpeciesRule("allow_mule", EntityTypes.MULE, "Mule");
        ALLOW_LLAMA = registerSpeciesRule("allow_llama", EntityTypes.LLAMA, "Llama");
        ALLOW_TRADER_LLAMA = registerSpeciesRule("allow_trader_llama", EntityTypes.TRADER_LLAMA, "Trader Llama");
        ALLOW_CAMEL = registerSpeciesRule("allow_camel", EntityTypes.CAMEL, "Camel");
        ALLOW_WOLF = registerSpeciesRule("allow_wolf", EntityTypes.WOLF, "Wolf");
        ALLOW_CAT = registerSpeciesRule("allow_cat", EntityTypes.CAT, "Cat");
        ALLOW_FOX = registerSpeciesRule("allow_fox", EntityTypes.FOX, "Fox");
        ALLOW_OCELOT = registerSpeciesRule("allow_ocelot", EntityTypes.OCELOT, "Ocelot");
        ALLOW_TURTLE = registerSpeciesRule("allow_turtle", EntityTypes.TURTLE, "Turtle");
        ALLOW_FROG = registerSpeciesRule("allow_frog", EntityTypes.FROG, "Frog");
        ALLOW_AXOLOTL = registerSpeciesRule("allow_axolotl", EntityTypes.AXOLOTL, "Axolotl");
        ALLOW_POLAR_BEAR = registerSpeciesRule("allow_polar_bear", EntityTypes.POLAR_BEAR, "Polar Bear");
        ALLOW_PANDA = registerSpeciesRule("allow_panda", EntityTypes.PANDA, "Panda");
        ALLOW_RABBIT = registerSpeciesRule("allow_rabbit", EntityTypes.RABBIT, "Rabbit");
        ALLOW_GOAT = registerSpeciesRule("allow_goat", EntityTypes.GOAT, "Goat");
        ALLOW_ARMADILLO = registerSpeciesRule("allow_armadillo", EntityTypes.ARMADILLO, "Armadillo");
        ALLOW_SNIFFER = registerSpeciesRule("allow_sniffer", EntityTypes.SNIFFER, "Sniffer");
        ALLOW_BEE = registerSpeciesRule("allow_bee", EntityTypes.BEE, "Bee");
        ALLOW_STRIDER = registerSpeciesRule("allow_strider", EntityTypes.STRIDER, "Strider");
        ALLOW_HOGLIN = registerSpeciesRule("allow_hoglin", EntityTypes.HOGLIN, "Hoglin");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            NaturalReproductionCommand.register(dispatcher);
        });

        // Register Data-Driven Animal Genetics with DasikLibrary API across ALL vanilla animal species
        Map<String, TraitConfig> animalTraits = Map.of(
            "scale", new TraitConfig("scale", "minecraft:scale", "ADD_VALUE", 0.0f, 1.0f, 0.10f, 1.20f),
            "max_health", new TraitConfig("max_health", "minecraft:generic.max_health", "ADD_VALUE", 2.0f, 0.5f, -4.0f, 12.0f),
            "movement_speed", new TraitConfig("movement_speed", "minecraft:generic.movement_speed", "ADD_MULTIPLIED_BASE", 0.05f, 0.5f, -0.04f, 0.08f)
        );

        Map<String, Map<String, MutationRule>> animalMutations = Map.of(
            "default", Map.of(
                "scale", new MutationRule("uniform", 0.80f, 0.95f),
                "max_health", new MutationRule("triangular", -2.0f, 8.0f),
                "movement_speed", new MutationRule("triangular", -0.03f, 0.06f)
            )
        );

        GeneticsConfig config = new GeneticsConfig(animalTraits, animalMutations);

        List<EntityType<?>> allAnimals = List.of(
            EntityTypes.COW, EntityTypes.PIG, EntityTypes.SHEEP, EntityTypes.CHICKEN,
            EntityTypes.MOOSHROOM, EntityTypes.HORSE, EntityTypes.DONKEY, EntityTypes.MULE,
            EntityTypes.LLAMA, EntityTypes.TRADER_LLAMA, EntityTypes.CAMEL, EntityTypes.WOLF,
            EntityTypes.CAT, EntityTypes.FOX, EntityTypes.OCELOT, EntityTypes.TURTLE,
            EntityTypes.FROG, EntityTypes.AXOLOTL, EntityTypes.POLAR_BEAR, EntityTypes.PANDA,
            EntityTypes.RABBIT, EntityTypes.GOAT, EntityTypes.ARMADILLO, EntityTypes.SNIFFER,
            EntityTypes.BEE, EntityTypes.STRIDER, EntityTypes.HOGLIN
        );

        for (EntityType<?> type : allAnimals) {
            EntityGeneticsRegistry.register(type, config);
            GeneticsLimitRegistry.registerMin(type, "scale", (entity, defaultMin) -> {
                if (entity.level() instanceof ServerLevel sl) {
                    return DynamicGameRuleManager.getInt(sl, MIN_SCALE) / 100.0f;
                }
                return 0.10f;
            });
            GeneticsLimitRegistry.registerMax(type, "scale", (entity, defaultMax) -> {
                if (entity.level() instanceof ServerLevel sl) {
                    return DynamicGameRuleManager.getInt(sl, MAX_SCALE) / 100.0f;
                }
                return 1.20f;
            });
        }

        LOGGER.info("Natural Reproduction: Universal Data-Driven Genetics & 27 Per-Species Toggles Registered.");
    }

    private GameRule<Boolean> registerSpeciesRule(String ruleName, EntityType<?> type, String displayName) {
        GameRule<Boolean> rule = DynamicGameRuleManager.booleanRule("natural-reproduction:" + ruleName, SPECIES_CATEGORY, true)
            .name("Allow " + displayName + " Reproduction")
            .description("When true, " + displayName.toLowerCase() + "s can breed autonomously when habitat conditions are met.")
            .register();
        SPECIES_TOGGLES.put(type, rule);
        RULE_NAME_MAP.put(ruleName, rule);
        return rule;
    }

    public static boolean isSpeciesAllowed(ServerLevel level, EntityType<?> type) {
        GameRule<Boolean> rule = SPECIES_TOGGLES.get(type);
        if (rule != null && level != null) {
            return DynamicGameRuleManager.getBoolean(level, rule);
        }
        return true;
    }
}

