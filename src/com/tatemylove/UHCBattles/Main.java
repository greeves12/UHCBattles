package com.tatemylove.UHCBattles;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.tatemylove.UHCBattles.Arena.*;
import com.tatemylove.UHCBattles.Commands.MainCommand;
import com.tatemylove.UHCBattles.Files.AchievementFile;
import com.tatemylove.UHCBattles.Files.ArenaFile;
import com.tatemylove.UHCBattles.Files.LobbyFile;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public static String prefix = "§6§l[§b§lU§c§lH§a§lC§f§l-§5§lB§3§la§2§lt§d§lt§8§ll§7§le§a§ls§6§l] ";
    public static ArrayList<Player> WaitingPlayers = new ArrayList<>();
    public static ArrayList<Player> PlayingPlayers = new ArrayList<>();
    public static int min_players = 2;
    public static int startCountDownId;
    public static int startCountDownInternal;
    public static int finalcountdown;
    private ProtocolManager protocolManager;
    public static int closeCountDownid;



    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        protocolManager = ProtocolLibrary.getProtocolManager();
        BaseArena.states = BaseArena.ArenaStates.Countdown;
        startCountDown();

        ActivePinger pinger = new ActivePinger();
        pinger.runTaskTimerAsynchronously(this, 50, 5);

        ArenaFile.setup(this);
        AchievementFile.setup(this);
        LobbyFile.setup(this);
        ThisPlugin.getPlugin().getConfig().options().copyDefaults(true);
        ThisPlugin.getPlugin().saveDefaultConfig();
        ThisPlugin.getPlugin().reloadConfig();

        MainCommand cmd = new MainCommand(this);
        getCommand("battles").setExecutor(cmd);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }
    public void startCountDown(){
        startCountDownId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameCountDown(this), 0L, 20L);
        GameCountDown.TimeUntilStart = 80;
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

    public static void startFinalCountdown(){
        finalcountdown = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ThisPlugin.getPlugin(), new FinalCountdown(), 0L, 20L);
        FinalCountdown.timeuntilend = 5;
    }
    public static void stopFinalCountdown(){
        Bukkit.getServer().getScheduler().cancelTask(finalcountdown);
    }
    public static void startCloseCountDown(){
        closeCountDownid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ThisPlugin.getPlugin(), new ServerCloseCountDown(), 0L, 20L);
        ServerCloseCountDown.timeuntilend = 3;
    }
}
