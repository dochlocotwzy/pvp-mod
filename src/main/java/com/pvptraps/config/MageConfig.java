package com.pvptraps.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "pvptraps_mage")
public class MageConfig implements ConfigData {
    @ConfigEntry.Category("mana")
    @ConfigEntry.Gui.CollapsibleObject
    public ManaSettings mana = new ManaSettings();

    @ConfigEntry.Category("abilities")
    @ConfigEntry.Gui.CollapsibleObject
    public AbilitySettings abilities = new AbilitySettings();

    public static class ManaSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 500)
        public int maxMana = 100;
        @ConfigEntry.Gui.Tooltip
        public double regenPerSecond = 4.0;
        @ConfigEntry.Gui.Tooltip
        public boolean persistThroughDeath = true;
    }

    public static class AbilitySettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int targetRangeBlocks = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int barrierManaCost = 25;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int barrierCooldownTicks = 200;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 600)
        public int barrierDurationTicks = 80;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int flashManaCost = 20;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int flashCooldownTicks = 120;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 400)
        public int flashDurationTicks = 60;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int arcaneBoltManaCost = 15;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int arcaneBoltCooldownTicks = 40;
        @ConfigEntry.Gui.Tooltip
        public double arcaneBoltSpeed = 1.7;
        @ConfigEntry.Gui.Tooltip
        public double arcaneBoltRange = 32.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 5, max = 200)
        public int arcaneBoltLifetimeTicks = 60;
        @ConfigEntry.Gui.Tooltip
        public float arcaneBoltDamage = 6.0f;
        @ConfigEntry.Gui.Tooltip
        public float arcaneBoltSize = 0.4f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int energyImpulseManaCost = 25;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int energyImpulseCooldownTicks = 160;
        @ConfigEntry.Gui.Tooltip
        public double energyImpulseRadius = 7.0;
        @ConfigEntry.Gui.Tooltip
        public double energyImpulseKnockback = 1.8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int frostNovaManaCost = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int frostNovaCooldownTicks = 240;
        @ConfigEntry.Gui.Tooltip
        public double frostNovaRadius = 5.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 200)
        public int frostNovaDurationTicks = 80;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int healingRuneManaCost = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int healingRuneCooldownTicks = 300;
        @ConfigEntry.Gui.Tooltip
        public double healingRuneRadius = 6.0;
        @ConfigEntry.Gui.Tooltip
        public float healingRuneHealAmount = 6.0f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int smokeVeilManaCost = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int smokeVeilCooldownTicks = 360;
        @ConfigEntry.Gui.Tooltip
        public double smokeVeilRadius = 10.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 1200)
        public int smokeVeilDurationTicks = 240;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int galeStepManaCost = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int galeStepCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        public double galeStepDistance = 4.0;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int runicSealManaCost = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int runicSealCooldownTicks = 180;
        @ConfigEntry.Gui.Tooltip
        public double runicSealRange = 12.0;
        @ConfigEntry.Gui.Tooltip
        public double runicSealRadius = 2.25;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 200)
        public int runicSealDurationTicks = 60;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int blessingManaCost = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int blessingCooldownTicks = 240;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 600)
        public int blessingDurationTicks = 160;
    }
}
