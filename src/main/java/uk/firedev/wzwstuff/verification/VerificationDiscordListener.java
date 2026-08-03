package uk.firedev.wzwstuff.verification;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NonNull;
import uk.firedev.wzwstuff.WzWStuff;

public class VerificationDiscordListener extends ListenerAdapter {

    private static final VerificationDiscordListener instance = new VerificationDiscordListener();

    private VerificationDiscordListener() {}

    public static @NonNull VerificationDiscordListener getInstance() {
        return instance;
    }

    @Override
    public void onSlashCommandInteraction(@NonNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("mcverify")) {
            return;
        }
        Guild guild = event.getGuild();
        long expectedId = WzWStuff.getInstance().getConfig().getLong("discord.guild-id");
        if (guild == null || guild.getIdLong() != expectedId) {
            event.reply("You cannot use this command here.").setEphemeral(true).queue();
            return;
        }
        int code = event.getOption("code").getAsInt();
        Bukkit.getScheduler().runTask(
            WzWStuff.getInstance(),
            () -> {
                String message = VerificationManager.getInstance().verify(code);
                event.reply(message).setEphemeral(true).queue();
            }
        );
    }

}
