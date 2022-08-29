package com.shanebeestudios.gt.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static String PREFIX = "&7[&bGeyser&3Teams&7] "; // todo May use HEX colors
    private static final String PREFIX_CONSOLE = "&7[&bGeyser&3Teams&7] ";
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]){6}>");
    private static final String[] BUKKIT_VERSION = Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.");

    @SuppressWarnings("deprecation")
    public static String getColString(@NotNull String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        if (isRunningMinecraft(1, 16)) {

            while (matcher.find()) {
                final ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
                final String before = string.substring(0, matcher.start());
                final String after = string.substring(matcher.end());
                string = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(string);
            }
        } else {
            string = HEX_PATTERN.matcher(string).replaceAll("");
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void setPrefix(String prefix) {
        PREFIX = prefix;
    }

    public static void log(@NotNull String format, Object... objects) {
        Bukkit.getConsoleSender().sendMessage(getColString(PREFIX_CONSOLE + String.format(format, objects)));
    }

    public static void sendMessage(@NotNull CommandSender receiver, @NotNull String format, Object... objects) {
        receiver.sendMessage(getColString(PREFIX + String.format(format, objects)));
    }

    /**
     * Check if server is running a minimum Minecraft version
     *
     * @param major Major version to check (Most likely just going to be 1)
     * @param minor Minor version to check
     * @return True if running this version or higher
     */
    public static boolean isRunningMinecraft(int major, int minor) {
        return isRunningMinecraft(major, minor, 0);
    }

    /**
     * Check if server is running a minimum Minecraft version
     *
     * @param major    Major version to check (Most likely just going to be 1)
     * @param minor    Minor version to check
     * @param revision Revision to check
     * @return True if running this version or higher
     */
    public static boolean isRunningMinecraft(int major, int minor, int revision) {
        int maj = Integer.parseInt(BUKKIT_VERSION[0]);
        int min = Integer.parseInt(BUKKIT_VERSION[1]);
        int rev;
        try {
            rev = Integer.parseInt(BUKKIT_VERSION[2]);
        } catch (Exception ignore) {
            rev = 0;
        }
        return maj > major || min > minor || (min == minor && rev >= revision);
    }

}
