package uk.firedev.wzwstuff;

import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.messages.message.ComponentSingleMessage;

import java.util.function.Supplier;

import static uk.firedev.daisylib.messages.message.ComponentMessage.componentMessage;

/**
 * Hardcoded messages because i'm lazy
 */
public enum Messages {
    VERIFICATION_ALREADY_VERIFIED(
        () -> componentMessage("<aqua>[Verification] <white>You are already verified.")
    ),
    VERIFICATION_NOT_VERIFIED(
        () -> componentMessage("<aqua>[Verification] <white>You are not verified.")
    ),
    VERIFICATION_CODE_SENT(
        () -> componentMessage("<aqua>[Verification] <white>Verification started. Type /mcverify on discord and paste this code: {code}")
    ),
    VERIFICATION_VERIFIED(
        () -> componentMessage("<aqua>[Verification] <white>You have been verified.")
    ),
    VERIFICATION_UNLINKED(
        () -> componentMessage("<aqua>[Verification] <white>You are no longer verified.")
    );

    private final Supplier<ComponentSingleMessage> messageSupplier;

    Messages(@NonNull Supplier<@NonNull ComponentSingleMessage> messageSupplier) {
        this.messageSupplier = messageSupplier;
    }

    public @NonNull ComponentSingleMessage get() {
        return messageSupplier.get();
    }

}
