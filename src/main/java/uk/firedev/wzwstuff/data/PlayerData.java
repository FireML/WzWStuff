package uk.firedev.wzwstuff.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.daisylib.database.Database;
import uk.firedev.daisylib.external.vault.VaultWrapper;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.economy.WzWStuffEconomy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;

    private Instant unloadInstant;
    private double balance = 0.0D;

    protected PlayerData(@NonNull UUID uuid, @NonNull ResultSet set) throws SQLException {
        this.uuid = uuid;
        this.balance = set.getDouble("balance");
        markAccessed();
    }

    public static @NonNull PlayerData playerData(@NonNull UUID uuid) throws RuntimeException {
        return WzWStuff.getInstance().getDatabase().getPlayerDataOrThrow(uuid);
    }

    public void markAccessed() {
        this.unloadInstant = Instant.now().plus(Duration.ofSeconds(WzWStuff.getInstance().getMainConfig().getDatabaseCacheDuration()));
    }

    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Gets the username of this PlayerData's owner
     * @return The PlayerData owner's username, or N/A if it cannot be found.
     */
    public @NonNull String getUsername() {
        markAccessed();
        return Optional.of(Bukkit.getOfflinePlayer(uuid))
            .map(OfflinePlayer::getName)
            .orElse("N/A");
    }

    /**
     * @return The {@link Player} linked to this data, or null if the player is offline.
     */
    public @Nullable Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }

    /**
     * @return The {@link OfflinePlayer} linked to this data.
     */
    public @NonNull OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    // Database Methods

    public boolean canUnload() {
        Player onlinePlayer = Bukkit.getPlayer(uuid);
        return onlinePlayer == null && Instant.now().isAfter(unloadInstant);
    }

    public void save() {
        Instant unloadInstant = this.unloadInstant;
        WzWStuff.getInstance().getDatabase().savePlayerData(this);
        this.unloadInstant = unloadInstant;
    }

    // Economy Methods

    public double getBalance() {
        return this.balance;
    }

    public @NonNull String getFormattedBalance() {
        return WzWStuffEconomy.get().format(this.balance);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double incrementBalance(double increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("Negative values can not be passed to #incrementBalance.");
        }
        this.balance += increment;
        return this.balance;
    }

    public double decrementBalance(double decrement) {
        if (decrement < 0) {
            throw new IllegalArgumentException("Negative values can not be passed to #decrementBalance.");
        }
        this.balance -= decrement;
        return this.balance;
    }

}
