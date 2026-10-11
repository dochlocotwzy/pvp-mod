package com.pvpmage;

import com.pvpmage.config.MageConfigManager;
import com.pvpmage.entity.ModEntities;
import com.pvpmage.item.ModItems;
import com.pvpmage.util.MageAbilityService;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvpTraps implements ModInitializer {
    public static final String MOD_ID = "pvpmage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MageConfigManager.init();
        ModItems.register();
        MageAbilityService.register();
        LOGGER.info("PvP Mage: инициализация завершена");
    }
}
