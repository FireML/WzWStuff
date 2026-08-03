package uk.firedev.wzwstuff.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.util.Loggers;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.verification.DiscordVerifyCommand;
import uk.firedev.wzwstuff.verification.VerificationDiscordListener;

import java.util.EnumSet;

public class DiscordBot {

    private static final DiscordBot instance = new DiscordBot();
    private JDA bot;

    private DiscordBot() {}

    public static @NonNull DiscordBot getInstance() {
        return instance;
    }

    public void load() {
        Loggers.info(WzWStuff.getInstance().getLogger(), "Loading discord bot.");

        try {
            JDABuilder builder = initializeBuilder();
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);
            this.bot = buildBot(builder);
            awaitBotReady();
            updateCommands();
        } catch (InvalidTokenException exception) {
            throw new IllegalStateException("Failed to load discord bot.", exception);
        }
    }

    public @NonNull JDA getBot() {
        if (bot == null) {
            throw new IllegalStateException("Discord Bot is not loaded.");
        }
        return bot;
    }

    protected JDABuilder initializeBuilder() {
        return JDABuilder.createLight(
            WzWStuff.getInstance().getConfig().getString("discord.bot-token"),
            EnumSet.allOf(GatewayIntent.class)
        );
    }

    private JDA buildBot(JDABuilder builder) {
        return builder.build();
    }

    private void awaitBotReady() {
        try {
            this.bot.awaitReady();
        } catch (InterruptedException exception) {
            Loggers.error(WzWStuff.getInstance().getLogger(), "Waiting for bot to load was interrupted!", exception);
        }
    }

    private void updateCommands() {
        this.bot.updateCommands()
            .addCommands(DiscordVerifyCommand.get())
            .queue();
    }

}
