package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Json5ConfigSerializer;

public final class ConfigManager {

    private ConfigManager() {
    }

    public static void init() {
        AutoConfig.register(TrapConfig.class, Json5ConfigSerializer::new);
    }

    public static TrapConfig get() {
        return AutoConfig.getConfigHolder(TrapConfig.class).getConfig();
    }

    public static TrapConfig.TrapTypeSettings getTrapType(String trapTypeId) {
        return get().trapTypes.stream()
                .filter(t -> t.trapTypeId.equals(trapTypeId))
                .findFirst()
                .orElseGet(() -> get().trapTypes.isEmpty()
                        ? TrapConfig.TrapTypeSettings.createDefault()
                        : get().trapTypes.get(0));
    }
}
