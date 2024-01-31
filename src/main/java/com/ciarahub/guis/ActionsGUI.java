package com.ciarahub.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.ciarahub.CiaraHub;

import java.util.List;
import java.util.Map;

public class ActionsGUI {
    public static void handleActions(Player player, List<Map<String, String>> actions, CiaraHub plugin) {
        Bukkit.getLogger().info("Traitement des actions pour le joueur " + player.getName());
        for (Map<String, String> action : actions) {
            for (String key : action.keySet()) {
                String value = action.get(key);
                if (value.startsWith("open:")) {
                    player.closeInventory(); // Fermer l'inventaire actuel
                    String guiName = value.substring(5);
                    Bukkit.getLogger().info("Ouverture du GUI '" + guiName + "' pour " + player.getName());
                    Bukkit.getScheduler().runTaskLater(plugin, () -> new ManagerGUI(plugin).openInventory(player, guiName), 1L);
                }
            }
        }
    }
}
