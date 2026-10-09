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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MageAbilityService {
    private static final Map<UUID, ManaState> PLAYERS = new HashMap<>();
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
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                PLAYERS.remove(handler.player.getUuid()));

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
        if ("magic_barrier".equals(id)) {
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
                || "arcane_bolt".equals(id) || "energy_impulse".equals(id);
    }

    private static int manaCost(String id, MageConfig config) {
        return switch (id) {
            case "magic_barrier" -> config.abilities.barrierManaCost;
            case "flash_of_light" -> config.abilities.flashManaCost;
            case "arcane_bolt" -> config.abilities.arcaneBoltManaCost;
            case "energy_impulse" -> config.abilities.energyImpulseManaCost;
            default -> Integer.MAX_VALUE;
        };
    }

    private static int cooldownTicks(String id, MageConfig config) {
        return switch (id) {
            case "magic_barrier" -> config.abilities.barrierCooldownTicks;
            case "flash_of_light" -> config.abilities.flashCooldownTicks;
            case "arcane_bolt" -> config.abilities.arcaneBoltCooldownTicks;
            case "energy_impulse" -> config.abilities.energyImpulseCooldownTicks;
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
        world.spawnParticles(ParticleTypes.FLASH, x, y, z, 1, 0, 0, 0, 0);
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
            double dz = target.getZ() - caster.getZ();
            double distance = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));
            if (distance > radius) {
                continue;
            }
            double strength = knockback * (1.0 - distance / (radius + 0.01));
            target.addVelocity(dx / distance * strength, 0.25 + strength * 0.2, dz / distance * strength);
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
