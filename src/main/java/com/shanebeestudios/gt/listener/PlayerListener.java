package com.shanebeestudios.gt.listener;

import com.shanebeestudios.gt.GeyserTeams;
import com.shanebeestudios.gt.config.Config;
import com.shanebeestudios.gt.data.GTeam;
import com.shanebeestudios.gt.util.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final GeyserTeams plugin;

    public PlayerListener(GeyserTeams plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getTeamManager().joinPlayerToTeam(player);

        if (!Config.OPTIONS_JOIN_ENABLED) return;
        String format = getFormat(player, Config.OPTIONS_JOIN_FORMAT);
        if (format != null) {
            event.setJoinMessage(Utils.getColString(format));
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        if (!Config.OPTIONS_QUIT_ENABLED) return;
        String format = getFormat(event.getPlayer(), Config.OPTIONS_QUIT_FORMAT);
        if (format != null) {
            event.setQuitMessage(Utils.getColString(format));
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || !Config.OPTIONS_CHAT_ENABLED) return;

        Player player = event.getPlayer();
        String message = event.getMessage();

        String format = getFormat(player, Config.OPTIONS_CHAT_FORMAT);
        if (format != null) {
            format = format.replace("<message>", ChatColor.stripColor(Utils.getColString(message)));
            event.setFormat(Utils.getColString(format));
        }
    }

    @SuppressWarnings("deprecation")
    private String getFormat(Player player, String format) {
        GTeam gTeam = plugin.getTeamManager().getTeam(player);
        if (gTeam == null) {
            return null;
        }
        String form = format;
        form = form.replace("<player>", player.getDisplayName());
        form = form.replace("<prefix>", gTeam.getPrefix());
        form = form.replace("<suffix>", gTeam.getSuffix());
        return form;
    }

}
