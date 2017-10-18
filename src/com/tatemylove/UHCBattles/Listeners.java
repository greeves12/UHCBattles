package com.tatemylove.UHCBattles;

import com.tatemylove.UHCBattles.Arena.*;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import com.tatemylove.UHCBattles.Utilities.SendCoolMessages;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
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

        SendCoolMessages.clearTitleAndSubtitle(p);
        e.setQuitMessage(null);

    }
    @EventHandler
    public void onHit(EntityDamageEvent e) {
        Player p = (Player) e.getEntity();
        if (Main.PlayingPlayers.contains(p)) {
            if (InternalCountDown.timeuntilstart > 0) {
                if (!(e.getCause() == EntityDamageEvent.DamageCause.FALL)) {
                    e.setCancelled(true);
                }
            }
            if(InternalCountDown.timeuntilstart < 0){
                e.setCancelled(false);
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
        if(Main.WaitingPlayers.contains(p)){
            e.setCancelled(true);
        }
        if(Main.PlayingPlayers.contains(p)){
            if(InternalCountDown.timeuntilstart < 0){
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
                out.writeUTF("lobby");
            }catch (IOException ei){

            }
            p.sendMessage(Main.prefix + "§cYou have died and are being teleported back to the Hub");
            p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
        }
        UHC.endUHC();
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
            p.sendMessage(Main.prefix + "§aYou have joined UHC §dServer ID #" + ThisPlugin.getPlugin().getConfig().getInt("server-id"));
            p.sendMessage(Main.prefix + "§3To go back to hub type §5/battles leave");
            e.setJoinMessage(Main.prefix + "§b" + p.getName() + " §ehas joined the queue");
            p.teleport(SetLobby.getLobby());
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
        }
        if(BaseArena.states == BaseArena.ArenaStates.Started){
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try{
                out.writeUTF("Connect");
                out.writeUTF("lobby");
            }catch (IOException ei){

            }
            p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
            e.setJoinMessage(null);
        }
        SendCoolMessages.TabHeaderAndFooter("", "", p);
        SendCoolMessages.TabHeaderAndFooter("§2§lRecon§f§l-§4§lNetwork", "§dServer ID #" + ThisPlugin.getPlugin().getConfig().getInt("server-id"), p);
    }
    @EventHandler
    public void leavesChange(LeavesDecayEvent e){
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Restores a lil bit of health");
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
    @EventHandler
    public void chatChange(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String message = e.getMessage();
        if(Main.PlayingPlayers.contains(p)){
            if(UHC.blueTeam.contains(p)){
                for(Player pp : UHC.blueTeam){
                    pp.sendMessage("§3[Blue] " + p.getName() + ": " + message);
                }
            }
            if(UHC.redTeam.contains(p)){
                for(Player pp : UHC.redTeam){
                    pp.sendMessage("§c[Red] " + p.getName() +": "+ message);
                }
            }
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void keepArmor(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if(Main.PlayingPlayers.contains(p)){
            if(e.getSlotType() == InventoryType.SlotType.ARMOR){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void noHunger(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
}
