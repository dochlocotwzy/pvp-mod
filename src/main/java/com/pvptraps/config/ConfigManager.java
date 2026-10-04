package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import java.util.List;
import java.util.Objects;

public final class ConfigManager {

    private ConfigManager() {
    }

    public static void init() {
        // GsonConfigSerializer вместо Json5 - в актуальной версии Cloth Config
        // класс Json5-сериализатора недоступен под этим именем/пакетом.
        // Даёт обычный config/pvptraps.json (не .json5), функционально то же самое.
        AutoConfig.register(TrapConfig.class, GsonConfigSerializer::new);
    }

    public static TrapConfig get() {
        return AutoConfig.getConfigHolder(TrapConfig.class).getConfig();
    }

    public static TrapConfig.TrapTypeSettings getTrapType(String trapTypeId) {
        TrapConfig config = get();
        List<TrapConfig.TrapTypeSettings> trapTypes = config.trapTypes;
        if (trapTypes == null || trapTypes.isEmpty()) {
            return TrapConfig.TrapTypeSettings.createDefault();
        }

        if (trapTypeId != null) {
            for (TrapConfig.TrapTypeSettings settings : trapTypes) {
                if (settings != null && Objects.equals(settings.trapTypeId, trapTypeId)) {
                    return settings;
                }
            }
        }

        for (TrapConfig.TrapTypeSettings settings : trapTypes) {
            if (settings != null) {
                return settings;
            }
        }
        return TrapConfig.TrapTypeSettings.createDefault();
    }
}
