package com.ciarahub;

import com.ciarahub.commands.debugCommands.GetRankCommand;
import com.ciarahub.commands.SpawnCommand;
import com.ciarahub.commands.TimeCommand;
import com.ciarahub.databases.ConnectDatabase;
import com.ciarahub.databases.RankDatabase;
import com.ciarahub.listeners.*;
import com.ciarahub.managers.BossBarManager;
import com.ciarahub.managers.ScoreBoardManager;
import com.ciarahub.managers.TimeManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CiaraHub extends JavaPlugin implements Listener {
    private static CiaraHub instance;
    private BossBarManager bossBarManager;
    private ScoreBoardManager scoreBoardManager;
    private ConnectDatabase connectDatabase;
    private RankDatabase rankDatabase;

    public CiaraHub() {
    }

    public void onEnable() {
        instance = this;
        System.out.println("[CiaraHub] Activation...");

        // Sauvegarde de la configuration par défaut
        this.saveDefaultConfig();

        // Connexion à la base de données
        connectDatabase = new ConnectDatabase();
        if (connectDatabase.isConnected()) {
            System.out.println("[CiaraHub] Connecté à la base de données.");
            // Obtenez le chemin du fichier de configuration des rangs
            String ranksFilePath = this.getDataFolder().getAbsolutePath() + File.separator + "ranks.yml";
            rankDatabase = new RankDatabase(connectDatabase.getConnection(), ranksFilePath);

            // Utilisez la connexion de connectDatabase ici
            this.getCommand("getrank").setExecutor(new GetRankCommand(connectDatabase.getConnection()));
        } else {
            System.out.println("[CiaraHub] Impossible de se connecter à la base de données.");
        }
        // Initialisation des commandes et listeners
        this.getCommand("spawn").setExecutor(new SpawnCommand(this));
        this.getCommand("t").setExecutor(new TimeCommand());
        registerListeners();

        // Initialisation du BossBarManager et du ScoreBoardManager
        this.bossBarManager = new BossBarManager(this);
        this.scoreBoardManager = new ScoreBoardManager(rankDatabase);

        // Réglages supplémentaires
        Bukkit.getServer().setDefaultGameMode(GameMode.SURVIVAL);
        TimeManager timeManager = new TimeManager(this, (World)this.getServer().getWorlds().get(0));
        timeManager.start();
        this.getServer().getPluginManager().registerEvents(this, this);
        System.out.println("[CiaraHub] Activé avec succès !");
    }

    private void registerListeners() {
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
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        this.addPlayer(player);
        this.scoreBoardManager.createScoreboard(player);
    }

    public void addPlayer(Player player) {
        if (this.bossBarManager != null) {
            this.bossBarManager.addPlayer(player);
        } else {
            System.out.println("[CiaraHub] ERREUR : bossBarManager est null lors de l'ajout du joueur.");
        }
    }

    public void onDisable() {
        if (connectDatabase != null && connectDatabase.isConnected()) {
            connectDatabase.disconnectFromDatabase();
            System.out.println("[CiaraHub] Déconnecté de la base de données.");
        }
        System.out.println("[CiaraHub] Désactivation...");
    }

    public static CiaraHub getInstance() {
        return instance;
    }

    private void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    // Getters pour les managers
    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public ScoreBoardManager getScoreBoardManager() {
        return scoreBoardManager;
    }
}
