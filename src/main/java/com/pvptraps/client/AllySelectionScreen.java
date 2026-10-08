package com.pvptraps.client;

import com.pvptraps.config.MageConfigManager;
import com.pvptraps.network.MageCastPayload;
import com.pvptraps.util.MageTeamAdapter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public final class AllySelectionScreen extends Screen {
    private final String abilityId;
    private final List<PlayerEntity> allies = new ArrayList<>();
    private boolean noAlliesReported;

    public AllySelectionScreen(String abilityId) {
        super(Text.translatable("screen.pvptraps.mage.select_ally"));
        this.abilityId = abilityId;
    }

    @Override
    protected void init() {
        allies.clear();
        if (client == null || client.player == null || client.world == null) {
            return;
        }

        double maxRange = MageConfigManager.get().abilities.targetRangeBlocks;
        for (PlayerEntity candidate : client.world.getPlayers()) {
            if (candidate == client.player || !candidate.isAlive() || candidate.isSpectator()) {
                continue;
            }
            if (client.player.squaredDistanceTo(candidate) > maxRange * maxRange
                    || !MageTeamAdapter.areAllies(client.player, candidate)) {
                continue;
            }
            allies.add(candidate);
        }

        if (allies.isEmpty() && !noAlliesReported) {
            noAlliesReported = true;
            ClientPlayNetworking.send(new MageCastPayload(abilityId, -1));
        }

        int panelWidth = Math.min(300, width - 24);
        int left = (width - panelWidth) / 2;
        int y = Math.max(66, height / 2 - Math.min(allies.size(), 7) * 14);
        for (int i = 0; i < Math.min(allies.size(), 7); i++) {
            PlayerEntity ally = allies.get(i);
            int buttonY = y + i * 28;
            addDrawableChild(ButtonWidget.builder(Text.literal(ally.getName().getString()),
                    button -> choose(ally.getId()))
                    .dimensions(left + 12, buttonY, panelWidth - 24, 22)
                    .build());
        }
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.cancel"), button -> close())
                .dimensions(left + 12, height - 42, panelWidth - 24, 20)
                .build());
    }

    private void choose(int entityId) {
        ClientPlayNetworking.send(new MageCastPayload(abilityId, entityId));
        close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xB0081020);
        int panelWidth = Math.min(300, width - 24);
        int left = (width - panelWidth) / 2;
        int panelTop = Math.max(24, height / 2 - Math.min(allies.size(), 7) * 14 - 38);
        context.fill(left, panelTop, left + panelWidth, height - 18, 0xD8202E46);
        context.fill(left, panelTop, left + panelWidth, panelTop + 2, 0xFF79DDF2);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, panelTop + 12, 0xFFFFFFFF);
        if (allies.isEmpty()) {
            context.drawCenteredTextWithShadow(textRenderer,
                    Text.translatable("screen.pvptraps.mage.no_allies"),
                    width / 2, panelTop + 42, 0xFFFFC9C9);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
