package com.pvptraps.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TrapEffectScheduler {

    private record Pending(UUID playerId, EntityAttribute attribute, Identifier modifierId, long removeAtTick) {
    }

    private static final List<Pending> PENDING = new ArrayList<>();
    private static long currentTick = 0;

    private TrapEffectScheduler() {
    }

    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            currentTick++;
            PENDING.removeIf(p -> {
                if (currentTick < p.removeAtTick()) {
                    return false;
                }
                ServerPlayerEntity player = server.getPlayerManager().getPlayer(p.playerId());
                if (player != null) {
                    EntityAttributeInstance instance = player.getAttributeInstance(p.attribute());
                    if (instance != null) {
                        instance.removeModifier(p.modifierId());
                    }
                }
                return true;
            });
        });
    }

    public static void scheduleRemoval(UUID playerId, EntityAttribute attribute, Identifier modifierId, int durationTicks) {
        PENDING.add(new Pending(playerId, attribute, modifierId, currentTick + durationTicks));
    }
}
