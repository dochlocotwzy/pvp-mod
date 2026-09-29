package com.pvptraps.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModEntities {

    private ModEntities() {
    }

    public static final EntityType<TrapEntity> TRAP =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of("pvptraps", "trap"),
                    FabricEntityTypeBuilder.createMob()
                            .entityFactory(TrapEntity::new)
                            .spawnGroup(SpawnGroup.MISC)
                            .dimensions(EntityDimensions.fixed(0.9f, 0.15f))
                            .build()
            );

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(TRAP, TrapEntity.createTrapAttributes());
    }
}
