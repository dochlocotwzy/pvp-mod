package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public final class MageConfigManager {
    private MageConfigManager() {
    }

    public static void init() {
        AutoConfig.register(MageConfig.class, GsonConfigSerializer::new);
    }

    public static MageConfig get() {
        return AutoConfig.getConfigHolder(MageConfig.class).getConfig();
    }

    public static float getArcaneBoltSize() {
        return Math.max(0.2f, Math.min(1.0f, get().abilities.arcaneBoltSize));
    }
}
