package uk.firedev.wzwstuff.config;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.messages.message.ComponentSingleMessage;

import static uk.firedev.daisylib.messages.message.ComponentMessage.componentMessage;

public class MessageConfig {

    private static final MessageConfig INSTANCE = new MessageConfig();

    private MessageConfig() {}

    public static @NonNull MessageConfig get() {
        return INSTANCE;
    }

    // Verification Messages

    public @NonNull ComponentSingleMessage getVerificationAlreadyVerified() {
        return componentMessage("<aqua>[Verification] <white>You are already verified.");
    }

    public @NonNull ComponentSingleMessage getVerificationNotVerified() {
        return componentMessage("<aqua>[Verification] <white>You are not verified.");
    }

    public @NonNull ComponentSingleMessage getVerificationCodeSent(Component code) {
        return componentMessage("<aqua>[Verification] <white>Verification started. Type /mcverify on discord and paste this code: {code}")
            .replace("{code}", code);
    }

    public @NonNull ComponentSingleMessage getVerificationVerified() {
        return componentMessage("<aqua>[Verification] <white>You have been verified.");
    }

    public @NonNull ComponentSingleMessage getVerificationUnlinked() {
        return componentMessage("<aqua>[Verification] <white>You are no longer verified.");
    }

    public @NonNull String getLogVerified(@NonNull String userMention, @NonNull String emojiStr, @NonNull String playerName) {
        return "**{emoji} {mention} successfully verified for {player}**"
            .replace("{emoji}", emojiStr)
            .replace("{mention}", userMention)
            .replace("{player}", playerName);
    }

    public @NonNull String getLogUnverified(@NonNull String emojiStr, @NonNull String playerName) {
        return "**{emoji} {player} is no longer verified.**"
            .replace("{emoji}", emojiStr)
            .replace("{player}", playerName);
    }

    public @NonNull String getVerifyAlreadyLinked(@NonNull String playerName) {
        return "Cannot verify. You are already linked to {player}"
            .replace("{player}", playerName);
    }

    public @NonNull String getInvalidVerificationCode() {
        return "Invalid verification code.";
    }

    public @NonNull String getVerifyMustBeOnServer() {
        return "You must be on the server to verify.";
    }

    public @NonNull String getVerifySuccess(@NonNull String playerName) {
        return "Successfully verified for {player}"
            .replace("{player}", playerName);
    }

}
