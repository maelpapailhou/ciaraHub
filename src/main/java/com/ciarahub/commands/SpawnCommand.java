package com.ciarahub.commands;

import com.ciarahub.CiaraHub;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class SpawnCommand implements CommandExecutor {
    private CiaraHub main;
    private Map<String, Long> lastSpawnTime;

    public SpawnCommand(CiaraHub main) {
        this.main = main;
        this.lastSpawnTime = new HashMap();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            long currentTime = System.currentTimeMillis();
            if (this.lastSpawnTime.containsKey(player.getName()) && currentTime - (Long)this.lastSpawnTime.get(player.getName()) < 2000L) {
                return true;
            }

            this.lastSpawnTime.put(player.getName(), currentTime);
            FileConfiguration config = this.main.getConfig();
            double x = config.getDouble("spawn-coordinates.x");
            double y = config.getDouble("spawn-coordinates.y");
            double z = config.getDouble("spawn-coordinates.z");
            float yaw = (float)config.getDouble("spawn-coordinates.yaw");
            float pitch = (float)config.getDouble("spawn-coordinates.pitch");
            Location spawnLocation = new Location(player.getWorld(), x, y, z, yaw, pitch);
            if (player.getLocation().distanceSquared(spawnLocation) < 1.0) {
                player.sendMessage(ChatColor.GRAY + "Vous êtes déjà au spawn.");
                return true;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1, false, true, false));
            player.teleport(spawnLocation);
            player.getWorld().playSound(spawnLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }

        return true;
    }
}
