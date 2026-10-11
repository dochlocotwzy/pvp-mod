package com.pvptraps.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class ModEntities {
    private ModEntities() {
    }

    private static final RegistryKey<EntityType<?>> ARCANE_BOLT_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("pvptraps", "arcane_bolt"));

    public static final EntityType<ArcaneBoltEntity> ARCANE_BOLT = Registry.register(
            Registries.ENTITY_TYPE, ARCANE_BOLT_KEY,
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ArcaneBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.4f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(1)
                    .build(ARCANE_BOLT_KEY));
}
