package me.TahaCheji.data;

import me.TahaCheji.Main;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Loan {

    private final int LoanAmount;
    private final Player player;

    public Loan (int loanAmount, Player player) {
        this.LoanAmount = loanAmount;
        this.player = player;
        if(Main.loanHashMap.containsValue(this) && Main.loanHashMap.containsKey(player.getUniqueId())) {
            Main.loanHashMap.remove(player.getUniqueId(), this);
        } else {
            Main.loanHashMap.put(player.getUniqueId(), this);
        }
    }

    public void getLoan() throws IOException {
        PlayerData.setLoanAmount(player, LoanAmount);
    }

    public int getLoanAmount() {
        return LoanAmount;
    }

    public Player getPlayer() {
        return player;
    }
}
