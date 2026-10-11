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
    private static final RegistryKey<Item> FROST_NOVA_KEY = itemKey("frost_nova");
    private static final RegistryKey<Item> HEALING_RUNE_KEY = itemKey("healing_rune");
    private static final RegistryKey<Item> SMOKE_VEIL_KEY = itemKey("smoke_veil");
    private static final RegistryKey<Item> GALE_STEP_KEY = itemKey("gale_step");
    private static final RegistryKey<Item> RUNIC_SEAL_KEY = itemKey("runic_seal");
    private static final RegistryKey<Item> BLESSING_KEY = itemKey("blessing");
    private static final RegistryKey<Item> GRAVITY_SPHERE_KEY = itemKey("gravity_sphere");

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
    public static final MageAbilityItem FROST_NOVA = Registry.register(
            Registries.ITEM, FROST_NOVA_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(FROST_NOVA_KEY).maxCount(1), "frost_nova", false));
    public static final MageAbilityItem HEALING_RUNE = Registry.register(
            Registries.ITEM, HEALING_RUNE_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(HEALING_RUNE_KEY).maxCount(1), "healing_rune", false));
    public static final MageAbilityItem SMOKE_VEIL = Registry.register(
            Registries.ITEM, SMOKE_VEIL_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(SMOKE_VEIL_KEY).maxCount(1), "smoke_veil", false));
    public static final MageAbilityItem GALE_STEP = Registry.register(
            Registries.ITEM, GALE_STEP_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(GALE_STEP_KEY).maxCount(1), "gale_step", false));
    public static final MageAbilityItem RUNIC_SEAL = Registry.register(
            Registries.ITEM, RUNIC_SEAL_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(RUNIC_SEAL_KEY).maxCount(1), "runic_seal", false));
    public static final MageAbilityItem BLESSING = Registry.register(
            Registries.ITEM, BLESSING_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(BLESSING_KEY).maxCount(1), "blessing", true));
    public static final MageAbilityItem GRAVITY_SPHERE = Registry.register(
            Registries.ITEM, GRAVITY_SPHERE_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(GRAVITY_SPHERE_KEY).maxCount(1), "gravity_sphere", false));

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
                    entries.add(MAGIC_BARRIER);
                    entries.add(FLASH_OF_LIGHT);
                    entries.add(ARCANE_BOLT);
                    entries.add(ENERGY_IMPULSE);
                    entries.add(FROST_NOVA);
                    entries.add(HEALING_RUNE);
                    entries.add(SMOKE_VEIL);
                    entries.add(GALE_STEP);
                    entries.add(RUNIC_SEAL);
                    entries.add(BLESSING);
                    entries.add(GRAVITY_SPHERE);
                });
    }
}
