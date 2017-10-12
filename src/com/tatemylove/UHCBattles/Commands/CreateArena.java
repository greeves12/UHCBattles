package com.tatemylove.UHCBattles.Commands;

import com.tatemylove.UHCBattles.Files.ArenaFile;
import com.tatemylove.UHCBattles.Main;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.TreeMap;

public class CreateArena {

    public static void createArena(Player p, String name){
        TreeMap<Integer, Integer> numbers = new TreeMap<>();

        for (int k = 0; ArenaFile.getData().contains("Arenas." + k); k++) {
            numbers.put(k, k);
        }

        int newID;
        if (numbers.size() == 0) {
            newID = 0;
        } else {
            newID = numbers.lastEntry().getValue() + 1;
        }        ArenaFile.getData().set("Arenas." + newID + ".Name", name);
        ArenaFile.saveData();
        ArenaFile.reloadData();
        p.sendMessage(Main.prefix + "§bArena: §a" + name  + " §bcreated with the ID §a" + newID);
    }

    public static void setSpawns(Player p, String[] args, int id) {
        if (args.length == 3) {
            if (args[2].equalsIgnoreCase("blue")) {
                String world = p.getLocation().getWorld().getName();
                double x = p.getLocation().getX();
                double y = p.getLocation().getY();
                double z = p.getLocation().getZ();

                ArenaFile.getData().set("Arenas." + id + ".Spawns.Blue.World", world);
                ArenaFile.getData().set("Arenas." + id + ".Spawns.Blue.X", x);
                ArenaFile.getData().set("Arenas." + id + ".Spawns.Blue.Y", y);
                ArenaFile.getData().set("Arenas." + id + ".Spawns.Blue.Z", z);
                ArenaFile.saveData();
                ArenaFile.reloadData();

            }else if (args[2].equalsIgnoreCase("red")){
                String world = p.getLocation().getWorld().getName();
                double x = p.getLocation().getX();
                double y = p.getLocation().getY();
                double z = p.getLocation().getZ();

                ArenaFile.getData().set("Arenas." + id + ".Spawns.Red.World", world);
                ArenaFile.getData().set("Arenas." + id + ".Spawns.Red.X", x);
                ArenaFile.getData().set("Arenas." + id + ".Spawns.Red.Y", y);
                ArenaFile.getData().set("Arenas." + id + ".Spawns.Red.Z", z);
                ArenaFile.saveData();
                ArenaFile.reloadData();
            }
        }
    }
}
