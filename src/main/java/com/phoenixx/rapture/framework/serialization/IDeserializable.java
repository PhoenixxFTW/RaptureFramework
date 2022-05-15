package com.phoenixx.rapture.framework.serialization;

import com.phoenixx.rapture.framework.packet.PacketBuffer;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 8:58 p.m [2020-12-02]
 */
public interface IDeserializable {

    /**
     * Deserialize data from a {@link PacketBuffer}
     *
     * @param packetBuffer the {@link PacketBuffer} to read from
     */
    void deserialize(PacketBuffer packetBuffer) throws Exception;
}
