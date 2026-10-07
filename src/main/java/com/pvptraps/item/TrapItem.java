package com.pvptraps.item;

import com.pvptraps.config.ConfigManager;
import com.pvptraps.config.TrapConfig;
import com.pvptraps.entity.ModEntities;
import com.pvptraps.entity.TrapEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.type.TooltipDisplayComponent;
import java.util.function.Consumer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class TrapItem extends Item {

    private final String trapTypeId;

    public TrapItem(Settings settings, String trapTypeId) {
        super(settings);
        this.trapTypeId = trapTypeId;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> tooltip, TooltipType type) {
        TrapConfig.TrapTypeSettings config = ConfigManager.getTrapType(trapTypeId);
        tooltip.accept(Text.translatable("tooltip.pvptraps.effect." + trapTypeId).formatted(net.minecraft.util.Formatting.GRAY));
        tooltip.accept(Text.translatable("tooltip.pvptraps.damage", config.damage).formatted(net.minecraft.util.Formatting.GRAY));
        tooltip.accept(Text.translatable("tooltip.pvptraps.radius", config.triggerRadius).formatted(net.minecraft.util.Formatting.GRAY));
        tooltip.accept(Text.translatable("tooltip.pvptraps.cooldown", config.cooldownSeconds).formatted(net.minecraft.util.Formatting.GRAY));
        tooltip.accept(Text.translatable("tooltip.pvptraps.lifetime", config.trapLifetimeSeconds).formatted(net.minecraft.util.Formatting.GRAY));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (world.isClient() || player == null) {
            return ActionResult.SUCCESS;
        }

        TrapConfig.TrapTypeSettings config = ConfigManager.getTrapType(trapTypeId);
        ItemStack stack = context.getStack();
        if (player.getItemCooldownManager().isCoolingDown(stack)) {
            return ActionResult.FAIL;
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

        boolean spawned = ((ServerWorld) world).spawnEntity(trap);
        if (!spawned) {
            return ActionResult.FAIL;
        }

        if (config.cooldownSeconds > 0) {
            player.getItemCooldownManager().set(stack, config.cooldownSeconds * 20);
        }

        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }
}