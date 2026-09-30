package com.pvptraps.item;

import com.pvptraps.entity.ModEntities;
import com.pvptraps.entity.TrapEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrapItem extends Item {

    private final String trapTypeId;

    public TrapItem(Settings settings, String trapTypeId) {
        super(settings);
        this.trapTypeId = trapTypeId;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (world.isClient() || player == null) {
            return ActionResult.SUCCESS;
        }

        BlockPos placePos = context.getBlockPos().offset(context.getSide());

        TrapEntity trap = new TrapEntity(ModEntities.TRAP, world);
        trap.refreshPositionAndAngles(
                placePos.getX() + 0.5,
                placePos.getY(),
                placePos.getZ() + 0.5,
                0f, 0f
        );
        trap.configure(trapTypeId, player);

        ((ServerWorld) world).spawnEntity(trap);

        if (!player.getAbilities().creativeMode) {
            ItemStack stack = context.getStack();
            stack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }
}
