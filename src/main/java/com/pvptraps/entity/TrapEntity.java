package com.pvptraps.entity;

import com.pvptraps.config.ConfigManager;
import com.pvptraps.config.TrapConfig;
import com.pvptraps.util.TrapEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class TrapEntity extends MobEntity {

    private String trapTypeId = "default";
    private String ownerTeamName = null;
    private UUID ownerUuid = null;
    private boolean visibilityApplied = false;

    public TrapEntity(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.noClip = true;
        this.setInvulnerable(true);
    }

    public static DefaultAttributeContainer.Builder createTrapAttributes() {
        return MobEntity.createMobAttributes();
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new MobNavigation(this, world);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(net.minecraft.entity.Entity entity) {
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    public void configure(String trapTypeId, PlayerEntity owner) {
        this.trapTypeId = trapTypeId == null ? "default" : trapTypeId;
        this.ownerUuid = owner.getUuid();
        Team team = owner.getScoreboardTeam();
        this.ownerTeamName = team != null ? team.getName() : null;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getEntityWorld().isClient()) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld) this.getEntityWorld();
        TrapConfig.TrapTypeSettings settings = ConfigManager.getTrapType(trapTypeId);

        int lifetime = Math.max(1, settings.trapLifetimeSeconds);
        if (this.age >= lifetime * 20L) {
            this.discard();
            return;
        }

        int visibilitySeconds = Math.max(0, settings.enemyVisibilitySeconds);
        if (!visibilityApplied && this.age >= visibilitySeconds * 20L) {
            applyTeamVisibility(serverWorld);
        }

        boolean stepMode = "step".equalsIgnoreCase(settings.activationMode);
        double radius = stepMode ? 0.1 : (Double.isFinite(settings.triggerRadius)
                ? Math.max(0.0, settings.triggerRadius) : 0.0);
        List<PlayerEntity> nearby = serverWorld.getEntitiesByClass(
                PlayerEntity.class,
                this.getBoundingBox().expand(radius),
                player -> canTrigger(player) && (!stepMode || isStandingOnTrap(player))
        );

        PlayerEntity victim = nearby.stream()
                .min((a, b) -> Double.compare(
                        this.getPos().squaredDistanceTo(a.getPos()),
                        this.getPos().squaredDistanceTo(b.getPos())))
                .orElse(null);

        if (victim != null) {
            spawnActivationParticles(serverWorld, victim);
            double damage = Double.isFinite(settings.damage) ? Math.max(0.0, settings.damage) : 0.0;
            if (damage > 0.0) {
                victim.damage(serverWorld, serverWorld.getDamageSources().generic(), (float) damage);
            }
            TrapEffects.apply(victim, settings);
            this.discard();
        }
    }

    private void spawnActivationParticles(ServerWorld world, PlayerEntity victim) {
        ParticleEffect particle = switch (trapTypeId) {
            case "ice" -> ParticleTypes.SNOWFLAKE;
            case "poison" -> ParticleTypes.WITCH;
            case "electric" -> ParticleTypes.ELECTRIC_SPARK;
            case "smoke" -> ParticleTypes.SMOKE;
            case "weakening" -> ParticleTypes.DAMAGE_INDICATOR;
            case "sticky" -> ParticleTypes.CLOUD;
            case "fire" -> ParticleTypes.FLAME;
            case "exhaustion" -> ParticleTypes.POOF;
            default -> ParticleTypes.CRIT;
        };
        world.spawnParticles(particle,
                victim.getX(), victim.getY() + 0.8, victim.getZ(),
                trapTypeId.equals("electric") ? 18 : 10,
                0.35, 0.45, 0.35, 0.04);
    }

    private void applyTeamVisibility(ServerWorld world) {
        visibilityApplied = true;
        this.setInvisible(true);
        if (ownerTeamName != null) {
            Team team = world.getScoreboard().getTeam(ownerTeamName);
            if (team != null) {
                world.getScoreboard().addScoreHolderToTeam(this.getNameForScoreboard(), team);
            }
        }
    }

    /**
     * Step activation is intentionally limited to players directly over the trap's footprint,
     * with their feet no more than 1.1 blocks above the trap surface.
     */
    private boolean isStandingOnTrap(PlayerEntity player) {
        double dx = Math.abs(player.getX() - this.getX());
        double dz = Math.abs(player.getZ() - this.getZ());
        double feetY = player.getBoundingBox().minY;
        return dx <= 0.55 && dz <= 0.55
                && feetY >= this.getY() - 0.05
                && feetY <= this.getY() + 1.1;
    }

    private boolean canTrigger(PlayerEntity player) {
        if (player.isSpectator()) {
            return false;
        }
        if (ownerUuid != null && player.getUuid().equals(ownerUuid)) {
            return false;
        }
        TrapConfig.TrapTypeSettings settings = ConfigManager.getTrapType(trapTypeId);
        if (settings.ignoreWholeOwnerTeam && ownerTeamName != null) {
            Team playerTeam = player.getScoreboardTeam();
            if (playerTeam != null && playerTeam.getName().equals(ownerTeamName)) {
                return false;
            }
        }
        return true;
    }
}
