package me.TahaCheji.bank;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.data.Loan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoanGui implements InventoryHolder {

    Inventory gui;

    public LoanGui(Loan loan) {
        gui = Bukkit.createInventory(null, 9, ChatColor.GRAY + "" + ChatColor.BOLD + "Loan Collateral");
        List<String> lore = new ArrayList<>();
        ItemStack newItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta newmeta = newItem.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + "");
        newmeta.setLore(lore);
        newItem.setItemMeta(newmeta);

        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta meta = info.getItemMeta();
        List<String> infoLore = new ArrayList<>();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Loan Info");
        infoLore.add(ChatColor.GOLD + "Loan Amount: $" + loan.getLoanAmount());
        infoLore.add(ChatColor.GOLD + "Item Value Amount: ");
        infoLore.add("");
        infoLore.add("The item value must > or = to the loan amount-");
        infoLore.add("so you can get your loan");
        meta.setLore(infoLore);
        info.setItemMeta(meta);

        gui.setItem(0, newItem);
        gui.setItem(1, newItem);
        gui.setItem(2, null);
        gui.setItem(3, newItem);
        gui.setItem(4, info);
        gui.setItem(5, newItem);
        gui.setItem(6, null);
        gui.setItem(7, newItem);
        gui.setItem(8, newItem);
    }


    @Override
    public Inventory getInventory() {
        return gui;
    }
}
