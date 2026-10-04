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

        double radius = Double.isFinite(settings.triggerRadius)
                ? Math.max(0.0, settings.triggerRadius) : 0.0;
        List<PlayerEntity> nearby = serverWorld.getEntitiesByClass(
                PlayerEntity.class,
                this.getBoundingBox().expand(radius),
                this::canTrigger
        );

        PlayerEntity victim = nearby.stream()
                .min((a, b) -> Double.compare(
                        this.getPos().squaredDistanceTo(a.getPos()),
                        this.getPos().squaredDistanceTo(b.getPos())))
                .orElse(null);

        if (victim != null) {
            double damage = Double.isFinite(settings.damage) ? Math.max(0.0, settings.damage) : 0.0;
            if (damage > 0.0) {
                victim.damage(serverWorld, serverWorld.getDamageSources().generic(), (float) damage);
            }
            TrapEffects.apply(victim, settings);
            this.discard();
        }
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
