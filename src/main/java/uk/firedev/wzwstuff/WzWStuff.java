package uk.firedev.wzwstuff;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.DaisyLib;
import uk.firedev.daisylib.database.exceptions.DatabaseLoadException;
import uk.firedev.daisylib.external.vault.VaultWrapper;
import uk.firedev.daisylib.logging.Logging;
import uk.firedev.wzwstuff.command.GlowCommand;
import uk.firedev.wzwstuff.command.VerifyCommand;
import uk.firedev.wzwstuff.command.economy.BalanceCommand;
import uk.firedev.wzwstuff.command.economy.BaltopCommand;
import uk.firedev.wzwstuff.command.economy.MoneyCommand;
import uk.firedev.wzwstuff.command.economy.PayCommand;
import uk.firedev.wzwstuff.config.MainConfig;
import uk.firedev.wzwstuff.data.Database;
import uk.firedev.wzwstuff.discord.DiscordBot;
import uk.firedev.wzwstuff.economy.WzWStuffEconomy;
import uk.firedev.wzwstuff.listener.FirstJoinItems;
import uk.firedev.wzwstuff.placeholder.Placeholders;
import uk.firedev.wzwstuff.verification.VerificationDiscordListener;

public final class WzWStuff extends JavaPlugin {

    private static WzWStuff INSTANCE;

    private final MainConfig mainConfig = new MainConfig(this);
    private final Logging logging = Logging.logging(this);
    private final Database database = new Database(this);

    public WzWStuff() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException(getClass().getName() + " has already been assigned!");
        }
        INSTANCE = this;
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
        loadDatabase();
        registerCommands();
        DiscordBot.get().load(mainConfig.getDiscordToken());
        registerListeners();
        new Placeholders().register();
        loadEconomy();
    }

    private void loadDatabase() {
        try {
            database.load();
        } catch (DatabaseLoadException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void loadEconomy() {
        WzWStuffEconomy.get().register(this, ServicePriority.Highest);
        VaultWrapper.get().load();
        if (!(VaultWrapper.get().getEconomy() instanceof WzWStuffEconomy)) {
            throw new IllegalStateException("WzWStuffEconomy is not the economy provider.");
        }
    }

    public @NonNull MainConfig getMainConfig() {
        return this.mainConfig;
    }

    public @NonNull Logging getLogging() {
        return this.logging;
    }

    public @NonNull Database getDatabase() {
        return this.database;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new FirstJoinItems(), this);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(VerifyCommand.get());

            // Glow
            commands.registrar().register(GlowCommand.get());

            // Economy
            commands.registrar().register(BalanceCommand.get());
            commands.registrar().register(BaltopCommand.get());
            commands.registrar().register(MoneyCommand.get());
            commands.registrar().register(PayCommand.get());
        });
    }

}
