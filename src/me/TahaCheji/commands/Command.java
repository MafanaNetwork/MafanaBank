package me.TahaCheji.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.Main;
import me.TahaCheji.bank.CreditCard;
import me.TahaCheji.bank.LoanClickEvent;
import me.TahaCheji.bank.LoanGui;
import me.TahaCheji.data.Loan;
import me.TahaCheji.data.PlayerData;
import me.TahaCheji.data.TransactionType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class Command implements CommandExecutor {

   public static int coin;
    private Object InventoryClickEvent;

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("MafanaBank")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("You cannot do this!");
                return true;
            }
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + " /MafanaBank SetCoins Amount OR /MafanaBank SavedCoins");
                return true;
            }
            if(args[0].equalsIgnoreCase("DepositCoins")) {
                if(args[1] == null ) {
                    player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE +  "/MafanaBank SetCoins Amount");
                    return false;
                }
                int coins = Integer.parseInt(args[1]);
                double amount = PlayerData.getDepostitedCoins(player) + coins;
                Economy economy = Main.getEcon();
                if(coins > economy.getBalance(player)) {
                    player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "That is way to much!");
                    return true;
                }
                player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "You Deposited $" + coins);
                    try {
                        PlayerData.setDepostitedCoins(player, (int) (PlayerData.getDepostitedCoins(player) + (int) amount), TransactionType.DEPOSIT);
                        economy.withdrawPlayer(player, coins);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
            }
            if(args[0].equalsIgnoreCase("WithdrawCoins")) {
                if(args.length == 1) {
                    return true;
                }
                int coins = Integer.parseInt(args[1]);
                double amount = PlayerData.getDepostitedCoins(player) - coins;
                Economy economy = Main.getEcon();
                if(coins > PlayerData.getDepostitedCoins(player)) {
                    player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "That is way to much!");
                    return true;
                }
                player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "You Withdrew $" + coins);
                try {
                    PlayerData.setDepostitedCoins(player, (int) amount , TransactionType.WITHDRAW);
                    economy.depositPlayer(player, coins);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("DepositedCoins")) {
                player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "You have $" + PlayerData.getDepostitedCoins(player) + " saved in your bank!");
                return true;
            }
            if(args[0].equalsIgnoreCase("GetLoan")) {
                if(args.length == 1) {
                    player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE +  "/MafanaBank GetLoan Amount");
                    return true;
                }
                if(PlayerData.hasLoan(player)) {
                    player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE +  "You already have a loan outgoing pay it up...");
                    return true;
                }
                int coins = Integer.parseInt(args[1]);
                if(coins > 25000 * PlayerData.getCreditScore(player) / 1250) {
                    player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE +  "TOO MUCH MONEY BIG GUY! You can only get a loan lower then $" + 25000 * PlayerData.getCreditScore(player) / 1250);
                    return true;
                }
                   Loan loan = new Loan(coins, player);
                   player.openInventory(new LoanGui(loan).getInventory());
                return true;
            }
            if(args[0].equalsIgnoreCase("GetLoanAmount")) {
                player.sendMessage(ChatColor.GOLD + "$" + String.valueOf(PlayerData.getLoanAmount(player)));
                return true;
            }
            if(args[0].equalsIgnoreCase("PayLoan")) {
                if(PlayerData.hasLoan(player) == false) {
                    player.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE +  "You dont have a loan to pay...");
                    return true;
                }
                try {
                    PlayerData.payLoan(player);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("ResetBankCoins")) {
                player.sendMessage(ChatColor.GOLD + "MafanaBank: "  + "Reset Your Bank");
                try {
                    PlayerData.setDepostitedCoins(player, 0, TransactionType.RESET);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("Swipe")) {
                if(args.length == 1) {
                    player.sendMessage("You need a amount");
                    return false;
                }
                int coins = Integer.parseInt(args[1]);
                ItemStack itemStack = player.getItemInHand();
                if(itemStack == null || !new NBTItem(itemStack).getString("ItemKey").equalsIgnoreCase("CreditCard") ) {
                    player.sendMessage("Not a card");
                    return true;
                }
                Player newPlayer = Bukkit.getPlayer(new NBTItem(itemStack).getUUID("CardHolder"));
                if(newPlayer == null) return true;
                Economy economy = Main.getEcon();
                try {
                    if(coins < PlayerData.getDepostitedCoins(newPlayer)) {
                        PlayerData.setFakeDepostitedCoins(newPlayer, (int) (PlayerData.getDepostitedCoins(newPlayer) - coins), TransactionType.WITHDRAW);
                        CreditCard.setCreditBalance(newPlayer);
                        economy.depositPlayer(player, coins);
                    } else {
                        player.sendMessage("That is way to much!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }
}
