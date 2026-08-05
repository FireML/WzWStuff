package uk.firedev.wzwstuff.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.wzwstuff.verification.DiscordVerifyCommand;
import uk.firedev.wzwstuff.verification.VerificationDiscordListener;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public class DiscordBot extends uk.firedev.daisylib.discord.DiscordBot {

    private static final DiscordBot INSTANCE = new DiscordBot();

    private DiscordBot() {}

    public static @NonNull DiscordBot get() {
        return INSTANCE;
    }

    /**
     * Should we call {@link JDA#awaitReady()} when the bot is created?
     * <p>
     * This will block the main thread. Should only be used during startup.
     */
    @Override
    public boolean shouldAwaitReady() {
        return true;
    }

    @Override
    public @NonNull EnumSet<@NonNull GatewayIntent> getGatewayIntents() {
        return EnumSet.allOf(GatewayIntent.class);
    }

    @Override
    public @NonNull Collection<@NonNull CacheFlag> getCacheFlags() {
        return List.of(
            CacheFlag.EMOJI
        );
    }

    @Override
    public @NonNull MemberCachePolicy getMemberCachePolicy() {
        return MemberCachePolicy.ALL;
    }

    @Override
    public @NonNull List<@NonNull CommandData> getCommands() {
        return List.of(
            DiscordVerifyCommand.get()
        );
    }

    @Override
    public @NonNull Collection<? extends EventListener> getListeners() {
        return List.of(
            new VerificationDiscordListener()
        );
    }

    public @Nullable Emoji getEmoji(long id) {
        return getBot().getEmojiById(id);
    }

}
