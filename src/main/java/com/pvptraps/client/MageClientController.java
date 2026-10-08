package com.pvptraps.client;

import com.pvptraps.item.MageAbilityItem;
import com.pvptraps.network.MageCastPayload;
import com.pvptraps.network.MageFeedbackPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

public final class MageClientController {
    private static int secondFailurePulseTicks;
    private static int focusTicks;
    private static int returnTicks;
    private static int focusTargetId = -1;
    private static float savedYaw;
    private static float savedPitch;

    private MageClientController() {
    }

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!world.isClient()) {
                return ActionResult.PASS;
            }
            if (!(player.getStackInHand(hand).getItem() instanceof MageAbilityItem ability)) {
                return ActionResult.PASS;
            }

            MinecraftClient client = MinecraftClient.getInstance();
            if (ability.targetsAlly()) {
                client.setScreen(new AllySelectionScreen(ability.getAbilityId()));
            } else {
                ClientPlayNetworking.send(new MageCastPayload(ability.getAbilityId(), -1));
            }
            return ActionResult.SUCCESS;
        });

        ClientPlayNetworking.registerGlobalReceiver(MageFeedbackPayload.ID, (payload, context) ->
                context.client().execute(() -> {
                    if (payload.cue() == 0) {
                        playFailurePulse(context.client());
                    } else if (payload.cue() == 1 && payload.targetEntityId() >= 0) {
                        beginFocus(context.client(), payload.targetEntityId());
                    }
                }));
        ClientTickEvents.END_CLIENT_TICK.register(MageClientController::tick);
    }

    private static void playFailurePulse(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(), 0.65f, 0.55f);
        secondFailurePulseTicks = 3;
    }

    private static void beginFocus(MinecraftClient client, int entityId) {
        if (client.player == null || client.world == null) {
            return;
        }
        savedYaw = client.player.getYaw();
        savedPitch = client.player.getPitch();
        focusTargetId = entityId;
        focusTicks = 30;
        returnTicks = 0;
    }

    private static void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            secondFailurePulseTicks = 0;
            focusTicks = 0;
            returnTicks = 0;
            return;
        }

        if (secondFailurePulseTicks > 0 && --secondFailurePulseTicks == 0) {
            client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(), 0.65f, 0.38f);
        }

        if (focusTicks > 0) {
            Entity target = client.world.getEntityById(focusTargetId);
            if (target != null) {
                double dx = target.getX() - client.player.getX();
                double dy = target.getEyeY() - client.player.getEyeY();
                double dz = target.getZ() - client.player.getZ();
                double horizontal = Math.sqrt(dx * dx + dz * dz);
                float yaw = (float) (MathHelper.atan2(dz, dx) * 57.2957763671875) - 90.0f;
                float pitch = (float) (-(MathHelper.atan2(dy, horizontal) * 57.2957763671875));
                client.player.setYaw(MathHelper.lerpAngleDegrees(0.28f, client.player.getYaw(), yaw));
                client.player.setPitch(MathHelper.lerp(0.28f, client.player.getPitch(), pitch));
            }
            focusTicks--;
            if (focusTicks == 0) {
                returnTicks = 10;
            }
            return;
        }

        if (returnTicks > 0) {
            client.player.setYaw(MathHelper.lerpAngleDegrees(0.28f, client.player.getYaw(), savedYaw));
            client.player.setPitch(MathHelper.lerp(0.28f, client.player.getPitch(), savedPitch));
            if (--returnTicks == 0) {
                client.player.setYaw(savedYaw);
                client.player.setPitch(savedPitch);
                focusTargetId = -1;
            }
        }
    }
}
