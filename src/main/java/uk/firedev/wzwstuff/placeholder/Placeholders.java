package uk.firedev.wzwstuff.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.placeholders.IPlaceholder;
import uk.firedev.daisylib.placeholders.PlaceholderReceiver;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.placeholder.impl.griefprevention.GPClaims;
import uk.firedev.wzwstuff.placeholder.impl.griefprevention.GPRemainingBlocks;
import uk.firedev.wzwstuff.placeholder.impl.griefprevention.GPTotalBlocks;

import java.util.List;

public class Placeholders extends PlaceholderReceiver {

    @Override
    public @NonNull List<@NonNull IPlaceholder> getCustomPlaceholders() {
        return List.of(
            new GPClaims(),
            new GPRemainingBlocks(),
            new GPTotalBlocks()
        );
    }

    @Override
    public @NotNull String getIdentifier() {
        return "wzwstuff";
    }

    @Override
    public @NotNull String getAuthor() {
        return "FireML";
    }

    @Override
    public @NotNull String getVersion() {
        return WzWStuff.getInstance().getPluginMeta().getVersion();
    }

}
