package com.tatemylove.UHCBattles.Commands;

import com.tatemylove.UHCBattles.Arena.SetLobby;
import com.tatemylove.UHCBattles.Main;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import org.bukkit.Location;
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
            if(args[0].equalsIgnoreCase("goup")){
                Location loc = new Location(p.getWorld(),p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getBlockZ());
                loc.setY(loc.getWorld().getHighestBlockYAt(loc));
                p.teleport(loc);
            }
            if(args[0].equalsIgnoreCase("leave")){
                if(!Main.PlayingPlayers.contains(p)){
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try{
                        out.writeUTF("Connect");
                        out.writeUTF("Lobby");
                    }catch (IOException e){

                    }
                    p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
                }
            }
        }
        return true;
    }
}
