// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRule;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class YaclScreenHelper {

    public static ConfigScreenFactory<?> createScreen() {
        return YaclScreenHelper::buildScreen;
    }

    private static Screen buildScreen(Screen parent) {
        Minecraft client = Minecraft.getInstance();
        boolean currentEnabled = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.ENABLED) : true;
        int currentCap = client.level != null ? DynamicGameRuleManager.getInt(client.level, NaturalReproductionFabric.DENSITY_CAP) : 10;
        int currentRate = client.level != null ? DynamicGameRuleManager.getInt(client.level, NaturalReproductionFabric.RATE) : 24000;
        boolean currentScaleDrops = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.SCALE_DROPS) : true;
        boolean currentCrampedPenalty = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.CRAMPED_SPACE_PENALTY) : true;
        boolean currentInbreeding = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.INBREEDING_DEGRADATION) : true;
        boolean currentPastureEnrichment = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.PASTURE_ENRICHMENT) : true;
        boolean currentOvergrazing = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.OVERGRAZING) : true;
        boolean currentGestation = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.GESTATION_PERIOD) : true;
        boolean currentManualGestation = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.MANUAL_GESTATION) : true;
        int currentGestationDuration = client.level != null ? DynamicGameRuleManager.getInt(client.level, NaturalReproductionFabric.GESTATION_DURATION) : 24000;
        boolean currentFertilizedEggs = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS) : true;
        boolean currentInfertileReg = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS) : true;
        int currentDispenserRate = client.level != null ? DynamicGameRuleManager.getInt(client.level, NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE) : 75;
        boolean currentHerdDynamics = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.HERD_DYNAMICS) : true;
        boolean currentHerdStampede = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.HERD_STAMPEDE) : true;
        boolean currentBiomeFertility = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.BIOME_FERTILITY) : true;
        boolean currentBiomeVariants = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.BIOME_VARIANTS) : true;
        boolean currentTrackerLogs = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.TRACKER_LOGS) : false;

        int currentMinScale = client.level != null ? DynamicGameRuleManager.getInt(client.level, NaturalReproductionFabric.MIN_SCALE) : 10;
        int currentNormalScale = client.level != null ? DynamicGameRuleManager.getInt(client.level, NaturalReproductionFabric.NORMAL_SCALE) : 95;
        int currentMaxScale = client.level != null ? DynamicGameRuleManager.getInt(client.level, NaturalReproductionFabric.MAX_SCALE) : 120;

        try {
            Class<?> yaclClass = Class.forName("dev.isxander.yacl3.api.YetAnotherConfigLib");
            Method createBuilderMethod = yaclClass.getMethod("createBuilder");
            Object yaclBuilder = createBuilderMethod.invoke(null);

            Class<?> builderClass = yaclBuilder.getClass();
            Method titleMethod = findMethod(builderClass, "title");
            titleMethod.invoke(yaclBuilder, Component.translatable("title.natural-reproduction.config"));

            Class<?> categoryClass = Class.forName("dev.isxander.yacl3.api.ConfigCategory");
            Method categoryCreateBuilderMethod = categoryClass.getMethod("createBuilder");
            Object categoryBuilder = categoryCreateBuilderMethod.invoke(null);

            Method categoryNameMethod = findMethod(categoryBuilder.getClass(), "name");
            categoryNameMethod.invoke(categoryBuilder, Component.translatable("gamerule.category.natural-reproduction"));

            Class<?> groupClass = Class.forName("dev.isxander.yacl3.api.OptionGroup");
            Method groupCreateBuilderMethod = groupClass.getMethod("createBuilder");
            Object groupBuilder = groupCreateBuilderMethod.invoke(null);

            Method groupNameMethod = findMethod(groupBuilder.getClass(), "name");
            groupNameMethod.invoke(groupBuilder, Component.translatable("gamerule.category.natural-reproduction"));
            Method optionMethod = findMethod(groupBuilder.getClass(), "option");

            // Top-pinned Creator Support Ko-fi Banner (above all settings)
            try {
                Object kofiOption = buildKofiButtonOption();
                if (kofiOption != null) {
                    optionMethod.invoke(groupBuilder, kofiOption);
                }
            } catch (Throwable t) {
                NaturalReproductionFabric.LOGGER.warn("Failed to attach creator support button to YACL config", t);
            }

            // Add Enabled Option
            Object enabledOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:enabled"),
                Component.translatable("gamerule.natural-reproduction:enabled.description"),
                true,
                () -> currentEnabled,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.ENABLED != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.ENABLED, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, enabledOption);

            // Add Density Cap Option
            Object capOption = buildIntSliderOption(
                Component.translatable("gamerule.natural-reproduction:density_cap"),
                Component.translatable("gamerule.natural-reproduction:density_cap.description"),
                10,
                1, 100, 1,
                () -> currentCap,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.DENSITY_CAP != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.DENSITY_CAP, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, capOption);

            // Add Rate Option
            Object rateOption = buildIntSliderOption(
                Component.translatable("gamerule.natural-reproduction:rate"),
                Component.translatable("gamerule.natural-reproduction:rate.description"),
                24000,
                100, 240000, 100,
                () -> currentRate,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.RATE != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.RATE, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, rateOption);

            // Add Min Scale Option
            Object minScaleOption = buildIntSliderOption(
                Component.translatable("gamerule.natural-reproduction:min_scale"),
                Component.translatable("gamerule.natural-reproduction:min_scale.description"),
                10,
                5, 100, 5,
                () -> currentMinScale,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.MIN_SCALE != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.MIN_SCALE, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, minScaleOption);

            // Add Normal Scale Option
            Object normalScaleOption = buildIntSliderOption(
                Component.translatable("gamerule.natural-reproduction:normal_scale"),
                Component.translatable("gamerule.natural-reproduction:normal_scale.description"),
                95,
                50, 150, 5,
                () -> currentNormalScale,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.NORMAL_SCALE != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.NORMAL_SCALE, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, normalScaleOption);

            // Add Max Scale Option
            Object maxScaleOption = buildIntSliderOption(
                Component.translatable("gamerule.natural-reproduction:max_scale"),
                Component.translatable("gamerule.natural-reproduction:max_scale.description"),
                120,
                100, 200, 5,
                () -> currentMaxScale,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.MAX_SCALE != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.MAX_SCALE, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, maxScaleOption);

            // Add Scale Drops Option
            Object scaleDropsOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:scale_drops"),
                Component.translatable("gamerule.natural-reproduction:scale_drops.description"),
                true,
                () -> currentScaleDrops,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.SCALE_DROPS != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.SCALE_DROPS, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, scaleDropsOption);

            // Add Cramped Space Penalty Option
            Object crampedPenaltyOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:cramped_space_penalty"),
                Component.translatable("gamerule.natural-reproduction:cramped_space_penalty.description"),
                true,
                () -> currentCrampedPenalty,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.CRAMPED_SPACE_PENALTY != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.CRAMPED_SPACE_PENALTY, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, crampedPenaltyOption);

            // Add Inbreeding Lineage Degradation Option
            Object inbreedingOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:inbreeding_degradation"),
                Component.translatable("gamerule.natural-reproduction:inbreeding_degradation.description"),
                true,
                () -> currentInbreeding,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.INBREEDING_DEGRADATION != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.INBREEDING_DEGRADATION, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, inbreedingOption);

            // Add Pasture Enrichment Option
            Object pastureEnrichmentOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:pasture_enrichment"),
                Component.translatable("gamerule.natural-reproduction:pasture_enrichment.description"),
                true,
                () -> currentPastureEnrichment,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.PASTURE_ENRICHMENT != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.PASTURE_ENRICHMENT, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, pastureEnrichmentOption);

            // Add Overgrazing Option
            Object overgrazingOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:overgrazing"),
                Component.translatable("gamerule.natural-reproduction:overgrazing.description"),
                true,
                () -> currentOvergrazing,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.OVERGRAZING != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.OVERGRAZING, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, overgrazingOption);

            // Add Gestation Period Option
            Object gestationOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:gestation_period"),
                Component.translatable("gamerule.natural-reproduction:gestation_period.description"),
                true,
                () -> currentGestation,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.GESTATION_PERIOD != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.GESTATION_PERIOD, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, gestationOption);

            // Add Manual Gestation Option
            Object manualGestationOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:manual_gestation"),
                Component.translatable("gamerule.natural-reproduction:manual_gestation.description"),
                true,
                () -> currentManualGestation,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.MANUAL_GESTATION != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.MANUAL_GESTATION, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, manualGestationOption);

            // Add Gestation Duration Slider Option
            Object gestationDurationOption = buildIntSliderOption(
                Component.translatable("gamerule.natural-reproduction:gestation_duration"),
                Component.translatable("gamerule.natural-reproduction:gestation_duration.description"),
                24000,
                100, 240000, 100,
                () -> currentGestationDuration,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.GESTATION_DURATION != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.GESTATION_DURATION, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, gestationDurationOption);

            // Add Fertilized Chicken Eggs Option
            Object fertilizedEggsOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:fertilized_chicken_eggs"),
                Component.translatable("gamerule.natural-reproduction:fertilized_chicken_eggs.description"),
                true,
                () -> currentFertilizedEggs,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.FERTILIZED_CHICKEN_EGGS, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, fertilizedEggsOption);

            // Add Infertile Regular Eggs Option
            Object infertileRegOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:chicken_infertile_regular_eggs"),
                Component.translatable("gamerule.natural-reproduction:chicken_infertile_regular_eggs.description"),
                true,
                () -> currentInfertileReg,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.CHICKEN_INFERTILE_REGULAR_EGGS, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, infertileRegOption);

            // Add Dispenser Egg Hatch Chance Option
            Object dispenserRateOption = buildIntSliderOption(
                Component.translatable("gamerule.natural-reproduction:dispenser_egg_hatch_chance"),
                Component.translatable("gamerule.natural-reproduction:dispenser_egg_hatch_chance.description"),
                75,
                0, 100, 5,
                () -> currentDispenserRate,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.DISPENSER_EGG_HATCH_CHANCE, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, dispenserRateOption);

            // Add Herd Dynamics Option
            Object herdDynamicsOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:herd_dynamics"),
                Component.translatable("gamerule.natural-reproduction:herd_dynamics.description"),
                true,
                () -> currentHerdDynamics,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.HERD_DYNAMICS != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.HERD_DYNAMICS, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, herdDynamicsOption);

            // Add Herd Stampede Option
            Object herdStampedeOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:herd_stampede"),
                Component.translatable("gamerule.natural-reproduction:herd_stampede.description"),
                true,
                () -> currentHerdStampede,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.HERD_STAMPEDE != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.HERD_STAMPEDE, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, herdStampedeOption);

            // Add Biome Fertility Option
            Object biomeFertilityOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:biome_fertility"),
                Component.translatable("gamerule.natural-reproduction:biome_fertility.description"),
                true,
                () -> currentBiomeFertility,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.BIOME_FERTILITY != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.BIOME_FERTILITY, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, biomeFertilityOption);

            // Add Biome Variants Option
            Object biomeVariantsOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:biome_variants"),
                Component.translatable("gamerule.natural-reproduction:biome_variants.description"),
                true,
                () -> currentBiomeVariants,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.BIOME_VARIANTS != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.BIOME_VARIANTS, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, biomeVariantsOption);

            // Add Tracker Logs Option
            Object trackerLogsOption = buildBooleanOption(
                Component.translatable("gamerule.natural-reproduction:tracker_logs"),
                Component.translatable("gamerule.natural-reproduction:tracker_logs.description"),
                false,
                () -> currentTrackerLogs,
                val -> {
                    if (client.getSingleplayerServer() != null && NaturalReproductionFabric.TRACKER_LOGS != null) {
                        ServerLevel overworld = client.getSingleplayerServer().overworld();
                        if (overworld != null) {
                            overworld.getGameRules().set(NaturalReproductionFabric.TRACKER_LOGS, val, client.getSingleplayerServer());
                        }
                    }
                }
            );
            optionMethod.invoke(groupBuilder, trackerLogsOption);

            Method groupBuildMethod = findMethod(groupBuilder.getClass(), "build");
            Object optionGroup = groupBuildMethod.invoke(groupBuilder);

            Method categoryGroupMethod = findMethod(categoryBuilder.getClass(), "group");
            categoryGroupMethod.invoke(categoryBuilder, optionGroup);

            Method categoryBuildMethod = findMethod(categoryBuilder.getClass(), "build");
            Object category = categoryBuildMethod.invoke(categoryBuilder);

            Method builderCategoryMethod = findMethod(builderClass, "category");
            builderCategoryMethod.invoke(yaclBuilder, category);

            // Add Dedicated Species Toggles Category Tab
            Object speciesCategoryBuilder = categoryCreateBuilderMethod.invoke(null);
            findMethod(speciesCategoryBuilder.getClass(), "name").invoke(speciesCategoryBuilder, Component.translatable("gamerule.category.natural-reproduction.species_toggles"));

            Object speciesGroupBuilder = groupCreateBuilderMethod.invoke(null);
            findMethod(speciesGroupBuilder.getClass(), "name").invoke(speciesGroupBuilder, Component.translatable("gamerule.category.natural-reproduction.species_toggles"));

            for (Map.Entry<String, GameRule<Boolean>> entry : NaturalReproductionFabric.RULE_NAME_MAP.entrySet()) {
                String ruleKey = entry.getKey();
                GameRule<Boolean> rule = entry.getValue();

                Object speciesOption = buildBooleanOption(
                    Component.translatable("gamerule.natural-reproduction:" + ruleKey),
                    Component.translatable("gamerule.natural-reproduction:" + ruleKey + ".description"),
                    true,
                    () -> client.level != null ? DynamicGameRuleManager.getBoolean(client.level, rule) : true,
                    val -> {
                        if (client.getSingleplayerServer() != null && rule != null) {
                            ServerLevel overworld = client.getSingleplayerServer().overworld();
                            if (overworld != null) {
                                overworld.getGameRules().set(rule, val, client.getSingleplayerServer());
                            }
                        }
                    }
                );
                optionMethod.invoke(speciesGroupBuilder, speciesOption);
            }

            Object speciesOptionGroup = groupBuildMethod.invoke(speciesGroupBuilder);
            categoryGroupMethod.invoke(speciesCategoryBuilder, speciesOptionGroup);
            Object speciesCategory = categoryBuildMethod.invoke(speciesCategoryBuilder);

            builderCategoryMethod.invoke(yaclBuilder, speciesCategory);

            Method builderBuildMethod = findMethod(builderClass, "build");
            Object yacl = builderBuildMethod.invoke(yaclBuilder);

            Method generateScreenMethod = yacl.getClass().getMethod("generateScreen", Screen.class);
            return (Screen) generateScreenMethod.invoke(yacl, parent);

        } catch (Exception e) {
            NaturalReproductionFabric.LOGGER.error("Failed to build YACL configuration screen via reflection", e);
            return null;
        }
    }

    private static Object buildBooleanOption(Component name, Component description, boolean defaultValue, Supplier<Boolean> getter, Consumer<Boolean> setter) throws Exception {
        Class<?> optionClass = Class.forName("dev.isxander.yacl3.api.Option");
        Method createBuilderMethod = optionClass.getMethod("createBuilder");
        Object optionBuilder = createBuilderMethod.invoke(null);

        Class<?> optBuilderClass = optionBuilder.getClass();
        findMethod(optBuilderClass, "name").invoke(optionBuilder, name);

        Class<?> descClass = Class.forName("dev.isxander.yacl3.api.OptionDescription");
        Method descCreateBuilderMethod = descClass.getMethod("createBuilder");
        Object descBuilder = descCreateBuilderMethod.invoke(null);
        findMethod(descBuilder.getClass(), "text").invoke(descBuilder, new Object[]{new Object[]{description}});
        Object desc = findMethod(descBuilder.getClass(), "build").invoke(descBuilder);
        findMethod(optBuilderClass, "description").invoke(optionBuilder, desc);

        Class<?> bindingClass = Class.forName("dev.isxander.yacl3.api.Binding");
        Method genericBindingMethod = bindingClass.getMethod("generic", Object.class, Supplier.class, Consumer.class);
        Object binding = genericBindingMethod.invoke(null, defaultValue, getter, setter);
        findMethod(optBuilderClass, "binding").invoke(optionBuilder, binding);

        Class<?> boolCtrlClass = Class.forName("dev.isxander.yacl3.api.controller.BooleanControllerBuilder");
        Method createCtrlMethod = boolCtrlClass.getMethod("create", optionClass);
        Object controller = createCtrlMethod.invoke(null, optionBuilder);
        findMethod(optBuilderClass, "controller").invoke(optionBuilder, controller);

        return findMethod(optBuilderClass, "build").invoke(optionBuilder);
    }

    private static Object buildIntSliderOption(Component name, Component description, int defaultValue, int min, int max, int step, Supplier<Integer> getter, Consumer<Integer> setter) throws Exception {
        Class<?> optionClass = Class.forName("dev.isxander.yacl3.api.Option");
        Method createBuilderMethod = optionClass.getMethod("createBuilder");
        Object optionBuilder = createBuilderMethod.invoke(null);

        Class<?> optBuilderClass = optionBuilder.getClass();
        findMethod(optBuilderClass, "name").invoke(optionBuilder, name);

        Class<?> descClass = Class.forName("dev.isxander.yacl3.api.OptionDescription");
        Object descBuilder = descClass.getMethod("createBuilder").invoke(null);
        findMethod(descBuilder.getClass(), "text").invoke(descBuilder, new Object[]{new Object[]{description}});
        Object desc = findMethod(descBuilder.getClass(), "build").invoke(descBuilder);
        findMethod(optBuilderClass, "description").invoke(optionBuilder, desc);

        Class<?> bindingClass = Class.forName("dev.isxander.yacl3.api.Binding");
        Method genericBindingMethod = bindingClass.getMethod("generic", Object.class, Supplier.class, Consumer.class);
        Object binding = genericBindingMethod.invoke(null, defaultValue, getter, setter);
        findMethod(optBuilderClass, "binding").invoke(optionBuilder, binding);

        Class<?> sliderClass = Class.forName("dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController");
        Constructor<?> constructor = sliderClass.getConstructor(optionClass, int.class, int.class, int.class);
        Function<Object, Object> controllerFactory = opt -> {
            try {
                return constructor.newInstance(opt, min, max, step);
            } catch (Exception e) {
                return null;
            }
        };
        findMethod(optBuilderClass, "customController").invoke(optionBuilder, controllerFactory);

        return findMethod(optBuilderClass, "build").invoke(optionBuilder);
    }

    private static Object buildKofiButtonOption() throws Exception {
        Class<?> buttonOptClass = Class.forName("dev.isxander.yacl3.api.ButtonOption");
        Method createBuilderMethod = buttonOptClass.getMethod("createBuilder");
        Object builder = createBuilderMethod.invoke(null);
        Class<?> builderClass = builder.getClass();

        findMethod(builderClass, "name").invoke(builder, net.dasik.social.api.config.DasikSupportHelper.getButtonText());

        Class<?> descClass = Class.forName("dev.isxander.yacl3.api.OptionDescription");
        Method descCreateBuilderMethod = descClass.getMethod("createBuilder");
        Object descBuilder = descCreateBuilderMethod.invoke(null);
        findMethod(descBuilder.getClass(), "text").invoke(descBuilder, new Object[]{new Object[]{net.dasik.social.api.config.DasikSupportHelper.getTooltipText()}});
        Object desc = findMethod(descBuilder.getClass(), "build").invoke(descBuilder);
        findMethod(builderClass, "description").invoke(builder, desc);

        java.util.function.Consumer<Screen> action = screen -> net.dasik.social.api.config.DasikSupportHelper.openKofi(screen);
        Method actionMethod = null;
        for (Method m : builderClass.getMethods()) {
            if (m.getName().equals("action") && m.getParameterCount() == 1 && m.getParameterTypes()[0] == java.util.function.Consumer.class) {
                actionMethod = m;
                break;
            }
        }
        if (actionMethod != null) {
            actionMethod.invoke(builder, action);
        } else {
            java.util.function.BiConsumer<Screen, Object> biAction = (screen, opt) -> net.dasik.social.api.config.DasikSupportHelper.openKofi(screen);
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("action") && m.getParameterCount() == 1 && m.getParameterTypes()[0] == java.util.function.BiConsumer.class) {
                    m.invoke(builder, biAction);
                    break;
                }
            }
        }

        return findMethod(builderClass, "build").invoke(builder);
    }

    private static Method findMethod(Class<?> clazz, String name) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new RuntimeException("Method " + name + " not found on " + clazz.getName());
    }
}
