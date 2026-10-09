package com.pvptraps.client;

import com.pvptraps.network.MageManaPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class MageHud {
    private static int mana = 100;
    private static int maxMana = 100;

    private MageHud() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MageManaPayload.ID, (payload, context) ->
                context.client().execute(() -> {
                    mana = Math.max(0, payload.current());
                    maxMana = Math.max(1, payload.maximum());
                }));
        HudElementRegistry.addLast(Identifier.of("pvptraps", "mana_hud"), (drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.options.hudHidden) {
                return;
            }
            int x = 10;
            int y = 10;
            int width = 124;
            int barWidth = (int) (width * Math.min(1.0, (double) mana / maxMana));
            drawContext.fill(x - 4, y - 4, x + width + 4, y + 22, 0x990B1020);
            drawContext.fill(x, y + 11, x + width, y + 17, 0xFF303747);
            drawContext.fill(x, y + 11, x + barWidth, y + 17, 0xFF56D8FF);
            drawContext.drawTextWithShadow(client.textRenderer,
                    Text.literal("МАНА " + mana + "/" + maxMana), x, y, 0xFFEAFBFF);
        });
    }
}
