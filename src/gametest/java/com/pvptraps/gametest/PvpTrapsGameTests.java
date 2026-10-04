package com.pvptraps.gametest;

import com.pvptraps.entity.ModEntities;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;

public class PvpTrapsGameTests implements FabricGameTest {
    private static final String[] TRAP_ITEMS = {
            "spike_trap", "ice_trap", "poison_trap", "electric_trap",
            "smoke_trap", "weakening_trap", "sticky_trap", "fire_trap",
            "exhaustion_trap"
    };

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void trapEntityIsRegistered(TestContext context) {
        context.assertTrue(
                Registries.ENTITY_TYPE.get(Identifier.of("pvptraps", "trap")) == ModEntities.TRAP,
                "PvP Traps entity type must be registered"
        );
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void allTrapItemsAreRegistered(TestContext context) {
        for (String itemId : TRAP_ITEMS) {
            context.assertTrue(
                    Registries.ITEM.get(Identifier.of("pvptraps", itemId)) != Items.AIR,
                    "Trap item must be registered: " + itemId
            );
        }
        context.complete();
    }
}
