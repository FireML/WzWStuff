package uk.firedev.wzwstuff.config;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.daisylib.config.BasicConfig;
import uk.firedev.wzwstuff.WzWStuff;

public class MainConfig extends BasicConfig {

    public MainConfig(@NonNull WzWStuff plugin) {
        super("config.yml", "config.yml", plugin);
    }

    public @Nullable String getDiscordToken() {
        return getConfig().getString("discord.bot-token");
    }

    public long getGuildId() {
        return getConfig().getLong("discord.guild-id");
    }

    public long getLogChannel() {
        return getConfig().getLong("discord.log-channel");
    }

}
