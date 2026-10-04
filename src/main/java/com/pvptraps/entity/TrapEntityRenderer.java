package com.pvptraps.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Team;

/**
 * Controls per-viewer visibility. The entity remains tracked by all clients;
 * after the reveal period, only members of the owner's scoreboard team render it.
 *
 * The actual model draw is implemented separately from this visibility gate.
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
    public void render(EntityRenderState state, MatrixStack matrices,
                        OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        // Model rendering is intentionally kept separate from the visibility rule.
    }
}
