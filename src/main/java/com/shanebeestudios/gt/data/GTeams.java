package com.shanebeestudios.gt.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GTeams {

    private final Map<String, GTeam> TEAMS = new HashMap<>();
    private final Map<String, GTeam> TEAM_BY_ID = new HashMap<>();

    public GTeams() {
    }

    public void setTeam(@NotNull String name, @NotNull GTeam team) {
        TEAMS.put(name, team);
        TEAM_BY_ID.put(team.getId(), team);
    }

    @Nullable
    public GTeam getTeam(@NotNull String name) {
        if (TEAMS.containsKey(name)) {
            return TEAMS.get(name);
        }
        return null;
    }

    @Nullable
    public GTeam getByID(@NotNull String id) {
        if (TEAM_BY_ID.containsKey(id)) {
            return TEAM_BY_ID.get(id);
        }
        return null;
    }

}
