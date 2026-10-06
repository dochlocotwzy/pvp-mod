package com.pvptraps.entity;

import com.pvptraps.config.ConfigManager;
import com.pvptraps.config.TrapConfig;
import com.pvptraps.util.TrapEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class TrapEntity extends MobEntity {

    private static final TrackedData<String> OWNER_UUID = DataTracker.registerData(
            TrapEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> VISIBILITY_APPLIED = DataTracker.registerData(
            TrapEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private String trapTypeId = "default";
    private String ownerTeamName = null;
    private UUID ownerUuid = null;
    private boolean visibilityApplied = false;
    private boolean testTrap = false;
    private LivingEntity testVictim = null;

    public TrapEntity(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.noClip = true;
        this.setInvulnerable(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(OWNER_UUID, "");
        builder.add(VISIBILITY_APPLIED, false);
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
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        // Traps are gameplay objects, not combat entities. Even creative-mode
        // attacks must never destroy them; lifetime/activation are their only
        // normal removal paths.
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
        configure(trapTypeId);
        if (owner != null) {
            this.ownerUuid = owner.getUuid();
            this.dataTracker.set(OWNER_UUID, owner.getUuidAsString());
            Team team = owner.getScoreboardTeam();
            this.ownerTeamName = team != null ? team.getName() : null;
        }
    }

    /** Configures a command-spawned trap without an owning player or team. */
    public void configure(String trapTypeId) {
        this.trapTypeId = trapTypeId == null ? "default" : trapTypeId;
        this.ownerUuid = null;
        this.ownerTeamName = null;
        this.dataTracker.set(OWNER_UUID, "");
        this.dataTracker.set(VISIBILITY_APPLIED, false);
        this.testTrap = false;
        this.testVictim = null;
    }

    /** Marks a command-spawned trap so CI can verify that the gameplay trigger actually fired. */
    public void configureTest(String trapTypeId) {
        configure(trapTypeId);
        this.testTrap = true;
    }

    /** Binds the CI smoke test to its deterministic victim without affecting normal gameplay target selection. */
    public void setTestVictim(LivingEntity victim) {
        this.testVictim = victim;
    }

    public String getTrapTypeId() {
        return trapTypeId;
    }

    /** Executes the bound CI smoke-test victim immediately; normal gameplay never calls this. */
    public void triggerTestVictim(ServerWorld serverWorld) {
        if (!testTrap || testVictim == null || !canTrigger(testVictim, ConfigManager.getTrapType(trapTypeId))) {
            return;
        }
        TrapConfig.TrapTypeSettings settings = ConfigManager.getTrapType(trapTypeId);
        spawnActivationParticles(serverWorld, testVictim);
        double damage = Double.isFinite(settings.damage) ? Math.max(0.0, settings.damage) : 0.0;
        if (damage > 0.0) {
            testVictim.damage(serverWorld, serverWorld.getDamageSources().generic(), (float) damage);
        }
        TrapEffects.apply(testVictim, settings);
        com.pvptraps.PvpTraps.LOGGER.info("Trap smoke test triggered: type={}, target={}",
                trapTypeId, testVictim.getType().getTranslationKey());
        this.discard();
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
        // Use a broad spatial query for step traps; the exact footprint is enforced by
        // isStandingOnTrap below. A tiny expanded entity box can miss a mob whose body
        // overlaps the trap footprint but whose entity box does not overlap the trap's
        // thin vertical bounds enough for the world spatial index to return it.
        double radius = stepMode ? 1.0 : (Double.isFinite(settings.triggerRadius)
                ? Math.max(0.0, settings.triggerRadius) : 0.0);
        List<LivingEntity> nearby = serverWorld.getEntitiesByClass(
                LivingEntity.class,
                this.getBoundingBox().expand(radius),
                target -> canTrigger(target, settings) && (!stepMode || isStandingOnTrap(target))
        );

        LivingEntity victim = nearby.stream()
                .min((a, b) -> Double.compare(
                        squaredDistanceTo(a), squaredDistanceTo(b)))
                .orElse(null);
        // CI smoke tests bind the trap to the exact entity they spawned. This keeps the
        // test deterministic and does not change normal gameplay target selection.
        if (testTrap && testVictim != null && canTrigger(testVictim, settings)) {
            victim = testVictim;
        }

        if (victim != null) {
            spawnActivationParticles(serverWorld, victim);
            double damage = Double.isFinite(settings.damage) ? Math.max(0.0, settings.damage) : 0.0;
            if (damage > 0.0) {
                victim.damage(serverWorld, serverWorld.getDamageSources().generic(), (float) damage);
            }
            TrapEffects.apply(victim, settings);
            if (testTrap) {
                com.pvptraps.PvpTraps.LOGGER.info("Trap smoke test triggered: type={}, target={}", trapTypeId, victim.getType().getTranslationKey());
            }
            this.discard();
        }
    }

    private double squaredDistanceTo(LivingEntity player) {
        double dx = this.getX() - player.getX();
        double dy = this.getY() - player.getY();
        double dz = this.getZ() - player.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    private void spawnActivationParticles(ServerWorld world, LivingEntity victim) {
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
        this.dataTracker.set(VISIBILITY_APPLIED, true);
        if (ownerTeamName != null) {
            Team team = world.getScoreboard().getTeam(ownerTeamName);
            if (team != null) {
                world.getScoreboard().addScoreHolderToTeam(this.getNameForScoreboard(), team);
            }
        }
    }

    /**
     * Step activation uses the trap's full one-block footprint instead of requiring the
     * target's center to be close to the trap center. Any horizontal overlap with the
     * block footprint counts as stepping on the trap.
     */
    private boolean isStandingOnTrap(LivingEntity target) {
        var targetBox = target.getBoundingBox();
        double trapMinX = this.getX() - 0.5;
        double trapMaxX = this.getX() + 0.5;
        double trapMinZ = this.getZ() - 0.5;
        double trapMaxZ = this.getZ() + 0.5;
        double feetY = targetBox.minY;

        boolean overlapsFootprint = targetBox.maxX > trapMinX
                && targetBox.minX < trapMaxX
                && targetBox.maxZ > trapMinZ
                && targetBox.minZ < trapMaxZ;
        return overlapsFootprint
                && feetY >= this.getY() - 0.05
                && feetY <= this.getY() + 1.1;
    }

    @Override
    public boolean isInvisibleTo(PlayerEntity player) {
        if (!visibilityApplied && !this.dataTracker.get(VISIBILITY_APPLIED)) {
            return false;
        }
        if (player.isSpectator()) {
            return false;
        }

        String trackedOwnerUuid = this.dataTracker.get(OWNER_UUID);
        if (!trackedOwnerUuid.isEmpty() && player.getUuidAsString().equals(trackedOwnerUuid)) {
            return false;
        }

        Team trapTeam = this.getScoreboardTeam();
        Team playerTeam = player.getScoreboardTeam();
        if (trapTeam != null && playerTeam != null && trapTeam.getName().equals(playerTeam.getName())) {
            return false;
        }
        return true;
    }

    private boolean canTrigger(LivingEntity target, TrapConfig.TrapTypeSettings settings) {
        if (!target.isAlive() || target == this) {
            return false;
        }
        if (target instanceof PlayerEntity player) {
            if (!settings.affectPlayers || player.isSpectator()) {
                return false;
            }
            if (ownerUuid != null && player.getUuid().equals(ownerUuid)) {
                return false;
            }
            if (settings.ignoreWholeOwnerTeam && ownerTeamName != null) {
                Team playerTeam = player.getScoreboardTeam();
                if (playerTeam != null && playerTeam.getName().equals(ownerTeamName)) {
                    return false;
                }
            }
            return true;
        }
        return settings.affectMobs;
    }
}
