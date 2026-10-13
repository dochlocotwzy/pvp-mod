package com.pvpmage.gametest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.pvpmage.config.MageConfigManager;
import com.pvpmage.entity.ArcaneBoltEntity;
import com.pvpmage.entity.ModEntities;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.registry.Registries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
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

    @GameTest(structure = "fabric-gametest-api-v1:empty", maxTicks = 40)
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

    @GameTest(structure = "fabric-gametest-api-v1:empty", maxTicks = 40)
    public void fireballEntityCollisionDamagesAndIgnitesTarget(TestContext context)
            throws ReflectiveOperationException {
        ServerWorld world = context.getWorld();
        BlockPos pos = context.getAbsolutePos(new BlockPos(1, 2, 1));
        var target = EntityType.ZOMBIE.create(world, SpawnReason.TRIGGERED);
        context.assertTrue(target != null, "A zombie target should be constructible in the test world");
        target.refreshPositionAndAngles(pos.getX() + 2.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, 0.0f);
        context.assertTrue(world.spawnEntity(target), "The target should spawn in the test world");

        ArcaneBoltEntity fireball = new ArcaneBoltEntity(ModEntities.ARCANE_BOLT, world);
        fireball.refreshPositionAndAngles(pos.getX() + 1.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, 0.0f);
        fireball.configureFireball(64.0, 60, 6.0f, 4);
        context.assertTrue(world.spawnEntity(fireball), "The fireball projectile should spawn");
        float healthBefore = target.getHealth();

        // Invoke the real server-side collision handler directly. This isolates impact
        // behavior from projectile trajectory/physics and tests damage + ignition together.
        Method collisionHandler = ArcaneBoltEntity.class.getDeclaredMethod("onCollision", HitResult.class);
        collisionHandler.setAccessible(true);
        collisionHandler.invoke(fireball, new EntityHitResult(target));

        context.assertTrue(target.getHealth() < healthBefore,
                "Fireball entity impact should reduce the target's health");
        context.assertTrue(target.isOnFire(),
                "Fireball entity impact should ignite the target");
        context.assertTrue(fireball.isRemoved(),
                "Fireball should be discarded after collision handling");
        context.complete();
    }

    @GameTest(structure = "fabric-gametest-api-v1:empty", maxTicks = 40)
    public void electricBoltImpactSlowsAndChainsDamage(TestContext context)
            throws ReflectiveOperationException {
        ServerWorld world = context.getWorld();
        BlockPos pos = context.getAbsolutePos(new BlockPos(1, 2, 1));
        var firstTarget = EntityType.ZOMBIE.create(world, SpawnReason.TRIGGERED);
        var secondTarget = EntityType.ZOMBIE.create(world, SpawnReason.TRIGGERED);
        context.assertTrue(firstTarget != null && secondTarget != null,
                "Both lightning targets should be constructible in the test world");
        firstTarget.refreshPositionAndAngles(pos.getX() + 2.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, 0.0f);
        secondTarget.refreshPositionAndAngles(pos.getX() + 3.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, 0.0f);
        context.assertTrue(world.spawnEntity(firstTarget), "First lightning target should spawn");
        context.assertTrue(world.spawnEntity(secondTarget), "Second lightning target should spawn");
        float firstHealthBefore = firstTarget.getHealth();
        float secondHealthBefore = secondTarget.getHealth();

        ArcaneBoltEntity electricBolt = new ArcaneBoltEntity(ModEntities.ARCANE_BOLT, world);
        electricBolt.refreshPositionAndAngles(pos.getX() + 1.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, 0.0f);
        electricBolt.configureElectricBolt(64.0, 60, 5.0f);
        context.assertTrue(world.spawnEntity(electricBolt), "Electric bolt projectile should spawn");

        Method collisionHandler = ArcaneBoltEntity.class.getDeclaredMethod("onCollision", HitResult.class);
        collisionHandler.setAccessible(true);
        collisionHandler.invoke(electricBolt, new EntityHitResult(firstTarget));

        context.assertTrue(firstTarget.getHealth() < firstHealthBefore,
                "Electric bolt impact should damage the first target");
        context.assertTrue(firstTarget.hasStatusEffect(net.minecraft.entity.effect.StatusEffects.SLOWNESS),
                "Electric bolt impact should apply slowness to the first target");
        context.assertTrue(secondTarget.getHealth() < secondHealthBefore,
                "Chain lightning should damage a nearby second target");
        context.assertTrue(electricBolt.isRemoved(),
                "Electric bolt should be discarded after collision handling");
        context.complete();
    }

    @GameTest(structure = "fabric-gametest-api-v1:empty", maxTicks = 40)
    public void automaticFlightCollisionDamagesTarget(TestContext context) {
        ServerWorld world = context.getWorld();
        BlockPos pos = context.getAbsolutePos(new BlockPos(1, 2, 1));
        var target = EntityType.ZOMBIE.create(world, SpawnReason.TRIGGERED);
        context.assertTrue(target != null, "A zombie target should be constructible in the test world");
        target.refreshPositionAndAngles(pos.getX() + 4.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, 0.0f);
        context.assertTrue(world.spawnEntity(target), "The target should spawn in the test world");
        target.setAiDisabled(true);

        ArcaneBoltEntity bolt = new ArcaneBoltEntity(ModEntities.ARCANE_BOLT, world);
        bolt.refreshPositionAndAngles(pos.getX() + 1.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, 0.0f);
        bolt.configure(64.0, 60, 6.0f);
        bolt.setVelocity(1.0, 0.0, 0.0);
        context.assertTrue(world.spawnEntity(bolt), "The moving arcane bolt should spawn");
        float healthBefore = target.getHealth();

        // Do not call onCollision here: normal entity ticks must discover the hit.
        context.waitAndRun(8, () -> {
            context.assertTrue(target.getHealth() < healthBefore,
                    "A flying arcane bolt should automatically collide with and damage the target");
            context.assertTrue(bolt.isRemoved(),
                    "The projectile should be discarded by its automatically detected impact");
            context.complete();
        });
    }

    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        try {
            method.invoke(this, context);
        } catch (InvocationTargetException invocationFailure) {
            Throwable cause = invocationFailure.getCause();
            System.err.println("GameTest method '" + method.getName() + "' threw:");
            if (cause != null) {
                cause.printStackTrace(System.err);
                if (cause instanceof RuntimeException runtimeFailure) {
                    throw runtimeFailure;
                }
                if (cause instanceof Error errorFailure) {
                    throw errorFailure;
                }
                throw new RuntimeException("GameTest method '" + method.getName() + "' failed", cause);
            }
            throw invocationFailure;
        }
    }
}