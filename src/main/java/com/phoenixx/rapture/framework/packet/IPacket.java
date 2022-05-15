package com.phoenixx.rapture.framework.packet;

import com.phoenixx.rapture.framework.serialization.ISerialization;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:01 p.m [2020-11-16]
 */
public interface IPacket extends ISerialization {

    /**
     * Set the ID of this packet
     *
     * @param packetID The given ID
     */
    void setPacketID(int packetID);

    /**
     * Get the ID of this packet
     *
     * @return The ID
     */
    int getPacketID();
}
