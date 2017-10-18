package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import com.tatemylove.UHCBattles.ThisPlugin.ThisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.fusesource.jansi.Ansi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FinalCountdown extends BukkitRunnable {

    public static int timeuntilend;

    @Override
    public void run() {
        if (BaseArena.states == BaseArena.ArenaStates.Ended) {
            if (timeuntilend == 0) {
                for (Player p : Main.PlayingPlayers) {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF("lobby");
                    } catch (IOException e) {

                    }
                    p.sendPluginMessage(ThisPlugin.getPlugin(), "BungeeCord", b.toByteArray());
                }
                Bukkit.shutdown();
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