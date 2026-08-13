package uk.firedev.wzwstuff.command.economy;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.command.CommandUtils;
import uk.firedev.wzwstuff.config.MessageConfig;
import uk.firedev.wzwstuff.data.PlayerData;

public class MoneyCommand {

    public static @NonNull LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("money")
            .requires(stack -> stack.getSender().hasPermission("wzwstuff.admin"))
            .then(
                Commands.argument("player", ArgumentTypes.playerProfiles())
                    .then(set())
                    .then(check())
                    .then(add())
                    .then(take())
                    .then(transfer())
            ).build();
    }

    private static ArgumentBuilder<CommandSourceStack, ?> set() {
        return Commands.literal("set")
            .then(
                Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(ctx -> {
                        OfflinePlayer player = CommandUtils.parsePlayerProfileArgument(
                            ctx.getSource(),
                            ctx.getArgument("player", PlayerProfileListResolver.class)
                        );
                        double amount = ctx.getArgument("amount", double.class);

                        PlayerData data = PlayerData.playerData(player.getUniqueId());
                        data.setBalance(amount);

                        MessageConfig.get().getMoneySetSuccess(data, amount).send(ctx.getSource().getSender());
                        return 1;
                    })
            );
    }

    private static ArgumentBuilder<CommandSourceStack, ?> check() {
        return Commands.literal("check")
            .executes(ctx -> {
                OfflinePlayer player = CommandUtils.parsePlayerProfileArgument(
                    ctx.getSource(),
                    ctx.getArgument("player", PlayerProfileListResolver.class)
                );
                BalanceCommand.checkBalance(ctx.getSource().getSender(), player);
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, ?> add() {
        return Commands.literal("add")
            .then(
                Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(ctx -> {
                        OfflinePlayer player = CommandUtils.parsePlayerProfileArgument(
                            ctx.getSource(),
                            ctx.getArgument("player", PlayerProfileListResolver.class)
                        );
                        double amount = ctx.getArgument("amount", double.class);

                        PlayerData data = PlayerData.playerData(player.getUniqueId());
                        data.incrementBalance(amount);

                        MessageConfig.get().getMoneyAddSuccess(data, amount).send(ctx.getSource().getSender());
                        return 1;
                    })
            );
    }

    private static ArgumentBuilder<CommandSourceStack, ?> take() {
        return Commands.literal("take")
            .then(
                Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(ctx -> {
                        OfflinePlayer player = CommandUtils.parsePlayerProfileArgument(
                            ctx.getSource(),
                            ctx.getArgument("player", PlayerProfileListResolver.class)
                        );
                        double amount = ctx.getArgument("amount", double.class);

                        PlayerData data = PlayerData.playerData(player.getUniqueId());
                        if (data.getBalance() < amount) {
                            MessageConfig.get().getTargetNotEnoughMoney(data, amount).send(ctx.getSource().getSender());
                            return 1;
                        }
                        data.decrementBalance(amount);
                        MessageConfig.get().getMoneyTakeSuccess(data, amount).send(ctx.getSource().getSender());
                        return 1;
                    })
            );
    }

    private static ArgumentBuilder<CommandSourceStack, ?> transfer() {
        return Commands.literal("transfer")
            .then(
                Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                    .then(
                        Commands.argument("target", ArgumentTypes.playerProfiles())
                            .executes(ctx -> {
                                CommandSender sender = ctx.getSource().getSender();
                                OfflinePlayer player = CommandUtils.parsePlayerProfileArgument(
                                    ctx.getSource(),
                                    ctx.getArgument("player", PlayerProfileListResolver.class)
                                );
                                OfflinePlayer target = CommandUtils.parsePlayerProfileArgument(
                                    ctx.getSource(),
                                    ctx.getArgument("target", PlayerProfileListResolver.class)
                                );
                                double amount = ctx.getArgument("amount", double.class);

                                PlayerData playerData = PlayerData.playerData(player.getUniqueId());
                                PlayerData targetData = PlayerData.playerData(target.getUniqueId());

                                if (playerData.getBalance() < amount) {
                                    MessageConfig.get().getTargetNotEnoughMoney(playerData, amount).send(sender);
                                    return 1;
                                }
                                playerData.decrementBalance(amount);
                                targetData.incrementBalance(amount);

                                // Notify player
                                if (!sender.equals(player)) {
                                    MessageConfig.get().getPaySend(targetData, amount).send(player.getPlayer());
                                }
                                // Notify target
                                if (!sender.equals(target)) {
                                    MessageConfig.get().getPayReceive(playerData, amount).send(target.getPlayer());
                                }
                                // Notify admin
                                MessageConfig.get().getMoneyTransferSuccess(playerData, targetData, amount).send(sender);
                                return 1;
                            })
                    )
            );
    }

}
