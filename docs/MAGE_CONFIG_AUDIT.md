# Mage configuration audit

Status: in progress. This document tracks source-level configuration coverage; it is not a substitute for in-game tests.

## Current implementation

All 17 registered ability IDs are routed through `MageAbilityService` and have configurable mana cost and cooldown. The main gameplay parameters below are also represented in `MageConfig.AbilitySettings`:

| Ability | Configured gameplay parameters | Notes / remaining source-level audit |
|---|---|---|
| `magic_barrier` | target range, duration, resistance amplifier, beam segments, ring segments, center particle count, mana, cooldown | Sound mix remains constant. |
| `flash_of_light` | duration, resistance amplifier, glow/firework particle counts, mana, cooldown | Sound mix remains constant. |
| `arcane_bolt` | speed, range, lifetime, damage, size, launch/trail/expiry/impact particle counts, mana, cooldown | Particle spread and cadence remain implementation constants. |
| `energy_impulse` | radius, knockback power, mana, cooldown | Knockback now scales configured power into controlled velocity with a hard cap; shockwave segments and center/target particle counts are configurable. In-game distance must be measured. |
| `frost_nova` | radius, duration, slowness/weakness amplifiers, ring segments, center/target snowflake counts, target cloud count, ring secondary particle counts, mana, cooldown | Particle spread remains implementation-defined. |
| `healing_rune` | radius, heal amount, target heart/villager counts, ring segments, center particle count, mana, cooldown | Cast fails without spending mana/cooldown if nobody can be healed. |
| `smoke_veil` | radius, duration, reveal smoke particle count, mana, cooldown | Invisibility effect particles are disabled; break/reveal particle count is configurable. |
| `gale_step` | blink distance, departure/midpoint/arrival particle counts, mana, cooldown | Collision sampling step remains an implementation constant. |
| `runic_seal` | range, radius, duration, slowness/weakness amplifiers, glow duration, target/center particle counts, ring segments, mana, cooldown | Particle spread remains implementation-defined. |
| `blessing` | duration, speed/regeneration amplifiers, target range, villager/end-rod particle counts, mana, cooldown | Particle spread remains implementation-defined. |
| `gravity_sphere` | cast range, radius, duration, pull strength, particle multiplier, initial portal/end-rod counts, pulse/expiry counts, slowness duration/amplifier, mana, cooldown | Particle spread and two-tick pulse cadence remain implementation-defined. |
| `fireball` | speed, range, lifetime, damage, ignition duration/radius, launch/trail/impact/target particle counts, mana, cooldown | Ground-impact splash only occurs on block collision; direct entity-hit behavior remains separate. |
| `electric_bolt` | speed, range, lifetime, damage, launch/trail/impact counts, chain target limit/range/trail/jump-impact counts, spark cadence/count, slow duration/amplifier, mana, cooldown | Chain target uniqueness is enforced per cast. Confirm target count and cyan trail in-game. |
| `ice_wall` | distance, width, height, duration, placement/removal particle counts, mana, cooldown | Placement rules and block effects need gameplay validation. |
| `magic_doppelganger` | duration, spawn/idle/expiry particle counts, mana, cooldown | Candidate placement distances remain constants. |
| `gust_of_wind` | range, strength, cone angle, visual layers, particles per layer, target cloud/poof counts, mana, cooldown | One sweep particle per layer and particle spread remain implementation-defined. |
| `shadow_veil` | radius, duration, speed/slow/blindness durations and amplifiers, target smoke count, mana, cooldown | Potion particles are disabled for the caster's invisibility/speed effects; validate visibility and ally/enemy behavior in-game. |

## Changes made in this branch

- Added configurable status-effect amplifiers/durations for barrier, flash, frost nova, runic seal, blessing, gravity sphere, shadow veil, and electric bolt.
- Scaled energy-impulse velocity rather than using the configured value directly as blocks/tick; retained distance falloff and a safety cap.
- Expanded configurable particle counts for barrier, flash, projectile launch/trails/impacts, gale step, runic seal, blessing, gravity sphere, healing rune, frost nova, ice wall, doppelganger, gust of wind, and shadow/smoke veil.
- Rechecked source/config field consistency across `MageAbilityService` and `ArcaneBoltEntity`; all referenced ability fields resolve to `MageConfig.AbilitySettings` (the static scan's only apparent missing name was the `MageConfigManager` class itself, not a config field).
- Fixed electric bolt's direct-hit slowness to use `electricBoltSlowDurationTicks` and `electricBoltSlowAmplifier`, matching chained hits instead of silently hardcoding 30 ticks/amplifier 0.

## Required validation before closing the audit

- [ ] GitHub Actions `Build mod` succeeds on the latest PR head after the latest particle-config changes.
- [ ] Energy impulse knocks a nearby enemy back roughly 4–5 blocks without launching it excessively; repeat at the edge of the configured radius.
- [ ] Gravity sphere radius, duration, pull, and particle density match the intended balance.
- [ ] Chain lightning hits the intended number of distinct enemies and never repeats a target during one cast.
- [ ] Fireball block impact ignites nearby hostile targets; direct entity impact does not trigger the ground splash.
- [ ] Smoke veil and shadow veil do not reveal affected players with emitted status particles; unrelated players retain normal particles.
- [ ] Barrier, blessing, and target selection are tested with two players and scoreboard teams.
- [ ] Config GUI exposes the new fields and the JSON config loads safely with defaults and existing user configs.
- [ ] Review and configure remaining hardcoded particle counts where tunability is required by issue #221.
