package com.pvptraps.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemStackEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import com.pvptraps.item.ModItems;

/**
 * Рендерит ловушку тем же item model, что используется для её предмета.
 * После истечения enemyVisibilitySeconds TrapEntity#setInvisible скрывает
 * сущность для игроков, для которых ловушка должна быть невидимой.
 *
 * В 1.21.11 render() принимает OrderedRenderCommandQueue и CameraRenderState.
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

        // The custom renderer bypasses vanilla's normal entity-model visibility path.
        // Apply the player-specific trap visibility explicitly before rendering.
        var player = MinecraftClient.getInstance().player;
        state.invisible = player != null && entity.isInvisibleTo(player);

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
        if (state.invisible) {
            return;
        }

        matrices.push();
        matrices.translate(0.0, 0.52, 0.0);
        matrices.scale(1.0f, 1.0f, 1.0f);
        ItemEntityRenderer.render(matrices, queue, state.light, state, random);
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
