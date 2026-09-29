package com.pvptraps;

import com.pvptraps.entity.ModEntities;
import com.pvptraps.entity.TrapEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class PvpTrapsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.TRAP, TrapEntityRenderer::new);
    }
}
