package com.shanebeestudios.gt.listener;

import com.shanebeestudios.gt.GeyserTeams;
import com.shanebeestudios.gt.data.GTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.geysermc.floodgate.FloodgateAPI;
import org.geysermc.floodgate.util.DeviceOS;

import java.util.Locale;

public class PlayerListener implements Listener {

    private final GeyserTeams plugin;
    private final Scoreboard mainScoreboard;

    public PlayerListener(GeyserTeams plugin) {
        this.plugin = plugin;
        this.mainScoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GTeam gTeam;
        if (FloodgateAPI.isBedrockPlayer(player)) {
            DeviceOS deviceOS = FloodgateAPI.getPlayer(player).getDeviceOS();
            String name = deviceOS.toString().toLowerCase(Locale.ROOT).replace(" ", "_");
            gTeam = plugin.getGTeams().getTeam(name);
        } else {
            gTeam = plugin.getGTeams().getTeam("java");
        }
        if (gTeam != null) {
            Team team = mainScoreboard.getTeam(gTeam.getId());
            if (team != null) {
                team.addEntry(player.getName());
            }
        }
    }

}
