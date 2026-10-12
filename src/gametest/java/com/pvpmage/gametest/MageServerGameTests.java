package com.pvpmage.gametest;

import java.lang.reflect.Method;

import com.pvpmage.config.MageConfigManager;
import com.pvpmage.entity.ArcaneBoltEntity;
import com.pvpmage.entity.ModEntities;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * Minimal server-side integration tests. Keep these isolated from the live server
 * and expand them only after the GameTest harness is confirmed working in CI.
 */
public final class MageServerGameTests implements CustomTestMethodInvoker {
    @GameTest(structure = "fabric-gametest-api-v1:empty", maxTicks = 40)
    public void mageConfigIsAvailable(TestContext context) {
        context.assertTrue(MageConfigManager.get() != null,
                "MageConfigManager must be initialized before game tests run");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, maxTicks = 40)
    public void arcaneBoltCanSpawnAndExpire(TestContext context) {
        ServerWorld world = context.getWorld();
        Identifier expectedId = Identifier.of("pvpmage", "arcane_bolt");
        context.assertTrue(expectedId.equals(Registries.ENTITY_TYPE.getId(ModEntities.ARCANE_BOLT)),
                "Arcane bolt entity must be registered as pvpmage:arcane_bolt");

        BlockPos pos = context.getAbsolutePos(new BlockPos(1, 2, 1));
        ArcaneBoltEntity bolt = new ArcaneBoltEntity(ModEntities.ARCANE_BOLT, world);
        bolt.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, 0.0f);
        bolt.configure(64.0, 2, 0.0f);

        context.assertTrue(world.spawnEntity(bolt), "Arcane bolt should spawn in the test world");
        context.waitAndRun(5, () -> {
            context.assertTrue(bolt.isRemoved(),
                    "Arcane bolt should expire after its configured lifetime without crashing the server");
            context.complete();
        });
    }

    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }
}