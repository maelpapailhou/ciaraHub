package com.ciarahub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
    public PlayerDropItemListener() {
    }

    @EventHandler
    public void itemThrownEvent(PlayerDropItemEvent throwevent) {
        throwevent.setCancelled(false);
    }
}

