package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import java.util.List;
import java.util.Objects;

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
        List<TrapConfig.TrapTypeSettings> trapTypes = config.trapTypes;
        if (trapTypes != null) {
            for (TrapConfig.TrapTypeSettings settings : trapTypes) {
                if (settings != null && Objects.equals(settings.trapTypeId, trapTypeId)) {
                    return settings;
                }
            }
        }
        return trapTypeId == null
                ? TrapConfig.TrapTypeSettings.createDefault()
                : TrapConfig.presetFor(trapTypeId);
    }
}
