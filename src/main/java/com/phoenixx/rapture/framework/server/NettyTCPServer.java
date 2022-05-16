package com.phoenixx.rapture.framework.server;

import com.phoenixx.rapture.framework.managers.ConnectionManager;
import com.phoenixx.rapture.framework.util.NettyConfig;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:00 PM [15-05-2022]
 */
public class NettyTCPServer extends NettyUDPServer {

    public NettyTCPServer(NettyConfig nettyConfig, ConnectionManager<Integer> connectionManager) {
        this(nettyConfig, connectionManager, NioServerSocketChannel.class);
    }

    protected NettyTCPServer(NettyConfig nettyConfig, ConnectionManager<Integer> connectionManager, Class<? extends ServerChannel> serverChannel) {
        super(nettyConfig, connectionManager, serverChannel);
    }
}
