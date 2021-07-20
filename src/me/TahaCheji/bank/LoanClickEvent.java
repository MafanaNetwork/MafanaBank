package me.TahaCheji.bank;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.Main;
import me.TahaCheji.commands.Command;
import me.TahaCheji.data.Loan;
import me.TahaCheji.data.PlayerData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoanClickEvent implements Listener {

    BukkitTask t;


    public void clickEvent(InventoryClickEvent e) throws IOException {
        Player player = (Player) e.getWhoClicked();
        for (Loan loan : Main.loanHashMap.values()) {
            System.out.println("1");
            if(!player.getUniqueId().equals(loan.getPlayer().getUniqueId())) {
                System.out.println("2");
                return;
            }
            if (!e.getView().getTitle().contains("Loan")) {
                return;
            }
            if (e.getCurrentItem() == null) {
                continue;
            }
            if (e.getCurrentItem().getItemMeta() == null) {
                continue;
            }
            e.setCancelled(e.getSlot() != 2 && e.getClickedInventory() != e.getWhoClicked().getInventory());
            if (e.getSlot() == 2) {
                    if (e.getInventory().getItem(2) == null) {
                        e.getInventory().setItem(6, new ItemStack(Material.AIR));
                        return;
                    }

                    if (!new NBTItem(Objects.requireNonNull(e.getInventory().getItem(2))).hasKey("value")) {
                        return;
                    }

                        int itemValue = new NBTItem(Objects.requireNonNull(e.getInventory().getItem(2))).getInteger("value") * Objects.requireNonNull(e.getInventory().getItem(2)).getAmount();

                        ItemStack info = new ItemStack(Material.PAPER);
                        ItemMeta meta = info.getItemMeta();
                        List<String> infoLore = new ArrayList<>();
                        meta.setDisplayName(ChatColor.DARK_PURPLE + "Loan Info");
                        infoLore.add(ChatColor.GOLD + "Loan Amount: $" + loan.getLoanAmount());
                        infoLore.add(ChatColor.GOLD + "Item Value Amount: $" + itemValue);
                        infoLore.add("");
                        infoLore.add("The item value must > or = to the loan amount-");
                        infoLore.add("so you can get your loan");
                        meta.setLore(infoLore);
                        info.setItemMeta(meta);
                        e.getInventory().setItem(4, info);

                        ItemStack itemStack;
                        if (itemValue >= loan.getLoanAmount()) {
                            itemStack = new ItemStack(Material.GOLD_INGOT);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName(ChatColor.GOLD + "Loan");
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(ChatColor.GOLD + "Loan Amount: $" + loan.getLoanAmount());
                            lore.add(ChatColor.GRAY + "(Click To claim)");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                        } else {
                            itemStack = new ItemStack(Material.BARRIER);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName(ChatColor.GOLD + "Error");
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(ChatColor.RED + "The item you have does not have the same value as the loan.");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                        }
                        e.getInventory().setItem(6, itemStack);

                        System.out.println("5");
            }

            if (e.getSlot() == 6) {
                if (e.getCurrentItem() == null) {
                    return;
                }
                if (e.getCurrentItem().getItemMeta() == null) {
                    return;
                }
                if(!(e.getCurrentItem().getItemMeta().getDisplayName().contains("Loan"))) {
                    player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_NO,  10, 10);
                    return;
                }
                loan.getLoan();
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 10);
                player.closeInventory();
                System.out.println("4");
            }
        }
    }


    @EventHandler
    public void loanClick(InventoryClickEvent e) throws IOException {
        Player player = (Player) e.getWhoClicked();
        for (Loan loan : Main.loanHashMap.values()) {
            if (!player.getUniqueId().equals(loan.getPlayer().getUniqueId())) {
                return;
            }
            if (!e.getView().getTitle().contains("Loan")) {
                return;
            }
            if (e.getCurrentItem() == null) {
                continue;
            }
            if (e.getCurrentItem().getItemMeta() == null) {
                continue;
            }
            e.setCancelled(e.getSlot() != 2 && e.getClickedInventory() != e.getWhoClicked().getInventory());
            t = new BukkitRunnable() {
                @Override
                public void run() {
                    if (e.getInventory().getItem(2) == null) {
                        e.getInventory().setItem(6, new ItemStack(Material.AIR));
                        return;
                    }
                    if (!new NBTItem(Objects.requireNonNull(e.getInventory().getItem(2))).hasKey("value")) {
                        return;
                    }
                    int itemValue = new NBTItem(Objects.requireNonNull(e.getInventory().getItem(2))).getInteger("value") * Objects.requireNonNull(e.getInventory().getItem(2)).getAmount();

                    ItemStack info = new ItemStack(Material.PAPER);
                    ItemMeta meta = info.getItemMeta();
                    List<String> infoLore = new ArrayList<>();
                    meta.setDisplayName(ChatColor.DARK_PURPLE + "Loan Info");
                    infoLore.add(ChatColor.GOLD + "Loan Amount: $" + loan.getLoanAmount());
                    infoLore.add(ChatColor.GOLD + "Item Value Amount: $" + itemValue);
                    infoLore.add("");
                    infoLore.add("The item value must > or = to the loan amount-");
                    infoLore.add("so you can get your loan");
                    meta.setLore(infoLore);
                    info.setItemMeta(meta);
                    e.getInventory().setItem(4, info);

                    ItemStack itemStack;
                    if (itemValue >= loan.getLoanAmount()) {
                        itemStack = new ItemStack(Material.GOLD_INGOT);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GOLD + "Loan");
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD + "Loan Amount: $" + loan.getLoanAmount());
                        lore.add(ChatColor.GRAY + "(Click To Claim)");
                        itemMeta.setLore(lore);
                        itemStack.setItemMeta(itemMeta);
                    } else {
                        itemStack = new ItemStack(Material.BARRIER);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GOLD + "Error");
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.RED + "Not the same value.");
                        itemMeta.setLore(lore);
                        itemStack.setItemMeta(itemMeta);
                    }
                    e.getInventory().setItem(6, itemStack);
                    if (e.getSlot() == 6) {
                        if (e.getCurrentItem() == null) {
                            return;
                        }
                        if (e.getCurrentItem().getItemMeta() == null) {
                            return;
                        }
                        if (!(e.getCurrentItem().getItemMeta().getDisplayName().contains("Loan"))) {
                            player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_NO, 10, 10);
                            return;
                        }
                        t.cancel();
                        try {
                            loan.getLoan();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 10);
                        try {
                            PlayerData.setCollateralItem(player, e.getInventory().getItem(2));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        player.closeInventory();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 5L, 0L);

        }
    }

        @EventHandler
    public void onClose(InventoryCloseEvent e) {
            if (!e.getView().getTitle().contains("Loan")) {
                return;
            }
            if(e.getInventory().getItem(2) == null) {
                return;
            }
            Player player = (Player) e.getPlayer();
            player.getInventory().addItem(e.getInventory().getItem(2));
            t.cancel();
        }

}
