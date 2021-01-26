package com.shanebeestudios.gt.data;

import com.shanebeestudios.gt.util.Utils;
import org.bukkit.command.CommandSender;

public class Message {

    // System loading messages
    public static final Message LOADING_INSTANCE = get("&cCan not create another instance of this plugin... shutting down!");
    public static final Message LOADING_PLUGIN_START = get("&7Loading plugin...");
    public static final Message LOADING_PLUGIN_FINISH = get("&7Finished loading in &b%.2f seconds");
    public static final Message TEAM_LOADED = get("&aLoaded team &7'&b%s&7'");

    // Config messages
    public static final Message CONFIG_LOADED = get("&7config.yml loaded");

    private static Message get(String string) {
        return new Message(string);
    }

    private final String message;

    public Message(String message) {
        this.message = message;
    }

    public void send(CommandSender receiver, Object... params) {
        Utils.sendMessage(receiver, message, params);
    }

    public void log(Object... params) {
        Utils.log(message, params);
    }

}
