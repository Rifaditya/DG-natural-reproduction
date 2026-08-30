// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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

import java.util.List;

public class NaturalReproductionCommand {

    private static final List<String> SPECIES_KEYS = List.of(
        "allow_cow", "allow_pig", "allow_sheep", "allow_chicken", "allow_mooshroom",
        "allow_horse", "allow_donkey", "allow_mule", "allow_llama", "allow_trader_llama",
        "allow_camel", "allow_wolf", "allow_cat", "allow_fox", "allow_ocelot",
        "allow_turtle", "allow_frog", "allow_axolotl", "allow_polar_bear", "allow_panda",
        "allow_rabbit", "allow_goat", "allow_armadillo", "allow_sniffer", "allow_bee",
        "allow_strider", "allow_hoglin"
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> getSubtree = Commands.literal("get")
            .then(Commands.literal("enabled").executes(ctx -> executeGetBool(ctx, "enabled")))
            .then(Commands.literal("density_cap").executes(ctx -> executeGetInt(ctx, "density_cap")))
            .then(Commands.literal("rate").executes(ctx -> executeGetInt(ctx, "rate")))
            .then(Commands.literal("scale_drops").executes(ctx -> executeGetBool(ctx, "scale_drops")))
            .then(Commands.literal("cramped_space_penalty").executes(ctx -> executeGetBool(ctx, "cramped_space_penalty")))
            .then(Commands.literal("inbreeding_degradation").executes(ctx -> executeGetBool(ctx, "inbreeding_degradation")))
            .then(Commands.literal("pasture_enrichment").executes(ctx -> executeGetBool(ctx, "pasture_enrichment")))
            .then(Commands.literal("overgrazing").executes(ctx -> executeGetBool(ctx, "overgrazing")))
            .then(Commands.literal("gestation_period").executes(ctx -> executeGetBool(ctx, "gestation_period")))
            .then(Commands.literal("manual_gestation").executes(ctx -> executeGetBool(ctx, "manual_gestation")))
            .then(Commands.literal("gestation_duration").executes(ctx -> executeGetInt(ctx, "gestation_duration")))
            .then(Commands.literal("fertilized_chicken_eggs").executes(ctx -> executeGetBool(ctx, "fertilized_chicken_eggs")))
            .then(Commands.literal("chicken_infertile_regular_eggs").executes(ctx -> executeGetBool(ctx, "chicken_infertile_regular_eggs")))
            .then(Commands.literal("dispenser_egg_hatch_chance").executes(ctx -> executeGetInt(ctx, "dispenser_egg_hatch_chance")))
            .then(Commands.literal("herd_dynamics").executes(ctx -> executeGetBool(ctx, "herd_dynamics")))
            .then(Commands.literal("herd_stampede").executes(ctx -> executeGetBool(ctx, "herd_stampede")))
            .then(Commands.literal("biome_fertility").executes(ctx -> executeGetBool(ctx, "biome_fertility")))
            .then(Commands.literal("biome_variants").executes(ctx -> executeGetBool(ctx, "biome_variants")))
            .then(Commands.literal("tracker_logs").executes(ctx -> executeGetBool(ctx, "tracker_logs")))
            .then(Commands.literal("min_scale").executes(ctx -> executeGetInt(ctx, "min_scale")))
            .then(Commands.literal("max_scale").executes(ctx -> executeGetInt(ctx, "max_scale")));

        for (String key : SPECIES_KEYS) {
            getSubtree.then(Commands.literal(key).executes(ctx -> executeGetBool(ctx, key)));
        }

        LiteralArgumentBuilder<CommandSourceStack> setSubtree = Commands.literal("set")
            .then(Commands.literal("enabled")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "enabled", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("density_cap")
                .then(Commands.argument("val", IntegerArgumentType.integer(1, 100))
                    .executes(ctx -> executeSetInt(ctx, "density_cap", IntegerArgumentType.getInteger(ctx, "val")))))
            .then(Commands.literal("rate")
                .then(Commands.argument("val", IntegerArgumentType.integer(100, 240000))
                    .executes(ctx -> executeSetInt(ctx, "rate", IntegerArgumentType.getInteger(ctx, "val")))))
            .then(Commands.literal("min_scale")
                .then(Commands.argument("val", IntegerArgumentType.integer(10, 100))
                    .executes(ctx -> executeSetInt(ctx, "min_scale", IntegerArgumentType.getInteger(ctx, "val")))))
            .then(Commands.literal("max_scale")
                .then(Commands.argument("val", IntegerArgumentType.integer(100, 300))
                    .executes(ctx -> executeSetInt(ctx, "max_scale", IntegerArgumentType.getInteger(ctx, "val")))))
            .then(Commands.literal("scale_drops")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "scale_drops", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("cramped_space_penalty")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "cramped_space_penalty", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("inbreeding_degradation")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "inbreeding_degradation", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("pasture_enrichment")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "pasture_enrichment", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("overgrazing")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "overgrazing", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("gestation_period")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "gestation_period", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("manual_gestation")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "manual_gestation", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("gestation_duration")
                .then(Commands.argument("val", IntegerArgumentType.integer(100, 240000))
                    .executes(ctx -> executeSetInt(ctx, "gestation_duration", IntegerArgumentType.getInteger(ctx, "val")))))
            .then(Commands.literal("fertilized_chicken_eggs")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "fertilized_chicken_eggs", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("chicken_infertile_regular_eggs")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "chicken_infertile_regular_eggs", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("dispenser_egg_hatch_chance")
                .then(Commands.argument("val", IntegerArgumentType.integer(0, 100))
                    .executes(ctx -> executeSetInt(ctx, "dispenser_egg_hatch_chance", IntegerArgumentType.getInteger(ctx, "val")))))
            .then(Commands.literal("herd_dynamics")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "herd_dynamics", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("herd_stampede")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "herd_stampede", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("biome_fertility")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "biome_fertility", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("biome_variants")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "biome_variants", BoolArgumentType.getBool(ctx, "val")))))
            .then(Commands.literal("tracker_logs")
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, "tracker_logs", BoolArgumentType.getBool(ctx, "val")))));

        for (String key : SPECIES_KEYS) {
            setSubtree.then(Commands.literal(key)
                .then(Commands.argument("val", BoolArgumentType.bool())
                    .executes(ctx -> executeSetBool(ctx, key, BoolArgumentType.getBool(ctx, "val")))));
        }

        dispatcher.register(Commands.literal("naturalreproduction")
            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .then(Commands.literal("help").executes(NaturalReproductionCommand::executeHelp))
            .then(Commands.literal("status").executes(NaturalReproductionCommand::executeStatus))
            .then(getSubtree)
            .then(setSubtree)
            .then(Commands.literal("reset").executes(NaturalReproductionCommand::executeReset))
            .then(Commands.literal("reload").executes(NaturalReproductionCommand::executeReload))
            .then(Commands.literal("trackerlogs")
                .executes(NaturalReproductionCommand::executeListLogs)
                .then(Commands.literal("list").executes(NaturalReproductionCommand::executeListLogs))
                .then(Commands.literal("enable").executes(ctx -> executeSetBool(ctx, "tracker_logs", true)))
                .then(Commands.literal("disable").executes(ctx -> executeSetBool(ctx, "tracker_logs", false)))
                .then(Commands.literal("clear").executes(NaturalReproductionCommand::executeClearLogs)))
            .then(Commands.literal("logs")
                .executes(NaturalReproductionCommand::executeListLogs)
                .then(Commands.literal("list").executes(NaturalReproductionCommand::executeListLogs))
                .then(Commands.literal("enable").executes(ctx -> executeSetBool(ctx, "tracker_logs", true)))
                .then(Commands.literal("disable").executes(ctx -> executeSetBool(ctx, "tracker_logs", false)))
                .then(Commands.literal("clear").executes(NaturalReproductionCommand::executeClearLogs)))
        );
    }

    private static int executeHelp(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        src.sendSuccess(() -> Component.literal("=== Natural Reproduction Commands ==="), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction status - Display current rule states"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction get <rule> - Get value of a specific rule"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction set <rule> <val> - Modify a rule setting"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction trackerlogs - View autonomous reproduction event logs"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction trackerlogs enable/disable - Enable or disable reproduction logging"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction trackerlogs clear - Clear reproduction event log history"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction reset - Reset all rules to defaults"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction reload - Reload configuration"), false);
        return 1;
    }

    private static int executeStatus(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        boolean enabled = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.ENABLED);
        int cap = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DENSITY_CAP);
        int rate = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.RATE);
        int minScale = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.MIN_SCALE);
        int maxScale = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.MAX_SCALE);
        boolean scaleDrops = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.SCALE_DROPS);
        boolean crampedPenalty = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.CRAMPED_SPACE_PENALTY);
        boolean inbreeding = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.INBREEDING_DEGRADATION);
        boolean pastureEnrichment = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.PASTURE_ENRICHMENT);
        boolean overgrazing = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.OVERGRAZING);
        boolean gestation = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.GESTATION_PERIOD);
        boolean manualGestation = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.MANUAL_GESTATION);
        int gestationDuration = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.GESTATION_DURATION);
        boolean fertEggs = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS);
        boolean infertileReg = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS);
        int dispenserRate = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE);
        boolean herdDynamics = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.HERD_DYNAMICS);
        boolean herdStampede = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.HERD_STAMPEDE);
        boolean biomeFertility = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_FERTILITY);
        boolean biomeVariants = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_VARIANTS);
        boolean trackerLogs = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.TRACKER_LOGS);

        int count = 0;
        for (GameRule<Boolean> rule : NaturalReproductionFabric.SPECIES_TOGGLES.values()) {
            if (DynamicGameRuleManager.getBoolean(level, rule)) {
                count++;
            }
        }
        final int enabledSpeciesCount = count;

        ctx.getSource().sendSuccess(() -> Component.literal(
            String.format("Natural Reproduction Status: Enabled=%b, Density Cap=%d, Rate=%d, Min Scale=%d%%, Max Scale=%d%%, Scale Drops=%b, Cramped Penalty=%b, Inbreeding Degradation=%b, Pasture Enrichment=%b, Overgrazing=%b, Gestation=%b (Manual=%b, Duration=%d), Fertilized Eggs=%b, Infertile Reg Eggs=%b, Dispenser Hatch Rate=%d%%, Herd Dynamics=%b, Herd Stampede=%b, Biome Fertility=%b, Biome Variants=%b, Tracker Logs=%b, Species Toggles Enabled=%d/27", enabled, cap, rate, minScale, maxScale, scaleDrops, crampedPenalty, inbreeding, pastureEnrichment, overgrazing, gestation, manualGestation, gestationDuration, fertEggs, infertileReg, dispenserRate, herdDynamics, herdStampede, biomeFertility, biomeVariants, trackerLogs, enabledSpeciesCount)
        ), false);
        return 1;
    }

    private static int executeGetBool(CommandContext<CommandSourceStack> ctx, String ruleName) {
        ServerLevel level = ctx.getSource().getLevel();
        if ("enabled".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.ENABLED);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:enabled = " + val), false);
        } else if ("scale_drops".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.SCALE_DROPS);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:scale_drops = " + val), false);
        } else if ("cramped_space_penalty".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.CRAMPED_SPACE_PENALTY);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:cramped_space_penalty = " + val), false);
        } else if ("inbreeding_degradation".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.INBREEDING_DEGRADATION);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:inbreeding_degradation = " + val), false);
        } else if ("pasture_enrichment".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.PASTURE_ENRICHMENT);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:pasture_enrichment = " + val), false);
        } else if ("overgrazing".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.OVERGRAZING);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:overgrazing = " + val), false);
        } else if ("gestation_period".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.GESTATION_PERIOD);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:gestation_period = " + val), false);
        } else if ("manual_gestation".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.MANUAL_GESTATION);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:manual_gestation = " + val), false);
        } else if ("fertilized_chicken_eggs".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:fertilized_chicken_eggs = " + val), false);
        } else if ("chicken_infertile_regular_eggs".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:chicken_infertile_regular_eggs = " + val), false);
        } else if ("herd_dynamics".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.HERD_DYNAMICS);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:herd_dynamics = " + val), false);
        } else if ("herd_stampede".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.HERD_STAMPEDE);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:herd_stampede = " + val), false);
        } else if ("biome_fertility".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_FERTILITY);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:biome_fertility = " + val), false);
        } else if ("biome_variants".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_VARIANTS);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:biome_variants = " + val), false);
        } else if ("tracker_logs".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.TRACKER_LOGS);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:tracker_logs = " + val), false);
        } else if (NaturalReproductionFabric.RULE_NAME_MAP.containsKey(ruleName)) {
            GameRule<Boolean> rule = NaturalReproductionFabric.RULE_NAME_MAP.get(ruleName);
            if (rule != null) {
                boolean val = DynamicGameRuleManager.getBoolean(level, rule);
                ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:" + ruleName + " = " + val), false);
            }
        }
        return 1;
    }

    private static int executeGetInt(CommandContext<CommandSourceStack> ctx, String ruleName) {
        ServerLevel level = ctx.getSource().getLevel();
        if ("density_cap".equals(ruleName)) {
            int val = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DENSITY_CAP);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:density_cap = " + val), false);
        } else if ("rate".equals(ruleName)) {
            int val = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.RATE);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:rate = " + val + " ticks"), false);
        } else if ("gestation_duration".equals(ruleName)) {
            int val = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.GESTATION_DURATION);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:gestation_duration = " + val + " ticks"), false);
        } else if ("dispenser_egg_hatch_chance".equals(ruleName)) {
            int val = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:dispenser_egg_hatch_chance = " + val + "%"), false);
        } else if ("min_scale".equals(ruleName)) {
            int val = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.MIN_SCALE);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:min_scale = " + val + "%"), false);
        } else if ("max_scale".equals(ruleName)) {
            int val = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.MAX_SCALE);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:max_scale = " + val + "%"), false);
        }
        return 1;
    }

    private static int executeSetBool(CommandContext<CommandSourceStack> ctx, String ruleName, boolean value) {
        ServerLevel level = ctx.getSource().getLevel();
        if ("enabled".equals(ruleName) && NaturalReproductionFabric.ENABLED != null) {
            level.getGameRules().set(NaturalReproductionFabric.ENABLED, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:enabled to " + value), true);
        } else if ("scale_drops".equals(ruleName) && NaturalReproductionFabric.SCALE_DROPS != null) {
            level.getGameRules().set(NaturalReproductionFabric.SCALE_DROPS, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:scale_drops to " + value), true);
        } else if ("cramped_space_penalty".equals(ruleName) && NaturalReproductionFabric.CRAMPED_SPACE_PENALTY != null) {
            level.getGameRules().set(NaturalReproductionFabric.CRAMPED_SPACE_PENALTY, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:cramped_space_penalty to " + value), true);
        } else if ("inbreeding_degradation".equals(ruleName) && NaturalReproductionFabric.INBREEDING_DEGRADATION != null) {
            level.getGameRules().set(NaturalReproductionFabric.INBREEDING_DEGRADATION, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:inbreeding_degradation to " + value), true);
        } else if ("pasture_enrichment".equals(ruleName) && NaturalReproductionFabric.PASTURE_ENRICHMENT != null) {
            level.getGameRules().set(NaturalReproductionFabric.PASTURE_ENRICHMENT, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:pasture_enrichment to " + value), true);
        } else if ("overgrazing".equals(ruleName) && NaturalReproductionFabric.OVERGRAZING != null) {
            level.getGameRules().set(NaturalReproductionFabric.OVERGRAZING, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:overgrazing to " + value), true);
        } else if ("gestation_period".equals(ruleName) && NaturalReproductionFabric.GESTATION_PERIOD != null) {
            level.getGameRules().set(NaturalReproductionFabric.GESTATION_PERIOD, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:gestation_period to " + value), true);
        } else if ("manual_gestation".equals(ruleName) && NaturalReproductionFabric.MANUAL_GESTATION != null) {
            level.getGameRules().set(NaturalReproductionFabric.MANUAL_GESTATION, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:manual_gestation to " + value), true);
        } else if ("fertilized_chicken_eggs".equals(ruleName) && NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS != null) {
            level.getGameRules().set(NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:fertilized_chicken_eggs to " + value), true);
        } else if ("chicken_infertile_regular_eggs".equals(ruleName) && NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS != null) {
            level.getGameRules().set(NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:chicken_infertile_regular_eggs to " + value), true);
        } else if ("herd_dynamics".equals(ruleName) && NaturalReproductionFabric.HERD_DYNAMICS != null) {
            level.getGameRules().set(NaturalReproductionFabric.HERD_DYNAMICS, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:herd_dynamics to " + value), true);
        } else if ("herd_stampede".equals(ruleName) && NaturalReproductionFabric.HERD_STAMPEDE != null) {
            level.getGameRules().set(NaturalReproductionFabric.HERD_STAMPEDE, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:herd_stampede to " + value), true);
        } else if ("biome_fertility".equals(ruleName) && NaturalReproductionFabric.BIOME_FERTILITY != null) {
            level.getGameRules().set(NaturalReproductionFabric.BIOME_FERTILITY, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:biome_fertility to " + value), true);
        } else if ("biome_variants".equals(ruleName) && NaturalReproductionFabric.BIOME_VARIANTS != null) {
            level.getGameRules().set(NaturalReproductionFabric.BIOME_VARIANTS, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:biome_variants to " + value), true);
        } else if ("tracker_logs".equals(ruleName) && NaturalReproductionFabric.TRACKER_LOGS != null) {
            level.getGameRules().set(NaturalReproductionFabric.TRACKER_LOGS, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:tracker_logs to " + value), true);
        } else if (NaturalReproductionFabric.RULE_NAME_MAP.containsKey(ruleName)) {
            GameRule<Boolean> rule = NaturalReproductionFabric.RULE_NAME_MAP.get(ruleName);
            if (rule != null) {
                level.getGameRules().set(rule, value, level.getServer());
                ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:" + ruleName + " to " + value), true);
            }
        }
        return 1;
    }

    private static int executeSetInt(CommandContext<CommandSourceStack> ctx, String ruleName, int value) {
        ServerLevel level = ctx.getSource().getLevel();
        if ("density_cap".equals(ruleName) && NaturalReproductionFabric.DENSITY_CAP != null) {
            level.getGameRules().set(NaturalReproductionFabric.DENSITY_CAP, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:density_cap to " + value), true);
        } else if ("rate".equals(ruleName) && NaturalReproductionFabric.RATE != null) {
            level.getGameRules().set(NaturalReproductionFabric.RATE, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:rate to " + value), true);
        } else if ("gestation_duration".equals(ruleName) && NaturalReproductionFabric.GESTATION_DURATION != null) {
            level.getGameRules().set(NaturalReproductionFabric.GESTATION_DURATION, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:gestation_duration to " + value), true);
        } else if ("dispenser_egg_hatch_chance".equals(ruleName) && NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE != null) {
            level.getGameRules().set(NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:dispenser_egg_hatch_chance to " + value + "%"), true);
        } else if ("min_scale".equals(ruleName) && NaturalReproductionFabric.MIN_SCALE != null) {
            level.getGameRules().set(NaturalReproductionFabric.MIN_SCALE, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:min_scale to " + value + "%"), true);
        } else if ("max_scale".equals(ruleName) && NaturalReproductionFabric.MAX_SCALE != null) {
            level.getGameRules().set(NaturalReproductionFabric.MAX_SCALE, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:max_scale to " + value + "%"), true);
        }
        return 1;
    }

    private static int executeReset(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        if (NaturalReproductionFabric.ENABLED != null) {
            level.getGameRules().set(NaturalReproductionFabric.ENABLED, true, level.getServer());
        }
        if (NaturalReproductionFabric.DENSITY_CAP != null) {
            level.getGameRules().set(NaturalReproductionFabric.DENSITY_CAP, 10, level.getServer());
        }
        if (NaturalReproductionFabric.RATE != null) {
            level.getGameRules().set(NaturalReproductionFabric.RATE, 24000, level.getServer());
        }
        if (NaturalReproductionFabric.MIN_SCALE != null) {
            level.getGameRules().set(NaturalReproductionFabric.MIN_SCALE, 50, level.getServer());
        }
        if (NaturalReproductionFabric.MAX_SCALE != null) {
            level.getGameRules().set(NaturalReproductionFabric.MAX_SCALE, 130, level.getServer());
        }
        if (NaturalReproductionFabric.SCALE_DROPS != null) {
            level.getGameRules().set(NaturalReproductionFabric.SCALE_DROPS, true, level.getServer());
        }
        if (NaturalReproductionFabric.CRAMPED_SPACE_PENALTY != null) {
            level.getGameRules().set(NaturalReproductionFabric.CRAMPED_SPACE_PENALTY, true, level.getServer());
        }
        if (NaturalReproductionFabric.INBREEDING_DEGRADATION != null) {
            level.getGameRules().set(NaturalReproductionFabric.INBREEDING_DEGRADATION, true, level.getServer());
        }
        if (NaturalReproductionFabric.PASTURE_ENRICHMENT != null) {
            level.getGameRules().set(NaturalReproductionFabric.PASTURE_ENRICHMENT, true, level.getServer());
        }
        if (NaturalReproductionFabric.OVERGRAZING != null) {
            level.getGameRules().set(NaturalReproductionFabric.OVERGRAZING, true, level.getServer());
        }
        if (NaturalReproductionFabric.GESTATION_PERIOD != null) {
            level.getGameRules().set(NaturalReproductionFabric.GESTATION_PERIOD, true, level.getServer());
        }
        if (NaturalReproductionFabric.MANUAL_GESTATION != null) {
            level.getGameRules().set(NaturalReproductionFabric.MANUAL_GESTATION, true, level.getServer());
        }
        if (NaturalReproductionFabric.GESTATION_DURATION != null) {
            level.getGameRules().set(NaturalReproductionFabric.GESTATION_DURATION, 24000, level.getServer());
        }
        if (NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS != null) {
            level.getGameRules().set(NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS, true, level.getServer());
        }
        if (NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS != null) {
            level.getGameRules().set(NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS, true, level.getServer());
        }
        if (NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE != null) {
            level.getGameRules().set(NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE, 75, level.getServer());
        }
        if (NaturalReproductionFabric.HERD_DYNAMICS != null) {
            level.getGameRules().set(NaturalReproductionFabric.HERD_DYNAMICS, true, level.getServer());
        }
        if (NaturalReproductionFabric.HERD_STAMPEDE != null) {
            level.getGameRules().set(NaturalReproductionFabric.HERD_STAMPEDE, true, level.getServer());
        }
        if (NaturalReproductionFabric.BIOME_FERTILITY != null) {
            level.getGameRules().set(NaturalReproductionFabric.BIOME_FERTILITY, true, level.getServer());
        }
        if (NaturalReproductionFabric.BIOME_VARIANTS != null) {
            level.getGameRules().set(NaturalReproductionFabric.BIOME_VARIANTS, true, level.getServer());
        }
        if (NaturalReproductionFabric.TRACKER_LOGS != null) {
            level.getGameRules().set(NaturalReproductionFabric.TRACKER_LOGS, false, level.getServer());
        }
        for (GameRule<Boolean> speciesRule : NaturalReproductionFabric.SPECIES_TOGGLES.values()) {
            if (speciesRule != null) {
                level.getGameRules().set(speciesRule, true, level.getServer());
            }
        }
        ctx.getSource().sendSuccess(() -> Component.literal("Reset all Natural Reproduction rules (including all 27 species toggles) to default settings."), true);
        return 1;
    }

    private static int executeReload(CommandContext<CommandSourceStack> ctx) {
        MinecraftServer server = ctx.getSource().getServer();
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
        int finalCount = refreshedCount;
        ctx.getSource().sendSuccess(() -> Component.literal(
            String.format("Natural Reproduction configuration reloaded. Refreshed genetics & scale modifiers on %d loaded animal(s).", finalCount)), true);
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

    private static int executeClearLogs(CommandContext<CommandSourceStack> ctx) {
        BreedingTrackerLogger.clear();
        ctx.getSource().sendSuccess(() -> Component.literal("§a[Natural Reproduction Logs]§r All logged autonomous reproduction events cleared."), true);
        return 1;
    }
}
