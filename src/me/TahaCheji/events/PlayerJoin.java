package me.TahaCheji.events;

import de.tr7zw.nbtapi.NBTItem;
import jdk.nashorn.internal.ir.LiteralNode;
import me.TahaCheji.bank.CreditCard;
import me.TahaCheji.bank.npc.LoanShark;
import me.TahaCheji.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerJoin implements Listener {



    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        Player p = e.getPlayer();



        File playerData = new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        if (!new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString()).exists()) {
            new File("plugins/MafanaBank/playerData/" + p.getUniqueId().toString()).mkdir();
        }
        if (!playerData.exists()) {
            playerData.createNewFile();
            pD.set("bankInfo.name", p.getDisplayName());
            pD.set("bankInfo.deposited", 0);
            pD.set("bankInfo.loan", 0);
            pD.set("bankInfo.logIns", 0);
            pD.set("bankInfo.creditScore", 275);
            pD.set("bankInfo.creditCard", false);
            pD.save(playerData);
        }

        if(!pD.getBoolean("bankInfo.creditCard")) {
            p.getInventory().addItem(CreditCard.getCreditCard(p));
            pD.set("bankInfo.creditCard", true);
            pD.save(playerData);
        }

        double count = pD.getDouble("bankInfo.logIns"); //how many times you log in resets after you pay your loan
        if(PlayerData.hasLoan(p)) {
            pD.set("bankInfo.logIns", (int) (PlayerData.getLogIns(p) + 1));
            System.out.println(PlayerData.getLogIns(p));
            pD.save(playerData);
        }
        double persentiage = 2;
        if(count == 15) {
            double loanAmountPersentiage = PlayerData.getLoanAmount(p) * 10 / 100;
            double loanAmount = PlayerData.getLoanAmount(p) + loanAmountPersentiage;
            pD.set("bankInfo.loan", loanAmount);
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "15 Logins.");
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "Loan Shark has increased your loan amount by 10%. Better Pay Up.");
            LoanShark.LoanShark(p);
            pD.save(playerData);
        }
        if(count == 30) {
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "30 Logins.");
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "Loan Shark will wipe your inventory if you dont pay up soon...");
            LoanShark.LoanShark(p);
        }
        if(count == 40) {
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "40 Logins.");
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "Loan Shark wiped your inventory you can get it back at the bank... for a price...");
            LoanShark.LoanShark(p);
            PlayerData.saveInventory(p);
            p.getInventory().clear();
            p.getInventory().setContents(null);
            pD.save(playerData);
        }
        if (PlayerData.hasLoan(p)) {
            double coins = pD.getDouble("bankInfo.loan");
            double loanAmountPersentiage = coins * persentiage / 100;
            double loanAmount = coins + loanAmountPersentiage;
            pD.set("bankInfo.loan", loanAmount);;
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "Loan Shark has increased your loan amount by 2% every login. Better Pay Up.");
            p.sendMessage(ChatColor.GOLD + "MafanaBank: " + ChatColor.WHITE + "You still have $" + loanAmount);
            pD.save(playerData);
        }



    }



}
