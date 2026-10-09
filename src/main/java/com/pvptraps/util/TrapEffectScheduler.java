package com.pvptraps.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class TrapEffectScheduler {
    // Temporary attribute modifiers are removed by the server tick handler below.

    private record Pending(LivingEntity target, RegistryEntry<EntityAttribute> attribute, Identifier modifierId, long removeAtTick) {
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
                EntityAttributeInstance instance = p.target().getAttributeInstance(p.attribute());
                if (instance != null) {
                    instance.removeModifier(p.modifierId());
                }
                return true;
            });
        });
    }

    public static void scheduleRemoval(LivingEntity target, RegistryEntry<EntityAttribute> attribute, Identifier modifierId, int durationTicks) {
        PENDING.add(new Pending(target, attribute, modifierId, currentTick + durationTicks));
    }
}
