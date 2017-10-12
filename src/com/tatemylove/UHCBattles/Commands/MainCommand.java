package com.tatemylove.UHCBattles.Commands;

import com.tatemylove.UHCBattles.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
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
                    Main.min_players++;
                    p.sendMessage(Main.prefix + "You joined UHC");
                }
            }
        }

        return true;
    }
}
