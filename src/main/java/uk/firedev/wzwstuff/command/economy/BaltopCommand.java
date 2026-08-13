package uk.firedev.wzwstuff.command.economy;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.command.CommandUtils;
import uk.firedev.wzwstuff.economy.BaltopDialog;

public class BaltopCommand {

    public static @NonNull LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("baltop")
            .executes(ctx -> {
                Player player = CommandUtils.requirePlayer(ctx);
                BaltopDialog.open(player);
                return 1;
            })
            .build();
    }

}
