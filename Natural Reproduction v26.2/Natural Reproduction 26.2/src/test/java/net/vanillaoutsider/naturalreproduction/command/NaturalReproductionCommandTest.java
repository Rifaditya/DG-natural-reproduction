// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Zero-Mock Headless Brigadier Command Suite for Natural Reproduction.
 * Validates registration parity, syntax tree parsing, aliases (/nr and /naturalreproduction),
 * tab completion suggestions, bounds enforcement, and OP permission gating.
 */
public class NaturalReproductionCommandTest {

    private CommandDispatcher<CommandSourceStack> dispatcher;
    private CommandSourceStack adminSource;
    private CommandSourceStack unprivilegedSource;

    @BeforeAll
    static void initMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @BeforeEach
    void setUp() {
        dispatcher = new CommandDispatcher<>();
        NaturalReproductionCommand.register(dispatcher);

        adminSource = new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                PermissionSet.ALL_PERMISSIONS,
                "AdminTester",
                Component.literal("AdminTester"),
                null,
                null
        );

        unprivilegedSource = new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                PermissionSet.NO_PERMISSIONS,
                "GuestTester",
                Component.literal("GuestTester"),
                null,
                null
        );
    }

    @Test
    @DisplayName("Registration: Both /naturalreproduction and /nr are registered with identical subtrees")
    void testRootCommandRegistration() {
        CommandNode<CommandSourceStack> fullNode = dispatcher.getRoot().getChild("naturalreproduction");
        CommandNode<CommandSourceStack> aliasNode = dispatcher.getRoot().getChild("nr");

        assertNotNull(fullNode, "Root command /naturalreproduction must be registered");
        assertNotNull(aliasNode, "Short alias /nr must be registered");

        String[] expectedSubcommands = {
            "help", "status", "stats", "get", "set", "reset", "reload", "purge", "trackerlogs", "logs"
        };

        for (String sub : expectedSubcommands) {
            assertNotNull(fullNode.getChild(sub), "Subcommand /naturalreproduction " + sub + " is missing");
            assertNotNull(aliasNode.getChild(sub), "Subcommand /nr " + sub + " is missing");
        }
    }

    @Test
    @DisplayName("Syntax: Tab completions for /nr get include all core rules and species toggles")
    void testGetCompletions() throws Exception {
        CompletableFuture<Suggestions> future = dispatcher.getCompletionSuggestions(
                dispatcher.parse("nr get ", adminSource)
        );
        Suggestions suggestions = future.get();
        List<String> list = suggestions.getList().stream().map(Suggestion::getText).toList();

        // Core rules
        assertTrue(list.contains("enabled"), "Missing 'enabled' suggestion");
        assertTrue(list.contains("density_cap"), "Missing 'density_cap' suggestion");
        assertTrue(list.contains("rate"), "Missing 'rate' suggestion");
        assertTrue(list.contains("min_scale"), "Missing 'min_scale' suggestion");
        assertTrue(list.contains("normal_scale"), "Missing 'normal_scale' suggestion");
        assertTrue(list.contains("max_scale"), "Missing 'max_scale' suggestion");
        assertTrue(list.contains("scale_drops"), "Missing 'scale_drops' suggestion");
        assertTrue(list.contains("cramped_space_penalty"), "Missing 'cramped_space_penalty' suggestion");
        assertTrue(list.contains("inbreeding_degradation"), "Missing 'inbreeding_degradation' suggestion");
        assertTrue(list.contains("pasture_enrichment"), "Missing 'pasture_enrichment' suggestion");
        assertTrue(list.contains("overgrazing"), "Missing 'overgrazing' suggestion");
        assertTrue(list.contains("gestation_period"), "Missing 'gestation_period' suggestion");
        assertTrue(list.contains("manual_gestation"), "Missing 'manual_gestation' suggestion");
        assertTrue(list.contains("gestation_duration"), "Missing 'gestation_duration' suggestion");
        assertTrue(list.contains("fertilized_chicken_eggs"), "Missing 'fertilized_chicken_eggs' suggestion");
        assertTrue(list.contains("chicken_infertile_regular_eggs"), "Missing 'chicken_infertile_regular_eggs' suggestion");
        assertTrue(list.contains("dispenser_egg_hatch_chance"), "Missing 'dispenser_egg_hatch_chance' suggestion");
        assertTrue(list.contains("herd_dynamics"), "Missing 'herd_dynamics' suggestion");
        assertTrue(list.contains("herd_stampede"), "Missing 'herd_stampede' suggestion");
        assertTrue(list.contains("biome_fertility"), "Missing 'biome_fertility' suggestion");
        assertTrue(list.contains("biome_variants"), "Missing 'biome_variants' suggestion");
        assertTrue(list.contains("tracker_logs"), "Missing 'tracker_logs' suggestion");

        // Species toggles
        assertTrue(list.contains("allow_cow"), "Missing 'allow_cow' suggestion");
        assertTrue(list.contains("allow_pig"), "Missing 'allow_pig' suggestion");
        assertTrue(list.contains("allow_sheep"), "Missing 'allow_sheep' suggestion");
        assertTrue(list.contains("allow_chicken"), "Missing 'allow_chicken' suggestion");
        assertTrue(list.contains("allow_horse"), "Missing 'allow_horse' suggestion");
        assertTrue(list.contains("allow_sniffer"), "Missing 'allow_sniffer' suggestion");
    }

    @Test
    @DisplayName("Syntax: All valid /nr command branches parse cleanly with zero unread input")
    void testValidCommandParsing() {
        // Read-only queries
        assertParseSuccess("nr help", adminSource);
        assertParseSuccess("naturalreproduction help", adminSource);
        assertParseSuccess("nr status", adminSource);
        assertParseSuccess("nr stats", adminSource);
        assertParseSuccess("nr get enabled", adminSource);
        assertParseSuccess("nr get density_cap", adminSource);
        assertParseSuccess("nr get rate", adminSource);
        assertParseSuccess("nr get allow_cow", adminSource);

        // Modifying commands (booleans & integers)
        assertParseSuccess("nr set enabled true", adminSource);
        assertParseSuccess("nr set enabled false", adminSource);
        assertParseSuccess("nr set density_cap 10", adminSource);
        assertParseSuccess("nr set rate 24000", adminSource);
        assertParseSuccess("nr set min_scale 10", adminSource);
        assertParseSuccess("nr set normal_scale 95", adminSource);
        assertParseSuccess("nr set max_scale 120", adminSource);
        assertParseSuccess("nr set dispenser_egg_hatch_chance 75", adminSource);
        assertParseSuccess("nr set inbreeding_degradation true", adminSource);
        assertParseSuccess("nr set allow_pig true", adminSource);
        assertParseSuccess("nr set allow_pig false", adminSource);

        // Admin utilities
        assertParseSuccess("nr reset", adminSource);
        assertParseSuccess("nr reload", adminSource);
        assertParseSuccess("nr purge", adminSource);
        assertParseSuccess("nr purge caches", adminSource);
        assertParseSuccess("nr purge logs", adminSource);
        assertParseSuccess("nr purge all", adminSource);

        // Tracker logs
        assertParseSuccess("nr trackerlogs", adminSource);
        assertParseSuccess("nr trackerlogs list", adminSource);
        assertParseSuccess("nr trackerlogs enable", adminSource);
        assertParseSuccess("nr trackerlogs disable", adminSource);
        assertParseSuccess("nr trackerlogs clear", adminSource);
        assertParseSuccess("nr logs", adminSource);
        assertParseSuccess("nr logs list", adminSource);
    }

    @Test
    @DisplayName("Syntax: Invalid arguments and out-of-range values fail parsing")
    void testInvalidAndOutOfRangeParsing() {
        // Out of integer range
        assertParseFailure("nr set density_cap 0", adminSource);
        assertParseFailure("nr set density_cap 101", adminSource);
        assertParseFailure("nr set rate 50", adminSource);
        assertParseFailure("nr set min_scale 4", adminSource);
        assertParseFailure("nr set normal_scale 40", adminSource);
        assertParseFailure("nr set max_scale 250", adminSource);
        assertParseFailure("nr set dispenser_egg_hatch_chance -1", adminSource);
        assertParseFailure("nr set dispenser_egg_hatch_chance 101", adminSource);

        // Invalid boolean values
        assertParseFailure("nr set enabled notabool", adminSource);
        assertParseFailure("nr set inbreeding_degradation 123", adminSource);

        // Unknown rules and subcommands
        assertParseFailure("nr set non_existent_rule true", adminSource);
        assertParseFailure("nr get non_existent_rule", adminSource);
        assertParseFailure("nr purge invalid_target", adminSource);
    }

    @Test
    @DisplayName("Security: Unprivileged players can query read-only commands but are denied admin mutations")
    void testPermissionGating() {
        // Read-only queries must succeed for Level 0
        assertParseSuccess("nr help", unprivilegedSource);
        assertParseSuccess("naturalreproduction help", unprivilegedSource);
        assertParseSuccess("nr status", unprivilegedSource);
        assertParseSuccess("nr stats", unprivilegedSource);
        assertParseSuccess("nr get enabled", unprivilegedSource);
        assertParseSuccess("nr get density_cap", unprivilegedSource);
        assertParseSuccess("nr trackerlogs list", unprivilegedSource);
        assertParseSuccess("nr logs list", unprivilegedSource);

        // Modifying commands must be denied for Level 0
        assertParsePermissionDenied("nr set enabled false", unprivilegedSource);
        assertParsePermissionDenied("nr set density_cap 20", unprivilegedSource);
        assertParsePermissionDenied("nr reset", unprivilegedSource);
        assertParsePermissionDenied("nr reload", unprivilegedSource);
        assertParsePermissionDenied("nr purge", unprivilegedSource);
        assertParsePermissionDenied("nr purge caches", unprivilegedSource);
        assertParsePermissionDenied("nr purge logs", unprivilegedSource);
        assertParsePermissionDenied("nr purge all", unprivilegedSource);
        assertParsePermissionDenied("nr trackerlogs enable", unprivilegedSource);
        assertParsePermissionDenied("nr trackerlogs disable", unprivilegedSource);
        assertParsePermissionDenied("nr trackerlogs clear", unprivilegedSource);
        assertParsePermissionDenied("nr logs enable", unprivilegedSource);
        assertParsePermissionDenied("nr logs clear", unprivilegedSource);
    }

    private void assertParseSuccess(String command, CommandSourceStack source) {
        ParseResults<CommandSourceStack> parse = dispatcher.parse(command, source);
        assertFalse(parse.getReader().canRead(),
                "Command parsing left unread input for: '" + command + "' -> " + parse.getReader().getRemaining());
        assertEquals(0, parse.getExceptions().size(),
                "Command parsing generated exceptions for: '" + command + "': " + parse.getExceptions());
        assertNotNull(parse.getContext().getCommand(),
                "Command node has no executable handler bound for: '" + command + "'");
    }

    private void assertParseFailure(String command, CommandSourceStack source) {
        ParseResults<CommandSourceStack> parse = dispatcher.parse(command, source);
        boolean failed = parse.getContext().getCommand() == null
                || parse.getReader().canRead()
                || !parse.getExceptions().isEmpty();
        assertTrue(failed, "Command expected to fail parsing but succeeded: " + command);
    }

    private void assertParsePermissionDenied(String command, CommandSourceStack source) {
        ParseResults<CommandSourceStack> parse = dispatcher.parse(command, source);
        boolean denied = parse.getContext().getCommand() == null
                || parse.getReader().canRead()
                || !parse.getExceptions().isEmpty();
        assertTrue(denied, "Unprivileged source unexpectedly passed permission check for: " + command);
    }
}
