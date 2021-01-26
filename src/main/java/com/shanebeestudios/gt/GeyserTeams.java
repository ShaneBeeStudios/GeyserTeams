package com.shanebeestudios.gt;

import com.shanebeestudios.gt.config.Config;
import com.shanebeestudios.gt.data.GTeams;
import com.shanebeestudios.gt.data.Message;
import com.shanebeestudios.gt.listener.PlayerListener;
import com.shanebeestudios.gt.manager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GeyserTeams extends JavaPlugin {

    private static GeyserTeams instance;
    private GTeams gTeams;
    private Config pluginConfig;
    private TeamManager teamManager;

    @Override
    public void onEnable() {
        // We do not want to create a new instance of this plugin
        if (instance != null) {
            Message.LOADING_INSTANCE.log();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Message.LOADING_PLUGIN_START.log();
        instance = this;
        long start = System.currentTimeMillis();

        this.gTeams = new GTeams();
        this.pluginConfig = new Config(this);
        this.teamManager = new TeamManager(this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        float end = (float) (System.currentTimeMillis() - start) / 1000;
        Message.LOADING_PLUGIN_FINISH.log(end);
    }

    @Override
    public void onDisable() {
        //stuff
        this.pluginConfig = null;
        instance = null;
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

}
