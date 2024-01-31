package com.ciarahub.guis;

import com.ciarahub.CiaraHub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerGUI implements Listener {
    private final CiaraHub plugin;
    private final FileConfiguration config;
    private final Map<String, Integer> guiSizes;
    private final Map<String, String> guiTitles;
    private final Map<String, List<ItemStack>> guiItems;

    public ManagerGUI(CiaraHub plugin) {
        this.plugin = plugin;
        this.config = plugin.getGuiConfig();
        this.guiSizes = new HashMap<>();
        this.guiTitles = new HashMap<>();
        this.guiItems = new HashMap<>();
        initializeConfigData();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void initializeConfigData() {
        for (String guiName : config.getKeys(false)) {
            String title = config.getString(guiName + ".title", "Default Title");
            int size = config.getInt(guiName + ".size", 3) * 9;
            guiTitles.put(guiName, title);
            guiSizes.put(guiName, size);

            List<ItemStack> items = new ArrayList<>();
            List<Map<?, ?>> itemsConfig = config.getMapList(guiName + ".items");
            for (Map<?, ?> itemData : itemsConfig) {
                items.add(createItemFromConfig(itemData));
            }
            guiItems.put(guiName, items);
        }
    }

    private ItemStack createItemFromConfig(Map<?, ?> itemData) {
        String materialName = "STONE";
        if (itemData.containsKey("material") && itemData.get("material") instanceof String) {
            materialName = (String) itemData.get("material");
        }
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            material = Material.STONE;
        }

        String name = "";
        if (itemData.containsKey("name") && itemData.get("name") instanceof String) {
            name = (String) itemData.get("name");
        }

        List<String> lore = new ArrayList<>();
        if (itemData.get("lore") instanceof List<?>) {
            for (Object line : (List<?>) itemData.get("lore")) {
                if (line instanceof String) {
                    lore.add((String) line);
                }
            }
        }

        return createItem(material, name, lore);
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            List<String> coloredLore = new ArrayList<>();
            for (String line : lore) {
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(coloredLore);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || inventory.getHolder() != null) return;

        String title = "";
        if (!inventory.getViewers().isEmpty()) {
            title = inventory.getViewers().get(0).getOpenInventory().getTitle();
        }

        for (Map.Entry<String, String> entry : guiTitles.entrySet()) {
            if (entry.getValue().equals(title)) {
                event.setCancelled(true);
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.hasItemMeta()) {
                    // Handle item click actions here
                }
                break;
            }
        }
    }

    public void openInventory(Player player, String guiName) {
        String title = guiTitles.getOrDefault(guiName, "Default Inventory");
        int size = guiSizes.getOrDefault(guiName, 27);
        Inventory inventory = Bukkit.createInventory(null, size, title);

        List<ItemStack> items = guiItems.getOrDefault(guiName, new ArrayList<>());
        for (ItemStack item : items) {
            inventory.addItem(item);
        }

        player.openInventory(inventory);
    }
}
