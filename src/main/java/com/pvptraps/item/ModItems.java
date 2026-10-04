package com.pvptraps.item;

import com.pvptraps.config.ConfigManager;
import com.pvptraps.entity.ModEntities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class ModItems {

    private ModItems() {
    }

    private static final RegistryKey<Item> TRAP_ITEM_KEY = RegistryKey.of(
            RegistryKeys.ITEM,
            Identifier.of("pvptraps", "trap")
    );

    public static final Item TRAP_ITEM = Registry.register(
            Registries.ITEM,
            TRAP_ITEM_KEY,
            new TrapItem(new Item.Settings().registryKey(TRAP_ITEM_KEY)
                    .maxCount(Math.max(1, Math.min(64, ConfigManager.getTrapType("default").maxStackSize))), "default")
    );

    public static void register() {
        net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> entries.add(TRAP_ITEM));
    }
}