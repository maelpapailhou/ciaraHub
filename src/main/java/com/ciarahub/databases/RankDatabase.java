package com.ciarahub.databases;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RankDatabase {

    private Connection connection;
    private FileConfiguration ranksConfig;

    public RankDatabase(Connection connection, String ranksFilePath) {
        this.connection = connection;
        this.ranksConfig = YamlConfiguration.loadConfiguration(new File(ranksFilePath));
        System.out.println("RankDatabase: Constructeur appelé, connexion et fichier de rangs établis.");
    }

    public String getPlayerRank(UUID playerUUID) {
        System.out.println("RankDatabase: getPlayerRank appelé pour UUID: " + playerUUID);
        String rank = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `RANK` FROM players WHERE UUID = ?");
            statement.setString(1, playerUUID.toString());
            System.out.println("RankDatabase: Requête préparée exécutée.");
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    rank = resultSet.getString("RANK");
                    System.out.println("RankDatabase: Rang trouvé: " + rank);
                } else {
                    System.out.println("RankDatabase: Aucun rang trouvé pour UUID: " + playerUUID);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return rank;
    }

    public void setPlayerRank(UUID playerUUID, String newRank) {
        System.out.println("RankDatabase: setPlayerRank appelé pour UUID: " + playerUUID + ", Rang: " + newRank);
        try (PreparedStatement statement = connection.prepareStatement("UPDATE players SET `RANK` = ? WHERE UUID = ?")) {
            statement.setString(1, newRank);
            statement.setString(2, playerUUID.toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("RankDatabase: Mise à jour du rang réussie.");
            } else {
                System.out.println("RankDatabase: Aucune ligne affectée, mise à jour du rang échouée.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public String getPlayerPrefix(UUID playerUUID) {
        String rank = getPlayerRank(playerUUID);
        if (rank != null) {
            String prefix = ranksConfig.getString(rank + ".prefix");  // Assurez-vous que le chemin est correct
            if (prefix != null) {
                System.out.println("RankDatabase: Préfixe trouvé pour le rang " + rank + ": " + prefix);
                return prefix;
            } else {
                System.out.println("RankDatabase: Aucun préfixe trouvé pour le rang " + rank);
            }
        }
        return null;
    }

    private void handleSQLException(SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }


}
