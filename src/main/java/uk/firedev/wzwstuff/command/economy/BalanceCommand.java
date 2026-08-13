package uk.firedev.wzwstuff.command.economy;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.command.CommandUtils;
import uk.firedev.wzwstuff.config.MessageConfig;

public class BalanceCommand {

    public static @NonNull LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("balance")
            .executes(context -> {
                Player player = CommandUtils.requirePlayer(context.getSource());
                checkBalance(player, player);
                return 1;
            })
            .then(
                Commands.argument("target", ArgumentTypes.playerProfiles())
                    .requires(stack -> stack.getSender().hasPermission("wzwstuff.admin"))
                    .executes(ctx -> {
                        OfflinePlayer target = CommandUtils.parsePlayerProfileArgument(
                            ctx.getSource(),
                            ctx.getArgument("target", PlayerProfileListResolver.class)
                        );
                        checkBalance(ctx.getSource().getSender(), target);
                        return 1;
                    })
            )
            .build();
    }

    // Convenience

    protected static void checkBalance(@NonNull CommandSender sender, @NonNull OfflinePlayer target) {
        MessageConfig.get().getBalance(target).send(sender);
    }

}
