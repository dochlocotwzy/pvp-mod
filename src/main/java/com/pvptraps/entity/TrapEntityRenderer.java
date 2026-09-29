package com.pvptraps.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TrapEntityRenderer extends EntityRenderer<TrapEntity> {

    public TrapEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(TrapEntity entity) {
        return Identifier.of("minecraft", "textures/entity/pig/pig.png");
    }

    @Override
    public void render(TrapEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                        VertexConsumerProvider vertexConsumers, int light) {
        // намеренно пусто - ловушка не рисуется сама, видимость управляется через team/invisible
    }
}
