package uk.firedev.wzwstuff.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.daisylib.command.CommandUtils;
import uk.firedev.wzwstuff.command.arguments.NamedTextColorArgument;
import uk.firedev.wzwstuff.config.MessageConfig;
import uk.firedev.wzwstuff.utils.GlowHelper;

public class GlowCommand {

    public static @NonNull LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("glow")
            .requires(stack -> stack.getSender().hasPermission("wzwupdatesmc.command.glow"))
            .executes(ctx -> {
                Player player = CommandUtils.requirePlayer(ctx);
                setGlow(player, NamedTextColor.WHITE);
                return 1;
            })
            .then(
                Commands.literal("off")
                    .executes(ctx -> {
                        Player player = CommandUtils.requirePlayer(ctx);
                        setGlow(player, null);
                        return 1;
                    })
            )
            .then(
                Commands.argument("color", new NamedTextColorArgument())
                    .executes(ctx -> {
                        Player player = CommandUtils.requirePlayer(ctx);
                        NamedTextColor color = ctx.getArgument("color", NamedTextColor.class);
                        setGlow(player, color);
                        return 1;
                    })
            ).build();
    }

    private static void setGlow(@NonNull Player player, @Nullable NamedTextColor color) {
        if (color == null) {
            GlowHelper.disableGlow(player);
            player.setGlowing(false);
            MessageConfig.get().getGlowRemoved().send(player);
        } else {
            GlowHelper.enableGlow(player, color);
            player.setGlowing(true);
            MessageConfig.get().getGlowSet(color).send(player);
        }
    }

}
