package com.shanebeestudios.gt.config;

import com.shanebeestudios.gt.GeyserTeams;
import com.shanebeestudios.gt.data.GTeam;
import com.shanebeestudios.gt.data.GTeams;
import com.shanebeestudios.gt.data.Message;
import com.shanebeestudios.gt.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {

    private final GeyserTeams plugin;
    private File configFile;
    private FileConfiguration config;

    public Config(GeyserTeams plugin) {
        this.plugin = plugin;
        loadConfigFile();
    }

    private void loadConfigFile() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        matchConfig(config, configFile);
        loadConfig();
        Message.CONFIG_LOADED.log();
    }

    // Used to update config
    @SuppressWarnings("ConstantConditions")
    private void matchConfig(FileConfiguration config, File file) {
        try {
            boolean hasUpdated = false;
            InputStream test = plugin.getResource(file.getName());
            assert test != null;
            InputStreamReader is = new InputStreamReader(test);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(is);
            for (String key : defConfig.getConfigurationSection("").getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, defConfig.get(key));
                    hasUpdated = true;
                }
            }
            for (String key : config.getConfigurationSection("").getKeys(true)) {
                if (!defConfig.contains(key)) {
                    config.set(key, null);
                    hasUpdated = true;
                }
            }
            if (hasUpdated)
                config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        Utils.setPrefix(config.getString("messages.prefix"));
        loadTeams();
    }

    private void loadTeams() {
        ConfigurationSection teams = config.getConfigurationSection("teams");
        if (teams == null) {
            throw new IllegalArgumentException("");
        }
        GTeams gTeams = plugin.getGTeams();
        teams.getKeys(false).forEach(key -> {
            String prefix = teams.getString(key + ".prefix");
            String suffix = teams.getString(key + ".suffix");
            String color = teams.getString(key + ".color");
            int priority = teams.getInt(key + ".priority");
            GTeam gTeam = new GTeam(key, prefix, suffix, color, priority);
            gTeams.setTeam(key, gTeam);
        });
    }

}
