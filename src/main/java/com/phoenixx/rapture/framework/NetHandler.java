package com.phoenixx.rapture.framework;

import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.channel.ChannelHandler;
import io.netty.util.AttributeKey;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-16
 * 11:01 p.m.
 *
 * This is essentially the core of any client/server object. For example, {@link NetServerHandler} deals with all
 * server related tasks. Creating connections, proccessing handshake / creating sessions, etc etc.
 */
@ChannelHandler.Sharable
public abstract class NetHandler {

    public static final AttributeKey<NetHandler> NET_HANDLER_ATTR = AttributeKey.valueOf("net_handler");

    public NetHandler() {
    }

    /**
     * Get the name of this {@link NetHandler}.
     * @return The name.
     */
    public abstract String getName();
}
