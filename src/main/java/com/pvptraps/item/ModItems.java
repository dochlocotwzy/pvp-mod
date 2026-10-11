package com.pvpmage.item;

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
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of("pvpmage", path));
    }

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
    private static final RegistryKey<Item> FIREBALL_KEY = itemKey("fireball");
    private static final RegistryKey<Item> ELECTRIC_BOLT_KEY = itemKey("electric_bolt");
    private static final RegistryKey<Item> ICE_WALL_KEY = itemKey("ice_wall");
    private static final RegistryKey<Item> MAGIC_DOPPELGANGER_KEY = itemKey("magic_doppelganger");
    private static final RegistryKey<Item> GUST_OF_WIND_KEY = itemKey("gust_of_wind");
    private static final RegistryKey<Item> SHADOW_VEIL_KEY = itemKey("shadow_veil");

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
    public static final MageAbilityItem FIREBALL = Registry.register(
            Registries.ITEM, FIREBALL_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(FIREBALL_KEY).maxCount(1), "fireball", false));
    public static final MageAbilityItem ELECTRIC_BOLT = Registry.register(
            Registries.ITEM, ELECTRIC_BOLT_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(ELECTRIC_BOLT_KEY).maxCount(1), "electric_bolt", false));
    public static final MageAbilityItem ICE_WALL = Registry.register(Registries.ITEM, ICE_WALL_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(ICE_WALL_KEY).maxCount(1), "ice_wall", false));
    public static final MageAbilityItem MAGIC_DOPPELGANGER = Registry.register(Registries.ITEM, MAGIC_DOPPELGANGER_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(MAGIC_DOPPELGANGER_KEY).maxCount(1), "magic_doppelganger", false));
    public static final MageAbilityItem GUST_OF_WIND = Registry.register(Registries.ITEM, GUST_OF_WIND_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(GUST_OF_WIND_KEY).maxCount(1), "gust_of_wind", false));
    public static final MageAbilityItem SHADOW_VEIL = Registry.register(Registries.ITEM, SHADOW_VEIL_KEY,
            new MageAbilityItem(new Item.Settings().registryKey(SHADOW_VEIL_KEY).maxCount(1), "shadow_veil", false));

    public static void register() {
        net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
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
                    entries.add(FIREBALL);
                    entries.add(ELECTRIC_BOLT);
                    entries.add(ICE_WALL);
                    entries.add(MAGIC_DOPPELGANGER);
                    entries.add(GUST_OF_WIND);
                    entries.add(SHADOW_VEIL);
                });
    }
}
