package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import org.bukkit.Bukkit;
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
                UHC.endUHC();
            } else if (UHC.blueTeam.size() == 0){
                UHC.endUHC();
            }
        }
        if(BaseArena.states == BaseArena.ArenaStates.Ended || BaseArena.states == BaseArena.ArenaStates.Started){
            if(Bukkit.getOnlinePlayers().isEmpty()){
                Bukkit.shutdown();
            }
        }
    }
}
