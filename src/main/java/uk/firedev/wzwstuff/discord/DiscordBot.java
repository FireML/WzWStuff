package uk.firedev.wzwstuff.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.verification.DiscordVerifyCommand;

import java.util.EnumSet;

public class DiscordBot {

    private static final DiscordBot instance = new DiscordBot();
    private JDA bot;

    private DiscordBot() {}

    public static @NonNull DiscordBot getInstance() {
        return instance;
    }

    public void load() {
        WzWStuff.getInstance().getLogging().info("Loading discord bot.");

        try {
            JDABuilder builder = initializeBuilder();
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);
            builder.enableCache(CacheFlag.EMOJI);
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
            WzWStuff.getInstance().getMainConfig().getDiscordToken(),
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
            WzWStuff.getInstance().getLogging().error("Waiting for bot to load was interrupted!", exception);
        }
    }

    private void updateCommands() {
        this.bot.updateCommands()
            .addCommands(DiscordVerifyCommand.get())
            .queue();
    }

    public @Nullable Emoji getEmoji(long id) {
        return getBot().getEmojiById(id);
    }

    public void sendMessage(long channel, @NonNull String message) {
        TextChannel textChannel = getBot().getTextChannelById(channel);
        if (textChannel == null) {
            return;
        }
        textChannel.sendMessage(message).queue();
    }

}
