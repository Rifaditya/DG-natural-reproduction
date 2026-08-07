// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.vanillaoutsider.naturalreproduction.NaturalReproductionFabric;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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
        boolean currentBiomeFertility = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.BIOME_FERTILITY) : true;
        boolean currentBiomeVariants = client.level != null ? DynamicGameRuleManager.getBoolean(client.level, NaturalReproductionFabric.BIOME_VARIANTS) : true;

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
            Method optionMethod = findMethod(groupBuilder.getClass(), "option");
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

            Method groupBuildMethod = findMethod(groupBuilder.getClass(), "build");
            Object optionGroup = groupBuildMethod.invoke(groupBuilder);

            Method categoryGroupMethod = findMethod(categoryBuilder.getClass(), "group");
            categoryGroupMethod.invoke(categoryBuilder, optionGroup);

            Method categoryBuildMethod = findMethod(categoryBuilder.getClass(), "build");
            Object category = categoryBuildMethod.invoke(categoryBuilder);

            Method builderCategoryMethod = findMethod(builderClass, "category");
            builderCategoryMethod.invoke(yaclBuilder, category);

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

    private static Method findMethod(Class<?> clazz, String name) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new RuntimeException("Method " + name + " not found on " + clazz.getName());
    }
}
