package com.ciarahub.listeners;

import com.ciarahub.CiaraHub;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoidToSpawnListener implements Listener {
    private CiaraHub main;
    private Map<UUID, Integer> voidCountMap = new HashMap();
    private Map<UUID, Long> lastVoidTimeMap = new HashMap();
    private long voidCooldownMillis = 300000L;

    public VoidToSpawnListener(CiaraHub main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (event.getTo() != null && event.getTo().getY() < 20.0) {
            long currentTime = System.currentTimeMillis();
            long lastVoidTime = (Long)this.lastVoidTimeMap.getOrDefault(playerUUID, 0L);
            if (currentTime - lastVoidTime >= this.voidCooldownMillis) {
                this.voidCountMap.put(playerUUID, 1);
                this.lastVoidTimeMap.put(playerUUID, currentTime);
            } else {
                int voidCount = (Integer)this.voidCountMap.getOrDefault(playerUUID, 0) + 1;
                this.voidCountMap.put(playerUUID, voidCount);
                if (voidCount == 2) {
                    player.sendMessage(ChatColor.GRAY + "Vous ne pouvez pas vous suicider.");
                } else if (voidCount == 3) {
                    player.sendMessage(ChatColor.GRAY + "Restez avec nous " + ChatColor.LIGHT_PURPLE + "❤" + ChatColor.GRAY + ".");
                }
            }

            FileConfiguration config = this.main.getConfig();
            double x = config.getDouble("spawn-coordinates.x");
            double y = config.getDouble("spawn-coordinates.y") + 50.0;
            double z = config.getDouble("spawn-coordinates.z");
            float yaw = (float)config.getDouble("spawn-coordinates.yaw");
            float pitch = (float)config.getDouble("spawn-coordinates.pitch");
            Location newLocation = new Location(player.getWorld(), x, y, z, yaw, pitch);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.teleport(newLocation);
            player.playSound(newLocation, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);
            player.setAllowFlight(false);
            player.setFlying(false);
        }

    }
}
