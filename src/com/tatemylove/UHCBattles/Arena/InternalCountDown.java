package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import com.tatemylove.UHCBattles.Utilities.SendCoolMessages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Tate on 10/12/2017.
 */
public class InternalCountDown extends BukkitRunnable {
    public static int timeuntilstart;

    Main plugin;
    public InternalCountDown(Main pl) {
        plugin = pl;
    }

    @Override
    public void run() {
        if(BaseArena.states == BaseArena.ArenaStates.Started){
            if(timeuntilstart == 0){
                plugin.stopCountDownInternal();
                for(Player p : Main.PlayingPlayers){
                    SendCoolMessages.sendTitle(p,Main.prefix + "§5§lCombat has started!!", 30, 50, 30);
                    p.sendMessage(Main.prefix + "§5§lCombat has started!!");
                    Location location = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
                    location.setX( p.getLocation().getX() + Math.random() * 3);
                    location.setZ( p.getLocation().getZ() + Math.random() * 3);
                    location.setY(p.getWorld().getHighestBlockAt(location.getBlockX(), location.getBlockZ()).getY());
                    p.teleport(location);

                }
                Main.startShrink();
            }
            if(timeuntilstart > 60) {
            if(timeuntilstart % 300 == 0){
                    for (Player p : Main.PlayingPlayers) {
                        SendCoolMessages.sendTitle(p, "§b§l" + String.valueOf(timeuntilstart/60) + " §aminutes", 20, 50, 20);
                        p.sendMessage(Main.prefix + "§b§lYou have §a§l" + timeuntilstart / 60 + " minutes§b§l to get prepared!");
                    }
                }
            }
            if(timeuntilstart <= 60) {
                if(timeuntilstart % 10 == 0) {
                    for (Player p : Main.PlayingPlayers) {
                        SendCoolMessages.sendTitle(p, "§b§l" + String.valueOf(timeuntilstart) + " §aseconds", 20, 50, 20);
                        p.sendMessage(Main.prefix + "§b§lYou have §a§l" + timeuntilstart + " seconds§b§l to get prepared!");
                    }
                }
            }
        }
        timeuntilstart -= 1;
    }
}
