package me.TahaCheji.bank;

import com.mysql.fabric.xmlrpc.base.Array;
import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.Utl.NBTUtils;
import me.TahaCheji.Utl.RandomUtil;
import me.TahaCheji.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CreditCard {

    public static ItemStack getCreditCard(Player player) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "CreditCard");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        int[] cardNumber = {new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9)};
        int[] cvs = {new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9), new RandomUtil().getRandom(1, 9)};
        lore.add(ChatColor.GOLD + "CardNumber: " + cardNumber[0] + cardNumber[1] + cardNumber[2] + cardNumber[3] + " " + cardNumber[4] +
                cardNumber[5] + cardNumber[6] + cardNumber[7] + " " + cardNumber[8] + cardNumber[9] + cardNumber[10] + cardNumber[11] + " " + cardNumber[12] + cardNumber[13] + cardNumber[14] + cardNumber[15]);
        lore.add(ChatColor.GOLD + "DateOfCreation: " + dtf.format(now));
        lore.add("");
        lore.add(ChatColor.GOLD + "CVS: "+  cvs[0] + cvs[1] + cvs[2]);
        lore.add(ChatColor.GOLD + "CreditScore: " + (int) PlayerData.getCreditScore(player));
        lore.add("");
        lore.add(ChatColor.WHITE + "CardHolder: " + ChatColor.GOLD + player.getDisplayName());
        lore.add(ChatColor.WHITE + "CoinsOnCard: " + ChatColor.GOLD +  "$" + String.valueOf(PlayerData.getDepostitedCoins(player)));
        meta.setLore(lore);
        item.setItemMeta(meta);
        item = NBTUtils.setIntArray(item, "CardNumbers", cardNumber);
        item = NBTUtils.setString(item, "DateOfCreation", dtf.format(now));
        item = NBTUtils.setIntArray(item, "CVS", cvs);
        item = NBTUtils.setInt(item, "CreditScore", (int) PlayerData.getCreditScore(player));
        item = NBTUtils.setString(item, "ItemKey", "CreditCard");
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setUUID("CardHolder", player.getUniqueId());
        item = nbtItem.getItem();
        item = NBTUtils.setInt(item, "CoinsOnCard", (int) PlayerData.getDepostitedCoins(player));
        return item;
    }

    public static void setCreditBalance(Player player) {
        for(ItemStack itemStack : player.getInventory()) {
            if(itemStack == null) {
                continue;
            }
            if(itemStack.getItemMeta() == null) {
                continue;
            }
            if(new NBTItem(itemStack).getString("ItemKey").equalsIgnoreCase("CreditCard")) {
                ItemMeta meta = itemStack.getItemMeta();
                ArrayList<String> lore = new ArrayList<>();
                for (String string : meta.getLore()) {
                    lore.add(string);
                }
                lore.set(7, ChatColor.WHITE + "CoinsOnCard: " + ChatColor.GOLD + "$" + String.valueOf(PlayerData.getDepostitedCoins(player)));
                lore.set(4, ChatColor.GOLD + "CreditScore: " + (int) PlayerData.getCreditScore(player));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                NBTUtils.setInt(itemStack, "CoinsOnCard", (int) PlayerData.getDepostitedCoins(player));
            }
        }
    }


}
