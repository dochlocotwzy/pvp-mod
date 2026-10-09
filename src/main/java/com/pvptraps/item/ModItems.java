package com.pvptraps.item;

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

    private static RegistryKey<Item> itemKey(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of("pvptraps", path));
    }

    private static Item registerTrap(String id) {
        RegistryKey<Item> key = itemKey(id + "_trap");
        return Registry.register(Registries.ITEM, key,
                new TrapItem(new Item.Settings().registryKey(key).maxCount(16), id));
    }

    public static final Item SPIKE_TRAP = registerTrap("spike");
    public static final Item ICE_TRAP = registerTrap("ice");
    public static final Item POISON_TRAP = registerTrap("poison");
    public static final Item ELECTRIC_TRAP = registerTrap("electric");
    public static final Item SMOKE_TRAP = registerTrap("smoke");
    public static final Item WEAKENING_TRAP = registerTrap("weakening");
    public static final Item STICKY_TRAP = registerTrap("sticky");
    public static final Item FIRE_TRAP = registerTrap("fire");
    public static final Item EXHAUSTION_TRAP = registerTrap("exhaustion");
    /** Legacy item ID retained for worlds and commands that already use it. */
    private static final RegistryKey<Item> TRAP_ITEM_KEY = itemKey("trap");
    public static final Item TRAP_ITEM = Registry.register(
            Registries.ITEM, TRAP_ITEM_KEY,
            new TrapItem(new Item.Settings().registryKey(TRAP_ITEM_KEY).maxCount(16), "default"));

    private static final RegistryKey<Item> MAGIC_BARRIER_KEY = itemKey("magic_barrier");
    private static final RegistryKey<Item> FLASH_OF_LIGHT_KEY = itemKey("flash_of_light");
    private static final RegistryKey<Item> ARCANE_BOLT_KEY = itemKey("arcane_bolt");
    private static final RegistryKey<Item> ENERGY_IMPULSE_KEY = itemKey("energy_impulse");

    public static final MageAbilityItem MAGIC_BARRIER = Registry.register(
            Registries.ITEM, MAGIC_BARRIER_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(MAGIC_BARRIER_KEY).maxCount(1), "magic_barrier", true));
    public static final MageAbilityItem FLASH_OF_LIGHT = Registry.register(
            Registries.ITEM, FLASH_OF_LIGHT_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(FLASH_OF_LIGHT_KEY).maxCount(1), "flash_of_light", false));
    public static final MageAbilityItem ARCANE_BOLT = Registry.register(
            Registries.ITEM, ARCANE_BOLT_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(ARCANE_BOLT_KEY).maxCount(1), "arcane_bolt", false));
    public static final MageAbilityItem ENERGY_IMPULSE = Registry.register(
            Registries.ITEM, ENERGY_IMPULSE_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(ENERGY_IMPULSE_KEY).maxCount(1), "energy_impulse", false));

    public static void register() {
        net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.add(SPIKE_TRAP);
                    entries.add(ICE_TRAP);
                    entries.add(POISON_TRAP);
                    entries.add(ELECTRIC_TRAP);
                    entries.add(SMOKE_TRAP);
                    entries.add(WEAKENING_TRAP);
                    entries.add(STICKY_TRAP);
                    entries.add(FIRE_TRAP);
                    entries.add(EXHAUSTION_TRAP);
                    entries.add(TRAP_ITEM);
                    entries.add(MAGIC_BARRIER);
                    entries.add(FLASH_OF_LIGHT);
                    entries.add(ARCANE_BOLT);
                    entries.add(ENERGY_IMPULSE);
                });
    }
}
