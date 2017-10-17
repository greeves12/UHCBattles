package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Files.ArenaFile;
import com.tatemylove.UHCBattles.Main;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
                        p.getInventory().clear();

                        p.teleport(GetArena.getRedSpawn());
                        p.sendMessage(Main.prefix + "§cYou have joined the §lRED §cteam!");
                        p.setGameMode(GameMode.SURVIVAL);
                        p.setFoodLevel(20);
                        p.setHealth(20);

                        Color c = Color.fromRGB(255, 0, 0);
                        p.getInventory().setHelmet(getColorArmor(Material.LEATHER_HELMET, c));
                        p.getInventory().setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE, c));
                        p.getInventory().setLeggings(getColorArmor(Material.LEATHER_LEGGINGS, c));
                        p.getInventory().setBoots(getColorArmor(Material.LEATHER_BOOTS, c));
                    }else if(blueTeam.contains(p)){
                        p.getInventory().clear();

                        Color c = Color.fromRGB(0,0,255);
                        p.teleport(GetArena.getBlueSpawn());
                        p.sendMessage(Main.prefix + "§bYou have joined the §3§lBLUE §bteam!");
                        p.setGameMode(GameMode.SURVIVAL);
                        p.setFoodLevel(20);
                        p.setHealth(20);

                        p.getInventory().setHelmet(getColorArmor(Material.LEATHER_HELMET, c));
                        p.getInventory().setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE, c));
                        p.getInventory().setLeggings(getColorArmor(Material.LEATHER_LEGGINGS, c));
                        p.getInventory().setBoots(getColorArmor(Material.LEATHER_BOOTS, c));
                    }
                }
            }
        }
    }
    private static ItemStack getColorArmor(Material m, Color c) {
        ItemStack i = new ItemStack(m, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
        meta.setColor(c);
        i.setItemMeta(meta);
        return i;
    }

    public static void endUHC(String id){
        if(ArenaFile.getData().contains("Arenas." + id + ".Name")){
            if(BaseArena.states == BaseArena.ArenaStates.Started){
                for (int ID = 0; ID < pls.size(); ID++) {
                    final Player p = pls.get(ID);
                    if (redTeam.size() < blueTeam.size()) {
                        p.sendMessage(Main.prefix + "Blue team has won!");
                    }
                    if(blueTeam.size() < redTeam.size()){
                        p.sendMessage(Main.prefix + "Red team has won!");
                    }
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try{
                        out.writeUTF("Connect");
                        out.writeUTF("lobby");
                    }catch (IOException e){

                    }
                    p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
                    Bukkit.shutdown();
                }
                BaseArena.states = BaseArena.ArenaStates.Countdown;
                Main.PlayingPlayers.clear();
                redTeam.clear();
                blueTeam.clear();
                pls.clear();
                Team.clear();
            }
        }
    }
}

