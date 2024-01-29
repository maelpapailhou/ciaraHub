package com.ciarahub.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    private Map<UUID, Long> lastLoginMap = new HashMap();

    public PlayerJoinListener() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage((String)null);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        this.lastLoginMap.put(playerUUID, currentTime);
        player.sendMessage(this.buildPixelArt());
    }

    private String buildPixelArt() {
        String pixelFull = ChatColor.GOLD + "█";
        String pixelTransparent = " ";
        StringBuilder pixelArt = new StringBuilder();
        char[][] pattern = new char[][]{{'A', 'A', 'A', 'B', 'B', 'B', 'B', 'B', 'A'}, {'A', 'B', 'B', 'A', 'A', 'A', 'B', 'B', 'A'}, {'A', 'B', 'A', 'A', 'A', 'A', 'A', 'B', 'A'}, {'B', 'B', 'A', 'A', 'A', 'A', 'A', 'B', 'A'}, {'B', 'B', 'A', 'A', 'A', 'A', 'A', 'A', 'A'}, {'B', 'B', 'A', 'A', 'A', 'A', 'A', 'A', 'A'}, {'A', 'B', 'B', 'A', 'A', 'A', 'A', 'A', 'A'}, {'A', 'B', 'B', 'B', 'A', 'A', 'A', 'A', 'B'}, {'A', 'A', 'A', 'B', 'B', 'B', 'B', 'B', 'A'}};
        char[][] var5 = pattern;
        int var6 = pattern.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            char[] row = var5[var7];
            char[] var9 = row;
            int var10 = row.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                char cell = var9[var11];
                pixelArt.append(cell == 'A' ? pixelTransparent : pixelFull);
            }

            pixelArt.append("\n");
        }

        return pixelArt.toString();
    }
}
