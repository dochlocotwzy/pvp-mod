package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

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
        return get().trapTypes.stream()
                .filter(t -> t.trapTypeId.equals(trapTypeId))
                .findFirst()
                .orElseGet(() -> get().trapTypes.isEmpty()
                        ? TrapConfig.TrapTypeSettings.createDefault()
                        : get().trapTypes.get(0));
    }
}
