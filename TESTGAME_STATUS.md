# TESTGAME status

Checked 2026-10-12 UTC. Branch: `experiment/testgame-gametest`. Current tested head: `2381276412ffd388e1f3f997cefa1f9777f7c414`. Minecraft 1.21.11, Fabric API 0.141.3+1.21.11, Loader 0.16.10, Java 21.

## Verified runtime result

- [CI run #521](https://github.com/dochlocotwzy/pvp-mod/actions/runs/38308850813), job `build`, commit `2381276412ffd388e1f3f997cefa1f9777f7c414`: success.
- [CI run #520](https://github.com/dochlocotwzy/pvp-mod/actions/runs/38308539769), commit `f5b582b8f4f6a53ea1f0531c55b7a08acced16d9`: success.
- Both logs show `FabricGameTestRunner: Starting test server`, `Started game test server`, `3 GAME TESTS COMPLETE`, `All 3 required tests passed :)`, and `BUILD SUCCESSFUL`. This is confirmed server GameTest runtime execution, not compile-only evidence.
- Current CI job runs `gradle build --no-daemon`; Loom runs server GameTests during `build`. To run locally: `gradle runGameTest --no-daemon` or `gradle build --no-daemon`, using JDK 21.

## Architecture

- `build.gradle` enables Loom's isolated GameTest source set, test mod ID `pvpmage-test`, server tests enabled, client tests disabled.
- `src/gametest/resources/fabric.mod.json` declares the server-only test entrypoint.
- `src/gametest/java/com/pvpmage/gametest/MageServerGameTests.java` contains two project-specific tests: Mage config initialization; and arcane-bolt ID registration, spawn into the test world, and expiry after its configured lifetime. Minecraft's runner reports 3 required tests total.
- Official guide for 1.21.11: https://github.com/FabricMC/fabric-docs/blob/main/versions/1.21.11/develop/automatic-testing.md

## Failed attempts and fixes

- [#515](https://github.com/dochlocotwzy/pvp-mod/actions/runs/38305506335), [#516](https://github.com/dochlocotwzy/pvp-mod/actions/runs/38305511157), [#517](https://github.com/dochlocotwzy/pvp-mod/actions/runs/38305698789): GameTest compile errors; `FabricGameTest` and annotation property `templateName` do not exist in the selected Fabric API. Switched to `@GameTest(structure = "fabric-gametest-api-v1:empty", ...)`.
- [#518](https://github.com/dochlocotwzy/pvp-mod/actions/runs/38305905230) and [#519](https://github.com/dochlocotwzy/pvp-mod/actions/runs/38308261345): test server crashed on first access to `ModEntities`, with `IllegalStateException: This registry can't create intrusive holders` after registry freeze.
- Fix: force `ModEntities` class initialization during mod initialization by calling `ModEntities.register()`. This changes registration timing only; no spell mechanics were changed for the tests. Runs #520 and #521 pass.

## Limitations / warnings

- The clean test launch logs missing `server.properties` and `eula.txt` messages before starting the test server; tests still pass.
- The test runtime logs a legacy `pvptraps:recipe/trap.json` parse error because `pvptraps:trap` is unknown to that runtime registry. This warning remains unresolved; do not change the stable traps module just to silence it.
- Existing AutoConfig deprecation warning remains.
- Passing tests do not yet prove projectile collision/impact, damage/effects, fireball/lightning behavior, player knockback sync, client rendering, multiplayer compatibility, or behavior on a separately launched production server using the final JAR. Client GameTests are disabled.

## Next steps

1. Keep all harness work on this branch; no merge and no edits to `main`, stable traps, or existing PRs without explicit approval.
2. Add one focused server test at a time for projectile impact/effects with explicit assertions.
3. Investigate the legacy recipe warning separately, preserving stable trap assets.
4. Mark gameplay behavior verified only after a test asserts that behavior, not merely compilation or server startup.
