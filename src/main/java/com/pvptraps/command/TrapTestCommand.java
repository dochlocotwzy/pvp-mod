package com.pvptraps.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pvptraps.entity.ModEntities;
import com.pvptraps.entity.TrapEntity;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class TrapTestCommand {
    private static final Set<String> TRAP_TYPES = Set.of(
            "spike", "ice", "poison", "electric", "smoke",
            "weakening", "sticky", "fire", "exhaustion"
    );

    private TrapTestCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("pvptraps")
                        .then(literal("test")
                                .requires(source -> source.hasPermissionLevel(2))
                                .then(argument("type", StringArgumentType.word())
                                        .then(argument("x", DoubleArgumentType.doubleArg())
                                                .then(argument("y", DoubleArgumentType.doubleArg())
                                                        .then(argument("z", DoubleArgumentType.doubleArg())
                                                                .executes(context -> spawnTest(
                                                                        context.getSource(),
                                                                        StringArgumentType.getString(context, "type"),
                                                                        DoubleArgumentType.getDouble(context, "x"),
                                                                        DoubleArgumentType.getDouble(context, "y"),
                                                                        DoubleArgumentType.getDouble(context, "z")
                                                                ))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int spawnTest(ServerCommandSource source, String type, double x, double y, double z) {
        if (!TRAP_TYPES.contains(type)) {
            source.sendError(Text.literal("Неизвестная ловушка. Типы: " + String.join(", ", TRAP_TYPES)));
            return 0;
        }

        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)
                || Math.abs(x) > 30_000_000 || Math.abs(z) > 30_000_000
                || y < source.getWorld().getBottomY()
                || y >= source.getWorld().getTopY()) {
            source.sendError(Text.literal("Координаты вне допустимых границ мира."));
            return 0;
        }

        var world = source.getWorld();
        BlockPos pos = BlockPos.ofFloored(x, y, z);
        TrapEntity trap = new TrapEntity(ModEntities.TRAP, world);
        trap.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
        trap.configure(type);
        if (!world.spawnEntity(trap)) {
            source.sendError(Text.literal("Не удалось создать ловушку."));
            return 0;
        }

        ZombieEntity zombie = EntityType.ZOMBIE.create(world, SpawnReason.COMMAND);
        if (zombie == null) {
            trap.discard();
            source.sendError(Text.literal("Не удалось создать зомби."));
            return 0;
        }
        zombie.refreshPositionAndAngles(pos.getX() + 2.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
        if (!world.spawnEntity(zombie)) {
            trap.discard();
            source.sendError(Text.literal("Ловушка создана, но зомби заспавнить не удалось."));
            return 0;
        }

        source.sendFeedback(() -> Text.literal("Тест: ловушка " + type + " создана в "
                + pos.getX() + " " + pos.getY() + " " + pos.getZ()
                + ", зомби — рядом. Подойди к ловушке или подведи к ней моба."), false);
        return 1;
    }
}
