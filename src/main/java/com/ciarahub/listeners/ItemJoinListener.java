package com.ciarahub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemJoinListener implements Listener {
    private final JavaPlugin plugin;

    public ItemJoinListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoinItem(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Iterator var3 = this.plugin.getConfig().getConfigurationSection("join-items").getKeys(false).iterator();

        while(true) {
            int slot;
            ItemStack itemStack;
            while(true) {
                if (!var3.hasNext()) {
                    return;
                }

                String itemKey = (String)var3.next();
                String itemName = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("join-items." + itemKey + ".name"));
                List<String> lore = this.plugin.getConfig().getStringList("join-items." + itemKey + ".lore");
                List<String> coloredLore = new ArrayList();
                Iterator var8 = lore.iterator();

                while(var8.hasNext()) {
                    String line = (String)var8.next();
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }

                slot = this.plugin.getConfig().getInt("join-items." + itemKey + ".slot");
                if (itemKey.equals("item2")) {
                    itemStack = this.createPlayerHead(player, itemName, coloredLore);
                    break;
                }

                Material material = Material.getMaterial(this.plugin.getConfig().getString("join-items." + itemKey + ".material"));
                if (material != null) {
                    itemStack = this.createItem(material, itemName, coloredLore);
                    break;
                }
            }

            player.getInventory().setItem(slot, itemStack);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null) {
            String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            String command;
            if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    command = this.plugin.getConfig().getString("join-items." + itemName + ".left_click_command");
                    if (command != null && !command.isEmpty()) {
                        Bukkit.dispatchCommand(player, command);
                    }
                }
            } else {
                command = this.plugin.getConfig().getString("join-items." + itemName + ".right_click_command");
                if (command != null && !command.isEmpty()) {
                    Bukkit.dispatchCommand(player, command);
                }
            }
        }

    }

    private ItemStack createItem(Material material, String itemName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE});
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack createPlayerHead(Player player, String itemName, List<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
        skullMeta.setDisplayName(itemName);
        skullMeta.setLore(lore);
        skullMeta.setOwningPlayer(player);
        item.setItemMeta(skullMeta);
        return item;
    }
}
