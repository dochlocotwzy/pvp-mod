package com.pvptraps.entity;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Ловушка не должна иметь видимой модели вообще (её "видимость/невидимость"
 * управляется через LivingEntity#setInvisible + scoreboard-команду, см. TrapEntity).
 * Поэтому рендерер намеренно ничего не рисует.
 *
 * В 1.21.11 render() принимает OrderedRenderCommandQueue и CameraRenderState
 * (не VertexConsumerProvider напрямую, как в более старых версиях) - подтверждено
 * через javap по актуальному remapped jar.
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
                        OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        // намеренно пусто - ловушка не рисуется сама, видимость управляется через team/invisible
    }
}
