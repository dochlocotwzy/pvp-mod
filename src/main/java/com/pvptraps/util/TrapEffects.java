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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public final class TrapEffects {

    // Avoid overlapping triggers removing a newer temporary modifier too early.
    private static long modifierSequence = 0;

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
        Optional<RegistryEntry.Reference<StatusEffect>> effectEntry = Registries.STATUS_EFFECT.getEntry(id);
        if (effectEntry.isEmpty()) {
            PvpTraps.LOGGER.warn("Неизвестный эффект зелья в конфиге: {}", entry.effectId);
            return;
        }
        target.addStatusEffect(new StatusEffectInstance(effectEntry.get(), entry.durationTicks, entry.amplifier, false, true));
    }

    private static void applyAttributeModifier(PlayerEntity target, TrapConfig.AttributeModifierEntry entry) {
        Identifier attrId = Identifier.tryParse(entry.attributeId);
        if (attrId == null) {
            PvpTraps.LOGGER.warn("Некорректный id атрибута в конфиге: {}", entry.attributeId);
            return;
        }
        Optional<RegistryEntry.Reference<EntityAttribute>> attributeEntry = Registries.ATTRIBUTE.getEntry(attrId);
        if (attributeEntry.isEmpty()) {
            PvpTraps.LOGGER.warn("Неизвестный атрибут в конфиге: {}", entry.attributeId);
            return;
        }
        RegistryEntry<EntityAttribute> attribute = attributeEntry.get();
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
                "trap_" + attrId.getPath().replace('.', '_') + "_" + target.getUuidAsString()
                        + "_" + (++modifierSequence));

        instance.removeModifier(modifierId);

        EntityAttributeModifier modifier = new EntityAttributeModifier(modifierId, entry.amount, operation);
        instance.addTemporaryModifier(modifier);

        TrapEffectScheduler.scheduleRemoval(target.getUuid(), attribute, modifierId, entry.durationTicks);
    }
}
