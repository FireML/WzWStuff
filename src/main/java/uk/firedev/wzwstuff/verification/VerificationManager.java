package uk.firedev.wzwstuff.verification;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.config.MessageConfig;
import uk.firedev.wzwstuff.config.VerificationStorage;
import uk.firedev.wzwstuff.discord.DiscordBot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class VerificationManager {

    private static final VerificationManager INSTANCE = new VerificationManager();

    private static final Random RANDOM = new Random();
    private static final HoverEvent<Component> CLICK_TO_COPY = HoverEvent.hoverEvent(
        HoverEvent.Action.SHOW_TEXT,
        Component.text("Click to copy to clipboard.").color(NamedTextColor.YELLOW)
    );

    private final Map<Integer, UUID> verificationCache = new HashMap<>();

    private VerificationManager() {}

    public static @NonNull VerificationManager getInstance() {
        return INSTANCE;
    }

    public String verify(@NonNull User user, int code) {
        // Check if the user is already linked to a player.
        UUID linked = VerificationStorage.get().getLinkedPlayer(user.getIdLong());
        if (linked != null) {
            String name = Optional.ofNullable(Bukkit.getOfflinePlayer(linked).getName()).orElse("N/A");
            return MessageConfig.get().getVerifyAlreadyLinked(name);
        }

        UUID uuid = verificationCache.remove(code);
        // Verification code is not cached, it is invalid.
        if (uuid == null) {
            return MessageConfig.get().getInvalidVerificationCode();
        }
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return MessageConfig.get().getVerifyMustBeOnServer();
        }
        VerificationStorage.get().addVerification(uuid, user.getIdLong());
        MessageConfig.get().getVerificationVerified().send(player);
        logVerification(user.getAsMention(), player);
        return MessageConfig.get().getVerifySuccess(player.getName());
    }

    private void logVerification(@NonNull String userMention, @NonNull Player player) {
        Emoji emoji = DiscordBot.getInstance().getEmoji(1528099250079142019L);
        String emojiStr = emoji == null ? "" : emoji.getFormatted() + " ";
        DiscordBot.getInstance().sendMessage(
            WzWStuff.getInstance().getMainConfig().getLogChannel(),
            MessageConfig.get().getLogVerified(userMention, emojiStr, player.getName())
        );
    }

    public void startVerification(@NonNull Player player) {
        // Already verified.
        if (isVerified(player)) {
            MessageConfig.get().getVerificationAlreadyVerified().send(player);
            return;
        }
        UUID uuid = player.getUniqueId();
        // Already has a code.
        if (verificationCache.containsValue(uuid)) {
            sendMessage(getExistingCode(uuid), player);
            return;
        }
        int code = generateCode();
        verificationCache.put(code, uuid);
        sendMessage(code, player);
    }

    public void unlink(@NonNull Player player) {
        if (!isVerified(player)) {
            MessageConfig.get().getVerificationNotVerified().send(player);
            return;
        }
        VerificationStorage.get().removeVerification(player.getUniqueId());
        MessageConfig.get().getVerificationUnlinked().send(player);
        DiscordBot.getInstance().sendMessage(
            WzWStuff.getInstance().getMainConfig().getLogChannel(),
            MessageConfig.get().getLogUnverified(":x:", player.getName())
        );
    }

    public boolean isVerified(@NonNull Player player) {
        return VerificationStorage.get().isVerified(player.getUniqueId());
    }

    // Codes

    private int getExistingCode(UUID uuid) {
        for (Map.Entry<Integer, UUID> entry : verificationCache.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }
        // Should never happen.
        throw new IllegalStateException("UUID has no code.");
    }

    private int generateCode() {
        int code;
        do {
            code = RANDOM.nextInt(0, 100000);
        } while (verificationCache.containsKey(code));
        return code;
    }

    // Message

    private void sendMessage(int code, @NonNull Player player) {
        MessageConfig.get().getVerificationCodeSent(displayCode(code)).send(player);
    }

    private Component displayCode(int code) {
        return Component.text("[" + code + "]")
            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ClickEvent.Payload.string(String.valueOf(code))))
            .hoverEvent(CLICK_TO_COPY);
    }

}
