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

public final class ArcaneBoltEntity extends SnowballEntity {
    private double originX;
    private double originY;
    private double originZ;
    private double maxRange = 32.0;
    private float damage = 6.0f;
    private int lifetimeTicks = 60;
    private int livedTicks;

    public ArcaneBoltEntity(EntityType<? extends SnowballEntity> type, World world) {
        super(type, world);
    }

    @Override
    public ItemStack getStack() {
        return ModItems.ARCANE_BOLT.getDefaultStack();
    }

    public void configure(double maxRange, int lifetimeTicks, float damage) {
        this.maxRange = Math.max(1.0, maxRange);
        this.lifetimeTicks = Math.max(1, lifetimeTicks);
        this.damage = Math.max(0.0f, damage);
        this.originX = getX();
        this.originY = getY();
        this.originZ = getZ();
        calculateDimensions();
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
        world.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 1, 0.0, 0.0, 0.0, 0.0);
        double dx = getX() - originX;
        double dy = getY() - originY;
        double dz = getZ() - originZ;
        if (livedTicks >= lifetimeTicks || dx * dx + dy * dy + dz * dz >= maxRange * maxRange) {
            world.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 8, 0.12, 0.12, 0.12, 0.02);
            discard();
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
                }
            }
        }

        world.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 10, 0.18, 0.18, 0.18, 0.03);
        discard();
    }
}
