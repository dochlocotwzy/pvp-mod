package com.pvptraps.entity;

import com.pvptraps.config.MageConfigManager;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;

public final class MageArcaneBoltRenderer extends FlyingItemEntityRenderer<ArcaneBoltEntity> {
    public MageArcaneBoltRenderer(EntityRendererFactory.Context context) {
        super(context, 1.0f, true);
    }

    @Override
    public void render(FlyingItemEntityRenderState state, MatrixStack matrices,
                       OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        float scale = MageConfigManager.getArcaneBoltSize() / 0.25f;
        matrices.push();
        matrices.scale(scale, scale, scale);
        super.render(state, matrices, queue, cameraState);
        matrices.pop();
    }
}
