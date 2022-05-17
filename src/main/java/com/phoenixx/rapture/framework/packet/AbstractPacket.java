package com.phoenixx.rapture.framework.packet;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:30 PM [16-05-2022]
 */
public abstract class AbstractPacket implements IPacket {

    private int packetID;

    @Override
    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }

    @Override
    public int getPacketID() {
        return this.packetID;
    }
}
