package com.ciarahub.listeners;

import com.ciarahub.CiaraHub;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemSpawnListener implements Listener {
    private CiaraHub main;

    public ItemSpawnListener(CiaraHub main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        FileConfiguration config = this.main.getConfig();
        double spawnX = config.getDouble("spawn-coordinates.x");
        double spawnZ = config.getDouble("spawn-coordinates.z");
        double maxDistance = 50.0;
        double distanceX = Math.abs(playerLocation.getX() - spawnX);
        double distanceZ = Math.abs(playerLocation.getZ() - spawnZ);
        if (!(distanceX >= maxDistance) && !(distanceZ >= maxDistance)) {
            player.getInventory().setItem(1, (ItemStack)null);
        } else {
            ItemStack sunf = new ItemStack(Material.SUNFLOWER);
            ItemMeta sunfMeta = sunf.getItemMeta();
            sunfMeta.setDisplayName(ChatColor.YELLOW + "Retourner au spawn" + org.bukkit.ChatColor.GRAY + " (Appuyez)");
            sunf.setItemMeta(sunfMeta);
            player.getInventory().setItem(1, sunf);
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() == Material.SUNFLOWER && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            String command = "spawn";
            Bukkit.dispatchCommand(player, command);
        }

    }
}
