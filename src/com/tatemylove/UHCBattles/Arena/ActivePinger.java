package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Created by Tate on 10/17/2017.
 */
public class ActivePinger  extends BukkitRunnable {
    @Override
    public void run() {
        if(BaseArena.states == BaseArena.ArenaStates.Started) {
            if (UHC.redTeam.size() == 0) {
                UHC.endUHC();
            } else if (UHC.blueTeam.size() == 0){
                UHC.endUHC();
            }
            for(Player p : Bukkit.getOnlinePlayers()){
             if(!Main.PlayingPlayers.contains(p) || (!Main.WaitingPlayers.contains(p))){
                 ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b);
                 try{
                     out.writeUTF("Connect");
                     out.writeUTF("uhclobby");
                 }catch(Exception e){

                 }
                 p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
             }
            }
        }
        if(BaseArena.states == BaseArena.ArenaStates.Ended || BaseArena.states == BaseArena.ArenaStates.Started){
            if(Bukkit.getOnlinePlayers().isEmpty()){
                Bukkit.shutdown();
            }
        }
    }
}
