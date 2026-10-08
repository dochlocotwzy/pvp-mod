# Mage ability framework — implementation notes

This framework is intentionally independent of any server mode/class implementation. The mode may grant the items and optionally bridge its own team/mana rules later.

## Interaction rules
- One registered item per ability; there is no ability wheel.
- Targeted support abilities open a dedicated translucent teammate-selection screen.
- The server revalidates target eligibility, range, mana, and cooldown when a selection is submitted.
- Invalid target, insufficient mana, or active cooldown means no cast, no mana spent, and no cooldown started.
- Every rejected cast gives the caster a short, clearly distinct staccato failure cue (two clipped, low-pitched magical notes); feedback must not sound like the successful-cast sound.
- Ally-target cast animation may temporarily focus the camera on the selected ally for about 1–2 seconds, then restore normal camera control. The focus is cosmetic and never changes server targeting.
- Abilities must not edit arena blocks or leave persistent terrain changes.

## Initial ability slice
- Magic Barrier: select an eligible ally; apply a configurable temporary protective effect and beam/impact feedback.
- Flash of Light: brief configurable Resistance effect.
- Arcane Bolt: visible server-authoritative projectile with configurable speed, range, lifetime, and size.
- Energy Impulse: configurable knockback pulse.

## Integration boundaries
- Keep mana and cooldown handling behind a reusable service.
- Do not assume the external game mode's scoreboard, class tags, or team system. Provide a clear adapter seam for those checks.
- Mana persistence through death should be configurable.
- Balance values and visual timings should be configurable.
