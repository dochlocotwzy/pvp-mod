package com.pvptraps.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class ModItems {

    private ModItems() {
    }

    private static final Identifier TRAP_ID = Identifier.of("pvptraps", "trap");
    private static final RegistryKey<Item> TRAP_KEY = RegistryKey.of(RegistryKeys.ITEM, TRAP_ID);

    public static final Item TRAP_ITEM = Registry.register(
            Registries.ITEM,
            TRAP_KEY,
            new TrapItem(new Item.Settings().maxCount(16).registryKey(TRAP_KEY), "default")
    );

    public static void register() {
        net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> entries.add(TRAP_ITEM));
    }
}
