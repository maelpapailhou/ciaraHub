package com.ciarahub.guis;

import com.ciarahub.CiaraHub;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

import java.util.HashMap;
import java.util.Map;

public class OpenItemGUI implements Listener {
    private final CiaraHub plugin;
    private final FileConfiguration config;
    private final Map<Material, String> openItemToGuiMap;

    public OpenItemGUI(CiaraHub plugin) {
        this.plugin = plugin;
        this.config = plugin.getGuiConfig();
        this.openItemToGuiMap = new HashMap<>();
        initializeConfigData();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void initializeConfigData() {
        Bukkit.getLogger().info("[CiaraHub] Initialisation de la configuration des items pour ouvrir les GUIs.");
        config.getConfigurationSection("").getKeys(false).forEach(guiName -> {
            String openItem = config.getString(guiName + ".openitem");
            if (openItem != null) {
                Material material = Material.getMaterial(openItem.toUpperCase());
                if (material != null) {
                    openItemToGuiMap.put(material, guiName);
                    Bukkit.getLogger().info("[CiaraHub] Item '" + openItem + "' configuré pour ouvrir le GUI '" + guiName + "'.");
                } else {
                    Bukkit.getLogger().warning("[CiaraHub] Material '" + openItem + "' non reconnu pour le GUI '" + guiName + "'.");
                }
            }
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK ||
                event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) &&
                event.getItem() != null && openItemToGuiMap.containsKey(event.getItem().getType())) {
            String guiName = openItemToGuiMap.get(event.getItem().getType());
            Bukkit.getLogger().info("[CiaraHub] Joueur " + player.getName() + " a cliqué avec '" + event.getItem().getType() + "' pour ouvrir '" + guiName + "'.");
            new ManagerGUI(plugin).openInventory(player, guiName);
            event.setCancelled(true);
        }
    }
}
