package com.ciarahub;

import com.ciarahub.commands.debugCommands.GetRankCommand;
import com.ciarahub.commands.SpawnCommand;
import com.ciarahub.commands.TimeCommand;
import com.ciarahub.commands.debugCommands.TestGuiCommand;
import com.ciarahub.databases.ConnectDatabase;
import com.ciarahub.databases.RankDatabase;
import com.ciarahub.guis.ActionsGUI;
import com.ciarahub.guis.OpenItemGUI;
import com.ciarahub.listeners.*;
import com.ciarahub.managers.BossBarManager;
import com.ciarahub.managers.ScoreBoardManager;
import com.ciarahub.managers.TimeManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    private File guiFile;
    private FileConfiguration guiConfig;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("[CiaraHub] Activation du plugin...");

        this.saveDefaultConfig();
        initGuiConfig();

        connectDatabase = new ConnectDatabase();
        if (connectDatabase.isConnected()) {
            System.out.println("[CiaraHub] Connecté à la base de données.");
            String ranksFilePath = this.getDataFolder().getAbsolutePath() + File.separator + "ranks.yml";
            rankDatabase = new RankDatabase(connectDatabase.getConnection(), ranksFilePath);
            this.getCommand("getrank").setExecutor(new GetRankCommand(connectDatabase.getConnection()));
        } else {
            System.out.println("[CiaraHub] Impossible de se connecter à la base de données.");
        }

        this.getCommand("spawn").setExecutor(new SpawnCommand(this));
        this.getCommand("t").setExecutor(new TimeCommand());
        this.getCommand("testgui").setExecutor(new TestGuiCommand(this));

        registerListeners();

        bossBarManager = new BossBarManager(this);
        scoreBoardManager = new ScoreBoardManager(rankDatabase);

        Bukkit.getServer().setDefaultGameMode(GameMode.SURVIVAL);
        TimeManager timeManager = new TimeManager(this, (World)this.getServer().getWorlds().get(0));
        timeManager.start();
        this.getServer().getPluginManager().registerEvents(this, this);
        System.out.println("[CiaraHub] Plugin activé avec succès !");
    }

    private void initGuiConfig() {
        guiFile = new File(getDataFolder(), "gui.yml");
        if (!guiFile.exists()) {
            System.out.println("[CiaraHub] Fichier gui.yml introuvable, création en cours...");
            saveResource("gui.yml", false);
        }
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);
        System.out.println("[CiaraHub] Configuration gui.yml chargée.");
    }

    public FileConfiguration getGuiConfig() {
        return this.guiConfig;
    }

    public void reloadGuiConfig() {
        if (guiFile == null) {
            guiFile = new File(getDataFolder(), "gui.yml");
        }
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);
        System.out.println("[CiaraHub] Configuration gui.yml rechargée.");
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
        this.registerEvent(new OpenItemGUI(this));

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

    @Override
    public void onDisable() {
        if (connectDatabase != null && connectDatabase.isConnected()) {
            connectDatabase.disconnectFromDatabase();
            System.out.println("[CiaraHub] Déconnecté de la base de données.");
        }
        System.out.println("[CiaraHub] Désactivation du plugin...");
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
