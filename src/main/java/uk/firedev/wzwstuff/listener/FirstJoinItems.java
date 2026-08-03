package uk.firedev.wzwstuff.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import uk.firedev.wzwstuff.WzWStuff;

public class FirstJoinItems implements Listener {

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) {
            return;
        }
        WzWStuff.getInstance().getLogging().info("Giving " + player.getName() + " their first join items.");
        event.getPlayer().give(
            ItemStack.of(Material.BREAD, 16),
            ItemStack.of(Material.GOLDEN_SHOVEL)
        );
    }

}
