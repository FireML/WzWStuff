package uk.firedev.wzwstuff.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import uk.firedev.daisylib.command.CommandUtils;
import uk.firedev.wzwstuff.verification.VerificationManager;

public class VerifyCommand {

    public static LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("verify")
            .executes(ctx -> {
                Player player = CommandUtils.requirePlayer(ctx);
                VerificationManager.getInstance().startVerification(player);
                return 1;
            })
            .then(
                Commands.literal("unlink")
                    .executes(ctx -> {
                        Player player = CommandUtils.requirePlayer(ctx);
                        VerificationManager.getInstance().unlink(player);
                        return 1;
                    })
            )
            .build();
    }

}
