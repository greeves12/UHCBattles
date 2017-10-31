package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class FinalCountdown extends BukkitRunnable {

    public static int timeuntilend;

    @Override
    public void run() {
        if (BaseArena.states == BaseArena.ArenaStates.Ended) {
            if (timeuntilend == 0) {
                World world = Bukkit.getServer().getWorld("uhc");
                List<Entity> entList = world.getEntities();
                for(Entity current : entList){
                    if(current instanceof Item){
                        current.remove();
                    }
                }
                for (Player p : Main.PlayingPlayers) {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF("uhclobby");
                    } catch (IOException e) {

                    }
                    p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
                }
            }
            if (timeuntilend % 1 == 0) {
                for (Player p : Main.PlayingPlayers) {
                    p.sendMessage(Main.prefix + "§aGame has ended! Teleporting you back to the hub in " + timeuntilend + " §aseconds");
                    if (UHC.blueTeam.contains(p)) {
                        Firework f = (Firework) p.getPlayer().getWorld().spawn(p.getLocation(), Firework.class);
                        FireworkMeta fm = f.getFireworkMeta();

                        fm.addEffect(FireworkEffect.builder()
                                .trail(true)
                                .with(FireworkEffect.Type.BALL_LARGE)
                                .withColor(Color.BLUE)
                                .build());
                        fm.setPower(2);
                        f.setFireworkMeta(fm);
                    }
                    if(UHC.redTeam.contains(p)){
                        Firework f = (Firework) p.getPlayer().getWorld().spawn(p.getLocation(), Firework.class);
                        FireworkMeta fm = f.getFireworkMeta();

                        fm.addEffect(FireworkEffect.builder()
                                .trail(true)
                                .with(FireworkEffect.Type.BALL_LARGE)
                                .withColor(Color.RED)
                                .build());
                        fm.setPower(2);
                        f.setFireworkMeta(fm);
                    }
                }
            }
            timeuntilend -= 1;
        }
    }
}