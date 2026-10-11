# Mage configuration audit

Status: in progress. This document tracks source-level configuration coverage; it is not a substitute for in-game tests.

## Current implementation

All 17 registered ability IDs are routed through `MageAbilityService` and have configurable mana cost and cooldown. The main gameplay parameters below are also represented in `MageConfig.AbilitySettings`:

| Ability | Configured gameplay parameters | Notes / remaining source-level audit |
|---|---|---|
| `magic_barrier` | target range, duration, resistance amplifier, mana, cooldown | Particle ring size/count and sound mix remain constants. |
| `flash_of_light` | duration, resistance amplifier, mana, cooldown | Particle counts and sound mix remain constants. |
| `arcane_bolt` | speed, range, lifetime, damage, size, mana, cooldown | Projectile trail and impact particle counts remain constants. |
| `energy_impulse` | radius, knockback power, mana, cooldown | Knockback now scales configured power into controlled velocity with a hard cap; shockwave segments and center/target particle counts are configurable. In-game distance must be measured. |
| `frost_nova` | radius, duration, slowness/weakness amplifiers, mana, cooldown | Ring segments and center/target snowflake counts are configurable; remaining secondary particle counts are constants. |
| `healing_rune` | radius, heal amount, mana, cooldown | Particle counts remain constants. Cast fails without spending mana/cooldown if nobody can be healed. |
| `smoke_veil` | radius, duration, mana, cooldown | Invisibility effect particles are disabled; break/reveal visuals remain constants. |
| `gale_step` | blink distance, mana, cooldown | Collision sampling step and trail particles remain implementation constants. |
| `runic_seal` | range, radius, duration, slowness/weakness amplifiers, glow duration, mana, cooldown | Target particle count and ring density remain constants. |
| `blessing` | duration, speed/regeneration amplifiers, target range, mana, cooldown | Particle counts remain constants. |
| `gravity_sphere` | cast range, radius, duration, pull strength, particle multiplier, particles per pulse, slowness duration/amplifier, mana, cooldown | Core tuning is configurable; initial/final particle counts remain constants. |
| `fireball` | speed, range, lifetime, damage, direct-hit ignition duration, ground-impact ignition radius, mana, cooldown | Ground-impact splash only occurs on block collision; direct entity-hit behavior remains separate. Trail/impact particle counts remain constants. |
| `electric_bolt` | speed, range, lifetime, damage, chain target limit/range/particle counts, slow duration/amplifier, mana, cooldown | Chain target uniqueness is enforced per cast. Confirm target count and cyan trail in-game. |
| `ice_wall` | distance, width, height, duration, mana, cooldown | Placement rules and block/particle effects need gameplay validation; particle count remains constant. |
| `magic_doppelganger` | duration, mana, cooldown | Spawn particle counts and candidate placement distances remain constants. |
| `gust_of_wind` | range, strength, cone angle, visual layers, particles per layer, mana, cooldown | Main cone geometry and particle density are configurable; impact particle counts remain constants. |
| `shadow_veil` | radius, duration, speed/slow/blindness durations and amplifiers, mana, cooldown | Potion particles are disabled for the caster's invisibility/speed effects; validate visibility and ally/enemy behavior in-game. |

## Changes made in this branch

- Added configurable status-effect amplifiers/durations for barrier, flash, frost nova, runic seal, blessing, gravity sphere, shadow veil, and electric bolt.
- Scaled energy-impulse velocity rather than using the configured value directly as blocks/tick; retained distance falloff and a safety cap.
- Verified that configuration references used by `MageAbilityService` resolve to fields in `MageConfig`. This is a source consistency check, not a successful Gradle build result.

## Required validation before closing the audit

- [ ] GitHub Actions `Build mod` succeeds on the latest PR head.
- [ ] Energy impulse knocks a nearby enemy back roughly 4–5 blocks without launching it excessively; repeat at the edge of the configured radius.
- [ ] Gravity sphere radius, duration, pull, and particle density match the intended balance.
- [ ] Chain lightning hits the intended number of distinct enemies and never repeats a target during one cast.
- [ ] Fireball block impact ignites nearby hostile targets; direct entity impact does not trigger the ground splash.
- [ ] Smoke veil and shadow veil do not reveal affected players with emitted status particles; unrelated players retain normal particles.
- [ ] Barrier, blessing, and target selection are tested with two players and scoreboard teams.
- [ ] Config GUI exposes the new fields and the JSON config loads safely with defaults and existing user configs.
- [ ] Review and configure remaining hardcoded particle counts where tunability is required by issue #221.
