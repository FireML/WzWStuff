package uk.firedev.wzwstuff.economy;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.external.vault.SimpleEconomy;
import uk.firedev.wzwstuff.WzWStuff;
import uk.firedev.wzwstuff.data.PlayerData;

import java.util.List;

public class WzWStuffEconomy extends SimpleEconomy {

    private static final WzWStuffEconomy INSTANCE = new WzWStuffEconomy();

    private WzWStuffEconomy() {}

    public static WzWStuffEconomy get() {
        return INSTANCE;
    }

    private @NonNull PlayerData getData(OfflinePlayer player) {
        return PlayerData.playerData(player.getUniqueId());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "WzWStuffEconomy";
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        int amountInt = (int) amount;
        String name = amount == 1 ? currencyNameSingular() : currencyNamePlural();
        return amountInt + name;
    }

    @Override
    public String currencyNamePlural() {
        return "Claim Blocks"; // TODO update when we decide.
    }

    @Override
    public String currencyNameSingular() {
        return "Claim Block"; // TODO update when we decide.
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getData(player).getBalance();
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        PlayerData data = getData(player);
        if (data.getBalance() < amount) {
            return new EconomyResponse(
                0,
                0,
                EconomyResponse.ResponseType.FAILURE,
                "Player does not have enough currency."
            );
        }
        double balance = data.decrementBalance(amount);
        return new EconomyResponse(
            amount,
            balance,
            EconomyResponse.ResponseType.SUCCESS,
            null
        );
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        PlayerData data = getData(player);
        double balance = data.incrementBalance(amount);
        return new EconomyResponse(
            amount,
            balance,
            EconomyResponse.ResponseType.SUCCESS,
            null
        );
    }

    // Crap we don't worry about.

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        // Players always have an account because we store in PDC.
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        // Players always have an account because we store in PDC.
        return true;
    }


    // Crap we don't use.

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

}
