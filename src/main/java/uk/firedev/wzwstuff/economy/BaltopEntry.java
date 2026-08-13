package uk.firedev.wzwstuff.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record BaltopEntry(@NonNull UUID uuid, double balance) {

    /**
     * Gets the OfflinePlayer linked to this entry.
     */
    public @NonNull OfflinePlayer player() {
        return Bukkit.getOfflinePlayer(uuid);
    }

}
