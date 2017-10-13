package com.tatemylove.UHCBattles;

import com.tatemylove.UHCBattles.Arena.SetLobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(Main.PlayingPlayers.contains(p)){
            p.teleport(SetLobby.getLobby());
            Main.PlayingPlayers.remove(p);

        }
        if(Main.WaitingPlayers.contains(p)){
            p.teleport(SetLobby.getLobby());

            Main.WaitingPlayers.remove(p);

        }
    }
}
