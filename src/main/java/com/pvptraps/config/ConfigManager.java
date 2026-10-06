package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public final class ConfigManager {

    private ConfigManager() {
    }

    public static void init() {
        AutoConfig.register(TrapConfig.class, GsonConfigSerializer::new);
    }

    public static TrapConfig get() {
        return AutoConfig.getConfigHolder(TrapConfig.class).getConfig();
    }

    public static TrapConfig.TrapTypeSettings getTrapType(String trapTypeId) {
        TrapConfig config = get();
        if (trapTypeId == null) {
            return TrapConfig.TrapTypeSettings.createDefault();
        }
        return switch (trapTypeId) {
            case "spike" -> config.spike;
            case "ice" -> config.ice;
            case "poison" -> config.poison;
            case "electric" -> config.electric;
            case "smoke" -> config.smoke;
            case "weakening" -> config.weakening;
            case "sticky" -> config.sticky;
            case "fire" -> config.fire;
            case "exhaustion" -> config.exhaustion;
            default -> TrapConfig.presetFor(trapTypeId);
        };
    }
}
