package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Tate on 10/30/2017.
 */
public class ShrinkBorder extends BukkitRunnable {
    public static int timeuntilshrink;
    public static double size= 450;
    @Override
    public void run() {
        if (BaseArena.states == BaseArena.ArenaStates.Started) {
            if (timeuntilshrink == 0) {
                Main.stopShrink();
                for (Player p : Main.PlayingPlayers) {
                    p.sendMessage(Main.prefix + "§bBorder is final and won't shrink");
                }
            }
            if (timeuntilshrink % 300 == 0) {
                WorldBorder wb = Bukkit.getWorld("uhc").getWorldBorder();
                wb.setCenter(-145, -1121);
                wb.setSize(size);
                size -= 50;
                for(Player p : Main.PlayingPlayers){
                    p.sendMessage(Main.prefix + "§3Border has shrunk new size is §a" + wb.getSize());
                }
            }
        }
        timeuntilshrink -=1;
    }
}
