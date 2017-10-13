package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import org.bukkit.Location;
import org.bukkit.World;
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
                    Location loc = new Location(p.getWorld(),p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getBlockZ());
                    loc.setY(loc.getWorld().getHighestBlockYAt(loc));
                    p.teleport(loc);
                }
                plugin.stopCountDownInternal();
            }
            if(timeuntilstart % 60 == 0){
                for(Player p : Main.PlayingPlayers){
                    p.sendMessage(Main.prefix + timeuntilstart/60 + " minutes left to prepare!");
                }
            }
        }
        timeuntilstart -= 1;
    }
}
