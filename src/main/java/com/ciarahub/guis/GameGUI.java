package com.ciarahub.guis;

import com.ciarahub.CiaraHub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameGUI implements Listener {
    private final FileConfiguration config;

    public GameGUI(CiaraHub plugin) {
        this.config = plugin.getGuiConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getLogger().info("[CiaraHub] GameGUI initialisé.");
    }

    private ItemStack createItem(String material, String name, List<String> lore) {
        try {
            Material mat = Material.valueOf(material.toUpperCase());
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            meta.setLore(lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
            item.setItemMeta(meta);
            return item;
        } catch (Exception e) {
            Bukkit.getLogger().severe("[CiaraHub] Erreur lors de la création de l'item : " + e.getMessage());
            return null;
        }
    }

    public void openInventory(Player player) {
        try {
            String title = ChatColor.translateAlternateColorCodes('&', config.getString("gui.title"));
            int size = config.getInt("gui.size");
            Inventory inventory = Bukkit.createInventory(null, size, title);

            List<Map<?, ?>> itemsList = config.getMapList("gui.items");
            for (Map<?, ?> itemData : itemsList) {
                int slot = (Integer) itemData.get("slot");
                String material = (String) itemData.get("material");
                String name = (String) itemData.get("name");
                List<String> lore = (List<String>) itemData.get("lore");
                ItemStack item = createItem(material, name, lore);
                if (item != null) {
                    inventory.setItem(slot, item);
                }
            }

            player.openInventory(inventory);
            Bukkit.getLogger().info("[CiaraHub] Inventaire ouvert pour le joueur " + player.getName());
        } catch (Exception e) {
            Bukkit.getLogger().severe("[CiaraHub] Erreur lors de l'ouverture de l'inventaire : " + e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK ||
                event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.COMPASS) {
                Bukkit.getLogger().info("[CiaraHub] Joueur " + player.getName() + " a cliqué avec une boussole.");
                openInventory(player);
                event.setCancelled(true); // Pour empêcher les interactions par défaut de la boussole
            }
        }
    }
}
