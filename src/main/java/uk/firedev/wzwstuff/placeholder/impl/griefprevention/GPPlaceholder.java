package uk.firedev.wzwstuff.placeholder.impl.griefprevention;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.placeholders.IPlaceholder;

public abstract class GPPlaceholder implements IPlaceholder {

    protected @NonNull PlayerData getPlayerData(@NonNull OfflinePlayer player) {
        return GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());
    }

}
