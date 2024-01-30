package com.ciarahub.commands.debugCommands;

import com.ciarahub.CiaraHub;
import com.ciarahub.guis.GameGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestGuiCommand implements CommandExecutor {

    private final CiaraHub plugin;

    public TestGuiCommand(CiaraHub plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent exécuter cette commande.");
            return true;
        }

        Player player = (Player) sender;
        GameGUI gameGUI = new GameGUI(plugin);
        gameGUI.openInventory(player);

        return true;
    }
}
