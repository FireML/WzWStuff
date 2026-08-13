package uk.firedev.wzwstuff.placeholder.impl.griefprevention;

import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class GPTotalBlocks extends GPPlaceholder {

    @Override
    public boolean shouldProcess(@NonNull String identifier) {
        return identifier.equals("gp_total_blocks");
    }

    @Override
    public @Nullable String parse(@Nullable OfflinePlayer player, @NonNull String identifier) {
        if (player == null) {
            return null;
        }
        PlayerData data = getPlayerData(player);
        int combined = data.getAccruedClaimBlocks() + data.getBonusClaimBlocks();
        return String.valueOf(combined);
    }

}
