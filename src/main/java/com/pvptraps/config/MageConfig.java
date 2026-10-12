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
        @ConfigEntry.Gui.Tooltip
        public String hudVisibleTags = "";
    }

    public static class AbilitySettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("general")
        public int targetRangeBlocks = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("magic_barrier")
        public int barrierManaCost = 25;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("magic_barrier")
        public int barrierCooldownTicks = 200;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 600)
        @ConfigEntry.Category("magic_barrier")
        public int barrierDurationTicks = 80;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("magic_barrier")
        public int barrierResistanceAmplifier = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("flash_of_light")
        public int flashManaCost = 20;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("flash_of_light")
        public int flashCooldownTicks = 120;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 400)
        @ConfigEntry.Category("flash_of_light")
        public int flashDurationTicks = 60;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("flash_of_light")
        public int flashResistanceAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("arcane_bolt")
        public int arcaneBoltManaCost = 15;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("arcane_bolt")
        public int arcaneBoltCooldownTicks = 40;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("arcane_bolt")
        public double arcaneBoltSpeed = 1.7;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("arcane_bolt")
        public double arcaneBoltRange = 32.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 5, max = 200)
        @ConfigEntry.Category("arcane_bolt")
        public int arcaneBoltLifetimeTicks = 60;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("arcane_bolt")
        public float arcaneBoltDamage = 6.0f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("arcane_bolt")
        public float arcaneBoltSize = 0.4f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("energy_impulse")
        public int energyImpulseManaCost = 25;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("energy_impulse")
        public int energyImpulseCooldownTicks = 160;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("energy_impulse")
        public double energyImpulseRadius = 7.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("energy_impulse")
        public double energyImpulseKnockback = 8.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 8, max = 128)
        @ConfigEntry.Category("energy_impulse")
        public int energyImpulseShockwaveSegments = 48;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        @ConfigEntry.Category("energy_impulse")
        public int energyImpulseTargetParticleCount = 8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("energy_impulse")
        public int energyImpulseCenterParticleCount = 36;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaManaCost = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaCooldownTicks = 240;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("frost_nova")
        public double frostNovaRadius = 5.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 200)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaDurationTicks = 80;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaSlownessAmplifier = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaWeaknessAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 8, max = 128)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaRingSegments = 64;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 256)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaCenterParticleCount = 80;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaTargetParticleCount = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("healing_rune")
        public int healingRuneManaCost = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("healing_rune")
        public int healingRuneCooldownTicks = 300;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("healing_rune")
        public double healingRuneRadius = 6.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("healing_rune")
        public float healingRuneHealAmount = 6.0f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("smoke_veil")
        public int smokeVeilManaCost = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("smoke_veil")
        public int smokeVeilCooldownTicks = 360;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("smoke_veil")
        public double smokeVeilRadius = 10.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 1200)
        @ConfigEntry.Category("smoke_veil")
        public int smokeVeilDurationTicks = 240;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("smoke_veil")
        public double smokeVeilRevealRadius = 6.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("gale_step")
        public int galeStepManaCost = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("gale_step")
        public int galeStepCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("gale_step")
        public double galeStepDistance = 4.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("gale_step")
        public double galeStepCollisionSampleStep = 0.05;


        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("runic_seal")
        public int runicSealManaCost = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("runic_seal")
        public int runicSealCooldownTicks = 180;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("runic_seal")
        public double runicSealRange = 12.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("runic_seal")
        public double runicSealRadius = 2.25;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 200)
        @ConfigEntry.Category("runic_seal")
        public int runicSealDurationTicks = 60;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("runic_seal")
        public int runicSealSlownessAmplifier = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("runic_seal")
        public int runicSealWeaknessAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("runic_seal")
        public int runicSealGlowDurationTicks = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("blessing")
        public int blessingManaCost = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("blessing")
        public int blessingCooldownTicks = 240;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 600)
        @ConfigEntry.Category("blessing")
        public int blessingDurationTicks = 160;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("blessing")
        public int blessingSpeedAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("blessing")
        public int blessingRegenerationAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereManaCost = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereCooldownTicks = 320;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("gravity_sphere")
        public double gravitySphereRange = 24.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("gravity_sphere")
        public double gravitySphereRadius = 8.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 1200)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereDurationTicks = 300;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("gravity_sphere")
        public double gravitySpherePullStrength = 0.24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySpherePullIntervalTicks = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 40)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereEffectIntervalTicks = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 40)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereSlownessDurationTicks = 6;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereSlownessAmplifier = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("fireball")
        public int fireballManaCost = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("fireball")
        public int fireballCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("fireball")
        public double fireballSpeed = 1.45;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("fireball")
        public double fireballRange = 30.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 5, max = 200)
        @ConfigEntry.Category("fireball")
        public int fireballLifetimeTicks = 55;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("fireball")
        public float fireballDamage = 7.0f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
        @ConfigEntry.Category("fireball")
        public int fireballIgniteSeconds = 4;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltManaCost = 20;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("electric_bolt")
        public double electricBoltSpeed = 1.9;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("electric_bolt")
        public double electricBoltRange = 28.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 5, max = 200)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltLifetimeTicks = 50;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("electric_bolt")
        public float electricBoltDamage = 6.0f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltMaxJumps = 3;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("electric_bolt")
        public double electricBoltJumpRange = 7.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltChainParticleCount = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltChainSparkEveryNthPoint = 3;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltChainSparkParticleCount = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereParticleMultiplier = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 32)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereBaseParticleCount = 6;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
        @ConfigEntry.Category("fireball")
        public int fireballGroundIgniteRadius = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereParticlesPerPulse = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltJumpDamageParticleCount = 20;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltSlowDurationTicks = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltSlowAmplifier = 0;

        // Particle counts for barriers, flash and projectile visuals are configurable independently.
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("magic_barrier")
        public int barrierBeamSegments = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("magic_barrier")
        public int barrierRingSegments = 32;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("magic_barrier")
        public int barrierCenterParticleCount = 32;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("flash_of_light")
        public int flashGlowParticleCount = 36;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("flash_of_light")
        public int flashFireworkParticleCount = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("arcane_bolt")
        public int arcaneBoltLaunchParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("fireball")
        public int fireballLaunchParticleCount = 10;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltLaunchParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("arcane_bolt")
        public int arcaneBoltTrailParticleCount = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("arcane_bolt")
        public int arcaneBoltSparkParticleCount = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("fireball")
        public int fireballTrailFlameParticleCount = 5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("fireball")
        public int fireballTrailSmokeParticleCount = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltTrailSparkParticleCount = 5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltTrailEndRodParticleCount = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int projectileExpireEndRodParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int projectileExpireFireworkParticleCount = 8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("fireball")
        public int fireballImpactFlameParticleCount = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("fireball")
        public int fireballImpactLavaParticleCount = 5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("fireball")
        public int fireballImpactSmokeParticleCount = 14;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltImpactSparkParticleCount = 28;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("electric_bolt")
        public int electricBoltImpactEndRodParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("arcane_bolt")
        public int arcaneBoltImpactEndRodParticleCount = 14;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("arcane_bolt")
        public int arcaneBoltImpactEnchantParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("fireball")
        public int fireballTargetFlameParticleCount = 8;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("gale_step")
        public int galeStepDepartureCloudParticleCount = 14;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("gale_step")
        public int galeStepMidpointSparkParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("gale_step")
        public int galeStepArrivalCloudParticleCount = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("runic_seal")
        public int runicSealTargetParticleCount = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 8, max = 128)
        @ConfigEntry.Category("runic_seal")
        public int runicSealRingSegments = 40;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("runic_seal")
        public int runicSealCenterParticleCount = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("blessing")
        public int blessingVillagerParticleCount = 22;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("blessing")
        public int blessingEndRodParticleCount = 14;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 256)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereInitialPortalParticleCount = 48;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 256)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereInitialEndRodParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("gravity_sphere")
        public int gravitySphereExpireParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("healing_rune")
        public int healingRuneTargetHeartParticleCount = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("healing_rune")
        public int healingRuneTargetVillagerParticleCount = 10;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 8, max = 128)
        @ConfigEntry.Category("healing_rune")
        public int healingRuneRingSegments = 40;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("healing_rune")
        public int healingRuneCenterParticleCount = 24;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaTargetCloudParticleCount = 8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaRingSnowflakeParticleCount = 2;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        @ConfigEntry.Category("frost_nova")
        public int frostNovaRingEndRodParticleCount = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        @ConfigEntry.Category("ice_wall")
        public int iceWallPlacementParticleCount = 4;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        @ConfigEntry.Category("ice_wall")
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
        @ConfigEntry.Category("gust_of_wind")
        public int gustOfWindTargetCloudParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("gust_of_wind")
        public int gustOfWindTargetPoofParticleCount = 6;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilTargetSmokeParticleCount = 12;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("smoke_veil")
        public int smokeVeilCastSmokeParticleCount = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("smoke_veil")
        public int smokeVeilCastLargeSmokeParticleCount = 6;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        @ConfigEntry.Category("smoke_veil")
        public int smokeVeilRevealSmokeParticleCount = 14;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("ice_wall")
        public int iceWallManaCost = 32;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("ice_wall")
        public int iceWallCooldownTicks = 360;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("ice_wall")
        public double iceWallDistance = 2.5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 3, max = 9)
        @ConfigEntry.Category("ice_wall")
        public int iceWallWidth = 5;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 2, max = 5)
        @ConfigEntry.Category("ice_wall")
        public int iceWallHeight = 3;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 40, max = 240)
        @ConfigEntry.Category("ice_wall")
        public int iceWallDurationTicks = 100;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("magic_doppelganger")
        public int magicDoppelgangerManaCost = 28;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("magic_doppelganger")
        public int magicDoppelgangerCooldownTicks = 280;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 40, max = 400)
        @ConfigEntry.Category("magic_doppelganger")
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
        @ConfigEntry.Category("gust_of_wind")
        public int gustOfWindManaCost = 18;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("gust_of_wind")
        public int gustOfWindCooldownTicks = 100;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("gust_of_wind")
        public double gustOfWindRange = 9.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("gust_of_wind")
        public double gustOfWindStrength = 1.25;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 120)
        @ConfigEntry.Category("gust_of_wind")
        public int gustOfWindConeDegrees = 70;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 2, max = 16)
        @ConfigEntry.Category("gust_of_wind")
        public int gustOfWindVisualLayers = 8;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 4, max = 48)
        @ConfigEntry.Category("gust_of_wind")
        public int gustOfWindParticlesPerLayer = 16;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        @ConfigEntry.Category("gust_of_wind")
        public int gustOfWindSweepParticleCount = 1;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilManaCost = 26;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilCooldownTicks = 260;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 240)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilDurationTicks = 80;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Category("shadow_veil")
        public double shadowVeilRadius = 4.0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 240)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilSpeedDurationTicks = 60;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilSpeedAmplifier = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 240)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilSlowDurationTicks = 50;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilSlowAmplifier = 1;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 240)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilBlindnessDurationTicks = 30;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        @ConfigEntry.Category("shadow_veil")
        public int shadowVeilBlindnessAmplifier = 0;
    }
}
