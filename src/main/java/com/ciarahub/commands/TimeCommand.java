package com.ciarahub.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {
    public TimeCommand() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            long time = player.getWorld().getTime();
            long days = time / 24000L;
            long hours = time % 24000L / 1000L;
            long minutes = time % 24000L % 1000L * 60L / 1000L;
            player.sendMessage("Il est actuellement " + days + " jour(s), " + hours + " heure(s), et " + minutes + " minute(s).");
        }

        return true;
    }
}
