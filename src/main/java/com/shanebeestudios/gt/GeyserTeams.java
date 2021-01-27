package com.shanebeestudios.gt;

import com.shanebeestudios.gt.config.Config;
import com.shanebeestudios.gt.data.GTeams;
import com.shanebeestudios.gt.data.Message;
import com.shanebeestudios.gt.listener.PlayerListener;
import com.shanebeestudios.gt.manager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GeyserTeams extends JavaPlugin {

    private static GeyserTeams instance;
    private GTeams gTeams;
    private Config pluginConfig;
    private TeamManager teamManager;

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        // We do not want to create a new instance of this plugin
        if (instance != null) {
            Message.LOADING_INSTANCE.log();
            pluginManager.disablePlugin(this);
            return;
        }

        if (pluginManager.getPlugin("floodgate-bukkit") == null) {
            Message.LOADING_NO_FLOODGATE.log();
            pluginManager.disablePlugin(this);
            return;
        }

        Message.LOADING_PLUGIN_START.log();
        instance = this;
        long start = System.currentTimeMillis();

        loadPlugin();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        float end = (float) (System.currentTimeMillis() - start) / 1000;
        Message.LOADING_PLUGIN_FINISH.log(end);
    }

    @Override
    public void onDisable() {
        unloadPlugin();
        instance = null;
    }

    private void loadPlugin() {
        this.gTeams = new GTeams();
        this.pluginConfig = new Config(this);
        this.teamManager = new TeamManager(this);
    }

    private void unloadPlugin() {
        this.pluginConfig = null;
    }

    public void reloadPlugin(@NotNull CommandSender receiver) {
        Message.RELOADING.send(receiver);
        long start = System.currentTimeMillis();

        unloadPlugin();
        loadPlugin();

        float end = (float) (System.currentTimeMillis() - start) / 1000;
        Message.LOADING_PLUGIN_FINISH.send(receiver, end);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reloadPlugin(sender);
        return true;
    }

    public static GeyserTeams getInstance() {
        return instance;
    }

    public GTeams getGTeams() {
        return gTeams;
    }

    public Config getPluginConfig() {
        return pluginConfig;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }
}
