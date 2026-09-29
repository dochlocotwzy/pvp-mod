package com.pvptraps.util;

import com.pvptraps.PvpTraps;
import com.pvptraps.config.TrapConfig;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class TrapEffects {

    private TrapEffects() {
    }

    public static void apply(PlayerEntity target, TrapConfig.TrapTypeSettings settings) {
        for (TrapConfig.PotionEffectEntry entry : settings.potionEffects) {
            applyPotionEffect(target, entry);
        }
        for (TrapConfig.AttributeModifierEntry entry : settings.attributeModifiers) {
            applyAttributeModifier(target, entry);
        }
    }

    private static void applyPotionEffect(PlayerEntity target, TrapConfig.PotionEffectEntry entry) {
        Identifier id = Identifier.tryParse(entry.effectId);
        if (id == null) {
            PvpTraps.LOGGER.warn("Некорректный id эффекта в конфиге: {}", entry.effectId);
            return;
        }
        StatusEffect effect = Registries.STATUS_EFFECT.get(id);
        if (effect == null) {
            PvpTraps.LOGGER.warn("Неизвестный эффект зелья в конфиге: {}", entry.effectId);
            return;
        }
        target.addStatusEffect(new StatusEffectInstance(effect, entry.durationTicks, entry.amplifier, false, true));
    }

    private static void applyAttributeModifier(PlayerEntity target, TrapConfig.AttributeModifierEntry entry) {
        Identifier attrId = Identifier.tryParse(entry.attributeId);
        if (attrId == null) {
            PvpTraps.LOGGER.warn("Некорректный id атрибута в конфиге: {}", entry.attributeId);
            return;
        }
        EntityAttribute attribute = Registries.ATTRIBUTE.get(attrId);
        if (attribute == null) {
            PvpTraps.LOGGER.warn("Неизвестный атрибут в конфиге: {}", entry.attributeId);
            return;
        }
        EntityAttributeInstance instance = target.getAttributeInstance(attribute);
        if (instance == null) {
            return;
        }

        EntityAttributeModifier.Operation operation;
        try {
            operation = EntityAttributeModifier.Operation.valueOf(entry.operation);
        } catch (IllegalArgumentException e) {
            PvpTraps.LOGGER.warn("Некорректная operation '{}' для атрибута {}, использую ADD_VALUE",
                    entry.operation, entry.attributeId);
            operation = EntityAttributeModifier.Operation.ADD_VALUE;
        }

        Identifier modifierId = Identifier.of(PvpTraps.MOD_ID,
                "trap_" + attrId.getPath().replace('.', '_') + "_" + target.getUuidAsString());

        instance.removeModifier(modifierId);

        EntityAttributeModifier modifier = new EntityAttributeModifier(modifierId, entry.amount, operation);
        instance.addTemporaryModifier(modifier);

        TrapEffectScheduler.scheduleRemoval(target.getUuid(), attribute, modifierId, entry.durationTicks);
    }
}
