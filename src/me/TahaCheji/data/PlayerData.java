package me.TahaCheji.data;

import com.earth2me.essentials.Essentials;
import com.sun.org.apache.bcel.internal.generic.NEW;
import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.Main;
import me.TahaCheji.bank.CreditCard;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlayerData {



    public static double getDepostitedCoins(Player p) {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        double coins = pD.getDouble("bankInfo.deposited");

        return coins;
    }

    public static void setDepostitedCoins(Player p, int coins, TransactionType type) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        double amount = pD.getDouble("bankInfo.deposited");
        pD.set("bankInfo.deposited", coins);
        pD.save(playerData);
        CreditCard.setCreditBalance(p);
        logTransaction(p, coins, type);
    }

    public static void setFakeDepostitedCoins(Player p, int coins, TransactionType type) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        double amount = pD.getDouble("bankInfo.deposited");
        pD.set("bankInfo.deposited", coins);
        pD.save(playerData);
        logTransaction(p, coins, type);
    }

    public static double getLoanAmount(Player p) {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        double coins = pD.getDouble("bankInfo.loan");

        return coins;
    }

    public static void setLoanAmount(Player p, int coins) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        pD.set("bankInfo.loan", coins);
        pD.save(playerData);
        Economy economy = Main.getEcon();
        double ammount = pD.getDouble("bankInfo.loan");
        p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "You got a loan of $" + ammount );
        economy.depositPlayer(p, ammount);
        logTransaction(p, coins, TransactionType.LOAN);

    }

    public static void payLoan(Player p) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        double coins = pD.getDouble("bankInfo.loan");
        Economy economy = Main.getEcon();
        double NEWcoins = pD.getDouble("bankInfo.loan") - economy.getBalance(p);
        if(!(NEWcoins <= 0)) {
            pD.set("bankInfo.loan", NEWcoins);
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "Good you still have $" + NEWcoins);
            Double payPercentage = getLoanAmount(p) / economy.getBalance(p);
            Double loginBonus = payPercentage * 40;
            int newLogins = (int) (getLogIns(p) - loginBonus);
            if(newLogins < 0) {
                newLogins = 0;
            }
            pD.set("bankInfo.logIns", newLogins);
            setCreditScore(p, (int) (getCreditScore(p) + 1));
            economy.withdrawPlayer(p, economy.getBalance(p));
            pD.save(playerData);
            return;
        }
        economy.withdrawPlayer(p, coins);
        double count = pD.getDouble("bankInfo.logIns");
        if(count > 15) {
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "You better not do that again....");
            setCreditScore(p, (int) (getCreditScore(p) - 10));
        }
        p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "HORRAY YOU PAYED YOUR LOAN!!!");
        pD.set("bankInfo.loan", 0);
        pD.set("bankInfo.logIns", 0);
        logTransaction(p, (int) coins, TransactionType.PAYLOAN);
        setCreditScore(p, (int) (getCreditScore(p) + 15));
        p.getInventory().addItem(getCollateralItem(p));
        if(getLogIns(p) >= 40) {
            restoreInventory(p);
            setCreditScore(p, (int) (getCreditScore(p) - 25));
        }
        pD.save(playerData);

    }

    public static boolean hasLoan(Player p) {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        double coins = pD.getDouble("bankInfo.loan");
        boolean has = false;

        if(coins >= 1) {
            has = true;
        }

        return has;
    }


    public static double getLogIns(Player p) {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        double count = pD.getDouble("bankInfo.logIns");

        return count;
    }

    public static void setLogins(Player p, int amount) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        pD.set("bankInfo.logIns", amount);
        pD.save(playerData);

    }


    public static ItemStack getCollateralItem(Player p) {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        ItemStack count = pD.getItemStack("Collateral.item");

        return count;
    }

    public static void setCollateralItem(Player p, ItemStack itemStack) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        pD.set("Collateral.item", itemStack);
        pD.save(playerData);

    }



    public static double getCreditScore(Player p) {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        double count = pD.getDouble("bankInfo.creditScore");

        return count;
    }

    public static void setCreditScore(Player p, int amount) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        if(amount >= 1250) {
            pD.set("bankInfo.creditScore", 1250);
            pD.save(playerData);
            CreditCard.setCreditBalance(p);
            return;
        } else if(amount <= 0) {
            pD.set("bankInfo.creditScore", 0);
            pD.save(playerData);
            CreditCard.setCreditBalance(p);
            return;
        }
        pD.set("bankInfo.creditScore", amount);
        pD.save(playerData);
        CreditCard.setCreditBalance(p);
    }



    public static void saveInventory(Player p) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        if(!(p.getInventory().getContents() == null)) {
            pD.set("inventory.armor", p.getInventory().getArmorContents());
        }
        if(!(p.getInventory().getArmorContents() == null) ){
            pD.set("inventory.content", p.getInventory().getContents());
        }
        pD.save(playerData);
    }

    @SuppressWarnings("unchecked")
    public static void restoreInventory(Player p) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        ItemStack[] content = ((List<ItemStack>) pD.get("inventory.armor")).toArray(new ItemStack[0]);
        p.getInventory().setArmorContents(content);
        content = ((List<ItemStack>) pD.get("inventory.content")).toArray(new ItemStack[0]);
        p.getInventory().setContents(content);
    }


    public static void logTransaction(Player p, int coins, TransactionType type) throws IOException {
        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if(coins >= 1) {
            pD.set("transactions." + p.getDisplayName() + "." + type.getLore() + " " + dtf.format(now), "+" + coins);
        } else {
            pD.set("transactions." + p.getDisplayName() + "." + type.getLore() + " " + dtf.format(now), "-" + coins);
        }
        pD.save(playerData);
        CreditCard.setCreditBalance(p);
    }

}
