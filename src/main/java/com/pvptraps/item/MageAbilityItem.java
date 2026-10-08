package com.pvptraps.item;

import net.minecraft.item.Item;

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
}
