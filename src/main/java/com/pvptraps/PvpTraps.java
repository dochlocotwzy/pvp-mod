package com.pvptraps;

import com.pvptraps.config.ConfigManager;
import com.pvptraps.entity.ModEntities;
import com.pvptraps.item.ModItems;
import com.pvptraps.util.TrapEffectScheduler;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvpTraps implements ModInitializer {

    public static final String MOD_ID = "pvptraps";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ConfigManager.init();

        ModEntities.registerAttributes();
        ModItems.register();
        TrapEffectScheduler.registerEvents();

        LOGGER.info("PvP Traps: инициализация завершена");
    }
}
