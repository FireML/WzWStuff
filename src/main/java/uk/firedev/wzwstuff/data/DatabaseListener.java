package uk.firedev.wzwstuff.data;

import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.firedev.wzwstuff.WzWStuff;

import java.util.UUID;

public class DatabaseListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerConnectionConfigureEvent event) {
        UUID uuid = event.getConnection().getProfile().getId();
        if (uuid == null) {
            return; // This should never happen.
        }
        WzWStuff.getInstance().getDatabase().loadPlayerData(uuid);
    }

}
