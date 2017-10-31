package com.tatemylove.UHCBattles.MySQL;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.tatemylove.UHCBattles.MySQL.MySQL.connection;

public class Kills {
    public static void firstWin(Player p){
        int number = 0;
        try {
            if (!exists(p)) {
                PreparedStatement ps = connection.prepareStatement("INSERT into UHCkills(uuid, points)\nvalues('" + p.getUniqueId().toString() + "', '" + number + "');");
                ps.executeUpdate();
                ps.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static boolean exists(Player p){
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT uuid FROM UHCkills");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getString("uuid").equals(p.getUniqueId().toString())) return true;
            }
            rs.close();
            ps.close();
            return false;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static void addWins(Player p){
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE UHCkills SET points= points+1 WHERE uuid='" + p.getUniqueId().toString() + "'");
            ps.executeUpdate();
            ps.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
