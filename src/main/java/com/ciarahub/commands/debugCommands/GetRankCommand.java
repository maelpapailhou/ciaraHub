package com.ciarahub.commands.debugCommands;

import com.ciarahub.CiaraHub;
import com.ciarahub.databases.RankDatabase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.util.UUID;

public class GetRankCommand implements CommandExecutor {

    private RankDatabase rankDatabase;

    public GetRankCommand(Connection connection) {
        try {
            // Obtenez le fichier ranks.yml à partir des ressources
            File ranksFile = new File(CiaraHub.getInstance().getDataFolder(), "ranks.yml");
            if (!ranksFile.exists()) {
                CiaraHub.getInstance().saveResource("ranks.yml", false);
            }

            this.rankDatabase = new RankDatabase(connection, ranksFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'initialisation de RankDatabase avec ranks.yml");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande ne peut être exécutée que par un joueur.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        System.out.println("GetRankCommand: Commande /getrank exécutée par le joueur: " + player.getName());

        try {
            String playerRank = rankDatabase.getPlayerRank(playerUUID);
            String playerPrefix = rankDatabase.getPlayerPrefix(playerUUID);
            if (playerRank != null) {
                System.out.println("GetRankCommand: Le rang du joueur " + player.getName() + " est: " + playerRank);
                player.sendMessage("Votre rang est: " + playerRank + (playerPrefix != null ? " (" + playerPrefix + ")" : ""));
            } else {
                System.out.println("GetRankCommand: Aucun rang trouvé pour le joueur " + player.getName());
                player.sendMessage("Aucun rang trouvé.");
            }
        } catch (Exception e) {
            System.err.println("GetRankCommand: Erreur lors de la récupération du rang. Erreur: " + e.getMessage());
            e.printStackTrace();
            player.sendMessage("Erreur lors de la récupération de votre rang.");
        }
        return true;
    }
}
