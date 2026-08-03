package uk.firedev.wzwstuff.verification;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class DiscordVerifyCommand {

    public static SlashCommandData get() {
        return Commands.slash("mcverify", "Verify yourself on the Minecraft Server.")
            .addOption(OptionType.INTEGER, "code", "Your verification code.", true);
    }

}
