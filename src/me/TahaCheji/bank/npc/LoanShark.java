package me.TahaCheji.bank.npc;

import me.TahaCheji.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mcmonkey.sentinel.SentinelIntegration;
import org.mcmonkey.sentinel.SentinelTrait;

public class LoanShark extends SentinelIntegration implements Listener {

    public static void LoanShark(Player player) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC LoanShark = registry.createNPC(EntityType.PLAYER, "shakinsharky");
        LoanShark.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Loan Shark" + ChatColor.GRAY + "(Npc Fighter)");
        LoanShark.setProtected(true);
        SkinTrait trait = LoanShark.getOrAddTrait(SkinTrait.class);
        trait.setSkinName("Blitszy_60");
        SentinelTrait sentinel = LoanShark.getOrAddTrait(SentinelTrait.class);
        sentinel.addTarget("player");
        sentinel.setHealth(10);
        sentinel.damage = 10;
        LoanShark.spawn(player.getLocation().add(0, 0, 2));
        player.sendMessage(ChatColor.GOLD + "LoanShark: PAY UP!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 10, 10);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                LoanShark.despawn();
            }
        }, 100L);

    }

}
