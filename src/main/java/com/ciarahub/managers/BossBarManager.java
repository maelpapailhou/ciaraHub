package com.ciarahub.managers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BossBarManager implements Listener {
    private final JavaPlugin plugin;
    private BossBar bossBar;
    private List<BarMessage> barMessages;
    private int currentIndex;
    private BukkitRunnable currentTask;

    public BossBarManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.barMessages = new ArrayList();
        this.currentIndex = 0;
        this.currentTask = null;
        this.initializeBarMessages();
        if (this.barMessages.isEmpty()) {
            plugin.getLogger().severe("Aucun message défini pour la BossBar.");
        } else {
            this.bossBar = this.createBossBar((BarMessage)this.barMessages.get(0));
            this.scheduleNextMessage();
        }
    }

    private void initializeBarMessages() {
        this.barMessages.add(new BarMessage(ChatColor.AQUA + "Vous jouez sur " + ChatColor.BOLD + "CiaraCube.fr", BarColor.BLUE, 100, BossBarManager.BarType.DYNAMIC, true));
        this.barMessages.add(new BarMessage(ChatColor.YELLOW + "Grades, particules et montures sur" + ChatColor.BOLD + "/BOUTIQUE", BarColor.YELLOW, 100, BossBarManager.BarType.STATIC, true));
    }

    public void addPlayer(Player player) {
        if (this.bossBar != null) {
            this.bossBar.addPlayer(player);
        }

    }

    public void removePlayer(Player player) {
        if (this.bossBar != null) {
            this.bossBar.removePlayer(player);
        }

    }

    private BossBar createBossBar(BarMessage barMessage) {
        return Bukkit.createBossBar(barMessage.text, barMessage.barColor, BarStyle.SOLID, new BarFlag[0]);
    }

    private void scheduleNextMessage() {
        if (this.currentTask != null && !this.currentTask.isCancelled()) {
            this.currentTask.cancel();
        }

        this.currentTask = new BukkitRunnable() {
            public void run() {
                if (BossBarManager.this.currentIndex >= BossBarManager.this.barMessages.size()) {
                    BossBarManager.this.currentIndex = 0;
                }

                if (!BossBarManager.this.barMessages.isEmpty()) {
                    BarMessage currentMessage = (BarMessage)BossBarManager.this.barMessages.get(BossBarManager.this.currentIndex);
                    BossBarManager.this.animateBossBar(currentMessage);
                    BossBarManager.this.currentIndex = (BossBarManager.this.currentIndex + 1) % BossBarManager.this.barMessages.size();
                }

                BossBarManager.this.scheduleNextMessage();
            }
        };
        if (!this.barMessages.isEmpty()) {
            BarMessage nextMessage = (BarMessage)this.barMessages.get(this.currentIndex);
            this.currentTask.runTaskLater(this.plugin, (long)(nextMessage.duration + 20));
        }

    }

    private void animateBossBar(final BarMessage barMessage) {
        this.bossBar.setTitle(barMessage.text);
        this.bossBar.setColor(barMessage.barColor);
        if (barMessage.barType == BossBarManager.BarType.DYNAMIC) {
            (new BukkitRunnable() {
                double progress;
                long startTime;
                long duration;

                {
                    this.progress = barMessage.fillIncreasing ? 0.0 : 1.0;
                    this.startTime = System.currentTimeMillis();
                    this.duration = (long)(barMessage.duration * 50);
                }

                public void run() {
                    long elapsedTime = System.currentTimeMillis() - this.startTime;
                    double fraction = (double)elapsedTime / (double)this.duration;
                    if (fraction > 1.0) {
                        fraction = 1.0;
                        this.cancel();
                    }

                    this.progress = barMessage.fillIncreasing ? fraction : 1.0 - fraction;
                    BossBarManager.this.bossBar.setProgress(this.progress);
                }
            }).runTaskTimer(this.plugin, 0L, 1L);
        } else if (barMessage.barType == BossBarManager.BarType.STATIC) {
            this.bossBar.setProgress(barMessage.fillIncreasing ? 1.0 : 0.0);
        }

    }

    public static class BarMessage {
        public final String text;
        public final BarColor barColor;
        public final int duration;
        public final BarType barType;
        public final boolean fillIncreasing;

        public BarMessage(String text, BarColor barColor, int duration, BarType barType, boolean fillIncreasing) {
            this.text = text;
            this.barColor = barColor;
            this.duration = duration;
            this.barType = barType;
            this.fillIncreasing = fillIncreasing;
        }
    }

    public static enum BarType {
        DYNAMIC,
        STATIC;

        private BarType() {
        }
    }
}
