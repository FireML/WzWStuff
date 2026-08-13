package uk.firedev.wzwstuff.economy;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.config.MessageConfig;
import uk.firedev.wzwstuff.dialog.InfoDialogBuilder;

import java.util.stream.Stream;

public class BaltopDialog {

    public static void open(@NonNull Player player) {
        MessageConfig.get().getBaltopOpening().send(player);
        WzWStuff.getInstance().getDatabase().fetchBaltop().thenAcceptAsync(values ->
            new BaltopDialog(player, values).open()
        );
    }

    private final Player player;
    private final InfoDialogBuilder builder;

    private BaltopDialog(@NonNull Player player, @NonNull Stream<BaltopEntry> values) {
        this.player = player;
        this.builder = new InfoDialogBuilder().withTitle(MessageConfig.get().getBaltopTitle());
        values.forEach(entry -> builder.addContent(MessageConfig.get().getBaltopEntry(entry)));
    }

    private void open() {
        builder.open(player);
    }

}
