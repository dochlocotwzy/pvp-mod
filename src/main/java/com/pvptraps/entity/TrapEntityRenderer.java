package com.pvptraps.entity;

import com.pvptraps.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.scoreboard.Team;

/**
 * Renders the trap's registered item model in the world and applies the
 * server-synchronized visibility window for each viewer.
 */
public class TrapEntityRenderer extends EntityRenderer<TrapEntity, TrapEntityRenderer.TrapRenderState> {

    private final net.minecraft.client.item.ItemModelManager itemModelManager;

    public TrapEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.itemModelManager = ctx.itemModelManager;
    }

    @Override
    public TrapRenderState createRenderState() {
        return new TrapRenderState();
    }

    @Override
    public void updateRenderState(TrapEntity entity, TrapRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        itemModelManager.updateForNonLivingEntity(
                state.itemState,
                new ItemStack(ModItems.TRAP_ITEM),
                ModelTransformationMode.GROUND,
                entity
        );
    }

    @Override
    public boolean shouldRender(TrapEntity entity, Frustum frustum,
                               double x, double y, double z) {
        if (!entity.areEnemiesAllowedToSee()) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) {
                return false;
            }
            Team trapTeam = entity.getScoreboardTeam();
            Team viewerTeam = client.player.getScoreboardTeam();
            if (trapTeam == null || viewerTeam == null
                    || !trapTeam.getName().equals(viewerTeam.getName())) {
                return false;
            }
        }
        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public void render(TrapRenderState state, MatrixStack matrices,
                       OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrices.push();
        // The model is a flat one-block footprint, centered on the entity.
        matrices.translate(0.0, 0.001, 0.0);
        state.itemState.render(matrices, queue, state.light, 0, state.outlineColor);
        matrices.pop();
    }

    public static class TrapRenderState extends EntityRenderState {
        public final ItemRenderState itemState = new ItemRenderState();
    }
}
