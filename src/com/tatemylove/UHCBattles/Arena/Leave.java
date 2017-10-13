package com.tatemylove.UHCBattles.Arena;

import com.tatemylove.UHCBattles.Main;
import org.bukkit.entity.Player;

/**
 * Created by Tate on 10/12/2017.
 */
public class Leave {

    public static void leaveLobby(Player p){
        Main.PlayingPlayers.remove(p);
        Main.WaitingPlayers.remove(p);
    }
}
