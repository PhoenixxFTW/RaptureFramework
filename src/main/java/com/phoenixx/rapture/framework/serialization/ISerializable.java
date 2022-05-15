package com.phoenixx.rapture.framework.serialization;

import com.phoenixx.rapture.framework.packet.PacketBuffer;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 8:58 p.m [2020-11-20]
 */
public interface ISerializable {
    /**
     * Serialise data into a {@link PacketBuffer}
     *
     * @param packetBuffer the {@link PacketBuffer} to write to
     * @return {@link PacketBuffer} that was just written to
     */
    PacketBuffer serialize(PacketBuffer packetBuffer) throws Exception;
}
