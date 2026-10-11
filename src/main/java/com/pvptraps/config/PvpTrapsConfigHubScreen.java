package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public final class PvpTrapsConfigHubScreen {
    private PvpTrapsConfigHubScreen() {
    }

    public static Screen create(Screen parent) {
        return AutoConfig.getConfigScreen(MageConfig.class, parent).get();
    }
}
