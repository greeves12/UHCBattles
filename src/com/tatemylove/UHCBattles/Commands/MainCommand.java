package com.tatemylove.UHCBattles.Commands;

import com.tatemylove.UHCBattles.Arena.SetLobby;
import com.tatemylove.UHCBattles.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            if(args[0].equalsIgnoreCase("join")){
                if(p.hasPermission("uhc.join")){
                    Main.WaitingPlayers.add(p);
                    p.sendMessage(Main.prefix + "§3You joined UHC");
                    plugin.startCountDown();
                }
            }
            if(args[0].equalsIgnoreCase("setlobby")){
                if(p.hasPermission("uhc.lobbyset")){
                    SetLobby.setLobby(p);
                    p.sendMessage(Main.prefix + "§aYou have set the lobby");
                }
            }
            if(args[0].equalsIgnoreCase("goup")){
                p.teleport((Location) p.getWorld().getHighestBlockAt(p.getLocation()));
            }
        }

        return true;
    }
}
