package com.pvpmage.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "pvpmage_mage")
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
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int barrierResistanceAmplifier = 1;
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
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int flashResistanceAmplifier = 0;
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
        @ConfigEntry.BoundedDiscrete(min = 8, max = 128)
        public int energyImpulseShockwaveSegments = 48;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int energyImpulseTargetParticleCount = 8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int energyImpulseCenterParticleCount = 36;
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
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int frostNovaSlownessAmplifier = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int frostNovaWeaknessAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 8, max = 128)
        public int frostNovaRingSegments = 64;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 256)
        public int frostNovaCenterParticleCount = 80;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int frostNovaTargetParticleCount = 22;
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
        public double smokeVeilRevealRadius = 6.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int galeStepManaCost = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        public int galeStepCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        public double galeStepDistance = 4.0;
        @ConfigEntry.Gui.Tooltip
        public double galeStepCollisionSampleStep = 0.05;


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
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int runicSealSlownessAmplifier = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int runicSealWeaknessAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        public int runicSealGlowDurationTicks = 40;

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
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int blessingSpeedAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int blessingRegenerationAmplifier = 0;
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
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int gravitySpherePullIntervalTicks = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 40)
        public int gravitySphereEffectIntervalTicks = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 40)
        public int gravitySphereSlownessDurationTicks = 6;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int gravitySphereSlownessAmplifier = 0;
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
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        public int electricBoltChainSparkEveryNthPoint = 3;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        public int electricBoltChainSparkParticleCount = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
        public int gravitySphereParticleMultiplier = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 32)
        public int gravitySphereBaseParticleCount = 6;
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
        public int electricBoltSlowDurationTicks = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int electricBoltSlowAmplifier = 0;

        // Particle counts for barriers, flash and projectile visuals are configurable independently.
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int barrierBeamSegments = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int barrierRingSegments = 32;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int barrierCenterParticleCount = 32;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int flashGlowParticleCount = 36;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int flashFireworkParticleCount = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int arcaneBoltLaunchParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int fireballLaunchParticleCount = 10;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int electricBoltLaunchParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int arcaneBoltTrailParticleCount = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int arcaneBoltSparkParticleCount = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int fireballTrailFlameParticleCount = 5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int fireballTrailSmokeParticleCount = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int electricBoltTrailSparkParticleCount = 5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int electricBoltTrailEndRodParticleCount = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int projectileExpireEndRodParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int projectileExpireFireworkParticleCount = 8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int fireballImpactFlameParticleCount = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int fireballImpactLavaParticleCount = 5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int fireballImpactSmokeParticleCount = 14;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int electricBoltImpactSparkParticleCount = 28;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int electricBoltImpactEndRodParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int arcaneBoltImpactEndRodParticleCount = 14;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int arcaneBoltImpactEnchantParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int fireballTargetFlameParticleCount = 8;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int galeStepDepartureCloudParticleCount = 14;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int galeStepMidpointSparkParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int galeStepArrivalCloudParticleCount = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int runicSealTargetParticleCount = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 8, max = 128)
        public int runicSealRingSegments = 40;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int runicSealCenterParticleCount = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int blessingVillagerParticleCount = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int blessingEndRodParticleCount = 14;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 256)
        public int gravitySphereInitialPortalParticleCount = 48;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 256)
        public int gravitySphereInitialEndRodParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int gravitySphereExpireParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int healingRuneTargetHeartParticleCount = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int healingRuneTargetVillagerParticleCount = 10;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 8, max = 128)
        public int healingRuneRingSegments = 40;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int healingRuneCenterParticleCount = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int frostNovaTargetCloudParticleCount = 8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        public int frostNovaRingSnowflakeParticleCount = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        public int frostNovaRingEndRodParticleCount = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        public int iceWallPlacementParticleCount = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        public int iceWallRemovalParticleCount = 3;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int doppelgangerSpawnPortalParticleCount = 28;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int doppelgangerSpawnEndRodParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 32)
        public int doppelgangerIdleParticleCount = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int doppelgangerExpireParticleCount = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int gustOfWindTargetCloudParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int gustOfWindTargetPoofParticleCount = 6;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int shadowVeilTargetSmokeParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int smokeVeilCastSmokeParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int smokeVeilCastLargeSmokeParticleCount = 6;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int smokeVeilRevealSmokeParticleCount = 14;

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
        public double doppelgangerPlacementMinDistance = 1.5;
        @ConfigEntry.Gui.Tooltip
        public double doppelgangerPlacementDistanceStep = 0.5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
        public int doppelgangerPlacementAttempts = 4;


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
        @ConfigEntry.BoundedDiscrete(min = 2, max = 16)
        public int gustOfWindVisualLayers = 8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 4, max = 48)
        public int gustOfWindParticlesPerLayer = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        public int gustOfWindSweepParticleCount = 1;

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
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 240)
        public int shadowVeilSpeedDurationTicks = 60;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int shadowVeilSpeedAmplifier = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 240)
        public int shadowVeilSlowDurationTicks = 50;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int shadowVeilSlowAmplifier = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 240)
        public int shadowVeilBlindnessDurationTicks = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int shadowVeilBlindnessAmplifier = 0;
    }
}
