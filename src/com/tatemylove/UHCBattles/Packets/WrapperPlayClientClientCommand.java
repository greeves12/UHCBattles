package com.tatemylove.UHCBattles.Packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

/**
 * Created by Tate on 10/17/2017.
 */
public class WrapperPlayClientClientCommand extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.CLIENT_COMMAND;

    public WrapperPlayClientClientCommand() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientClientCommand(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve whether or not we're logging in or respawning.
     * @return The current command
     */
    public EnumWrappers.ClientCommand getCommand() {
        return handle.getClientCommands().read(0);
    }

    /**
     *
     *
     * Set whether or not we're logging in or respawning.
     * @param value - new value.
     */
    public void setCommand(EnumWrappers.ClientCommand value) {
        handle.getClientCommands().write(0, value);
    }
}
