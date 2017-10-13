package com.tatemylove.UHCBattles;

import com.tatemylove.UHCBattles.Arena.InternalCountDown;
import com.tatemylove.UHCBattles.Arena.SetLobby;
import com.tatemylove.UHCBattles.Arena.UHC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
    @EventHandler
    public void onHit(EntityDamageEvent e) {
        Player p = (Player) e.getEntity();
        if (Main.PlayingPlayers.contains(p)) {
            if (InternalCountDown.timeuntilstart < 0) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void noFriendlyFire(EntityDamageByEntityEvent e){
        Player p = (Player) e.getEntity();
        Player pp = (Player) e.getDamager();
        if(Main.PlayingPlayers.contains(p) && (Main.PlayingPlayers.contains(pp))){
            if(UHC.blueTeam.contains(pp) && (UHC.blueTeam.contains(p))){
                e.setCancelled(true);
            }
            if(UHC.redTeam.contains(pp) && (UHC.redTeam.contains(p))){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void noBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(Main.PlayingPlayers.contains(p)){
            if(InternalCountDown.timeuntilstart == 0){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void deathByPlayer(PlayerDeathEvent e){
        Player p = e.getEntity();
        Player pp = e.getEntity().getKiller();
        if(Main.PlayingPlayers.contains(p) && (Main.PlayingPlayers.contains(pp))){
            for(Player ps : Main.PlayingPlayers){
                ps.sendMessage(Main.prefix + "§5Player: §c" + p.getName() + " §5has been killed by " + pp.getName());
            }
            p.teleport(SetLobby.getLobby());
            p.sendMessage(Main.prefix + "§bYou died and have been teleported back to the lobby!");
        }
    }
    @EventHandler
    public void heathRegen(EntityRegainHealthEvent e){
        Player p = (Player) e.getEntity();
        if(Main.PlayingPlayers.contains(p)){
            e.setCancelled(true);
        }
    }
}
