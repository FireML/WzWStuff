package uk.firedev.wzwstuff.placeholder.impl.griefprevention;

import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class GPClaims extends GPPlaceholder {

    @Override
    public boolean shouldProcess(@NonNull String identifier) {
        return identifier.equals("gp_claims");
    }

    @Override
    public @Nullable String parse(@Nullable OfflinePlayer player, @NonNull String identifier) {
        if (player == null) {
            return null;
        }
        PlayerData data = getPlayerData(player);
        return String.valueOf(data.getClaims().size());
    }

}
