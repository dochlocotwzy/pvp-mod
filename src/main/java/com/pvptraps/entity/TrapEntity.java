package com.pvptraps.entity;

import com.pvptraps.config.ConfigManager;
import com.pvptraps.config.TrapConfig;
import com.pvptraps.util.TrapEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * Stationary PvP trap. The server controls triggering and synchronizes when
 * the initial all-player reveal period has ended.
 */
public class TrapEntity extends MobEntity {

    private static final TrackedData<Boolean> ENEMIES_CAN_SEE =
            DataTracker.registerData(TrapEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private String trapTypeId = "default";
    private String ownerTeamName = null;
    private UUID ownerUuid = null;

    public TrapEntity(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.noClip = true;
        this.setInvulnerable(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ENEMIES_CAN_SEE, true);
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

        // Put the trap on the owner's team immediately, so that team data is
        // available to clients when the entity starts being tracked.
        if (team != null && !owner.getEntityWorld().isClient()) {
            ((ServerWorld) owner.getEntityWorld()).getScoreboard()
                    .addScoreHolderToTeam(this.getNameForScoreboard(), team);
        }
    }

    public boolean areEnemiesAllowedToSee() {
        return this.dataTracker.get(ENEMIES_CAN_SEE);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getEntityWorld().isClient()) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld) this.getEntityWorld();

        TrapConfig.TrapTypeSettings settings = ConfigManager.getTrapType(trapTypeId);

        if (this.age >= settings.enemyVisibilitySeconds * 20
                && this.dataTracker.get(ENEMIES_CAN_SEE)) {
            this.dataTracker.set(ENEMIES_CAN_SEE, false);
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
