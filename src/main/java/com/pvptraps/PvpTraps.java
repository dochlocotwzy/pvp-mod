package com.pvptraps;

import com.pvptraps.config.MageConfigManager;
import com.pvptraps.entity.ModEntities;
import com.pvptraps.item.ModItems;
import com.pvptraps.util.MageAbilityService;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvpTraps implements ModInitializer {
    public static final String MOD_ID = "pvptraps";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MageConfigManager.init();
        ModItems.register();
        MageAbilityService.register();
        LOGGER.info("PvP Mage: инициализация завершена");
    }
}
