// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.dasik.social.api.config.GuiHelper;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return GuiHelper.getOptionalYaclFactory(
            "natural-reproduction",
            "net.vanillaoutsider.naturalreproduction.client.YaclScreenHelper",
            "createScreen"
        );
    }
}
