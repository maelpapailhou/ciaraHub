package com.ciarahub.listeners;

import com.ciarahub.CiaraHub;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocationListener implements Listener {
    private final CiaraHub main;

    public PlayerSpawnLocationListener(CiaraHub main) {
        this.main = main;
    }

    @EventHandler
    public void spawnLocation(PlayerSpawnLocationEvent e) {
        Player player = e.getPlayer();
        FileConfiguration config = this.main.getConfig();
        double x = config.getDouble("spawn-coordinates.x");
        double y = config.getDouble("spawn-coordinates.y");
        double z = config.getDouble("spawn-coordinates.z");
        float yaw = (float)config.getDouble("spawn-coordinates.yaw");
        float pitch = (float)config.getDouble("spawn-coordinates.pitch");
        Location spawnLocation = new Location(player.getWorld(), x, y, z, yaw, pitch);
        e.setSpawnLocation(spawnLocation);
    }
}
