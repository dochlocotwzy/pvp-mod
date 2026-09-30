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

/**
 * Ловушка: не имеет коллизии (noClip), не двигается, не реагирует на урон,
 * каждый тик вручную проверяет, не стоит ли кто-то в её хитбоксе.
 *
 * Настройки ловушки (trapTypeId/владелец/команда) не сохраняются в NBT между
 * рестартами сервера - ловушка живёт считанные секунды до срабатывания или
 * до перезаписи visibilityApplied, так что при обычном рестарте сервера этот
 * недолговечный объект просто исчезнет вместе с чанком, что приемлемо.
 */
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
        this.trapTypeId = trapTypeId;
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

        if (!visibilityApplied && this.age >= settings.enemyVisibilitySeconds * 20) {
            applyTeamVisibility(serverWorld);
        }

        List<PlayerEntity> nearby = serverWorld.getEntitiesByClass(
                PlayerEntity.class,
                this.getBoundingBox(),
                this::canTrigger
        );

        if (!nearby.isEmpty()) {
            PlayerEntity victim = nearby.get(0);
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
