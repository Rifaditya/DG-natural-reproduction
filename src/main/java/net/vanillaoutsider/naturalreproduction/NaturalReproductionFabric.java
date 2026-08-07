// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.EntityGeneticsRegistry;
import net.dasik.social.api.genetics.GeneticsConfig;
import net.dasik.social.api.genetics.MutationRule;
import net.dasik.social.api.genetics.TraitConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.vanillaoutsider.naturalreproduction.command.NaturalReproductionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NaturalReproductionFabric implements ModInitializer {
    public static final String MOD_ID = "natural-reproduction";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final GameRuleCategory CATEGORY = DynamicGameRuleManager.registerCategory(
        Identifier.fromNamespaceAndPath(MOD_ID, "natural_reproduction")
    );

    public static GameRule<Boolean> ENABLED;
    public static GameRule<Integer> DENSITY_CAP;
    public static GameRule<Integer> RATE;
    public static GameRule<Boolean> SCALE_DROPS;
    public static GameRule<Boolean> CRAMPED_SPACE_PENALTY;
    public static GameRule<Boolean> BIOME_FERTILITY;
    public static GameRule<Boolean> BIOME_VARIANTS;

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
            .description("When true, breeding in cramped spaces stunting offspring scale down to 0.25x; breeding in spacious pastures recovers size genetics.")
            .register();

        BIOME_FERTILITY = DynamicGameRuleManager.booleanRule("natural-reproduction:biome_fertility", CATEGORY, true)
            .name("Native Biome Fertility Boost")
            .description("When true, animals in native biomes get 2x faster breeding frequency and +15% offspring genetics quality.")
            .register();

        BIOME_VARIANTS = DynamicGameRuleManager.booleanRule("natural-reproduction:biome_variants", CATEGORY, true)
            .name("Biome Variant Skin Adaptation")
            .description("When true, offspring born in specific biomes dynamically adapt their visual entity variant skin (e.g. Snowy Wolves, Desert Frogs).")
            .register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            NaturalReproductionCommand.register(dispatcher);
        });

        // Register Data-Driven Animal Genetics with DasikLibrary API across ALL vanilla animal species
        Map<String, TraitConfig> animalTraits = Map.of(
            "scale", new TraitConfig("scale", "minecraft:scale", "ADD_VALUE", 0.0f, 1.0f, 0.75f, 1.30f),
            "max_health", new TraitConfig("max_health", "minecraft:generic.max_health", "ADD_VALUE", 2.0f, 0.5f, -4.0f, 12.0f),
            "movement_speed", new TraitConfig("movement_speed", "minecraft:generic.movement_speed", "ADD_MULTIPLIED_BASE", 0.05f, 0.5f, -0.04f, 0.08f)
        );

        Map<String, Map<String, MutationRule>> animalMutations = Map.of(
            "default", Map.of(
                "scale", new MutationRule("uniform", 0.75f, 1.30f),
                "max_health", new MutationRule("triangular", -2.0f, 8.0f),
                "movement_speed", new MutationRule("triangular", -0.03f, 0.06f)
            )
        );

        GeneticsConfig config = new GeneticsConfig(animalTraits, animalMutations);

        // Livestock & Farmland Fauna
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.COW, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.PIG, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.SHEEP, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.CHICKEN, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.MOOSHROOM, config);

        // Equines & Camelids
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.HORSE, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.DONKEY, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.MULE, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.LLAMA, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.TRADER_LLAMA, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.CAMEL, config);

        // Canines & Felines
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.WOLF, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.CAT, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.FOX, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.OCELOT, config);

        // Amphibians & Aquatics
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.TURTLE, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.FROG, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.AXOLOTL, config);

        // Bears & Wild Mammals
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.POLAR_BEAR, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.PANDA, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.RABBIT, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.GOAT, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.ARMADILLO, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.SNIFFER, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.BEE, config);

        // Nether Fauna
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.STRIDER, config);
        EntityGeneticsRegistry.register(net.minecraft.world.entity.EntityTypes.HOGLIN, config);

        LOGGER.info("Natural Reproduction: Universal Data-Driven Genetics Registered for 25+ Animal Species (DasikLibrary API).");
    }
}
