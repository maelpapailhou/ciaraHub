package com.ciarahub.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class TimeManager implements Runnable {
    private final World world;
    private final JavaPlugin plugin;

    public TimeManager(JavaPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    public void run() {
        this.world.setTime(6000L);
    }

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 0L, 20L);
    }
}
