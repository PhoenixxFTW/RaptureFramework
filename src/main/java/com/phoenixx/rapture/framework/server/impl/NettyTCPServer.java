package com.phoenixx.rapture.framework.server.impl;

import com.phoenixx.rapture.framework.channel.DefaultChannelInitializer;
import com.phoenixx.rapture.framework.managers.ConnectionManager;
import com.phoenixx.rapture.framework.server.AbstractNettyServer;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import com.phoenixx.rapture.framework.util.ConsumerSupplier;
import com.phoenixx.rapture.framework.util.NettyConfig;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:00 PM [15-05-2022]
 */
public class NettyTCPServer extends AbstractNettyServer {

    public NettyTCPServer(NettyConfig nettyConfig, NetServerHandler<?,?,?> netServerHandler, ConsumerSupplier<Channel, NetServerHandler<?,?,?>> channelInitConsumer) {
        super(nettyConfig, netServerHandler, new ConnectionManager<>(), new DefaultChannelInitializer(channelInitConsumer), NioServerSocketChannel.class);
    }

    @Override
    public String getServerName() {
        return "NettyTCPServer";
    }
}
