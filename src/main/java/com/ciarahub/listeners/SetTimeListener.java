package com.ciarahub.listeners;

import com.ciarahub.CiaraHub;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class SetTimeListener implements Listener {
    private final CiaraHub main;
    private final World world;

    public SetTimeListener(CiaraHub main, World world) {
        this.main = main;
        this.world = world;
    }

    public void start() {
        (new BukkitRunnable() {
            public void run() {
                SetTimeListener.this.world.setTime(6000L);
            }
        }).runTaskTimer(this.main, 0L, 20L);
    }
}
