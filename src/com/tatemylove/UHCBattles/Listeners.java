package com.tatemylove.UHCBattles;

import com.tatemylove.UHCBattles.Arena.*;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import com.tatemylove.UHCBattles.Utilities.SendCoolMessages;
import net.mcjukebox.plugin.bukkit.api.JukeboxAPI;
import net.mcjukebox.plugin.bukkit.api.ResourceType;
import net.mcjukebox.plugin.bukkit.api.models.Media;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
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

        Entity entity = e.getEntity();
        if(entity instanceof Player) {
            Player p = (Player) entity;
            if (Main.PlayingPlayers.contains(p)) {
                if (InternalCountDown.timeuntilstart > 0) {
                    if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                            e.setCancelled(true);
                    }
                }
                if (InternalCountDown.timeuntilstart < 0) {
                    e.setCancelled(false);
                }
            }
            if (Main.WaitingPlayers.contains(p)) {
                e.setCancelled(true);
            }
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
                out.writeUTF("uhclobby");
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
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
            BookMeta bm = (BookMeta) book.getItemMeta();
            bm.setDisplayName("§5§lInformation");
            bm.setPages("§b§l§nAbout:" + "\n\n" + "§5This is a Gamemode that consists of two teams of 5 that will include Strategic  PvP and Teamwork! You will start off with 10 steak at the start of each game and will have 30 minutes to go and get resources from both mining in caves and gathering", "§5resources on the surface. Once 30 minutes is up you will get teleported to surface where you can either meet up with you're team and go fight the other team or you can go solo and fight the other team. Remember to have fun!",
                    "§b§l§nCommands:" + "\n\n" + "§5Chat is automatically filtered to team chat!" + "\n\n" + "/battles pack §2~ Opens up your teams storage" + "\n\n" + "§5/battles sc §2~ Sends your coordinates to your teammates",
                    "§b§l§nUseful Information" + "\n\n" + "§5Trees have a chance of dropping a golden apple" + "\n\n" + "At the end of 30 minutes you will be automatically teleported to the surface" + "\n\n" + "PvP is disabled during the 30 minutes but", "§5you can still take damage from other things"+
            "\n\n" + "CutClean is installed on our servers meaning you don't have to smelt" + "\n\n" + "Have fun! §2" + p.getName());
            bm.setAuthor("tatemylove");
            bm.setTitle("§5§lInformation");
            book.setItemMeta(bm);
            p.getInventory().setItem(1, book);

        }
        if(BaseArena.states == BaseArena.ArenaStates.Started){
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try{
                out.writeUTF("Connect");
                out.writeUTF("uhclobby");
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
        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 1);
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
                    out.writeUTF("uhclobby");
                }catch (IOException io){

                }
                p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
            }
        }
    }
    @EventHandler
    public void invRemove(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if(Main.WaitingPlayers.contains(p)){
            if(!p.hasPermission("uhc.remove")){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void invRemove2(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if(Main.WaitingPlayers.contains(p)){
            if(!p.hasPermission("uhc.remove2")){
                e.setCancelled(true);
            }
        }
    }
}
