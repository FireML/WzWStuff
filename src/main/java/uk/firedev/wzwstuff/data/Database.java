package uk.firedev.wzwstuff.data;

import org.bukkit.Bukkit;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.daisylib.database.DatabaseModule;
import uk.firedev.daisylib.database.SQLiteDatabase;
import uk.firedev.daisylib.database.exceptions.DatabaseLoadException;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.config.MainConfig;
import uk.firedev.wzwstuff.economy.BaltopEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class Database extends SQLiteDatabase {

    private final Map<String, String> columns = new HashMap<>();
    private final Map<UUID, PlayerData> playerDataCache = new HashMap<>();

    public Database(@NonNull WzWStuff plugin) {
        super(plugin);
        columns.put("uuid", "VARCHAR(36) NOT NULL PRIMARY KEY");
        columns.put("balance", "REAL NOT NULL DEFAULT 0.0");
    }

    @Override
    public void load() throws DatabaseLoadException {
        super.load();
        Bukkit.getPluginManager().registerEvents(new DatabaseListener(), getPlugin());
    }

    @Override
    public void save() {
        Iterator<PlayerData> iterator = playerDataCache.values().iterator();
        while (iterator.hasNext()) {
            PlayerData data = iterator.next();
            savePlayerData(data);
            if (data.canUnload()) {
                iterator.remove();
                WzWStuff.getInstance().getLogging().info("Unloaded PlayerData for " + data.getUuid());
            }
        }
    }

    @NonNull
    @Override
    public String getTable() {
        return "wzwstuff_players";
    }

    @Override
    public @NonNull Map<String, String> getColumns() {
        return columns;
    }

    @Override
    public Optional<Long> getAutoSaveSeconds() {
        return Optional.of(WzWStuff.getInstance().getMainConfig().getDatabaseSaveInterval());
    }

    public void loadPlayerData(@NonNull UUID uuid) {
        createPlayerDataIfMissing(uuid);
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM wzwstuff_players WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet set = ps.executeQuery();
            set.next();
            PlayerData data = new PlayerData(uuid, set);
            set.close();
            playerDataCache.put(uuid, data);
            WzWStuff.getInstance().getLogging().info("Loaded PlayerData for " + uuid);
        } catch (SQLException exception) {
            WzWStuff.getInstance().getLogging().error("Failed to load player data for " + uuid, exception);
        }
    }

    public void unloadPlayerData(@NonNull UUID uuid) {
        PlayerData cachedData = playerDataCache.remove(uuid);
        if (cachedData == null) {
            return;
        }
        savePlayerData(cachedData);
        WzWStuff.getInstance().getLogging().info("Unloaded PlayerData for " + uuid);
    }

    public void savePlayerData(@NonNull PlayerData data) {
        try (PreparedStatement ps = getConnection().prepareStatement("INSERT OR REPLACE INTO wzwstuff_players (uuid, balance) VALUES (?, ?)")) {
            ps.setString(1, data.getUuid().toString());
            ps.setDouble(2, data.getBalance());
            ps.execute();
        } catch (SQLException exception) {
            WzWStuff.getInstance().getLogging().error("Failed to save player data for " + data.getUuid(), exception);
        }
    }

    public @Nullable PlayerData getPlayerData(@NonNull UUID uuid) {
        // Player has never joined the server
        if (Bukkit.getOfflinePlayer(uuid).getFirstPlayed() == 0) {
            return null;
        }
        PlayerData data = playerDataCache.get(uuid);
        if (data == null) {
            loadPlayerData(uuid);
            data = playerDataCache.get(uuid);
        }
        return data;
    }

    public @NonNull PlayerData getPlayerDataOrThrow(@NonNull UUID uuid) throws RuntimeException {
        return getPlayerDataOrThrow(uuid, "Could not find player data for " + uuid);
    }

    public @NonNull PlayerData getPlayerDataOrThrow(@NonNull UUID uuid, @NonNull String throwMessage) throws RuntimeException {
        PlayerData data = getPlayerData(uuid);
        if (data == null) {
            throw new RuntimeException(throwMessage);
        }
        return data;
    }

    private void createPlayerDataIfMissing(@NonNull UUID uuid) {
        try (PreparedStatement ps = getConnection().prepareStatement("INSERT OR IGNORE INTO wzwstuff_players (uuid) VALUES (?)")) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException exception) {
            WzWStuff.getInstance().getLogging().error("Failed to create player data for " + uuid, exception);
        }
    }

    // Economy Things

    public CompletableFuture<Stream<BaltopEntry>> fetchBaltop() {
        Comparator<BaltopEntry> comparator = Comparator.comparingDouble(BaltopEntry::balance).reversed();

        String sql = "SELECT * FROM wzwstuff_players ORDER BY balance DESC";

        return CompletableFuture.supplyAsync(() -> {
            List<BaltopEntry> list = new ArrayList<>();
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        BaltopEntry entry = parseBaltop(rs);
                        if (entry != null) {
                            list.add(entry);
                        }
                    }
                }
            } catch (SQLException exception) {
                WzWStuff.getInstance().getLogging().error("Failed to fetch baltop.", exception);
                return Stream.of();
            }
            return list.stream().sorted(comparator);
        });
    }

    private @Nullable BaltopEntry parseBaltop(@NonNull ResultSet rs) throws SQLException {
        String uuidStr = rs.getString("uuid");
        double balance = rs.getDouble("balance");
        try {
            UUID uuid = UUID.fromString(uuidStr);
            return new BaltopEntry(uuid, balance);
        } catch (NullPointerException | IllegalArgumentException exception) {
            return null;
        }
    }

}
