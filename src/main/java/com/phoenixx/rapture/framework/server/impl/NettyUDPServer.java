package com.phoenixx.rapture.framework.server.impl;

import com.phoenixx.rapture.framework.channel.DefaultChannelInitializer;
import com.phoenixx.rapture.framework.channel.UDPServerChannel;
import com.phoenixx.rapture.framework.managers.ConnectionManager;
import com.phoenixx.rapture.framework.server.AbstractNettyServer;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import com.phoenixx.rapture.framework.util.ConsumerSupplier;
import com.phoenixx.rapture.framework.util.NettyConfig;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 10:01 p.m [2020-11-12]
 */
public class NettyUDPServer extends AbstractNettyServer {

    private static final Logger LOGGER = LogManager.getLogger(NettyUDPServer.class);

    public NettyUDPServer(NettyConfig nettyConfig, NetServerHandler<?,?,?> netServerHandler, ConsumerSupplier<Channel, NetServerHandler<?,?,?>> channelInitConsumer) {
        super(nettyConfig, netServerHandler, new ConnectionManager<>(), new DefaultChannelInitializer(channelInitConsumer), UDPServerChannel.class);
    }

    @Override
    public String getServerName() {
        return "NettyUDPServer";
    }
}
