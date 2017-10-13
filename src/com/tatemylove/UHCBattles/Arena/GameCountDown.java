package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Files.ArenaFile;
import com.tatemylove.UHCBattles.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class GameCountDown extends BukkitRunnable{

    public static int TimeUntilStart = 30;

    final ArrayList<String> arena = new ArrayList<>();

    Main plugin;
    public GameCountDown(Main pl){
        plugin = pl;
    }


    @Override
    public void run() {
        arena.add(ArenaFile.getData().getString("Arenas." + GetArena.getNextArena() + ".Name"));
        if (BaseArena.states == BaseArena.ArenaStates.Countdown) {
            if (TimeUntilStart == 0) {
                if (Main.WaitingPlayers.size() <= Main.min_players) {
                    plugin.restartCountDown();
                    for (Player p : Main.WaitingPlayers) {
                        p.sendMessage(Main.prefix + "§cNot enough players, restarting countdown");
                    }
                    return;
                }
                BaseArena.states = BaseArena.ArenaStates.Started;
                UHC.AssignTeam(Integer.toString(GetArena.getCurrentArena()));
                UHC.startUHC(Integer.toString(GetArena.getCurrentArena()));
                arena.clear();
                plugin.stopCountDown();
                plugin.startCountDownInternal();
            }
            if ((TimeUntilStart % 10 == 0) || (TimeUntilStart < 0)) {
                for (Player p : Main.WaitingPlayers) {
                    p.sendMessage(Main.prefix + TimeUntilStart + " §aseconds until start! Next Arena is §5" + GetArena.getNextArena());
                }
            }
            TimeUntilStart -= 1;
        }
    }
}
