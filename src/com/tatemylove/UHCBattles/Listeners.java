package com.tatemylove.UHCBattles;

import com.tatemylove.UHCBattles.Arena.*;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Listeners implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(Main.PlayingPlayers.contains(p)){
            Main.PlayingPlayers.remove(p);
        }
        if(Main.WaitingPlayers.contains(p)){
            Main.WaitingPlayers.remove(p);
        }
        e.setQuitMessage(null);
    }
    @EventHandler
    public void onHit(EntityDamageEvent e) {
        Player p = (Player) e.getEntity();
        if (Main.PlayingPlayers.contains(p)) {
            if (InternalCountDown.timeuntilstart > 0) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void noFriendlyFire(EntityDamageByEntityEvent e){
        Player p = (Player) e.getEntity();
        Player pp = (Player) e.getDamager();
        if(Main.PlayingPlayers.contains(p)){
            if(e.getEntity() == pp){
                e.setCancelled(true);
            }
        }
        if(Main.WaitingPlayers.contains(p)){
            if(e.getEntity() == pp){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void noBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(Main.PlayingPlayers.contains(p)){
            if(InternalCountDown.timeuntilstart > 0){
                e.setCancelled(false);
            }else if(InternalCountDown.timeuntilstart == 0){
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
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try{
                out.writeUTF("Connect");
                out.writeUTF("Lobby");
            }catch (IOException ei){

            }
            p.sendMessage(Main.prefix + "§cYou have died and are being teleported back to the Hub");
            p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
        }
        if(UHC.redTeam.size() == 0){
            UHC.endUHC(Integer.toString(GetArena.getCurrentArena()));
        }
        if(UHC.blueTeam.size() == 0){
            UHC.endUHC(Integer.toString(GetArena.getCurrentArena()));
        }
    }
    @EventHandler
    public void heathRegen(EntityRegainHealthEvent e){
        Player p = (Player) e.getEntity();
        if(Main.PlayingPlayers.contains(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (BaseArena.states == BaseArena.ArenaStates.Countdown) {
            Main.WaitingPlayers.add(p);
            p.sendMessage(Main.prefix + "§aYou have joined UHC §5#1");
            p.sendMessage(Main.prefix + "§3To go back to hub type §5/battles leave");
            p.sendMessage("§c(This is only available before the game starts)");
            e.setJoinMessage(Main.prefix + "§b" + p.getName() + " §ahas joined the queue");
        }
        if(BaseArena.states == BaseArena.ArenaStates.Started){
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try{
                out.writeUTF("Connect");
                out.writeUTF("Lobby");
            }catch (IOException ei){

            }
            p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
            e.setJoinMessage(null);
        }
    }
    @EventHandler
    public void leavesChange(LeavesDecayEvent e){
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Restores 4 hearts");
        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta goldenMeta = goldenApple.getItemMeta();
        goldenMeta.setDisplayName("§2§lUHC§f§l-§8§lApple");
        goldenMeta.setLore(lore);
        goldenApple.setItemMeta(goldenMeta);
        int i = ThreadLocalRandom.current().nextInt(100) + 1;
        if(e.getBlock().getType() == Material.LEAVES) {
            if (i > 0 && i <= 10) {
                Block block = e.getBlock();
                block.setType(Material.AIR);
                block.getWorld().dropItemNaturally(block.getLocation(), goldenApple);
            }
        }
    }
}
