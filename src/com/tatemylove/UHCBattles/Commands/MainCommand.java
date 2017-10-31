package com.tatemylove.UHCBattles.Commands;

import com.tatemylove.UHCBattles.Arena.SetLobby;
import com.tatemylove.UHCBattles.Arena.UHC;
import com.tatemylove.UHCBattles.Main;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MainCommand implements CommandExecutor {

    Main plugin;

    public MainCommand (Main pl){
        plugin = pl;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("create")) {
                if (p.hasPermission("uhc.create")) {
                    if (args.length == 2) {
                        String name = args[1];
                        CreateArena.createArena(p, name);
                    }
                }
            }
            if(args[0].equalsIgnoreCase("set")){
                if(p.hasPermission("uhc.set")){
                    String k = args[1];
                    int id = Integer.parseInt(k);
                    CreateArena.setSpawns(p, args, id);
                }
            }
            if(args[0].equalsIgnoreCase("setlobby")){
                if(p.hasPermission("uhc.lobbyset")){
                    SetLobby.setLobby(p);
                    p.sendMessage(Main.prefix + "§aYou have set the lobby");
                }
            }
            if(args[0].equalsIgnoreCase("leave")){
                if(Main.WaitingPlayers.contains(p)){
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try{
                        out.writeUTF("Connect");
                        out.writeUTF("uhclobby");
                    }catch (IOException e){

                    }
                    p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
                }
            }
            if(args[0].equalsIgnoreCase("pack")){
                if(Main.PlayingPlayers.contains(p)){
                    if(UHC.blueTeam.contains(p)){
                        p.openInventory(UHC.blueTeamBack);
                    }
                    if(UHC.redTeam.contains(p)){
                        p.openInventory(UHC.redTeamBack);
                    }
                }
            }
            if(args[0].equalsIgnoreCase("sc")){
                if(Main.PlayingPlayers.contains(p)){
                    if(UHC.blueTeam.contains(p)){
                        double x = p.getLocation().getX();
                        double y = p.getLocation().getY();
                        double z = p.getLocation().getZ();

                        for(Player pp : UHC.blueTeam){
                            pp.sendMessage("§3[Blue] §l" + p.getName() + " §3coordinates are");
                            pp.sendMessage("X: " + String.valueOf(Math.floor(x)) + ", Y:" + Math.floor(y) + ", Z:" + Math.floor(z));
                        }
                    }
                    if(UHC.redTeam.contains(p)){
                        double x = p.getLocation().getX();
                        double y = p.getLocation().getY();
                        double z = p.getLocation().getZ();

                        for(Player pp : UHC.redTeam){
                            pp.sendMessage("§c[Red] §l" + p.getName() + " §ccoordinates are");
                            pp.sendMessage("X: " + String.valueOf(Math.floor(x)) + ", Y:" + Math.floor(y) + ", Z:" + Math.floor(z));
                        }
                    }
                }
            }
        }
        return true;
    }
}
