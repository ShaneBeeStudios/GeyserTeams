package com.shanebeestudios.gt.manager;

import com.shanebeestudios.gt.GeyserTeams;
import com.shanebeestudios.gt.data.GTeam;
import com.shanebeestudios.gt.data.GTeams;
import com.shanebeestudios.gt.data.Message;
import com.shanebeestudios.gt.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.geysermc.floodgate.FloodgateAPI;
import org.geysermc.floodgate.util.DeviceOS;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TeamManager {

    private final GeyserTeams PLUGIN;
    private final Scoreboard MAIN_BOARD;
    private final Map<UUID, GTeam> TEAMS;

    public TeamManager(GeyserTeams plugin) {
        this.PLUGIN = plugin;
        this.MAIN_BOARD = plugin.getServer().getScoreboardManager().getMainScoreboard();
        this.TEAMS = new HashMap<>();

        registerTeams();

        // If the plugin/server is reloaded and player's are online, reassign teams
        reloadPlayers();
    }

    private void registerTeams() {
        // TODO this is just for testing
        this.MAIN_BOARD.getTeams().forEach(team -> {
            if (team.getName().startsWith("gt-")) {
                team.unregister();
            }
        });

        // Register Geyser teams based on devices
        GTeams gTeams = PLUGIN.getGTeams();
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
        Team team = this.MAIN_BOARD.getTeam(teamID);
        if (team == null) {
            team = this.MAIN_BOARD.registerNewTeam(teamID);
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

    private void reloadPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::joinPlayerToTeam);
    }

    public void joinPlayerToTeam(@NotNull Player player) {
        String playerName = player.getName();

        // Fallback team (if not using a Bedrock device)
        String teamName = "java";
        if (FloodgateAPI.isBedrockPlayer(player)) {
            DeviceOS deviceOS = FloodgateAPI.getPlayer(player).getDeviceOS();
            teamName = deviceOS.toString().toLowerCase(Locale.ROOT).replace(" ", "_");
        }

        GTeam gTeam = PLUGIN.getGTeams().getTeam(teamName);
        if (gTeam == null) {
            Message.JOIN_TEAM_NO_FOUND.log(playerName, teamName);
            return;
        }

        Team team = MAIN_BOARD.getTeam(gTeam.getId());
        if (team == null) {
            Message.JOIN_TEAM_NO_FOUND.log(playerName, teamName);
            return;
        }

        team.addEntry(playerName);
        this.TEAMS.put(player.getUniqueId(), gTeam);
    }

    @Nullable
    public GTeam getTeam(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        if (TEAMS.containsKey(uuid)) {
            return TEAMS.get(uuid);
        }
        return null;
    }

}
