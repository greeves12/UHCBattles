package com.tatemylove.UHCBattles;

import com.tatemylove.UHCBattles.Arena.BaseArena;
import com.tatemylove.UHCBattles.Arena.GameCountDown;
import com.tatemylove.UHCBattles.Arena.InternalCountDown;
import com.tatemylove.UHCBattles.Commands.MainCommand;
import com.tatemylove.UHCBattles.Files.AchievementFile;
import com.tatemylove.UHCBattles.Files.ArenaFile;
import com.tatemylove.UHCBattles.Files.LobbyFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public static String prefix = "§6[UHCBattles] ";
    public static ArrayList<Player> WaitingPlayers = new ArrayList<>();
    public static ArrayList<Player> PlayingPlayers = new ArrayList<>();
    public static int min_players = 0;
    public static int max_players = 10;
    public static int startCountDownId;
    public static int startCountDownInternal;

    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        ArenaFile.setup(this);
        AchievementFile.setup(this);
        LobbyFile.setup(this);

        MainCommand cmd = new MainCommand(this);
        getCommand("battles").setExecutor(cmd);
    }
    public void startCountDown(){
        startCountDownId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameCountDown(this), 0L, 20L);
        GameCountDown.TimeUntilStart = 30;
    }

    public void stopCountDown(){
        getServer().getScheduler().cancelTask(startCountDownId);
    }
    public void restartCountDown(){
        stopCountDown();
        startCountDown();
    }

    public void startCountDownInternal(){
        startCountDownInternal = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new InternalCountDown(this), 0L, 20L);
        InternalCountDown.timeuntilstart = 1800;
    }
    public void stopCountDownInternal(){
        getServer().getScheduler().cancelTask(startCountDownInternal);
    }
    public void restartCountDownInternal(){
        stopCountDownInternal();
        startCountDownInternal();
    }
}
