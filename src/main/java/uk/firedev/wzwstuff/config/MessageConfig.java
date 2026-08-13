package uk.firedev.wzwstuff.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.object.ObjectContents;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.external.vault.VaultWrapper;
import uk.firedev.daisylib.messages.message.ComponentMessage;
import uk.firedev.daisylib.messages.message.ComponentSingleMessage;
import uk.firedev.wzwstuff.data.PlayerData;
import uk.firedev.wzwstuff.economy.BaltopEntry;
import uk.firedev.wzwstuff.economy.WzWStuffEconomy;

import java.util.Optional;

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

    public @NonNull ComponentSingleMessage getBalance(@NonNull OfflinePlayer player) {
        PlayerData data = PlayerData.playerData(player.getUniqueId());
        return componentMessage("<aqua>[Economy] <white>{player}'s Balance: <gold>{balance}")
            .replace("{player}", data.getUsername())
            .replace("{balance}", data.getFormattedBalance());
    }

    public ComponentMessage<?, ?> getBaltopOpening() {
        return componentMessage("<aqua>[Economy] <white>Opening /baltop...");
    }

    public ComponentMessage<?, ?> getBaltopTitle() {
        return componentMessage("<#bae4e8>Player Balance Leaderboard");
    }

    public ComponentMessage<?, ?> getBaltopEntry(@NonNull BaltopEntry entry) {
        return componentMessage( "<#bae4e8>{sprite} {player}: <white>{amount}")
            .replace("{sprite}", Component.object(ObjectContents.playerHead(entry.uuid())))
            .replace("{player}", Optional.ofNullable(entry.player().getName()).orElse("N/A"))
            .replace("{amount}", formatEconomy(entry.balance()));
    }

    public ComponentMessage<?, ?> getMoneySetSuccess(@NonNull PlayerData playerData, double amount) {
        return componentMessage("<aqua>[Economy] <white>Set {player}'s balance to {amount}.")
            .replace("{player}", playerData.getUsername())
            .replace("{amount}", formatEconomy(amount));
    }

    public ComponentMessage<?, ?> getMoneyAddSuccess(@NonNull PlayerData playerData, double amount) {
        return componentMessage("<aqua>[Economy] <white>Added {amount} to {player}'s balance.")
            .replace("{amount}", formatEconomy(amount))
            .replace("{player}", playerData.getUsername());
    }

    public ComponentMessage<?, ?> getMoneyTakeSuccess(@NonNull PlayerData playerData, double amount) {
        return componentMessage("<aqua>[Economy] <white>Taken {amount} from {player}'s balance.")
            .replace("{amount}", formatEconomy(amount))
            .replace("{player}", playerData.getUsername());
    }

    public ComponentMessage<?, ?> getMoneyTransferSuccess(@NonNull PlayerData playerData, @NonNull PlayerData targetData, double amount) {
        return componentMessage("<aqua>[Economy] <white>Transferred {amount} from {player} to {target}.")
            .replace("{player}", playerData.getUsername())
            .replace("{target}", targetData.getUsername())
            .replace("{amount}", formatEconomy(amount));
    }

    public ComponentMessage<?, ?> getNotEnoughMoney(double amount) {
        return componentMessage("<aqua>[Economy] <red>You do not have {amount}!")
            .replace("{amount}", formatEconomy(amount));
    }

    public ComponentMessage<?, ?> getTargetNotEnoughMoney(@NonNull PlayerData playerData, double amount) {
        return componentMessage("<aqua>[Economy] <red>{player} does not have {amount}.")
            .replace("{player}", playerData.getUsername())
            .replace("{amount}", formatEconomy(amount));
    }

    public ComponentMessage<?, ?> getCannotPayYourself() {
        return componentMessage("<aqua>[Economy] <red>You cannot send money to yourself!");
    }

    public ComponentMessage<?, ?> getPaySend(@NonNull PlayerData targetData, double amount) {
        return componentMessage("<aqua>[Economy] <white>Sent {amount} to {target}.")
            .replace("{amount}", formatEconomy(amount))
            .replace("{target}", targetData.getUsername());
    }

    public ComponentMessage<?, ?> getPayReceive(@NonNull PlayerData senderData, double amount) {
        return componentMessage("<aqua>[Economy] <white>Received {amount} from {player}.")
            .replace("{amount}", formatEconomy(amount))
            .replace("{player}", senderData.getUsername());
    }

    private String formatEconomy(double amount) {
        return WzWStuffEconomy.get().format(amount);
    }

}
