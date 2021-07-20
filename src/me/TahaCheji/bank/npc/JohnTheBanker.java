package me.TahaCheji.bank.npc;

import me.TahaCheji.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mcmonkey.sentinel.SentinelTrait;

public class JohnTheBanker implements Listener {

    public static void JohnTheBanker(Location location) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC LoanShark = registry.createNPC(EntityType.PLAYER, "shakinsharky");
        LoanShark.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "John The Banker" + ChatColor.GRAY + " [Banker]");
        SkinTrait trait = LoanShark.getOrAddTrait(SkinTrait.class);
        trait.setSkinName("Reikela");
        LoanShark.setProtected(true);
        LoanShark.spawn(location);
    }


}
