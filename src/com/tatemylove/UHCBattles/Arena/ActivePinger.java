package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Tate on 10/17/2017.
 */
public class ActivePinger  extends BukkitRunnable {
    @Override
    public void run() {
        if(BaseArena.states == BaseArena.ArenaStates.Started) {
            if (UHC.redTeam.size() == 0) {
                for(Player p : Main.PlayingPlayers){
                    p.sendMessage(Main.prefix + "§3Blue has won!");
                }
                UHC.endUHC();
            } else if (UHC.blueTeam.size() == 0){
                for(Player p : Main.PlayingPlayers){
                    p.sendMessage(Main.prefix + "§cRed has won!");
                }
                UHC.endUHC();
            }
        }
    }
}
