package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Tate on 10/12/2017.
 */
public class InternalCountDown extends BukkitRunnable {
    public static int timeuntilstart = 1800;

    Main plugin;
    public InternalCountDown(Main pl) {
        plugin = pl;
    }

    @Override
    public void run() {
        if(BaseArena.states == BaseArena.ArenaStates.Started){
            if(timeuntilstart == 0){
                for(Player p : Main.PlayingPlayers){
                    p.sendMessage(Main.prefix + "§5Combat has started!!");
                    p.teleport((Location) p.getWorld().getHighestBlockAt(p.getLocation()));
                }
                plugin.stopCountDownInternal();
            }
        }
        timeuntilstart -= 1;
    }
}
