package com.pvptraps.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/** Integration seam for modes that do not use scoreboard teams. */
@FunctionalInterface
public interface MageTeamAdapter {
    AtomicReference<MageTeamAdapter> ACTIVE = new AtomicReference<>(MageTeamAdapter::scoreboardTeams);

    boolean isAlly(PlayerEntity first, PlayerEntity second);

    static boolean areAllies(PlayerEntity first, PlayerEntity second) {
        return ACTIVE.get().isAlly(first, second);
    }

    static void install(MageTeamAdapter adapter) {
        ACTIVE.set(Objects.requireNonNull(adapter));
    }

    private static boolean scoreboardTeams(PlayerEntity first, PlayerEntity second) {
        Team firstTeam = first.getScoreboardTeam();
        return firstTeam != null && firstTeam == second.getScoreboardTeam();
    }
}
