package com.phoenixx.rapture.framework.protocol;

import com.phoenixx.rapture.framework.packet.AbstractPacketRegistry;
import io.netty.util.AttributeKey;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-25
 * 3:52 a.m.
 */
public interface IProtocol {

    AttributeKey<IProtocol> PROTOCOL_ATTR = AttributeKey.valueOf("protocol");

    /**
     * Get the ID of the given {@link IProtocol} enum
     *
     * @return Protocol ID
     */
    int getProtocolID();

    /**
     * Get the {@link AbstractPacketRegistry} associated with the given {@link IProtocol} enum
     *
     * @return {@link AbstractPacketRegistry}
     */
    AbstractPacketRegistry getPacketRegistry();
}
