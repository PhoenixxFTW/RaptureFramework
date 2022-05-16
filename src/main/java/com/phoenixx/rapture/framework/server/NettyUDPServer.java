package com.phoenixx.rapture.framework.server;

import com.phoenixx.rapture.framework.channel.AbstractChannelInitializer;
import com.phoenixx.rapture.framework.channel.UDPServerChannel;
import com.phoenixx.rapture.framework.managers.ConnectionManager;
import com.phoenixx.rapture.framework.util.NettyConfig;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.Set;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 10:01 p.m [2020-11-12]
 */
public class NettyUDPServer extends AbstractNettyServer {

    private static final Logger LOGGER = LogManager.getLogger(NettyUDPServer.class);

    public NettyUDPServer(NettyConfig nettyConfig, ConnectionManager<Integer> connectionManager) {
        this(nettyConfig, connectionManager, UDPServerChannel.class);
    }

    protected NettyUDPServer(NettyConfig nettyConfig, ConnectionManager<Integer> connectionManager, Class<? extends ServerChannel> serverChannel) {
        super(nettyConfig, connectionManager, null, serverChannel);
    }

    @Override
    public void bind(InetSocketAddress socketAddress) throws Exception {
        super.bind(socketAddress);
        try {
            Map<ChannelOption<?>, Object> channelOptions = nettyConfig.getChannelOptions();

            if (null != channelOptions) {
                Set<ChannelOption<?>> keySet = channelOptions.keySet();
                for (@SuppressWarnings("rawtypes") ChannelOption option : keySet) {
                    getBootStrap().option(option, channelOptions.get(option));
                }
            }

            Channel channel = getBootStrap().bind(nettyConfig.getSocketAddress()).addListener(f -> {
                if (f.isSuccess()) {
                    onBindSuccess(nettyConfig.getSocketAddress());
                } else {
                    onBindFailure(nettyConfig.getSocketAddress(), f.cause());
                }
            }).channel();

            ALL_CHANNELS.add(channel);
        } catch (Exception ex) {
            LOGGER.error("UDP Server start error, going to shut down", ex);
            super.stopServer();
            throw ex;
        }
    }

    /**
     * Called when the server successfully binds to a given address.
     *
     * @param address The {@link SocketAddress} we are now bound too.
     */
    public void onBindSuccess(SocketAddress address) {
        LOGGER.info("Bind success! Now listening for connections on {}", address.toString());
    }

    /**
     * Called when the server fails to bind to the given address.
     *
     * @param address The {@link SocketAddress} we attempted to bind too
     * @param throwable The cause of why the binding failed
     */
    public void onBindFailure(SocketAddress address, @Nullable Throwable throwable) {
        LOGGER.info("Bind failed! Check no other services are running on {} and that the address specified exists on the local host", address.toString());
        if(throwable!=null) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void setChannelInitializer(AbstractChannelInitializer channelInitializer) {
        super.setChannelInitializer(channelInitializer);
    }

    @Override
    public String getServerName() {
        return "";
    }

    @Override
    public String getServerVersion() {
        return "";
    }

    @Override
    public String toString() {
        return "NettyUDPServer [socketAddress=" + nettyConfig.getSocketAddress() + ", portNumber=" + nettyConfig.getPortNumber() + "]";
    }
}
