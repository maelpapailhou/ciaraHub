package com.ciarahub.managers;

import com.ciarahub.databases.RankDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreBoardManager {

    private RankDatabase rankDatabase;

    public ScoreBoardManager(RankDatabase rankDatabase) {
        this.rankDatabase = rankDatabase;
    }

    public void createScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("playerInfo", "dummy", ChatColor.translateAlternateColorCodes('&', "&6...&r &b&lCiaraCube&r&b.fr &6..."));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Obtention de la date actuelle
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        // Configuration des lignes
        String[] lines = {
                "&7" + currentDate,
                "",
                "&6» &b&l" + player.getName(),
                "&3  &7Grade&f: " + getFormattedRank(player),
                "&3  &7Émeraudes&f: &a147",  // Ces valeurs sont à titre d'exemple
                "&3  &7Étoiles&f: &e18 900", // et doivent être ajustées selon vos besoins.
                "",
                "&6» &b&lLobby &r&b#1",
                "&5  &7Connectés&f: &a3 701",
                "&5  &7Amis&f: &a3"
        };

        for (int i = 0; i < lines.length; i++) {
            Score score = objective.getScore(ChatColor.translateAlternateColorCodes('&', lines[i]));
            score.setScore(lines.length - i);
        }

        player.setScoreboard(scoreboard);
    }

    private String getFormattedRank(Player player) {
        String rankPrefix = rankDatabase.getPlayerPrefix(player.getUniqueId());
        if (rankPrefix == null) rankPrefix = "&7Aucun";  // Utilisez le code couleur &7 pour "Aucun"
        return ChatColor.translateAlternateColorCodes('&', rankPrefix);
    }
}
