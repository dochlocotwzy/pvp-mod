package com.pvptraps.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModItems {

    private ModItems() {
    }

    public static final Item TRAP_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of("pvptraps", "trap"),
            new TrapItem(new Item.Settings().maxCount(16), "default")
    );

    public static void register() {
        net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> entries.add(TRAP_ITEM));
    }
}
