package com.phoenixx.rapture.framework.serialization;

import com.phoenixx.rapture.framework.packet.PacketBuffer;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-12-02
 * 8:58 p.m.
 */
public interface IDeserializable {

    /**
     * Deserialize data from a {@link PacketBuffer}
     *
     * @param packetBuffer the {@link PacketBuffer} to read from
     */
    void deserialize(PacketBuffer packetBuffer) throws Exception;
}
