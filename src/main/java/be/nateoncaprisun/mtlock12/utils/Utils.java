package be.nateoncaprisun.mtlock12.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Utils {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static void sendMessage(Player player, String input) {
        player.sendMessage(color(input));
    }
    public static boolean isDouble(String s) {
        boolean amIValid;
        try {
            Double.parseDouble(s);
            amIValid = true;
        } catch (NumberFormatException e) {
            amIValid = false;
        }
        return amIValid;
    }
}
