package uk.firedev.wzwstuff;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.DaisyLib;
import uk.firedev.daisylib.logging.Logging;
import uk.firedev.wzwstuff.command.VerifyCommand;
import uk.firedev.wzwstuff.config.MainConfig;
import uk.firedev.wzwstuff.discord.DiscordBot;
import uk.firedev.wzwstuff.listener.FirstJoinItems;
import uk.firedev.wzwstuff.verification.VerificationDiscordListener;

public final class WzWStuff extends JavaPlugin {

    private static WzWStuff INSTANCE;

    private final MainConfig mainConfig;
    private final Logging logging;

    public WzWStuff() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException(getClass().getName() + " has already been assigned!");
        }
        INSTANCE = this;
        mainConfig = new MainConfig(this);
        logging = Logging.logging(this);
    }

    public static @NonNull WzWStuff getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(WzWStuff.class.getSimpleName() + " has not been assigned!");
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        DaisyLib.get().init(this);
        registerCommands();
        DiscordBot.get().load(mainConfig.getDiscordToken());
        registerListeners();
    }

    public @NonNull MainConfig getMainConfig() {
        return this.mainConfig;
    }

    public @NonNull Logging getLogging() {
        return this.logging;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new FirstJoinItems(), this);
        // Verification
        DiscordBot.get().getBot().addEventListener(VerificationDiscordListener.getInstance());
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(VerifyCommand.get());
        });
    }

}
