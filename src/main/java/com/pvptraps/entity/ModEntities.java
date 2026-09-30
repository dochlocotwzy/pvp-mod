package com.pvptraps.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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

    private static final RegistryKey<EntityType<?>> TRAP_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("pvptraps", "trap"));

    public static final EntityType<TrapEntity> TRAP =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    TRAP_KEY,
                    FabricEntityTypeBuilder.createMob()
                            .entityFactory(TrapEntity::new)
                            .spawnGroup(SpawnGroup.MISC)
                            .dimensions(EntityDimensions.fixed(0.9f, 0.15f))
                            .build(TRAP_KEY)
            );

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(TRAP, TrapEntity.createTrapAttributes());
    }
}
