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

}
