# In-game test checklist — PvP Mage

Target: Minecraft 1.21.11. Do not mark gameplay checks complete based on compilation or source inspection alone. Record game/Fabric versions and attach logs or screenshots for failures.

## Config screen and localization
- [ ] Open the PvP Mage config screen and confirm the root title says “PvP Mage”, not “PvP Traps”.
- [ ] Confirm Mana and Abilities are visible as top-level groups.
- [ ] Confirm ability categories are grouped and readable: General, Magic Barrier, Flash of Light, Arcane Bolt, Energy Impulse, Frost Nova, Healing Rune, Smoke Veil, Gale Step, Runic Seal, Blessing, Gravity Sphere, Fireball, Electric Bolt, Ice Wall, Magic Doppelganger, Gust of Wind, Shadow Veil.
- [ ] Repeat with Russian and English language settings; check for raw translation keys, duplicate labels, or trap-only entries.

## Projectile visuals
- [ ] Cast Fireball and watch it in flight: flame/smoke trail and a distinct fireball body, not the purple Arcane Bolt icon.
- [ ] Check the Fireball item icon in hand/hotbar separately; it must remain unchanged.
- [ ] Cast Electric Bolt: verify a distinct blue-white electrical body/trail, not the purple Arcane Bolt icon.
- [ ] Check the Electric Bolt item icon in hand/hotbar separately; it must remain unchanged.
- [ ] Confirm Arcane Bolt retains its own purple/enchantment-style trail and is visually distinguishable from both variants.

## Mana HUD tag visibility
- [ ] Leave `hudVisibleTags` empty and confirm the mana HUD remains visible to everyone (backwards-compatible default).
- [ ] Set `hudVisibleTags` to `mage,magic_user`; add `/tag <player> add mage` to one player and confirm only tagged players see the HUD.
- [ ] Remove the tag and confirm the HUD disappears on the next mana update; confirm mana regeneration and spell casting still work regardless of HUD visibility.

## Energy Impulse
- [ ] In a flat test area, place a zombie at a measured distance (for example 3 blocks) from the caster and record initial coordinates.
- [ ] Cast Energy Impulse and record horizontal displacement and vertical displacement after 5, 10, and 20 ticks.
- [ ] Repeat at near, middle, and edge of the configured radius; verify knockback falls off with distance and horizontal shove remains stronger than vertical lift.
- [ ] Repeat with a non-ally player on a test server and with an ally; confirm velocity is synced to non-ally players and allies are not knocked back.

## Gravity Sphere
- [ ] Cast at a fixed point and confirm the visible field appears at the aimed location.
- [ ] Verify targets inside the radius are pulled toward the center, targets outside are unaffected, and allies are excluded.
- [ ] Time the default duration (300 ticks = 15 seconds); verify pull pulses and slow effect cadence feel consistent.
- [ ] Check that the field and particles clean up when it expires, the caster dies, or the caster changes dimension/disconnects.

## Smoke Veil and Shadow Veil (separate abilities)
- [ ] Smoke Veil: confirm its activation burst appears, affected allies become invisible, and there is no continuous caster-only trail from this ability.
- [ ] Smoke Veil: approach an enemy within the configured reveal radius; confirm the veil ends and the reveal burst appears as intended.
- [ ] Shadow Veil: confirm the caster becomes invisible and receives its configured speed effect; visible smoke/reverse-portal particles intentionally trail around the caster every 2 ticks while active.
- [ ] Shadow Veil: confirm the trail stops on expiry, death, or disconnect, and does not appear around affected targets.
- [ ] Check that each ability's intended visibility/reveal behavior is understandable in a real PvP encounter.

## High-value regression checks
- [ ] Magic Barrier: protection and duration match configuration.
- [ ] Flash of Light: duration and resistance match configuration.
- [ ] Frost Nova and Runic Seal: status effects, radius, and duration match configuration.
- [ ] Healing Rune and Blessing: only intended targets are affected; allies are handled correctly.
- [ ] Fireball: direct-hit damage, ignition, and ground-impact ignition work as configured; allies are not ignited by direct hit.
- [ ] Electric Bolt: direct-hit damage, slow, chain jumps, jump range, and ally exclusion work.
- [ ] Gale Step: stops safely at walls and does not clip through blocks.
- [ ] Ice Wall: temporary blocks are removed after expiry without deleting unrelated blocks.
- [ ] Magic Doppelganger: decoy appears, persists for configured duration, and cleans up.
- [ ] Gust of Wind: push direction and strength are correct; allies are handled correctly.
- [ ] No trap items/entities/assets are present in the Mage-only JAR or creative inventory.
- [ ] Check latest.log for errors/warnings after startup and after exercising each ability.

Status: pending in-game test. Source/CI checks are not gameplay verification.
