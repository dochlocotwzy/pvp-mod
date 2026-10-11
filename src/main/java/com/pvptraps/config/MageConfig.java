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
        public double energyImpulseKnockback = 8.0;
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
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int gravitySphereManaCost = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int gravitySphereCooldownTicks = 320;
        @ConfigEntry.Gui.Tooltip
        public double gravitySphereRange = 24.0;
        @ConfigEntry.Gui.Tooltip
        public double gravitySphereRadius = 20.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 1200)
        public int gravitySphereDurationTicks = 720;
        @ConfigEntry.Gui.Tooltip
        public double gravitySpherePullStrength = 0.24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int fireballManaCost = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int fireballCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        public double fireballSpeed = 1.45;
        @ConfigEntry.Gui.Tooltip
        public double fireballRange = 30.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 5, max = 200)
        public int fireballLifetimeTicks = 55;
        @ConfigEntry.Gui.Tooltip
        public float fireballDamage = 7.0f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
        public int fireballIgniteSeconds = 4;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int electricBoltManaCost = 20;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int electricBoltCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        public double electricBoltSpeed = 1.9;
        @ConfigEntry.Gui.Tooltip
        public double electricBoltRange = 28.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 5, max = 200)
        public int electricBoltLifetimeTicks = 50;
        @ConfigEntry.Gui.Tooltip
        public float electricBoltDamage = 6.0f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
        public int electricBoltMaxJumps = 3;
        @ConfigEntry.Gui.Tooltip
        public double electricBoltJumpRange = 7.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int electricBoltChainParticleCount = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
        public int gravitySphereParticleMultiplier = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
        public int fireballGroundIgniteRadius = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int gravitySphereParticlesPerPulse = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int electricBoltJumpDamageParticleCount = 20;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int iceWallManaCost = 32;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int iceWallCooldownTicks = 360;
        @ConfigEntry.Gui.Tooltip
        public double iceWallDistance = 2.5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 3, max = 9)
        public int iceWallWidth = 5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 2, max = 5)
        public int iceWallHeight = 3;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 40, max = 240)
        public int iceWallDurationTicks = 100;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int magicDoppelgangerManaCost = 28;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int magicDoppelgangerCooldownTicks = 280;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 40, max = 400)
        public int magicDoppelgangerDurationTicks = 160;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int gustOfWindManaCost = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int gustOfWindCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        public double gustOfWindRange = 9.0;
        @ConfigEntry.Gui.Tooltip
        public double gustOfWindStrength = 1.25;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 120)
        public int gustOfWindConeDegrees = 70;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int shadowVeilManaCost = 26;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int shadowVeilCooldownTicks = 260;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 240)
        public int shadowVeilDurationTicks = 80;
        @ConfigEntry.Gui.Tooltip
        public double shadowVeilRadius = 4.0;
    }
}
