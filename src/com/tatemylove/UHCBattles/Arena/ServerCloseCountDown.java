package com.tatemylove.UHCBattles.Arena;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Tate on 10/22/2017.
 */
public class ServerCloseCountDown extends BukkitRunnable {
    public static int timeuntilend;
    @Override
    public void run() {
        if(timeuntilend == 0){
            Bukkit.shutdown();
        }
        timeuntilend -= 1;
    }
}
