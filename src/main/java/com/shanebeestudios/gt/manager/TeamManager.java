package com.shanebeestudios.gt.manager;

import com.shanebeestudios.gt.GeyserTeams;
import com.shanebeestudios.gt.data.GTeam;
import com.shanebeestudios.gt.data.GTeams;
import com.shanebeestudios.gt.data.Message;
import com.shanebeestudios.gt.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.geysermc.floodgate.util.DeviceOS;

import java.util.Locale;

public class TeamManager {

    private final GeyserTeams plugin;
    private final Scoreboard mainScoreboard;

    public TeamManager(GeyserTeams plugin) {
        this.plugin = plugin;
        this.mainScoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        registerTeams();
    }

    private void registerTeams() {
        // TODO this is just for testing
        this.mainScoreboard.getTeams().forEach(team -> {
            if (team.getName().startsWith("gt-")) {
                team.unregister();
            }
        });

        // Register Geyser teams based on devices
        GTeams gTeams = plugin.getGTeams();
        for (DeviceOS value : DeviceOS.values()) {
            if (value == DeviceOS.NX) {
                // I have no clue what this device really is
                continue;
            }

            String name = value.toString().toLowerCase(Locale.ROOT).replace(" ", "_");
            GTeam gTeam = gTeams.getTeam(name);
            if (gTeam != null) {
                registerTeam(name, gTeam);
            }
        }

        // Now we register our Java team
        GTeam javaTeam = gTeams.getTeam("java");
        if (javaTeam != null) {
            registerTeam("java", javaTeam);
        }
    }

    private void registerTeam(String name, GTeam gTeam) {
        String teamID = gTeam.getId();
        Team team = this.mainScoreboard.getTeam(teamID);
        if (team == null) {
            team = this.mainScoreboard.registerNewTeam(teamID);
        }
        team.setPrefix(Utils.getColString(gTeam.getPrefix()));
        team.setSuffix(Utils.getColString(gTeam.getSuffix()));

        ChatColor color;
        try {
            color = ChatColor.valueOf(gTeam.getColor().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignore) {
            color = ChatColor.GRAY;
        }
        team.setColor(color);
        Message.TEAM_LOADED.log(name);
    }

}
