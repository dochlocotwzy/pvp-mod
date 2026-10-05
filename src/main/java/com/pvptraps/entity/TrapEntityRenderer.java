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
public class TrapEntityRenderer extends EntityRenderer<TrapEntity, ItemStackEntityRenderState> {

    private final net.minecraft.client.item.ItemModelManager itemModelManager;
    private final Random random = Random.create();

    public TrapEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.itemModelManager = ctx.getItemModelManager();
        this.shadowRadius = 0.0f;
        this.shadowOpacity = 0.0f;
    }

    @Override
    public ItemStackEntityRenderState createRenderState() {
        return new ItemStackEntityRenderState();
    }

    @Override
    public void updateRenderState(TrapEntity entity, ItemStackEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        Item item = getTrapItem(entity.getTrapTypeId());
        if (item == null) {
            state.itemRenderState.clear();
            return;
        }
        state.update(entity, new ItemStack(item), itemModelManager);
    }

    @Override
    public void render(ItemStackEntityRenderState state, MatrixStack matrices,
                       OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrices.push();
        matrices.translate(0.0, 0.08, 0.0);
        matrices.scale(0.65f, 0.65f, 0.65f);
        ItemEntityRenderer.renderStack(matrices, queue, state.light, state, random);
        matrices.pop();
    }

    private static Item getTrapItem(String trapTypeId) {
        return switch (trapTypeId) {
            case "spike" -> ModItems.SPIKE_TRAP;
            case "ice" -> ModItems.ICE_TRAP;
            case "poison" -> ModItems.POISON_TRAP;
            case "electric" -> ModItems.ELECTRIC_TRAP;
            case "smoke" -> ModItems.SMOKE_TRAP;
            case "weakening" -> ModItems.WEAKENING_TRAP;
            case "sticky" -> ModItems.STICKY_TRAP;
            case "fire" -> ModItems.FIRE_TRAP;
            case "exhaustion" -> ModItems.EXHAUSTION_TRAP;
            default -> null;
        };
    }
}
