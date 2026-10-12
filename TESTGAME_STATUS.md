# Testgame status

Server-side Fabric GameTests for Minecraft 1.21.11 are enabled in the isolated `experiment/testgame-gametest` branch. CI run #520 completed successfully on commit `f5b582b8f4f6a53ea1f0531c55b7a08acced16d9`: the real GameTest server started, all three required tests passed, and Gradle reported `BUILD SUCCESSFUL`.

Root cause: `ModEntities` was first initialized from the test after the entity registry was frozen, causing `IllegalStateException: This registry can't create intrusive holders`. The fix explicitly invokes `ModEntities.register()` from the mod initializer. The test verifies the registered `pvpmage:arcane_bolt` entity can spawn in the test world and is removed after its configured lifetime.

No merge was performed; only the experimental branch was changed. The legacy trap recipe warning remains unrelated and did not prevent the test suite from passing.
