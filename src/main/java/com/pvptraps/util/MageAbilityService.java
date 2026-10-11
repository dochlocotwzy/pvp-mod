package com.pvptraps.util;

import com.pvptraps.config.MageConfig;
import com.pvptraps.config.MageConfigManager;
import com.pvptraps.entity.ArcaneBoltEntity;
import com.pvptraps.entity.ModEntities;
import com.pvptraps.item.MageAbilityItem;
import com.pvptraps.network.MageCastPayload;
import com.pvptraps.network.MageFeedbackPayload;
import com.pvptraps.network.MageManaPayload;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MageAbilityService {
    private static final Map<UUID, ManaState> PLAYERS = new HashMap<>();
    private static final Map<UUID, SmokeVeilState> SMOKE_VEIL_TARGETS = new HashMap<>();
    private static final Map<UUID, GravitySphereState> GRAVITY_SPHERES = new HashMap<>();
    private static long serverTick;

    private MageAbilityService() {
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(MageCastPayload.ID, MageCastPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MageFeedbackPayload.ID, MageFeedbackPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MageManaPayload.ID, MageManaPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(MageCastPayload.ID,
                (payload, context) -> handleCast(context.player(), payload));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            serverTick++;
            MageConfig config = MageConfigManager.get();
            double maxMana = Math.max(1, config.mana.maxMana);
            double regenPerTick = Math.max(0.0, config.mana.regenPerSecond) / 20.0;
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ManaState state = stateFor(player);
                state.mana = Math.min(maxMana, state.mana + regenPerTick);
                if (serverTick % 5 == 0) {
                    ServerPlayNetworking.send(player, new MageManaPayload((int) Math.round(state.mana), (int) maxMana));
                }
            }
            tickSmokeVeil(server);
            tickGravitySpheres(server);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            PLAYERS.remove(handler.player.getUuid());
            SMOKE_VEIL_TARGETS.remove(handler.player.getUuid());
            GRAVITY_SPHERES.remove(handler.player.getUuid());
            SMOKE_VEIL_TARGETS.entrySet().removeIf(entry -> entry.getValue().ownerUuid.equals(handler.player.getUuid()));
        });

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            if (!alive && !MageConfigManager.get().mana.persistThroughDeath) {
                PLAYERS.put(newPlayer.getUuid(), new ManaState(MageConfigManager.get().mana.maxMana));
            }
        });
    }

    public static double getMana(ServerPlayerEntity player) {
        return stateFor(player).mana;
    }

    public static void setMana(ServerPlayerEntity player, double mana) {
        stateFor(player).mana = Math.max(0.0, Math.min(MageConfigManager.get().mana.maxMana, mana));
    }

    public static float getConfiguredBoltSize() {
        return MageConfigManager.getArcaneBoltSize();
    }

    private static ManaState stateFor(ServerPlayerEntity player) {
        return PLAYERS.computeIfAbsent(player.getUuid(),
                ignored -> new ManaState(Math.max(1, MageConfigManager.get().mana.maxMana)));
    }

    private static void handleCast(ServerPlayerEntity player, MageCastPayload payload) {
        String id = payload.abilityId();
        if (!isKnownAbility(id) || !player.isAlive() || player.isSpectator() || !isHoldingAbility(player, id)) {
            reject(player);
            return;
        }

        MageConfig config = MageConfigManager.get();
        ManaState state = stateFor(player);
        if (serverTick < state.cooldowns.getOrDefault(id, 0L)) {
            reject(player);
            return;
        }

        PlayerEntity ally = null;
        if ("magic_barrier".equals(id) || "blessing".equals(id)) {
            Entity candidate = ((ServerWorld) player.getEntityWorld()).getEntityById(payload.targetEntityId());
            if (!(candidate instanceof PlayerEntity target)
                    || !target.isAlive()
                    || target.isSpectator()
                    || target == player
                    || target.getEntityWorld() != player.getEntityWorld()
                    || player.squaredDistanceTo(target) > (double) config.abilities.targetRangeBlocks * config.abilities.targetRangeBlocks
                    || !MageTeamAdapter.areAllies(player, target)) {
                reject(player);
                return;
            }
            ally = target;
        }

        int cost = manaCost(id, config);
        if (state.mana < cost) {
            reject(player);
            return;
        }

        boolean applied = switch (id) {
            case "magic_barrier" -> applyBarrier(player, ally, config);
            case "flash_of_light" -> applyFlash(player, config);
            case "arcane_bolt" -> launchArcaneBolt(player, config);
            case "energy_impulse" -> applyEnergyImpulse(player, config);
            case "frost_nova" -> applyFrostNova(player, config);
            case "healing_rune" -> applyHealingRune(player, config);
            case "smoke_veil" -> applySmokeVeil(player, config);
            case "gale_step" -> applyGaleStep(player, config);
            case "runic_seal" -> applyRunicSeal(player, config);
            case "blessing" -> applyBlessing(player, ally, config);
            case "gravity_sphere" -> applyGravitySphere(player, config);
            case "fireball" -> launchFireball(player, config);
            case "electric_bolt" -> launchElectricBolt(player, config);
            default -> false;
        };
        if (!applied) {
            reject(player);
            return;
        }

        state.mana -= cost;
        state.cooldowns.put(id, serverTick + cooldownTicks(id, config));
    }

    private static boolean isHoldingAbility(ServerPlayerEntity player, String abilityId) {
        return matches(player.getMainHandStack().getItem(), abilityId)
                || matches(player.getOffHandStack().getItem(), abilityId);
    }

    private static boolean matches(net.minecraft.item.Item item, String abilityId) {
        return item instanceof MageAbilityItem mageItem && mageItem.getAbilityId().equals(abilityId);
    }

    private static boolean isKnownAbility(String id) {
        return "magic_barrier".equals(id) || "flash_of_light".equals(id)
                || "arcane_bolt".equals(id) || "energy_impulse".equals(id)
                || "frost_nova".equals(id) || "healing_rune".equals(id)
                || "smoke_veil".equals(id) || "gale_step".equals(id)
                || "runic_seal".equals(id) || "blessing".equals(id)
                || "gravity_sphere".equals(id) || "fireball".equals(id)
                || "electric_bolt".equals(id);
    }

    private static int manaCost(String id, MageConfig config) {
        return switch (id) {
            case "magic_barrier" -> config.abilities.barrierManaCost;
            case "flash_of_light" -> config.abilities.flashManaCost;
            case "arcane_bolt" -> config.abilities.arcaneBoltManaCost;
            case "energy_impulse" -> config.abilities.energyImpulseManaCost;
            case "frost_nova" -> config.abilities.frostNovaManaCost;
            case "healing_rune" -> config.abilities.healingRuneManaCost;
            case "smoke_veil" -> config.abilities.smokeVeilManaCost;
            case "gale_step" -> config.abilities.galeStepManaCost;
            case "runic_seal" -> config.abilities.runicSealManaCost;
            case "blessing" -> config.abilities.blessingManaCost;
            case "gravity_sphere" -> config.abilities.gravitySphereManaCost;
            case "fireball" -> config.abilities.fireballManaCost;
            case "electric_bolt" -> config.abilities.electricBoltManaCost;
            default -> Integer.MAX_VALUE;
        };
    }

    private static int cooldownTicks(String id, MageConfig config) {
        return switch (id) {
            case "magic_barrier" -> config.abilities.barrierCooldownTicks;
            case "flash_of_light" -> config.abilities.flashCooldownTicks;
            case "arcane_bolt" -> config.abilities.arcaneBoltCooldownTicks;
            case "energy_impulse" -> config.abilities.energyImpulseCooldownTicks;
            case "frost_nova" -> config.abilities.frostNovaCooldownTicks;
            case "healing_rune" -> config.abilities.healingRuneCooldownTicks;
            case "smoke_veil" -> config.abilities.smokeVeilCooldownTicks;
            case "gale_step" -> config.abilities.galeStepCooldownTicks;
            case "runic_seal" -> config.abilities.runicSealCooldownTicks;
            case "blessing" -> config.abilities.blessingCooldownTicks;
            case "gravity_sphere" -> config.abilities.gravitySphereCooldownTicks;
            case "fireball" -> config.abilities.fireballCooldownTicks;
            case "electric_bolt" -> config.abilities.electricBoltCooldownTicks;
            default -> 1;
        };
    }

    private static boolean applyBarrier(ServerPlayerEntity caster, PlayerEntity target, MageConfig config) {
        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE, config.abilities.barrierDurationTicks, 1, false, true));
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double sx = caster.getX(), sy = caster.getEyeY(), sz = caster.getZ();
        double tx = target.getX(), ty = target.getEyeY(), tz = target.getZ();
        for (int i = 0; i <= 16; i++) {
            double t = i / 16.0;
            world.spawnParticles(ParticleTypes.END_ROD,
                    sx + (tx - sx) * t, sy + (ty - sy) * t, sz + (tz - sz) * t,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
        // Visible rotating-looking shield ring around the protected ally.
        for (int i = 0; i < 32; i++) {
            double angle = Math.PI * 2.0 * i / 32.0;
            double px = tx + Math.cos(angle) * 0.72;
            double pz = tz + Math.sin(angle) * 0.72;
            world.spawnParticles(ParticleTypes.END_ROD, px, ty - 0.45, pz, 1, 0, 0.015, 0, 0);
            world.spawnParticles(ParticleTypes.ENCHANT, px, ty + 0.35, pz, 1, 0, 0.01, 0, 0);
        }
        world.spawnParticles(ParticleTypes.ENCHANT, tx, ty, tz, 32, 0.4, 0.6, 0.4, 0.6);
        world.playSound(null, tx, ty, tz, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                SoundCategory.PLAYERS, 0.9f, 1.35f);
        world.playSound(null, tx, ty, tz, SoundEvents.ITEM_TOTEM_USE,
                SoundCategory.PLAYERS, 0.35f, 1.65f);
        ServerPlayNetworking.send(caster, new MageFeedbackPayload(1, target.getId()));
        return true;
    }

    private static boolean applyFlash(ServerPlayerEntity caster, MageConfig config) {
        caster.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE, config.abilities.flashDurationTicks, 0, false, true));
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double x = caster.getX(), y = caster.getY() + 1.0, z = caster.getZ();
        world.spawnParticles(ParticleTypes.GLOW, x, y, z, 36, 0.55, 0.75, 0.55, 0.08);
        world.spawnParticles(ParticleTypes.FIREWORK, x, y, z, 22, 0.6, 0.7, 0.6, 0.12);
        world.playSound(null, x, y, z, SoundEvents.BLOCK_BEACON_POWER_SELECT,
                SoundCategory.PLAYERS, 0.8f, 1.65f);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,
                SoundCategory.PLAYERS, 0.45f, 1.8f);
        return true;
    }

    private static boolean launchArcaneBolt(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        ArcaneBoltEntity bolt = new ArcaneBoltEntity(ModEntities.ARCANE_BOLT, world);
        bolt.setOwner(caster);
        bolt.refreshPositionAndAngles(caster.getX(), caster.getEyeY() - 0.1, caster.getZ(),
                caster.getYaw(), caster.getPitch());
        bolt.configure(config.abilities.arcaneBoltRange,
                config.abilities.arcaneBoltLifetimeTicks, config.abilities.arcaneBoltDamage);
        bolt.setVelocity(caster, caster.getPitch(), caster.getYaw(), 0.0f,
                (float) Math.max(0.1, Math.min(4.0, config.abilities.arcaneBoltSpeed)), 0.0f);
        if (!world.spawnEntity(bolt)) {
            return false;
        }
        world.spawnParticles(ParticleTypes.ENCHANT, bolt.getX(), bolt.getY(), bolt.getZ(),
                12, 0.12, 0.12, 0.12, 0.3);
        world.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ENTITY_BREEZE_SHOOT, SoundCategory.PLAYERS, 0.8f, 1.35f);
        return true;
    }

    private static boolean launchFireball(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        ArcaneBoltEntity fireball = new ArcaneBoltEntity(ModEntities.ARCANE_BOLT, world);
        fireball.setOwner(caster);
        fireball.refreshPositionAndAngles(caster.getX(), caster.getEyeY() - 0.1, caster.getZ(),
                caster.getYaw(), caster.getPitch());
        fireball.configureFireball(config.abilities.fireballRange,
                config.abilities.fireballLifetimeTicks, config.abilities.fireballDamage,
                config.abilities.fireballIgniteSeconds);
        fireball.setVelocity(caster, caster.getPitch(), caster.getYaw(), 0.0f,
                (float) Math.max(0.1, Math.min(4.0, config.abilities.fireballSpeed)), 0.0f);
        if (!world.spawnEntity(fireball)) {
            return false;
        }
        world.spawnParticles(ParticleTypes.FLAME, fireball.getX(), fireball.getY(), fireball.getZ(),
                10, 0.12, 0.12, 0.12, 0.025);
        world.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 0.8f, 0.85f);
        return true;
    }

    private static boolean launchElectricBolt(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        ArcaneBoltEntity bolt = new ArcaneBoltEntity(ModEntities.ARCANE_BOLT, world);
        bolt.setOwner(caster);
        bolt.refreshPositionAndAngles(caster.getX(), caster.getEyeY() - 0.1, caster.getZ(),
                caster.getYaw(), caster.getPitch());
        bolt.configureElectricBolt(config.abilities.electricBoltRange,
                config.abilities.electricBoltLifetimeTicks, config.abilities.electricBoltDamage);
        bolt.setVelocity(caster, caster.getPitch(), caster.getYaw(), 0.0f,
                (float) Math.max(0.1, Math.min(4.0, config.abilities.electricBoltSpeed)), 0.0f);
        if (!world.spawnEntity(bolt)) {
            return false;
        }
        world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, bolt.getX(), bolt.getY(), bolt.getZ(),
                18, 0.12, 0.12, 0.12, 0.035);
        world.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ENTITY_BREEZE_SHOOT, SoundCategory.PLAYERS, 0.75f, 1.8f);
        return true;
    }

    private static boolean applyEnergyImpulse(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double radius = Math.max(0.5, Math.min(12.0, config.abilities.energyImpulseRadius));
        double knockback = Math.max(0.1, Math.min(4.0, config.abilities.energyImpulseKnockback));
        for (LivingEntity target : world.getEntitiesByClass(LivingEntity.class,
                caster.getBoundingBox().expand(radius),
                entity -> entity.isAlive() && entity != caster && !entity.isSpectator())) {
            if (target instanceof PlayerEntity playerTarget && MageTeamAdapter.areAllies(caster, playerTarget)) {
                continue;
            }
            double dx = target.getX() - caster.getX();
            double dy = (target.getY() + target.getHeight() * 0.5) - (caster.getY() + caster.getHeight() * 0.5);
            double dz = target.getZ() - caster.getZ();
            double distance = Math.max(0.1, Math.sqrt(dx * dx + dy * dy + dz * dz));
            if (distance > radius) {
                continue;
            }
            double strength = knockback * (1.0 - distance / (radius + 0.01));
            double horizontalDistance = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));
            target.addVelocity(dx / horizontalDistance * strength, 0.25 + Math.max(0.0, dy / distance) * strength * 0.5
                    + strength * 0.2, dz / horizontalDistance * strength);
            world.spawnParticles(ParticleTypes.POOF, target.getX(), target.getY() + 0.5, target.getZ(),
                    8, 0.18, 0.25, 0.18, 0.04);
        }
        // Ground-level expanding shockwave ring; terrain is never modified.
        for (int i = 0; i < 48; i++) {
            double angle = Math.PI * 2.0 * i / 48.0;
            double px = caster.getX() + Math.cos(angle) * radius;
            double pz = caster.getZ() + Math.sin(angle) * radius;
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, px, caster.getY() + 0.12, pz,
                    1, 0.04, 0.12, 0.04, 0.025);
            world.spawnParticles(ParticleTypes.POOF, px, caster.getY() + 0.08, pz,
                    1, 0.03, 0.03, 0.03, 0.005);
        }
        world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, caster.getX(), caster.getY() + 1.0, caster.getZ(),
                36, radius / 2.0, 0.6, radius / 2.0, 0.08);
        world.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ENTITY_BREEZE_WIND_BURST, SoundCategory.PLAYERS, 0.9f, 1.0f);
        world.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 0.25f, 1.65f);
        return true;
    }

    /**
     * Short horizontal blink. The full player box is sampled every 0.2 blocks;
     * the destination is the last collision-free sample, so walls cannot be skipped.
     */
    private static boolean applyGaleStep(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double distance = Math.max(0.5, Math.min(8.0, config.abilities.galeStepDistance));
        double yaw = Math.toRadians(caster.getYaw());
        double dx = -Math.sin(yaw);
        double dz = Math.cos(yaw);
        Box originalBox = caster.getBoundingBox();
        double safeDistance = 0.0;

        for (double step = 0.2; step < distance + 1.0E-6; step += 0.2) {
            double candidateDistance = Math.min(step, distance);
            Box candidate = originalBox.offset(dx * candidateDistance, 0.0, dz * candidateDistance);
            if (!world.isSpaceEmpty(caster, candidate)) {
                break;
            }
            safeDistance = candidateDistance;
            if (candidateDistance >= distance) {
                break;
            }
        }
        if (safeDistance < 0.4) {
            return false;
        }

        double startX = caster.getX();
        double startY = caster.getY();
        double startZ = caster.getZ();
        caster.requestTeleport(startX + dx * safeDistance, startY, startZ + dz * safeDistance);
        world.spawnParticles(ParticleTypes.CLOUD, startX, startY + 0.8, startZ,
                14, 0.22, 0.3, 0.22, 0.025);
        world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                startX + dx * safeDistance * 0.5, startY + 0.9, startZ + dz * safeDistance * 0.5,
                18, 0.18, 0.25, 0.18, 0.04);
        world.spawnParticles(ParticleTypes.CLOUD, caster.getX(), caster.getY() + 0.8, caster.getZ(),
                16, 0.22, 0.3, 0.22, 0.025);
        world.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ENTITY_BREEZE_WIND_BURST, SoundCategory.PLAYERS, 0.7f, 1.5f);
        return true;
    }

    /** Server-raycast snare; aimed entities take priority over the block impact point. */
    private static boolean applyRunicSeal(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double range = Math.max(2.0, Math.min(32.0, config.abilities.runicSealRange));
        double radius = Math.max(1.0, Math.min(4.0, config.abilities.runicSealRadius));
        int duration = Math.max(20, Math.min(200, config.abilities.runicSealDurationTicks));
        Vec3d point = getLookPoint(caster, world, range);
        double cx = point.x;
        double cy = point.y + 0.15;
        double cz = point.z;
        double radiusSquared = radius * radius;
        boolean affected = false;

        Box area = new Box(cx - radius, cy - radius, cz - radius,
                cx + radius, cy + radius, cz + radius);
        for (LivingEntity target : world.getEntitiesByClass(LivingEntity.class, area,
                entity -> entity.isAlive() && entity != caster && !entity.isSpectator())) {
            if (target instanceof PlayerEntity ally && MageTeamAdapter.areAllies(caster, ally)) {
                continue;
            }
            double tx = target.getX();
            double ty = target.getY() + target.getHeight() * 0.5;
            double tz = target.getZ();
            double dx = tx - cx;
            double dy = ty - cy;
            double dz = tz - cz;
            if (dx * dx + dy * dy + dz * dz > radiusSquared || !caster.canSee(target)) {
                continue;
            }
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 4, false, true));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, 0, false, true));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, Math.min(duration, 40), 0, false, true));
            world.spawnParticles(ParticleTypes.WITCH, tx, target.getY() + 0.8, tz,
                    16, 0.28, 0.45, 0.28, 0.03);
            affected = true;
        }
        if (!affected) {
            return false;
        }
        for (int i = 0; i < 40; i++) {
            double angle = Math.PI * 2.0 * i / 40.0;
            world.spawnParticles(ParticleTypes.ENCHANT,
                    cx + Math.cos(angle) * radius, cy, cz + Math.sin(angle) * radius,
                    1, 0.01, 0.04, 0.01, 0.0);
        }
        world.spawnParticles(ParticleTypes.END_ROD, cx, cy, cz, 22,
                radius * 0.3, 0.12, radius * 0.3, 0.025);
        world.playSound(null, cx, cy, cz, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
                SoundCategory.PLAYERS, 0.8f, 0.7f);
        world.playSound(null, cx, cy, cz, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                SoundCategory.PLAYERS, 0.45f, 0.8f);
        return true;
    }

    /**
     * Finds the closest living entity under the crosshair before a blocking wall;
     * if no entity is aimed at, the point is the normal block-raycast impact.
     */
    private static Vec3d getLookPoint(ServerPlayerEntity caster, ServerWorld world, double range) {
        Vec3d origin = new Vec3d(caster.getX(), caster.getEyeY(), caster.getZ());
        Vec3d direction = caster.getRotationVec(1.0f).normalize();
        HitResult blockHit = caster.raycast(range, 1.0f, false);
        double maxDistance = blockHit.getType() == HitResult.Type.BLOCK
                ? Math.min(range, origin.distanceTo(blockHit.getPos())) : range;
        Box search = caster.getBoundingBox().stretch(direction.multiply(maxDistance)).expand(1.0);
        LivingEntity nearest = null;
        double nearestAlong = maxDistance + 1.0;

        for (LivingEntity target : world.getEntitiesByClass(LivingEntity.class, search,
                entity -> entity.isAlive() && entity != caster && !entity.isSpectator())) {
            Vec3d toTarget = new Vec3d(target.getX() - origin.x, target.getEyeY() - origin.y,
                    target.getZ() - origin.z);
            double along = toTarget.dotProduct(direction);
            if (along <= 0.0 || along > maxDistance || along >= nearestAlong || !caster.canSee(target)) {
                continue;
            }
            double px = toTarget.x - direction.x * along;
            double py = toTarget.y - direction.y * along;
            double pz = toTarget.z - direction.z * along;
            if (px * px + py * py + pz * pz > 0.85 * 0.85) {
                continue;
            }
            nearest = target;
            nearestAlong = along;
        }
        if (nearest != null) {
            return new Vec3d(nearest.getX(), nearest.getY() + nearest.getHeight() * 0.5, nearest.getZ());
        }
        return blockHit.getPos();
    }

    private static boolean applyBlessing(ServerPlayerEntity caster, PlayerEntity target, MageConfig config) {
        if (target == null || !target.isAlive() || target.isSpectator()
                || target.getEntityWorld() != caster.getEntityWorld()
                || !MageTeamAdapter.areAllies(caster, target)) {
            return false;
        }
        int duration = Math.max(20, Math.min(600, config.abilities.blessingDurationTicks));
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, 0, false, true));
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, duration, 0, false, true));
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double x = target.getX(), y = target.getY() + target.getHeight() * 0.65, z = target.getZ();
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 22,
                0.35, 0.55, 0.35, 0.04);
        world.spawnParticles(ParticleTypes.END_ROD, x, y, z, 14,
                0.25, 0.5, 0.25, 0.025);
        world.playSound(null, x, y, z, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                SoundCategory.PLAYERS, 0.75f, 1.55f);
        return true;
    }

    private static boolean applyGravitySphere(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double range = Math.max(4.0, Math.min(32.0, config.abilities.gravitySphereRange));
        Vec3d point = getLookPoint(caster, world, range);
        double radius = Math.max(2.0, Math.min(10.0, config.abilities.gravitySphereRadius));
        int duration = Math.max(20, Math.min(200, config.abilities.gravitySphereDurationTicks));
        double pull = Math.max(0.02, Math.min(0.35, config.abilities.gravitySpherePullStrength));
        double cx = point.x;
        double cy = point.y + 0.35;
        double cz = point.z;
        GRAVITY_SPHERES.put(caster.getUuid(), new GravitySphereState(
                world, cx, cy, cz, radius, pull, serverTick + duration));
        world.spawnParticles(ParticleTypes.PORTAL, cx, cy, cz, 48,
                radius * 0.25, radius * 0.25, radius * 0.25, 0.12);
        world.spawnParticles(ParticleTypes.END_ROD, cx, cy, cz, 18,
                radius * 0.18, radius * 0.18, radius * 0.18, 0.035);
        world.playSound(null, cx, cy, cz, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS, 0.65f, 0.65f);
        return true;
    }

    private static void tickGravitySpheres(net.minecraft.server.MinecraftServer server) {
        if (GRAVITY_SPHERES.isEmpty()) {
            return;
        }
        GRAVITY_SPHERES.entrySet().removeIf(entry -> {
            ServerPlayerEntity owner = server.getPlayerManager().getPlayer(entry.getKey());
            GravitySphereState field = entry.getValue();
            if (owner == null || !owner.isAlive() || owner.getEntityWorld() != field.world
                    || serverTick >= field.expiresAtTick) {
                field.world.spawnParticles(ParticleTypes.PORTAL, field.x, field.y, field.z,
                        18, 0.35, 0.35, 0.35, 0.08);
                return true;
            }
            if (serverTick % 2 != 0) {
                return false;
            }

            Box area = new Box(field.x - field.radius, field.y - field.radius, field.z - field.radius,
                    field.x + field.radius, field.y + field.radius, field.z + field.radius);
            for (LivingEntity target : field.world.getEntitiesByClass(LivingEntity.class, area,
                    entity -> entity.isAlive() && entity != owner && !entity.isSpectator())) {
                if (target instanceof PlayerEntity ally && MageTeamAdapter.areAllies(owner, ally)) {
                    continue;
                }
                double dx = field.x - target.getX();
                double dy = field.y - (target.getY() + target.getHeight() * 0.5);
                double dz = field.z - target.getZ();
                double distanceSquared = dx * dx + dy * dy + dz * dz;
                if (distanceSquared > field.radius * field.radius || distanceSquared < 0.04) {
                    continue;
                }
                double distance = Math.sqrt(distanceSquared);
                double strength = field.pullStrength * (1.0 - distance / field.radius);
                target.addVelocity(dx / distance * strength, dy / distance * strength * 0.65,
                        dz / distance * strength);
                if (serverTick % 4 == 0) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 6, 0, false, false));
                    field.world.spawnParticles(ParticleTypes.PORTAL,
                            target.getX(), target.getY() + target.getHeight() * 0.5, target.getZ(),
                            5, 0.18, 0.22, 0.18, 0.08);
                }
            }
            field.world.spawnParticles(ParticleTypes.PORTAL, field.x, field.y, field.z,
                    6, field.radius * 0.12, field.radius * 0.12, field.radius * 0.12, 0.05);
            return false;
        });
    }

    private static boolean applyHealingRune(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double radius = Math.max(1.0, Math.min(12.0, config.abilities.healingRuneRadius));
        float healAmount = Math.max(0.5f, Math.min(20.0f, config.abilities.healingRuneHealAmount));
        double cx = caster.getX();
        double cy = caster.getY() + caster.getHeight() * 0.5;
        double cz = caster.getZ();
        boolean healedAny = false;

        // The rune only heals the caster and allied players; it never benefits enemies or mobs.
        for (LivingEntity target : world.getEntitiesByClass(LivingEntity.class,
                caster.getBoundingBox().expand(radius),
                entity -> entity.isAlive() && !entity.isSpectator())) {
            if (!(target instanceof PlayerEntity targetPlayer)
                    || (targetPlayer != caster && !MageTeamAdapter.areAllies(caster, targetPlayer))) {
                continue;
            }
            double dx = target.getX() - cx;
            double dy = target.getY() + target.getHeight() * 0.5 - cy;
            double dz = target.getZ() - cz;
            if (dx * dx + dy * dy + dz * dz > radius * radius
                    || target.getHealth() >= target.getMaxHealth()) {
                continue;
            }

            float previousHealth = target.getHealth();
            target.heal(healAmount);
            if (target.getHealth() <= previousHealth) {
                continue;
            }
            healedAny = true;
            world.spawnParticles(ParticleTypes.HEART, target.getX(), target.getY() + target.getHeight() * 0.75,
                    target.getZ(), 4, 0.28, 0.25, 0.28, 0.02);
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, target.getX(), target.getY() + 0.8,
                    target.getZ(), 10, 0.35, 0.45, 0.35, 0.03);
        }

        // Do not spend mana or start a cooldown if nobody in the pulse could be healed.
        if (!healedAny) {
            return false;
        }

        for (int i = 0; i < 40; i++) {
            double angle = Math.PI * 2.0 * i / 40.0;
            double px = cx + Math.cos(angle) * radius;
            double pz = cz + Math.sin(angle) * radius;
            world.spawnParticles(ParticleTypes.END_ROD, px, caster.getY() + 0.12, pz,
                    1, 0.025, 0.035, 0.025, 0.005);
        }
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, cx, cy, cz,
                24, radius * 0.35, 0.45, radius * 0.35, 0.04);
        world.playSound(null, cx, cy, cz, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                SoundCategory.PLAYERS, 0.9f, 1.6f);
        return true;
    }

    private static boolean applyFrostNova(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double radius = Math.max(2.0, Math.min(10.0, config.abilities.frostNovaRadius));
        int duration = Math.max(20, Math.min(200, config.abilities.frostNovaDurationTicks));
        double cx = caster.getX(), cy = caster.getY() + 0.8, cz = caster.getZ();

        for (LivingEntity target : world.getEntitiesByClass(LivingEntity.class,
                caster.getBoundingBox().expand(radius),
                entity -> entity.isAlive() && entity != caster && !entity.isSpectator())) {
            if (target instanceof PlayerEntity playerTarget && MageTeamAdapter.areAllies(caster, playerTarget)) {
                continue;
            }
            double dx = target.getX() - cx;
            double dy = (target.getY() + target.getHeight() * 0.5) - cy;
            double dz = target.getZ() - cz;
            if (dx * dx + dy * dy + dz * dz > radius * radius) {
                continue;
            }
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 2, false, true));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, 0, false, true));
            world.spawnParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getY() + 0.8, target.getZ(),
                    22, 0.45, 0.55, 0.45, 0.035);
            world.spawnParticles(ParticleTypes.CLOUD, target.getX(), target.getY() + 0.4, target.getZ(),
                    8, 0.3, 0.2, 0.3, 0.02);
        }

        for (int i = 0; i < 64; i++) {
            double angle = Math.PI * 2.0 * i / 64.0;
            double px = cx + Math.cos(angle) * radius;
            double pz = cz + Math.sin(angle) * radius;
            world.spawnParticles(ParticleTypes.SNOWFLAKE, px, cy, pz, 2, 0.08, 0.22, 0.08, 0.015);
            world.spawnParticles(ParticleTypes.END_ROD, px, cy - 0.25, pz, 1, 0.025, 0.04, 0.025, 0.005);
        }
        world.spawnParticles(ParticleTypes.SNOWFLAKE, cx, cy, cz, 80,
                radius * 0.45, 0.55, radius * 0.45, 0.06);
        world.playSound(null, cx, cy, cz, SoundEvents.BLOCK_GLASS_BREAK,
                SoundCategory.PLAYERS, 0.9f, 0.65f);
        world.playSound(null, cx, cy, cz, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                SoundCategory.PLAYERS, 0.55f, 0.7f);
        return true;
    }

    private static boolean applySmokeVeil(ServerPlayerEntity caster, MageConfig config) {
        ServerWorld world = (ServerWorld) caster.getEntityWorld();
        double radius = Math.max(1.0, Math.min(32.0, config.abilities.smokeVeilRadius));
        int duration = Math.max(20, Math.min(1200, config.abilities.smokeVeilDurationTicks));
        double radiusSquared = radius * radius;
        int affected = 0;

        for (ServerPlayerEntity target : world.getPlayers()) {
            if (!target.isAlive() || target.isSpectator()
                    || (target != caster && !MageTeamAdapter.areAllies(caster, target))) {
                continue;
            }
            if (caster.squaredDistanceTo(target) > radiusSquared) {
                continue;
            }
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.INVISIBILITY, duration, 0, true, false, true));
            SMOKE_VEIL_TARGETS.put(target.getUuid(),
                    new SmokeVeilState(caster.getUuid(), serverTick + duration));
            world.spawnParticles(ParticleTypes.LARGE_SMOKE,
                    target.getX(), target.getY() + 1.0, target.getZ(), 18, 0.35, 0.65, 0.35, 0.015);
            world.spawnParticles(ParticleTypes.PORTAL,
                    target.getX(), target.getY() + 0.8, target.getZ(), 12, 0.35, 0.5, 0.35, 0.15);
            affected++;
        }

        if (affected == 0) {
            return false;
        }
        world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                caster.getX(), caster.getY() + 0.4, caster.getZ(), 35,
                radius * 0.35, 0.35, radius * 0.35, 0.015);
        world.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.9f, 0.7f);
        world.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.PLAYERS, 0.55f, 1.5f);
        return true;
    }

    private static void tickSmokeVeil(net.minecraft.server.MinecraftServer server) {
        if (SMOKE_VEIL_TARGETS.isEmpty()) {
            return;
        }
        SMOKE_VEIL_TARGETS.entrySet().removeIf(entry -> {
            UUID targetUuid = entry.getKey();
            SmokeVeilState veil = entry.getValue();
            ServerPlayerEntity target = server.getPlayerManager().getPlayer(targetUuid);
            ServerPlayerEntity owner = server.getPlayerManager().getPlayer(veil.ownerUuid);
            if (target == null || owner == null || !target.isAlive() || !owner.isAlive()
                    || serverTick >= veil.expiresAtTick) {
                return true;
            }
            if (target.getEntityWorld() != owner.getEntityWorld()) {
                target.removeStatusEffect(StatusEffects.INVISIBILITY);
                return true;
            }

            boolean exposed = nearEnemyPlayer(target) || nearEnemyPlayer(owner);
            if (exposed) {
                UUID ownerUuid = veil.ownerUuid;
                for (Map.Entry<UUID, SmokeVeilState> linked : SMOKE_VEIL_TARGETS.entrySet()) {
                    if (linked.getValue().ownerUuid.equals(ownerUuid)) {
                        ServerPlayerEntity linkedPlayer = server.getPlayerManager().getPlayer(linked.getKey());
                        if (linkedPlayer != null) {
                            linkedPlayer.removeStatusEffect(StatusEffects.INVISIBILITY);
                            ServerWorld linkedWorld = (ServerWorld) linkedPlayer.getEntityWorld();
                            linkedWorld.spawnParticles(ParticleTypes.SMOKE,
                                    linkedPlayer.getX(), linkedPlayer.getY() + 1.0, linkedPlayer.getZ(),
                                    14, 0.3, 0.55, 0.3, 0.01);
                        }
                    }
                }
                return true;
            }
            return false;
        });
    }

    private static boolean nearEnemyPlayer(ServerPlayerEntity player) {
        for (ServerPlayerEntity other : player.getEntityWorld().getPlayers()) {
            if (other == player || !other.isAlive() || other.isSpectator()
                    || MageTeamAdapter.areAllies(player, other)) {
                continue;
            }
            if (player.squaredDistanceTo(other) <= 36.0) {
                return true;
            }
        }
        return false;
    }

    private static final class GravitySphereState {
        private final ServerWorld world;
        private final double x;
        private final double y;
        private final double z;
        private final double radius;
        private final double pullStrength;
        private final long expiresAtTick;

        private GravitySphereState(ServerWorld world, double x, double y, double z,
                                   double radius, double pullStrength, long expiresAtTick) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.radius = radius;
            this.pullStrength = pullStrength;
            this.expiresAtTick = expiresAtTick;
        }
    }

    private static final class SmokeVeilState {
        private final UUID ownerUuid;
        private final long expiresAtTick;

        private SmokeVeilState(UUID ownerUuid, long expiresAtTick) {
            this.ownerUuid = ownerUuid;
            this.expiresAtTick = expiresAtTick;
        }
    }

    private static void reject(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new MageFeedbackPayload(0, -1));
    }

    private static final class ManaState {
        private double mana;
        private final Map<String, Long> cooldowns = new HashMap<>();

        private ManaState(double initialMana) {
            this.mana = initialMana;
        }
    }
}
