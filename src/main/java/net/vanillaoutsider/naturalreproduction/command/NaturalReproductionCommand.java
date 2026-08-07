// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

public class NaturalReproductionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("naturalreproduction")
            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .then(Commands.literal("help")
                .executes(NaturalReproductionCommand::executeHelp))
            .then(Commands.literal("status")
                .executes(NaturalReproductionCommand::executeStatus))
            .then(Commands.literal("get")
                .then(Commands.literal("enabled")
                    .executes(ctx -> executeGetBool(ctx, "enabled")))
                .then(Commands.literal("density_cap")
                    .executes(ctx -> executeGetInt(ctx, "density_cap")))
                .then(Commands.literal("rate")
                    .executes(ctx -> executeGetInt(ctx, "rate")))
                .then(Commands.literal("scale_drops")
                    .executes(ctx -> executeGetBool(ctx, "scale_drops")))
                .then(Commands.literal("cramped_space_penalty")
                    .executes(ctx -> executeGetBool(ctx, "cramped_space_penalty")))
                .then(Commands.literal("biome_fertility")
                    .executes(ctx -> executeGetBool(ctx, "biome_fertility")))
                .then(Commands.literal("biome_variants")
                    .executes(ctx -> executeGetBool(ctx, "biome_variants"))))
            .then(Commands.literal("set")
                .then(Commands.literal("enabled")
                    .then(Commands.argument("val", BoolArgumentType.bool())
                        .executes(ctx -> executeSetBool(ctx, "enabled", BoolArgumentType.getBool(ctx, "val")))))
                .then(Commands.literal("density_cap")
                    .then(Commands.argument("val", IntegerArgumentType.integer(1, 100))
                        .executes(ctx -> executeSetInt(ctx, "density_cap", IntegerArgumentType.getInteger(ctx, "val")))))
                .then(Commands.literal("rate")
                    .then(Commands.argument("val", IntegerArgumentType.integer(100, 240000))
                        .executes(ctx -> executeSetInt(ctx, "rate", IntegerArgumentType.getInteger(ctx, "val")))))
                .then(Commands.literal("scale_drops")
                    .then(Commands.argument("val", BoolArgumentType.bool())
                        .executes(ctx -> executeSetBool(ctx, "scale_drops", BoolArgumentType.getBool(ctx, "val")))))
                .then(Commands.literal("cramped_space_penalty")
                    .then(Commands.argument("val", BoolArgumentType.bool())
                        .executes(ctx -> executeSetBool(ctx, "cramped_space_penalty", BoolArgumentType.getBool(ctx, "val")))))
                .then(Commands.literal("biome_fertility")
                    .then(Commands.argument("val", BoolArgumentType.bool())
                        .executes(ctx -> executeSetBool(ctx, "biome_fertility", BoolArgumentType.getBool(ctx, "val")))))
                .then(Commands.literal("biome_variants")
                    .then(Commands.argument("val", BoolArgumentType.bool())
                        .executes(ctx -> executeSetBool(ctx, "biome_variants", BoolArgumentType.getBool(ctx, "val"))))))
            .then(Commands.literal("reset")
                .executes(NaturalReproductionCommand::executeReset))
            .then(Commands.literal("reload")
                .executes(NaturalReproductionCommand::executeReload))
        );
    }

    private static int executeHelp(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        src.sendSuccess(() -> Component.literal("=== Natural Reproduction Commands ==="), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction status - Display current rule states"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction get <rule> - Get value of a specific rule"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction set <rule> <val> - Modify a rule setting"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction reset - Reset all rules to defaults"), false);
        src.sendSuccess(() -> Component.literal("/naturalreproduction reload - Reload configuration"), false);
        return 1;
    }

    private static int executeStatus(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        boolean enabled = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.ENABLED);
        int cap = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.DENSITY_CAP);
        int rate = DynamicGameRuleManager.getInt(level, NaturalReproductionFabric.RATE);
        boolean scaleDrops = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.SCALE_DROPS);
        boolean crampedPenalty = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.CRAMPED_SPACE_PENALTY);
        boolean biomeFertility = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_FERTILITY);
        boolean biomeVariants = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_VARIANTS);

        ctx.getSource().sendSuccess(() -> Component.literal(
            String.format("Natural Reproduction Status: Enabled=%b, Density Cap=%d, Rate=%d, Scale Drops=%b, Cramped Penalty=%b, Biome Fertility=%b, Biome Variants=%b", enabled, cap, rate, scaleDrops, crampedPenalty, biomeFertility, biomeVariants)
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
        } else if ("biome_fertility".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_FERTILITY);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:biome_fertility = " + val), false);
        } else if ("biome_variants".equals(ruleName)) {
            boolean val = DynamicGameRuleManager.getBoolean(level, NaturalReproductionFabric.BIOME_VARIANTS);
            ctx.getSource().sendSuccess(() -> Component.literal("natural-reproduction:biome_variants = " + val), false);
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
        } else if ("biome_fertility".equals(ruleName) && NaturalReproductionFabric.BIOME_FERTILITY != null) {
            level.getGameRules().set(NaturalReproductionFabric.BIOME_FERTILITY, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:biome_fertility to " + value), true);
        } else if ("biome_variants".equals(ruleName) && NaturalReproductionFabric.BIOME_VARIANTS != null) {
            level.getGameRules().set(NaturalReproductionFabric.BIOME_VARIANTS, value, level.getServer());
            ctx.getSource().sendSuccess(() -> Component.literal("Set natural-reproduction:biome_variants to " + value), true);
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
        if (NaturalReproductionFabric.SCALE_DROPS != null) {
            level.getGameRules().set(NaturalReproductionFabric.SCALE_DROPS, true, level.getServer());
        }
        if (NaturalReproductionFabric.CRAMPED_SPACE_PENALTY != null) {
            level.getGameRules().set(NaturalReproductionFabric.CRAMPED_SPACE_PENALTY, true, level.getServer());
        }
        if (NaturalReproductionFabric.BIOME_FERTILITY != null) {
            level.getGameRules().set(NaturalReproductionFabric.BIOME_FERTILITY, true, level.getServer());
        }
        if (NaturalReproductionFabric.BIOME_VARIANTS != null) {
            level.getGameRules().set(NaturalReproductionFabric.BIOME_VARIANTS, true, level.getServer());
        }
        ctx.getSource().sendSuccess(() -> Component.literal("Reset all Natural Reproduction rules to default settings."), true);
        return 1;
    }

    private static int executeReload(CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendSuccess(() -> Component.literal("Natural Reproduction configuration reloaded."), true);
        return 1;
    }
}
