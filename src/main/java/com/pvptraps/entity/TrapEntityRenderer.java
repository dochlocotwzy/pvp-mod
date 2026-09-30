package com.pvptraps.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Ловушка не должна иметь видимой модели вообще (её "видимость/невидимость"
 * управляется через LivingEntity#setInvisible + scoreboard-команду, см. TrapEntity).
 * Поэтому рендерер намеренно ничего не рисует.
 *
 * В 1.21.11 EntityRenderer работает через отдельный объект EntityRenderState
 * (снимок состояния сущности для рендера), а не напрямую через саму сущность.
 * Мы используем базовый EntityRenderState без кастомных полей, так как рисовать
 * всё равно ничего не собираемся.
 */
public class TrapEntityRenderer extends EntityRenderer<TrapEntity, EntityRenderState> {

    public TrapEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void render(EntityRenderState state, MatrixStack matrices,
                        VertexConsumerProvider vertexConsumers, int light) {
        // намеренно пусто - ловушка не рисуется сама, видимость управляется через team/invisible
    }
}
