package com.phoenixx.rapture.framework.packet.impl;

import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:30 PM [16-05-2022]
 */
public abstract class AbstractPacket implements IPacket {

    private int packetID;

    protected PacketBuffer packetBuffer;

    protected final Logger LOGGER = LogManager.getLogger(getClass());

    public AbstractPacket(PacketBuffer packetBuffer) {
        this.packetBuffer = packetBuffer;
    }

    public AbstractPacket() {
        this.packetBuffer = new PacketBuffer();
    }

    @Override
    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }

    @Override
    public void setPacketBuffer(PacketBuffer packetBuffer) {
        this.packetBuffer = packetBuffer;
    }

    @Override
    public PacketBuffer getPacketBuffer() {
        return this.packetBuffer;
    }

    @Override
    public int getPacketID() {
        return this.packetID;
    }
}
