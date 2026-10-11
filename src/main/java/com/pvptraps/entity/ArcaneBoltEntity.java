package com.pvptraps.entity;

import com.pvptraps.config.MageConfigManager;
import com.pvptraps.item.ModItems;
import com.pvptraps.util.MageTeamAdapter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import java.util.HashSet;
import java.util.Set;

public final class ArcaneBoltEntity extends SnowballEntity {
    private double originX;
    private double originY;
    private double originZ;
    private double maxRange = 32.0;
    private float damage = 6.0f;
    private int lifetimeTicks = 60;
    private int livedTicks;
    private boolean fireball;
    private boolean electricBolt;
    private int igniteSeconds = 4;

    public ArcaneBoltEntity(EntityType<? extends SnowballEntity> type, World world) {
        super(type, world);
    }

    @Override
    public ItemStack getStack() {
        if (fireball) return ModItems.FIREBALL.getDefaultStack();
        if (electricBolt) return ModItems.ELECTRIC_BOLT.getDefaultStack();
        return ModItems.ARCANE_BOLT.getDefaultStack();
    }

    public void configure(double maxRange, int lifetimeTicks, float damage) {
        this.fireball = false;
        this.electricBolt = false;
        this.maxRange = Math.max(1.0, maxRange);
        this.lifetimeTicks = Math.max(1, lifetimeTicks);
        this.damage = Math.max(0.0f, damage);
        this.originX = getX();
        this.originY = getY();
        this.originZ = getZ();
        calculateDimensions();
    }

    public void configureFireball(double maxRange, int lifetimeTicks, float damage, int igniteSeconds) {
        configure(maxRange, lifetimeTicks, damage);
        this.fireball = true;
        this.igniteSeconds = Math.max(1, Math.min(10, igniteSeconds));
    }

    public void configureElectricBolt(double maxRange, int lifetimeTicks, float damage) {
        configure(maxRange, lifetimeTicks, damage);
        this.electricBolt = true;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        float size = MageConfigManager.getArcaneBoltSize();
        return EntityDimensions.fixed(size, size);
    }

    @Override
    public void tick() {
        super.tick();
        if (!(getEntityWorld() instanceof ServerWorld world) || isRemoved()) {
            return;
        }

        livedTicks++;
        if (fireball) {
            world.spawnParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 5, 0.06, 0.06, 0.06, 0.015);
            world.spawnParticles(ParticleTypes.SMOKE, getX(), getY(), getZ(), 2, 0.035, 0.035, 0.035, 0.005);
        } else if (electricBolt) {
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 5, 0.06, 0.06, 0.06, 0.035);
            world.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 1, 0.02, 0.02, 0.02, 0.005);
        } else {
            world.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 2, 0.015, 0.015, 0.015, 0.005);
            world.spawnParticles(ParticleTypes.ENCHANT, getX(), getY(), getZ(), 2, 0.06, 0.06, 0.06, 0.12);
            if (livedTicks % 3 == 0) {
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(),
                        1, 0.025, 0.025, 0.025, 0.015);
            }
        }
        double dx = getX() - originX;
        double dy = getY() - originY;
        double dz = getZ() - originZ;
        if (livedTicks >= lifetimeTicks || dx * dx + dy * dy + dz * dz >= maxRange * maxRange) {
            world.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 12, 0.18, 0.18, 0.18, 0.04);
            world.spawnParticles(ParticleTypes.FIREWORK, getX(), getY(), getZ(), 8, 0.12, 0.12, 0.12, 0.03);
            discard();
        }
    }


    private void chainLightning(ServerWorld world, LivingEntity firstTarget, Entity owner) {
        var config = MageConfigManager.get().abilities;
        int maxTargets = Math.max(1, Math.min(8, config.electricBoltMaxJumps));
        double jumpRange = Math.max(1.0, Math.min(24.0, config.electricBoltJumpRange));
        Set<java.util.UUID> hitTargets = new HashSet<>();
        hitTargets.add(firstTarget.getUuid());
        LivingEntity previous = firstTarget;
        for (int jump = 1; jump < maxTargets; jump++) {
            final LivingEntity from = previous;
            var area = from.getBoundingBox().expand(jumpRange);
            LivingEntity next = world.getEntitiesByClass(LivingEntity.class, area,
                    candidate -> candidate.isAlive() && !candidate.isSpectator()
                            && candidate != owner && candidate != from
                            && !hitTargets.contains(candidate.getUuid())
                            && from.squaredDistanceTo(candidate) <= jumpRange * jumpRange
                            && !(owner instanceof PlayerEntity caster
                                && candidate instanceof PlayerEntity target
                                && MageTeamAdapter.areAllies(caster, target)))
                    .stream().min(java.util.Comparator.comparingDouble(from::squaredDistanceTo)).orElse(null);
            if (next == null) break;
            hitTargets.add(next.getUuid());
            next.damage(world, world.getDamageSources().magic(), damage);
            next.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.SLOWNESS,
                    Math.max(1, Math.min(200, MageConfigManager.get().abilities.electricBoltSlowDurationTicks)),
                    Math.max(0, Math.min(4, MageConfigManager.get().abilities.electricBoltSlowAmplifier)), false, true));
            spawnChainTrail(world, from, next, config.electricBoltChainParticleCount);
            world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    next.getX(), next.getY() + next.getHeight() * 0.5, next.getZ(),
                    Math.max(1, Math.min(128, config.electricBoltJumpDamageParticleCount)),
                    0.2, 0.35, 0.2, 0.015);
            previous = next;
        }
    }

    private static void spawnChainTrail(ServerWorld world, LivingEntity from, LivingEntity to, int particleCount) {
        int count = Math.max(1, Math.min(128, particleCount));
        double sx = from.getX(), sy = from.getY() + from.getHeight() * 0.5, sz = from.getZ();
        double dx = to.getX() - sx;
        double dy = to.getY() + to.getHeight() * 0.5 - sy;
        double dz = to.getZ() - sz;
        for (int i = 0; i <= count; i++) {
            double t = (double) i / count;
            world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    sx + dx * t, sy + dy * t, sz + dz * t, 1, 0.015, 0.015, 0.015, 0.0);
            if (i % 3 == 0) world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    sx + dx * t, sy + dy * t, sz + dz * t, 1, 0.01, 0.01, 0.01, 0.01);
        }
    }

    private void igniteNearbyOnGroundImpact(ServerWorld world, double x, double y, double z, Entity owner) {
        int radius = Math.max(1, Math.min(8, MageConfigManager.get().abilities.fireballGroundIgniteRadius));
        var area = new net.minecraft.util.math.Box(x - radius, y - 1.0, z - radius,
                x + radius, y + 1.0, z + radius);
        for (LivingEntity target : world.getEntitiesByClass(LivingEntity.class, area,
                candidate -> candidate.isAlive() && candidate != owner && !candidate.isSpectator())) {
            if (owner instanceof PlayerEntity caster && target instanceof PlayerEntity playerTarget
                    && MageTeamAdapter.areAllies(caster, playerTarget)) continue;
            double dx = target.getX() - x, dz = target.getZ() - z;
            if (dx * dx + dz * dz <= radius * radius) {
                target.setOnFireFor(igniteSeconds);
                world.spawnParticles(ParticleTypes.FLAME, target.getX(), target.getY() + 0.6,
                        target.getZ(), 8, 0.18, 0.25, 0.18, 0.025);
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!(getEntityWorld() instanceof ServerWorld world)) {
            return;
        }

        if (hitResult instanceof EntityHitResult entityHitResult) {
            Entity hit = entityHitResult.getEntity();
            Entity owner = getOwner();
            if (hit instanceof LivingEntity living && living.isAlive() && hit != owner) {
                boolean friendly = owner instanceof PlayerEntity caster
                        && hit instanceof PlayerEntity target
                        && MageTeamAdapter.areAllies(caster, target);
                if (!friendly) {
                    living.damage(world, world.getDamageSources().magic(), damage);
                    if (fireball) {
                        living.setOnFireFor(igniteSeconds);
                    } else if (electricBolt) {
                        var config = MageConfigManager.get().abilities;
                        living.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                                net.minecraft.entity.effect.StatusEffects.SLOWNESS,
                                Math.max(1, Math.min(200, config.electricBoltSlowDurationTicks)),
                                Math.max(0, Math.min(4, config.electricBoltSlowAmplifier)), false, true));
                        chainLightning(world, living, owner);
                    }
                }
            }
        }

        if (fireball) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                igniteNearbyOnGroundImpact(world, getX(), getY(), getZ(), getOwner());
            }
            world.spawnParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 24, 0.28, 0.28, 0.28, 0.05);
            world.spawnParticles(ParticleTypes.LAVA, getX(), getY(), getZ(), 5, 0.18, 0.18, 0.18, 0.02);
            world.spawnParticles(ParticleTypes.SMOKE, getX(), getY(), getZ(), 14, 0.22, 0.22, 0.22, 0.02);
            world.playSound(null, getX(), getY(), getZ(),
                    net.minecraft.sound.SoundEvents.ENTITY_GENERIC_EXPLODE.value(),
                    net.minecraft.sound.SoundCategory.PLAYERS, 0.45f, 1.25f);
        } else if (electricBolt) {
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 28, 0.22, 0.22, 0.22, 0.06);
            world.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 12, 0.18, 0.18, 0.18, 0.04);
            world.playSound(null, getX(), getY(), getZ(),
                    net.minecraft.sound.SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                    net.minecraft.sound.SoundCategory.PLAYERS, 0.55f, 1.9f);
        } else {
            world.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 14, 0.18, 0.18, 0.18, 0.04);
            world.spawnParticles(ParticleTypes.ENCHANT, getX(), getY(), getZ(), 18, 0.22, 0.22, 0.22, 0.4);
            world.playSound(null, getX(), getY(), getZ(),
                    net.minecraft.sound.SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,
                    net.minecraft.sound.SoundCategory.PLAYERS, 0.45f, 1.7f);
        }
        discard();
    }
}
