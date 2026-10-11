package com.pvpmage;

import com.pvpmage.client.MageClientController;
import com.pvpmage.client.MageHud;
import com.pvpmage.entity.MageArcaneBoltRenderer;
import com.pvpmage.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class PvpTrapsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ARCANE_BOLT, MageArcaneBoltRenderer::new);
        MageClientController.register();
        MageHud.register();
    }
}
