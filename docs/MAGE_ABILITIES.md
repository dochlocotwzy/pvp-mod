# Mage ability framework

The Mage abilities are implemented as standalone, reusable items in the `pvptraps` mod. They do not depend on the external mode's class tags or scoreboard mana. The built-in mana pool is configured in `config/pvptraps_mage.json`; an integration can grant the items and install a custom `MageTeamAdapter` on both logical sides.

## Items and behavior

- **Magic Barrier** — opens an ally selector; the server revalidates the selected player, team adapter, range, life state, mana and cooldown. On success it applies temporary Resistance and sends a particle beam. The client briefly focuses on the ally, then smoothly restores the previous view.
- **Flash of Light** — grants brief Resistance to the caster.
- **Arcane Bolt** — a visible, server-authoritative thrown projectile with configurable speed, range, lifetime, damage and size. The configured entity dimensions follow the visual size; impacts never edit blocks.
- **Energy Impulse** — knocks back nearby non-allies without modifying terrain.
- **Gale Step** — a short horizontal blink with server-side collision checks every 0.2 blocks. The destination is the last empty player bounding box; it stops before walls and does not edit blocks.
- **Runic Seal** — a server-raycast snare at the look impact point; it slows and weakens visible enemies in a small radius, but never catches allies or targets through solid walls.
- **Blessing** — opens the ally selector and grants the selected teammate Speed and Regeneration for a configurable duration.
- **Gravity Sphere** — a temporary server-side field pulls non-allies toward a configurable point, briefly slowing them; it expires automatically and never edits blocks.
- **Fireball** — a configurable projectile that damages and ignites a directly hit non-ally. It uses particles and sound for the impact but deliberately does not create a terrain-damaging explosion.
- **Electric Bolt** — a single-target projectile that deals magic damage and briefly slows its target. It does not chain to other entities and does not summon lightning.

## Shared rules

- Each ability has an individual registered item; there is no wheel.
- Mana maximum, passive regeneration, ability costs, cooldowns and effect/projectile values are configurable.
- Mana is keyed by player UUID and by default survives death. Set `mana.persistThroughDeath=false` to refill on respawn.
- Invalid ability requests, unavailable targets, insufficient mana and active cooldowns do not spend mana or start cooldowns. They produce a short two-pulse failure sound.
- Team checks are isolated behind `MageTeamAdapter`. The default adapter uses scoreboard teams; a mode using another team system should install its own adapter on both the client (for the selector) and server (for authoritative validation).
- All effects are entity/status/particle/sound based. Abilities do not place, break, or persistently alter arena blocks.

## Integration surface

- Grant `ModItems.MAGIC_BARRIER`, `FLASH_OF_LIGHT`, `ARCANE_BOLT` and `ENERGY_IMPULSE` to the Mage class.
- Read or set the built-in mana pool with `MageAbilityService.getMana(ServerPlayerEntity)` and `MageAbilityService.setMana(ServerPlayerEntity, double)`.
- Install custom team rules with `MageTeamAdapter.install((first, second) -> ...)` on both logical sides.

This is an evolving implementation slice. Verify in a real client/server session, especially the ally GUI, collision-aware dash, projectile hitboxes and mana/cooldown rejection feedback.
