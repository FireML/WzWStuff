package uk.firedev.wzwstuff.utils;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NonNull;
import uk.firedev.wzwstuff.WzWStuff;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GlowHelper {

    private static final Map<NamedTextColor, Team> loadedTeams = new HashMap<>();

    static {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        for (NamedTextColor color : NamedTextColor.NAMES.values()) {
            String teamName = "WzwStuff_" + color.name().toLowerCase(Locale.ROOT);
            Team existingTeam = manager.getMainScoreboard().getTeam(teamName);
            if (existingTeam != null) {
                loadedTeams.put(color, existingTeam);
            } else {
                Team newTeam = manager.getMainScoreboard().registerNewTeam(teamName);
                newTeam.color(color);
                loadedTeams.put(color, newTeam);
            }
        }
    }

    private GlowHelper() {}

    public static void enableGlow(@NonNull Player player, @NonNull String color) {
        NamedTextColor namedTextColor = NamedTextColor.NAMES.value(color.toLowerCase(Locale.ROOT));
        if (namedTextColor == null) {
            WzWStuff.getInstance().getLogging().warn("Invalid glow color: " + color);
            return;
        }
        enableGlow(player, namedTextColor);
    }

    public static void enableGlow(@NonNull Player player, @NonNull NamedTextColor color) {
        Team team = loadedTeams.get(color);
        team.addPlayer(player);
    }

    public static void disableGlow(@NonNull Player player) {
        loadedTeams.values().forEach(team -> team.removePlayer(player));
    }

}
