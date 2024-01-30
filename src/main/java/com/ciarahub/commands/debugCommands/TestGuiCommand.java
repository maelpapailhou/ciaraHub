package com.ciarahub.commands.debugCommands;

import com.ciarahub.CiaraHub;
import com.ciarahub.guis.ManagerGUI;
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
        if (args.length == 0) {
            player.sendMessage("Veuillez spécifier un GUI à ouvrir.");
            return true;
        }

        String guiName = args[0].toLowerCase();
        if (!plugin.getGuiConfig().contains(guiName)) {
            player.sendMessage("Le GUI spécifié n'existe pas.");
            return true;
        }

        ManagerGUI gameGUI = new ManagerGUI(plugin);
        gameGUI.openInventory(player, guiName);
        return true;
    }
}
