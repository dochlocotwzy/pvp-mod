package com.pvptraps.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class PvpTrapsConfigHubScreen extends Screen {
    private final Screen parent;

    public PvpTrapsConfigHubScreen(Screen parent) {
        super(Text.translatable("screen.pvptraps.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = width / 2 - 100;
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pvptraps.config.traps"),
                button -> client.setScreen(AutoConfig.getConfigScreen(TrapConfig.class, this).get()))
                .dimensions(left, height / 2 - 28, 200, 22)
                .build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pvptraps.config.mage"),
                button -> client.setScreen(AutoConfig.getConfigScreen(MageConfig.class, this).get()))
                .dimensions(left, height / 2 + 2, 200, 22)
                .build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> close())
                .dimensions(left, height / 2 + 34, 200, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xD0101620);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 68, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
