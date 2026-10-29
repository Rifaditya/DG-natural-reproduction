// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.3
package net.vanillaoutsider.naturalreproduction.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.dasik.social.api.command.GameRuleCommandBridge;
import net.dasik.social.api.command.ModCommandDispatcher;
import net.dasik.social.api.command.UniversalCommandRegistry;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gamerules.GameRule;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;
import net.vanillaoutsider.naturalreproduction.util.BreedingLogEntry;
import net.vanillaoutsider.naturalreproduction.util.BreedingTrackerLogger;
import net.vanillaoutsider.naturalreproduction.util.SpatialBreedingCacheHelper;

public final class NaturalReproductionCommand {
    public static final String ROOT_LITERAL = "naturalreproduction";
    public static final String SHORT_ALIAS = "nr";
    public static final String MOD_NAME = "Natural Reproduction";

    public static final List<String> SPECIES_KEYS = List.of(
        "allow_cow", "allow_pig", "allow_sheep", "allow_chicken", "allow_mooshroom",
        "allow_horse", "allow_donkey", "allow_mule", "allow_llama", "allow_trader_llama",
        "allow_camel", "allow_wolf", "allow_cat", "allow_fox", "allow_ocelot",
        "allow_turtle", "allow_frog", "allow_axolotl", "allow_polar_bear", "allow_panda",
        "allow_rabbit", "allow_goat", "allow_armadillo", "allow_sniffer", "allow_bee",
        "allow_strider", "allow_hoglin"
    );

    private static volatile GameRuleCommandBridge activeBridge;

    private NaturalReproductionCommand() {}

    public static void init() {
        ModCommandDispatcher.init();
        activeBridge = createBridge();
        ModCommandDispatcher.registerSuite(ROOT_LITERAL, builder -> configureBuilder(builder, activeBridge, ROOT_LITERAL));
        ModCommandDispatcher.registerSuite(SHORT_ALIAS, builder -> configureBuilder(builder, activeBridge, SHORT_ALIAS));
    }

    public static UniversalCommandRegistry.Builder configureBuilder(
            UniversalCommandRegistry.Builder builder,
            GameRuleCommandBridge bridge,
            String literal
    ) {
        return builder
            .withRootLiteral(literal)
            .withModName(MOD_NAME)
            .withBridge(bridge)
            .withStatusSupplier(NaturalReproductionCommand::buildStatusLines)
            .withReloadAction(NaturalReproductionCommand::executeReloadAction)
            .withCustomSubcommand(Commands.literal("stats")
                .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                .executes(ctx -> executeStats(ctx, bridge)))
            .withCustomSubcommand(Commands.literal("purge")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(NaturalReproductionCommand::executePurgeAll)
                .then(Commands.literal("caches").executes(NaturalReproductionCommand::executePurgeCaches))
                .then(Commands.literal("logs").executes(NaturalReproductionCommand::executePurgeLogs))
                .then(Commands.literal("all").executes(NaturalReproductionCommand::executePurgeAll)))
            .withCustomSubcommand(Commands.literal("trackerlogs")
                .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                .executes(NaturalReproductionCommand::executeListLogs)
                .then(Commands.literal("list").executes(NaturalReproductionCommand::executeListLogs))
                .then(Commands.literal("enable")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(ctx -> executeSetTrackerLogs(ctx, true)))
                .then(Commands.literal("disable")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(ctx -> executeSetTrackerLogs(ctx, false)))
                .then(Commands.literal("clear")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(NaturalReproductionCommand::executeClearLogs)))
            .withCustomSubcommand(Commands.literal("logs")
                .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                .executes(NaturalReproductionCommand::executeListLogs)
                .then(Commands.literal("list").executes(NaturalReproductionCommand::executeListLogs))
                .then(Commands.literal("enable")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(ctx -> executeSetTrackerLogs(ctx, true)))
                .then(Commands.literal("disable")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(ctx -> executeSetTrackerLogs(ctx, false)))
                .then(Commands.literal("clear")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(NaturalReproductionCommand::executeClearLogs)));
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (activeBridge == null) {
            activeBridge = createBridge();
        }
        configureBuilder(UniversalCommandRegistry.builder(ROOT_LITERAL, dispatcher), activeBridge, ROOT_LITERAL).register();
        configureBuilder(UniversalCommandRegistry.builder(SHORT_ALIAS, dispatcher), activeBridge, SHORT_ALIAS).register();
    }

    public static GameRuleCommandBridge createBridge() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        // Register 19 core rules with saturated bounds
        bridge.registerBoolean("enabled", NaturalReproductionFabric.ENABLED, true);
        bridge.registerInteger("density_cap", NaturalReproductionFabric.DENSITY_CAP, 10, 1, 100);
        bridge.registerInteger("rate", NaturalReproductionFabric.RATE, 24000, 100, 240000);
        bridge.registerInteger("min_scale", NaturalReproductionFabric.MIN_SCALE, 10, 5, 100);
        bridge.registerInteger("normal_scale", NaturalReproductionFabric.NORMAL_SCALE, 95, 50, 150);
        bridge.registerInteger("max_scale", NaturalReproductionFabric.MAX_SCALE, 120, 100, 200);
        bridge.registerBoolean("scale_drops", NaturalReproductionFabric.SCALE_DROPS, true);
        bridge.registerBoolean("cramped_space_penalty", NaturalReproductionFabric.CRAMPED_SPACE_PENALTY, true);
        bridge.registerBoolean("inbreeding_degradation", NaturalReproductionFabric.INBREEDING_DEGRADATION, true);
        bridge.registerBoolean("pasture_enrichment", NaturalReproductionFabric.PASTURE_ENRICHMENT, true);
        bridge.registerBoolean("overgrazing", NaturalReproductionFabric.OVERGRAZING, true);
        bridge.registerBoolean("gestation_period", NaturalReproductionFabric.GESTATION_PERIOD, true);
        bridge.registerBoolean("manual_gestation", NaturalReproductionFabric.MANUAL_GESTATION, true);
        bridge.registerInteger("gestation_duration", NaturalReproductionFabric.GESTATION_DURATION, 24000, 100, 240000);
        bridge.registerBoolean("fertilized_chicken_eggs", NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS, true);
        bridge.registerBoolean("chicken_infertile_regular_eggs", NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS, true);
        bridge.registerInteger("dispenser_egg_hatch_chance", NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE, 75, 0, 100);
        bridge.registerBoolean("herd_dynamics", NaturalReproductionFabric.HERD_DYNAMICS, true);
        bridge.registerBoolean("herd_stampede", NaturalReproductionFabric.HERD_STAMPEDE, true);
        bridge.registerBoolean("biome_fertility", NaturalReproductionFabric.BIOME_FERTILITY, true);
        bridge.registerBoolean("biome_variants", NaturalReproductionFabric.BIOME_VARIANTS, true);
        bridge.registerBoolean("tracker_logs", NaturalReproductionFabric.TRACKER_LOGS, false);

        // Register 27 species toggles
        for (String key : SPECIES_KEYS) {
            GameRule<Boolean> rule = NaturalReproductionFabric.RULE_NAME_MAP.get(key);
            bridge.registerBoolean(key, rule, true);
        }
        NaturalReproductionFabric.RULE_NAME_MAP.forEach((name, rule) -> {
            bridge.registerBoolean(name, rule, true);
        });

        return bridge;
    }

    public static GameRuleCommandBridge getBridge() {
        if (activeBridge == null) {
            activeBridge = createBridge();
        }
        return activeBridge;
    }

    private static List<String> buildStatusLines(ServerLevel level) {
        if (level == null) {
            return List.of();
        }
        int count = 0;
        for (GameRule<Boolean> rule : NaturalReproductionFabric.SPECIES_TOGGLES.values()) {
            if (rule != null && DynamicGameRuleManager.getBoolean(level, rule)) {
                count++;
            }
        }
        return List.of(
            String.format("Species Toggles Active: %d/27", count),
            String.format("Spatial Caches: Density=%d, Pasture=%d",
                SpatialBreedingCacheHelper.getDensityCacheSize(),
                SpatialBreedingCacheHelper.getPastureCacheSize()),
            String.format("Reproduction Tracker Logs: %d events",
                BreedingTrackerLogger.getLogCount())
        );
    }

    private static void executeReloadAction(MinecraftServer server) {
        int refreshedCount = 0;
        if (server != null) {
            for (ServerLevel sl : server.getAllLevels()) {
                for (Entity entity : sl.getAllEntities()) {
                    if (entity instanceof LivingEntity living && DasikAnimalGeneticsAPI.hasGenetics(living)) {
                        GeneticsEngine.applyGeneticsModifiers(living);
                        refreshedCount++;
                    }
                }
            }
        }
        NaturalReproductionFabric.LOGGER.info("Natural Reproduction: Refreshed genetics & scale modifiers on {} loaded animal(s).", refreshedCount);
    }

    private static int executeStats(CommandContext<CommandSourceStack> ctx, GameRuleCommandBridge bridge) {
        CommandSourceStack src = ctx.getSource();
        int densityCacheSize = SpatialBreedingCacheHelper.getDensityCacheSize();
        int pastureCacheSize = SpatialBreedingCacheHelper.getPastureCacheSize();
        int logCount = BreedingTrackerLogger.getLogCount();
        src.sendSuccess(() -> Component.literal(
            String.format("§6=== [Natural Reproduction Stats] ===§r\n"
                + "§7Spatial Caches: Density Entries=§b%d§7, Pasture Entries=§b%d§7 | Autonomous Reproduction History: §b%d§7 recorded events",
                densityCacheSize, pastureCacheSize, logCount)
        ), false);
        return 1;
    }

    private static int executePurgeCaches(CommandContext<CommandSourceStack> ctx) {
        SpatialBreedingCacheHelper.clearCaches();
        ctx.getSource().sendSuccess(() -> Component.literal("§a[Natural Reproduction]§r Cleared all spatial density and pasture enrichment caches."), true);
        return 1;
    }

    private static int executePurgeLogs(CommandContext<CommandSourceStack> ctx) {
        BreedingTrackerLogger.clear();
        ctx.getSource().sendSuccess(() -> Component.literal("§a[Natural Reproduction Logs]§r All logged autonomous reproduction events cleared."), true);
        return 1;
    }

    private static int executePurgeAll(CommandContext<CommandSourceStack> ctx) {
        SpatialBreedingCacheHelper.clearCaches();
        BreedingTrackerLogger.clear();
        ctx.getSource().sendSuccess(() -> Component.literal("§a[Natural Reproduction]§r Cleared all spatial caches and reproduction tracker logs."), true);
        return 1;
    }

    private static int executeListLogs(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        List<BreedingLogEntry> entries = BreedingTrackerLogger.getRecentLogs(10);

        if (entries.isEmpty()) {
            src.sendSuccess(() -> Component.literal("§e[Natural Reproduction Logs]§r No autonomous breeding events recorded yet."), false);
            return 1;
        }

        src.sendSuccess(() -> Component.literal("§a=== Recent Autonomous Reproduction Logs (Last " + entries.size() + ") ==="), false);
        for (BreedingLogEntry entry : entries) {
            src.sendSuccess(() -> Component.literal(
                String.format("§7Day %d§r | §f%s§r at §b[%d, %d, %d]§r (%s) -> Scale: §e%.2fx§r [§6%s§r]",
                    entry.day(), entry.species(), entry.pos().getX(), entry.pos().getY(), entry.pos().getZ(),
                    entry.biomeId(), entry.scale(), entry.habitatStatus())
            ), false);
        }
        return 1;
    }

    private static int executeSetTrackerLogs(CommandContext<CommandSourceStack> ctx, boolean enabled) {
        ServerLevel level = ctx.getSource().getLevel();
        if (NaturalReproductionFabric.TRACKER_LOGS != null && level != null) {
            level.getGameRules().set(NaturalReproductionFabric.TRACKER_LOGS, enabled, level.getServer());
        }
        ctx.getSource().sendSuccess(() -> Component.literal("§a[Natural Reproduction]§r Set tracker_logs to " + enabled), true);
        return 1;
    }

    private static int executeClearLogs(CommandContext<CommandSourceStack> ctx) {
        BreedingTrackerLogger.clear();
        ctx.getSource().sendSuccess(() -> Component.literal("§a[Natural Reproduction Logs]§r All logged autonomous reproduction events cleared."), true);
        return 1;
    }
}
