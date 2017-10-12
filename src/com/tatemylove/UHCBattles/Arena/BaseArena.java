package com.tatemylove.UHCBattles.Arena;

public class BaseArena {
    public static ArenaStates states;

    public static enum ArenaStates {
        Waiting, Started, Countdown
    }

    public static ArenaStates getStates(){
        return states;
    }
}
