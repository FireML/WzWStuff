package uk.firedev.wzwstuff.config;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.daisylib.config.ConfigBase;
import uk.firedev.wzwstuff.WzWStuff;

import java.util.UUID;

public class VerificationStorage extends ConfigBase {

    private static final VerificationStorage INSTANCE = new VerificationStorage();

    private VerificationStorage() {
        super("verification-data.yml", null, WzWStuff.getInstance());
    }

    public static @NonNull VerificationStorage get() {
        return INSTANCE;
    }

    public boolean isVerified(@NonNull UUID uuid) {
        return getConfig().getLong(uuid.toString()) != 0;
    }

    public @Nullable UUID getLinkedPlayer(long id) {
        for (String string : getConfig().getKeys(false)) {
            long val = getConfig().getLong(string);
            if (val == id) {
                return UUID.fromString(string);
            }
        }
        return null;
    }

    public void addVerification(@NonNull UUID uuid, long id) {
        getConfig().set(uuid.toString(), id);
        save();
    }

    public void removeVerification(@NonNull UUID uuid) {
        getConfig().set(uuid.toString(), null);
        save();
    }

    @Override
    public void update() {}

}
