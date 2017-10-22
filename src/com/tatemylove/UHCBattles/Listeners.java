package com.tatemylove.UHCBattles;

import com.tatemylove.UHCBattles.Arena.*;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import com.tatemylove.UHCBattles.Utilities.SendCoolMessages;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        SendCoolMessages.clearTitleAndSubtitle(p);
        e.setQuitMessage(null);
    }
    @EventHandler
    public void onHit(EntityDamageEvent e) {
        Player p = (Player) e.getEntity();
        Entity entity = e.getEntity();
        if (Main.PlayingPlayers.contains(p)) {
            if (InternalCountDown.timeuntilstart > 0) {
               if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
                   if(entity instanceof Player){
                       e.setCancelled(true);
                   }
                }
            }
            if(InternalCountDown.timeuntilstart < 0){
                e.setCancelled(false);
            }
        }
        if(Main.WaitingPlayers.contains(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void noFriendlyFire(EntityDamageByEntityEvent e){
        Player p = (Player) e.getEntity();
        Player pp = (Player) e.getDamager();
        if(Main.PlayingPlayers.contains(p)){
            if(UHC.redTeam.contains(p) && (UHC.redTeam.contains(pp))){
                e.setCancelled(true);
            }
            if(UHC.blueTeam.contains(p) && (UHC.blueTeam.contains(pp))){
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
            if(!p.hasPermission("UHC.build")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void deathByPlayer(PlayerDeathEvent e){
        Player p = e.getEntity();
        Player pp = e.getEntity().getKiller();
        final CraftPlayer craftPlayer = (CraftPlayer) p;
        if(UHC.blueTeam.contains(p)){
            UHC.blueTeam.remove(p);
        }
        if(UHC.redTeam.contains(p)){
            UHC.redTeam.remove(p);
        }
        if(Main.PlayingPlayers.contains(p)) {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF("lobby");
            } catch (IOException ei) {

            }
            p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
        }
        ThisPlugin.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(ThisPlugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if(p.isDead()){
                    craftPlayer.getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
                }
            }
        });
            for(Player ps : Main.PlayingPlayers){
                ps.sendMessage(Main.prefix + "§bPlayer: §c" + p.getName() + " §dhas been killed by §c" + pp.getName());
            }
    }
    @EventHandler
    public void heathRegen(EntityRegainHealthEvent e){
        Player p = (Player) e.getEntity();
        if(Main.PlayingPlayers.contains(p)) {
            if (e.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        int k = ThisPlugin.getPlugin().getConfig().getInt("server-id");
        String header = ChatColor.translateAlternateColorCodes('&', ThisPlugin.getPlugin().getConfig().getString("header"));
        String footer = ChatColor.translateAlternateColorCodes('&', ThisPlugin.getPlugin().getConfig().getString("footer").replace("%id%", String.valueOf(k)));
        Player p = e.getPlayer();
        if (BaseArena.states == BaseArena.ArenaStates.Countdown) {
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            Main.WaitingPlayers.add(p);
            p.sendMessage(Main.prefix + "§aYou have joined UHC §dServer ID #" + ThisPlugin.getPlugin().getConfig().getInt("server-id"));
            p.sendMessage(Main.prefix + "§3To go back to hub type §5/battles leave");
            e.setJoinMessage(Main.prefix + "§b" + p.getName() + " §ehas joined the queue");
            p.teleport(SetLobby.getLobby());
            ItemStack leave = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta leaveMeta = leave.getItemMeta();
            leaveMeta.setDisplayName("§b§lLeave Game");
            leave.setItemMeta(leaveMeta);
            p.getInventory().setItem(8, leave);
            p.setHealth(20);
            p.setFoodLevel(20);
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
        SendCoolMessages.TabHeaderAndFooter(header, footer, p);
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
            if (i > 0 && i <= 1) {
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
    public void foodChange(FoodLevelChangeEvent e){
        Player p = (Player) e.getEntity();
        if(Main.WaitingPlayers.contains(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void clickItem(PlayerInteractEvent e){
        Action action = e.getAction();
        Player p = e.getPlayer();

        if(Main.WaitingPlayers.contains(p)){
            if(action == Action.RIGHT_CLICK_AIR && p.getItemInHand().getType() == Material.GLOWSTONE_DUST){
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try{
                    out.writeUTF("Connect");
                    out.writeUTF("lobby");
                }catch (IOException io){

                }
                p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
            }
        }
    }
}
