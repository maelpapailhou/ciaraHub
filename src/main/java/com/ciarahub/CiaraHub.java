package com.ciarahub;

import com.ciarahub.commands.SpawnCommand;
import com.ciarahub.commands.TimeCommand;
import com.ciarahub.listeners.*;
import com.ciarahub.managers.BossBarManager;
import com.ciarahub.managers.TimeManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CiaraHub extends JavaPlugin implements Listener {
    private static CiaraHub instance;
    private BossBarManager bossBarManager;

    public CiaraHub() {
    }

    public void onEnable() {
        instance = this;
        System.out.println("[CiaraSpawn] Activation...");
        this.getCommand("spawn").setExecutor(new SpawnCommand(this));
        this.getCommand("t").setExecutor(new TimeCommand());

        this.registerEvent(new PlayerJoinListener());
        this.registerEvent(new PlayerDropItemListener());
        this.registerEvent(new BlockInteractionListener());
        this.registerEvent(new PlayerSpawnLocationListener(this));
        this.registerEvent(new FullHealthListener());
        this.registerEvent(new FullFoodListener());
        this.registerEvent(new VoidToSpawnListener(this));
        this.registerEvent(new ItemSpawnListener(this));
        this.registerEvent(new InventoryProtectionListener());
        this.registerEvent(new ItemJoinListener(this));
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        this.bossBarManager = new BossBarManager(this);
        Bukkit.getServer().setDefaultGameMode(GameMode.SURVIVAL);
        TimeManager timeManager = new TimeManager(this, (World)this.getServer().getWorlds().get(0));
        timeManager.start();
        this.getServer().getPluginManager().registerEvents(this, this);
        System.out.println("[CiaraSpawn] Activé avec succès !");
    }

    public void addPlayer(Player player) {
        if (this.bossBarManager != null) {
            this.bossBarManager.addPlayer(player);
        } else {
            System.out.println("[CiaraSpawn] ERREUR : bossBarManager est null lors de l'ajout du joueur.");
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        this.addPlayer(player);
    }

    public void onDisable() {
        System.out.println("[CiaraSpawn] Désactivation...");
    }

    public static CiaraHub getInstance() {
        return instance;
    }

    private void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
}
