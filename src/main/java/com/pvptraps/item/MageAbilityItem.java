package com.pvpmage.item;

import com.pvpmage.config.MageConfig;
import com.pvpmage.config.MageConfigManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public final class MageAbilityItem extends Item {
    private final String abilityId;
    private final boolean targetAlly;

    public MageAbilityItem(Settings settings, String abilityId, boolean targetAlly) {
        super(settings);
        this.abilityId = abilityId;
        this.targetAlly = targetAlly;
    }

    public String getAbilityId() {
        return abilityId;
    }

    public boolean targetsAlly() {
        return targetAlly;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, tooltip, type);
        MageConfig config = MageConfigManager.get();
        MageConfig.AbilitySettings a = config.abilities;
        tooltip.accept(Text.translatable("item.pvpmage.tooltip.mana", manaCost(a)).formatted(Formatting.AQUA));
        tooltip.accept(Text.translatable("item.pvpmage.tooltip.cooldown", seconds(cooldown(a))).formatted(Formatting.GRAY));
        switch (abilityId) {
            case "magic_barrier" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.barrierDurationTicks)));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.resistance", a.barrierResistanceAmplifier + 1));
            }
            case "flash_of_light" -> tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.flashDurationTicks)));
            case "arcane_bolt" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.damage", a.arcaneBoltDamage));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.range", a.arcaneBoltRange));
            }
            case "energy_impulse" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.radius", a.energyImpulseRadius));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.knockback", a.energyImpulseKnockback));
            }
            case "frost_nova" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.radius", a.frostNovaRadius));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.frostNovaDurationTicks)));
            }
            case "healing_rune" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.radius", a.healingRuneRadius));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.heal", a.healingRuneHealAmount));
            }
            case "smoke_veil" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.radius", a.smokeVeilRadius));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.smokeVeilDurationTicks)));
            }
            case "gale_step" -> tooltip.accept(Text.translatable("item.pvpmage.tooltip.distance", a.galeStepDistance));
            case "runic_seal" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.range", a.runicSealRange));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.runicSealDurationTicks)));
            }
            case "blessing" -> tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.blessingDurationTicks)));
            case "gravity_sphere" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.radius", a.gravitySphereRadius));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.gravitySphereDurationTicks)));
            }
            case "fireball" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.damage", a.fireballDamage));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.range", a.fireballRange));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.ignite", a.fireballIgniteSeconds));
            }
            case "electric_bolt" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.damage", a.electricBoltDamage));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.range", a.electricBoltRange));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.jumps", a.electricBoltMaxJumps));
            }
            case "ice_wall" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.wallSize", a.iceWallWidth, a.iceWallHeight));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.iceWallDurationTicks)));
            }
            case "magic_doppelganger" -> tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.magicDoppelgangerDurationTicks)));
            case "gust_of_wind" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.range", a.gustOfWindRange));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.strength", a.gustOfWindStrength));
            }
            case "shadow_veil" -> {
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.radius", a.shadowVeilRadius));
                tooltip.accept(Text.translatable("item.pvpmage.tooltip.duration", seconds(a.shadowVeilDurationTicks)));
            }
        }
    }

    private int manaCost(MageConfig.AbilitySettings a) {
        return switch (abilityId) {
            case "magic_barrier" -> a.barrierManaCost;
            case "flash_of_light" -> a.flashManaCost;
            case "arcane_bolt" -> a.arcaneBoltManaCost;
            case "energy_impulse" -> a.energyImpulseManaCost;
            case "frost_nova" -> a.frostNovaManaCost;
            case "healing_rune" -> a.healingRuneManaCost;
            case "smoke_veil" -> a.smokeVeilManaCost;
            case "gale_step" -> a.galeStepManaCost;
            case "runic_seal" -> a.runicSealManaCost;
            case "blessing" -> a.blessingManaCost;
            case "gravity_sphere" -> a.gravitySphereManaCost;
            case "fireball" -> a.fireballManaCost;
            case "electric_bolt" -> a.electricBoltManaCost;
            case "ice_wall" -> a.iceWallManaCost;
            case "magic_doppelganger" -> a.magicDoppelgangerManaCost;
            case "gust_of_wind" -> a.gustOfWindManaCost;
            case "shadow_veil" -> a.shadowVeilManaCost;
            default -> 0;
        };
    }

    private int cooldown(MageConfig.AbilitySettings a) {
        return switch (abilityId) {
            case "magic_barrier" -> a.barrierCooldownTicks;
            case "flash_of_light" -> a.flashCooldownTicks;
            case "arcane_bolt" -> a.arcaneBoltCooldownTicks;
            case "energy_impulse" -> a.energyImpulseCooldownTicks;
            case "frost_nova" -> a.frostNovaCooldownTicks;
            case "healing_rune" -> a.healingRuneCooldownTicks;
            case "smoke_veil" -> a.smokeVeilCooldownTicks;
            case "gale_step" -> a.galeStepCooldownTicks;
            case "runic_seal" -> a.runicSealCooldownTicks;
            case "blessing" -> a.blessingCooldownTicks;
            case "gravity_sphere" -> a.gravitySphereCooldownTicks;
            case "fireball" -> a.fireballCooldownTicks;
            case "electric_bolt" -> a.electricBoltCooldownTicks;
            case "ice_wall" -> a.iceWallCooldownTicks;
            case "magic_doppelganger" -> a.magicDoppelgangerCooldownTicks;
            case "gust_of_wind" -> a.gustOfWindCooldownTicks;
            case "shadow_veil" -> a.shadowVeilCooldownTicks;
            default -> 0;
        };
    }

    private static double seconds(int ticks) {
        return Math.round(Math.max(0, ticks) / 20.0 * 10.0) / 10.0;
    }
}
