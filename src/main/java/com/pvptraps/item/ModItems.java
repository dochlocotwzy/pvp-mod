package com.pvptraps.item;

import com.pvptraps.config.ConfigManager;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class ModItems {
    private ModItems() {}

    public static final Item SPIKE_TRAP = register("spike_trap", "spike");
    public static final Item ICE_TRAP = register("ice_trap", "ice");
    public static final Item POISON_TRAP = register("poison_trap", "poison");
    public static final Item ELECTRIC_TRAP = register("electric_trap", "electric");
    public static final Item SMOKE_TRAP = register("smoke_trap", "smoke");
    public static final Item WEAKENING_TRAP = register("weakening_trap", "weakening");
    public static final Item STICKY_TRAP = register("sticky_trap", "sticky");
    public static final Item FIRE_TRAP = register("fire_trap", "fire");
    public static final Item EXHAUSTION_TRAP = register("exhaustion_trap", "exhaustion");

    private static Item register(String itemId, String trapTypeId) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("pvptraps", itemId));
        int count = Math.max(1, Math.min(64, ConfigManager.getTrapType(trapTypeId).maxStackSize));
        return Registry.register(Registries.ITEM, key,
                new TrapItem(new Item.Settings().registryKey(key).maxCount(count), trapTypeId));
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(SPIKE_TRAP);
            entries.add(ICE_TRAP);
            entries.add(POISON_TRAP);
            entries.add(ELECTRIC_TRAP);
            entries.add(SMOKE_TRAP);
            entries.add(WEAKENING_TRAP);
            entries.add(STICKY_TRAP);
            entries.add(FIRE_TRAP);
            entries.add(EXHAUSTION_TRAP);
        });
    }
}