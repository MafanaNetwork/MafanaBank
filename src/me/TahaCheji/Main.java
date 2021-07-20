package me.TahaCheji;

import me.TahaCheji.Utl.Files;
import me.TahaCheji.bank.LoanClickEvent;
import me.TahaCheji.bank.npc.JohnTheBanker;
import me.TahaCheji.commands.Command;
import me.TahaCheji.data.Loan;
import me.TahaCheji.events.PlayerJoin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Economy econ = null;
    public static HashMap<UUID, Loan> loanHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        Location location = new Location(Bukkit.getWorld("world"), 1, 1, 1);
        //JohnTheBanker.JohnTheBanker(location);
        if (!setupEconomy() ) {
            System.out.print("No econ plugin found.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            Files.initFiles();
        } catch (IOException | InvalidConfigurationException e2) {
            e2.printStackTrace();
        }
        RegisterEvents();
        RegisterCommands();
    }

    @Override
    public void onDisable() {

    }


    public void RegisterEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new LoanClickEvent(), this);
    }

    public void RegisterCommands() {
        getCommand("MafanaBank").setExecutor(new Command());
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static Main getInstance() {
        return instance;
    }



}
