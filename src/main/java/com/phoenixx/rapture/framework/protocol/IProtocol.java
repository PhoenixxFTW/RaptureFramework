package com.phoenixx.rapture.framework.protocol;

import com.phoenixx.rapture.framework.packet.impl.AbstractPacketRegistry;
import io.netty.util.AttributeKey;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 3:52 a.m [2020-11-25]
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
