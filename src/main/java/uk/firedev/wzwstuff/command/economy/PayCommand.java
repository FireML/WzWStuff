package uk.firedev.wzwstuff.command.economy;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.command.CommandUtils;
import uk.firedev.wzwstuff.config.MessageConfig;
import uk.firedev.wzwstuff.data.PlayerData;

public class PayCommand {

    public static @NonNull LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("pay")
            .then(
                Commands.argument("target", ArgumentTypes.playerProfiles())
                    .then(
                        Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                            .executes(ctx -> {
                                Player player = CommandUtils.requirePlayer(ctx);
                                OfflinePlayer target = CommandUtils.parsePlayerProfileArgument(
                                    ctx.getSource(),
                                    ctx.getArgument("target", PlayerProfileListResolver.class)
                                );
                                if (player.equals(target)) {
                                    MessageConfig.get().getCannotPayYourself().send(player);
                                    return 1;
                                }

                                double amount = ctx.getArgument("amount", double.class);

                                PlayerData playerData = PlayerData.playerData(player.getUniqueId());
                                if (playerData.getBalance() < amount) {
                                    MessageConfig.get().getNotEnoughMoney(amount).send(player);
                                    return 1;
                                }
                                PlayerData targetData = PlayerData.playerData(target.getUniqueId());
                                playerData.decrementBalance(amount);
                                targetData.incrementBalance(amount);

                                // Notify player
                                MessageConfig.get().getPaySend(targetData, amount).send(player);
                                // Notify target
                                MessageConfig.get().getPayReceive(playerData, amount).send(target.getPlayer());
                                return 1;
                            })
                    )
            ).build();
    }

}
