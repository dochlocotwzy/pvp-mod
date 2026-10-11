# In-game test checklist — PvP Mage

Use this checklist when the user is ready to test the mod in Minecraft 1.21.11. Do not mark any gameplay item complete based on compilation alone.

## Projectile visuals
- [ ] Cast the fire spell and watch the projectile in flight: it should look like a fireball with flame, not the purple Arcane Bolt icon.
- [ ] Check the fire spell item in the hand/hotbar separately; its item icon must remain unchanged.
- [ ] Cast the lightning spell: its projectile/effect should be a distinct blue-white electrical bolt or lightning effect, not the purple Arcane Bolt icon.
- [ ] Check the lightning spell item in the hand/hotbar separately; its item icon must remain unchanged.

## Energy Impulse
- [ ] In a flat test area, record a zombie's coordinates before casting.
- [ ] Cast Energy Impulse from a consistent distance and orientation.
- [ ] Record the zombie's horizontal displacement after several ticks, separately from its vertical movement.
- [ ] Repeat from another orientation/distance. Confirm it gives a substantial horizontal shove, not mostly a small upward hop.
- [ ] If testing player targets, check server-side behavior too.

## Invisibility particles versus cast decoration
- [ ] While the invisibility effect is active, verify that the invisible player no longer emits the ongoing particles that reveal their position to opponents.
- [ ] Cast the relevant spell and verify that the decorative one-shot black/smoke particles after activation are present.
- [ ] Confirm the cast decoration does not turn into ongoing particles throughout the invisibility duration.

## Basic regression
- [ ] Mod launches without errors; mage items and abilities are present.
- [ ] No trap items/entities/assets were accidentally reintroduced into the mage-only JAR.
- [ ] Record Minecraft/Fabric versions and any relevant log errors if something fails.

Status: pending in-game test. Build success is not gameplay verification.
