package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Files.ArenaFile;
import com.tatemylove.UHCBattles.Files.LobbyFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by Tate on 10/12/2017.
 */
public class SetLobby {


    public static void setLobby(Player p){

        String world = p.getLocation().getWorld().getName();
        double x = p.getLocation().getX();
        double y = p.getLocation().getY();
        double z = p.getLocation().getZ();

        LobbyFile.getData().set("Lobby.World", world);
        LobbyFile.getData().set("Lobby.X", x);
        LobbyFile.getData().set("Lobby.Y", y);
        LobbyFile.getData().set("Lobby.Z", z);

        LobbyFile.saveData();
        LobbyFile.reloadData();
    }
    public static Location getLobby(){
        final double x;
        final double y;
        final double z;
        final World world;

        world = Bukkit.getServer().getWorld(LobbyFile.getData().getString("Lobby.World"));
        x = LobbyFile.getData().getDouble("Lobby.X");
        y = LobbyFile.getData().getDouble("Lobby.Y");
        z = LobbyFile.getData().getDouble("Lobby.Z");


        Location getLobby = new Location(world, x, y, z);
        return getLobby;
    }
}
