package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import java.util.List;

public final class ConfigManager {
    private ConfigManager() {
    }

    public static void init() {
        AutoConfig.register(TrapConfig.class, GsonConfigSerializer::new);
        migrateLegacyTrapTypes();
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

    private static void migrateLegacyTrapTypes() {
        TrapConfig config = get();
        List<TrapConfig.TrapTypeSettings> legacy = config.trapTypes;
        if (legacy == null || legacy.isEmpty()) {
            return;
        }
        for (TrapConfig.TrapTypeSettings settings : legacy) {
            if (settings == null || settings.trapTypeId == null) {
                continue;
            }
            switch (settings.trapTypeId) {
                case "spike" -> config.spike = settings;
                case "ice" -> config.ice = settings;
                case "poison" -> config.poison = settings;
                case "electric" -> config.electric = settings;
                case "smoke" -> config.smoke = settings;
                case "weakening" -> config.weakening = settings;
                case "sticky" -> config.sticky = settings;
                case "fire" -> config.fire = settings;
                case "exhaustion" -> config.exhaustion = settings;
                default -> { }
            }
        }
        config.trapTypes = null;
        AutoConfig.getConfigHolder(TrapConfig.class).save();
    }
}
