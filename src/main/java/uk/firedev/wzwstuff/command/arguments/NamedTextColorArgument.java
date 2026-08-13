package uk.firedev.wzwstuff.command.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class NamedTextColorArgument implements CustomArgumentType.Converted<NamedTextColor, String> {

    private static final DynamicCommandExceptionType INVALID_COLOR = new DynamicCommandExceptionType(
        obj -> new LiteralMessage("Invalid Color: " + obj)
    );

    @Override
    public @NonNull NamedTextColor convert(@NonNull String nativeType) throws CommandSyntaxException {
        NamedTextColor color = NamedTextColor.NAMES.value(nativeType.toLowerCase(Locale.ROOT));
        if (color == null) {
            throw INVALID_COLOR.create(nativeType);
        }
        return color;
    }

    @Override
    public @NonNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(@NonNull CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
        NamedTextColor.NAMES.values().stream()
            .map(color -> color.name().toLowerCase(Locale.ROOT))
            .filter(name -> name.toLowerCase().startsWith(builder.getRemainingLowerCase()))
            .forEach(builder::suggest);
        return builder.buildFuture();
    }

}
