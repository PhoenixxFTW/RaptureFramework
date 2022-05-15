package com.phoenixx.rapture.framework.packet;

import com.phoenixx.rapture.framework.serialization.ISerialization;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-16
 * 11:01 p.m.
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
