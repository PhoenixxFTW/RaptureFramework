package com.phoenixx.rapture.framework.server.impl;

import com.phoenixx.rapture.framework.channel.DefaultChannelInitializer;
import com.phoenixx.rapture.framework.channel.UDPServerChannel;
import com.phoenixx.rapture.framework.managers.ConnectionManager;
import com.phoenixx.rapture.framework.server.AbstractNettyServer;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import com.phoenixx.rapture.framework.util.NettyConfig;
import io.netty.channel.Channel;

import java.util.function.Consumer;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 10:01 p.m [2020-11-12]
 */
public class NettyUDPServer extends AbstractNettyServer {

    public NettyUDPServer(NettyConfig nettyConfig, NetServerHandler<?,?,?> netServerHandler, Consumer<Channel> channelInitConsumer) {
        super(nettyConfig, netServerHandler, new ConnectionManager<>(), new DefaultChannelInitializer(netServerHandler, channelInitConsumer), UDPServerChannel.class);
    }

    @Override
    public String getServerName() {
        return "NettyUDPServer";
    }
}
