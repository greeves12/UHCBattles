package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Files.ArenaFile;
import com.tatemylove.UHCBattles.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UHC {
    public static HashMap<Player, String> Team = new HashMap<>();
    public static ArrayList<Player> redTeam = new ArrayList<>();
    public static ArrayList<Player> blueTeam = new ArrayList<>();
    public static ArrayList<Player> pls = Main.PlayingPlayers;
    public static int time = InternalCountDown.timeuntilstart/60;

    public static void AssignTeam(String id) {
        if (BaseArena.states == BaseArena.ArenaStates.Started) {
            redTeam.clear();
            blueTeam.clear();
            Team.clear();
            pls.clear();

            if (ArenaFile.getData().contains("Arenas." + id + ".Name")) {
                Main.PlayingPlayers.addAll(Main.WaitingPlayers);
                Main.WaitingPlayers.clear();
                for (int assign = 0; assign < Main.PlayingPlayers.size(); assign++) {
                    Player p = Main.PlayingPlayers.get(assign);

                    if (redTeam.size() < blueTeam.size()) {
                        redTeam.add(p);
                    } else if (blueTeam.size() < redTeam.size()) {
                        blueTeam.add(p);
                    } else {
                        Random RandomTeam = new Random();
                        int TeamID = 0;
                        TeamID = RandomTeam.nextInt(2);
                        if (TeamID == 0) {
                            redTeam.add(p);
                        } else {
                            blueTeam.add(p);
                        }
                    }
                    if (redTeam.contains(p)) {
                        Team.put(p, "Red");
                    } else {
                        Team.put(p, "Blue");
                    }

                    continue;
                }
            }
        }
    }

    public static void startUHC(String id){
        if(ArenaFile.getData().contains("Arenas." + id + ".Name")) {
            if(BaseArena.states == BaseArena.ArenaStates.Started){
                for(int ID = 0; ID < pls.size(); ID++){
                    final Player p = pls.get(ID);

                    if(redTeam.contains(p)){
                        p.teleport(getArena.getRedSpawn());
                        p.sendMessage(Main.prefix + "§cYou have joined the §lRED §cteam!");
                        p.sendMessage(Main.prefix + "§b§lYou have §a§l" + time + " minutes§b§l to get prepared!");
                        p.setFoodLevel(20);
                        p.setHealth(20);
                    }else if(blueTeam.contains(p)){
                        p.teleport(getArena.getBlueSpawn());
                        p.sendMessage(Main.prefix + "§3You have joined the §lBLUE team!");
                        p.sendMessage(Main.prefix + "§b§lYou have §a§l" + time + " minutes§b§l to get prepared!");
                        p.setFoodLevel(20);
                        p.setHealth(20);
                    }
                }
            }
        }

    }
}

