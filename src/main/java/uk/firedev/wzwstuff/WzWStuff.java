package uk.firedev.wzwstuff;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import uk.firedev.wzwstuff.listener.FirstJoinItems;

public final class WzWStuff extends JavaPlugin {

    private static WzWStuff INSTANCE;

    public WzWStuff() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException(getClass().getName() + " has already been assigned!");
        }
        INSTANCE = this;
    }

    public static @NotNull WzWStuff getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(WzWStuff.class.getSimpleName() + " has not been assigned!");
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FirstJoinItems(), this);
    }

}
