package uk.firedev.wzwstuff.verification;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;
import uk.firedev.wzwstuff.Messages;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.discord.DiscordBot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class VerificationManager {

    private static final VerificationManager INSTANCE = new VerificationManager();

    private static final Random RANDOM = new Random();
    private static final NamespacedKey VERIFIED_KEY = new NamespacedKey(WzWStuff.getInstance(), "verified");
    private static final HoverEvent<Component> CLICK_TO_COPY = HoverEvent.hoverEvent(
        HoverEvent.Action.SHOW_TEXT,
        Component.text("Click to copy to clipboard.").color(NamedTextColor.YELLOW)
    );

    private final Map<Integer, UUID> verificationCache = new HashMap<>();

    private VerificationManager() {}

    public static @NonNull VerificationManager getInstance() {
        return INSTANCE;
    }

    public String verify(@NonNull String userMention, int code) {
        UUID uuid = verificationCache.remove(code);
        // Verification code is not cached, it is invalid.
        if (uuid == null) {
            return "Invalid verification code.";
        }
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return "You must be on the server to verify.";
        }
        player.getPersistentDataContainer().set(VERIFIED_KEY, PersistentDataType.BOOLEAN, true);
        Messages.VERIFICATION_VERIFIED.get().send(player);
        logVerification(userMention, player);
        return "Successfully verified for " + player.getName();
    }

    private void logVerification(@NonNull String userMention, @NonNull Player player) {
        Emoji emoji = DiscordBot.getInstance().getEmoji(1528099250079142019L);
        String emojiStr = emoji == null ? "" : emoji.getFormatted() + " ";
        String logMessage = "**{emoji} {mention} successfully verified for {player}**";
        DiscordBot.getInstance().sendMessage(
            WzWStuff.getInstance().getConfig().getLong("discord.log-channel"),
            logMessage
                .replace("{emoji}", emojiStr)
                .replace("{mention}", userMention)
                .replace("{player}", player.getName())
        );
    }

    public void startVerification(@NonNull Player player) {
        // Already verified.
        if (player.getPersistentDataContainer().has(VERIFIED_KEY)) {
            Messages.VERIFICATION_ALREADY_VERIFIED.get().send(player);
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
        if (!player.getPersistentDataContainer().has(VERIFIED_KEY)) {
            Messages.VERIFICATION_NOT_VERIFIED.get().send(player);
            return;
        }
        player.getPersistentDataContainer().remove(VERIFIED_KEY);
        Messages.VERIFICATION_UNLINKED.get().send(player);
        String logMessage = "**{emoji} {player} is no longer verified.**";
        DiscordBot.getInstance().sendMessage(
            WzWStuff.getInstance().getConfig().getLong("discord.log-channel"),
            logMessage
                .replace("{emoji}", ":x:")
                .replace("{player}", player.getName())
        );
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
        Messages.VERIFICATION_CODE_SENT.get()
            .replace("{code}", displayCode(code))
            .send(player);
    }

    private Component displayCode(int code) {
        return Component.text("[" + code + "]")
            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ClickEvent.Payload.string(String.valueOf(code))))
            .hoverEvent(CLICK_TO_COPY);
    }

}
