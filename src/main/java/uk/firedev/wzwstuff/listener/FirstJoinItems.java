package uk.firedev.wzwstuff.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemType;
import uk.firedev.wzwstuff.WzWStuff;

public class FirstJoinItems implements Listener {

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) {
            return;
        }
        WzWStuff.getInstance().getLogger().info("Giving " + player.getName() + " their first join items.");
        event.getPlayer().give(
            ItemType.BREAD.createItemStack(16),
            ItemType.GOLDEN_SHOVEL.createItemStack()
        );
    }

}
