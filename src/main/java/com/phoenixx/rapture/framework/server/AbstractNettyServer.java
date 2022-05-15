package com.phoenixx.rapture.framework.server;

import com.phoenixx.rapture.framework.INetty;
import com.phoenixx.rapture.framework.channel.AbstractChannelInitializer;
import com.phoenixx.rapture.framework.channel.UDPServerChannel;
import com.phoenixx.rapture.framework.managers.ConnectionManager;
import com.phoenixx.rapture.framework.util.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-12
 * 9:49 p.m.
 */
public abstract class AbstractNettyServer implements NettyServer, INetty<NetServerHandler<?, ?, ?>> {

    protected final NettyConfig nettyConfig;
    private AbstractChannelInitializer channelInitializer;

    private int maxConnections;

    private NetServerHandler<?,?,?> netServerHandler;
    private ConnectionManager<Integer> connectionManager;
    private final ServerBootstrap bootstrap = new ServerBootstrap();

    private static final Logger LOGGER = LogManager.getLogger(AbstractNettyServer.class);
    public static final ChannelGroup ALL_CHANNELS = new DefaultChannelGroup("RAPTURE-CHANNELS", GlobalEventExecutor.INSTANCE);

    public AbstractNettyServer(NettyConfig nettyConfig, ConnectionManager<Integer> connectionManager){
        this(nettyConfig, connectionManager,null);
    }

    public AbstractNettyServer(NettyConfig nettyConfig, ConnectionManager<Integer> connectionManager, AbstractChannelInitializer channelInitializer) {
        this.nettyConfig = nettyConfig;
        this.connectionManager = connectionManager;
        this.channelInitializer = channelInitializer;

        /*String log4jConfigFile = "Rapture-Server-Framework/log4j2.properties";
        PropertyConfigurator.configure(log4jConfigFile);*/
    }

    @Override
    public void initializeServer() {
        LOGGER.info("Initialising network server");
        bootstrap.group(getBossGroup(), getWorkerGroup())
                .channel(UDPServerChannel.class)
                .childHandler(this.channelInitializer);
        LOGGER.info("Ready to bind");
    }

    @Override
    public void bind(InetSocketAddress socketAddress) throws Exception {
        nettyConfig.setSocketAddress(socketAddress);
        if(netServerHandler == null){
            LOGGER.error("NetServerHandler was null!");
        }
    }

    @Override
    public void stopServer() {
        LOGGER.info("Shutting down server {}", this.getClass().getName());
        ChannelGroupFuture future = ALL_CHANNELS.close();
        try {
            future.await();
        } catch (InterruptedException e) {
            LOGGER.error("Exception occurred while waiting for channels to close: ", e);
        } finally {

            if(getBossGroup()!=null) {
                getBossGroup().shutdownGracefully();
            }

            if(getWorkerGroup()!=null) {
                getWorkerGroup().shutdownGracefully();
            }
        }
    }

    @Override
    public void setNetHandler(NetServerHandler<?, ?, ?> netHandler) {
        this.netServerHandler = netHandler;
        this.netServerHandler.setAbstractNettyServer(this);
    }

    public void setConnectionManager(ConnectionManager<Integer> connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void setChannelInitializer(AbstractChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    @Override
    public void setMaxConnections(int amount) {
        this.maxConnections = amount;
    }

    @Override
    public int getCurrentConnectionCount() {
        return this.connectionManager.getConnections().size();
    }

    @Override
    public int getMaxConnections() {
        return this.maxConnections;
    }

    @Override
    public NetServerHandler<?, ?, ?> getNetHandler() {
        return this.netServerHandler;
    }

    public ConnectionManager<Integer> getConnectionManager() {
        return connectionManager;
    }

    public ServerBootstrap getBootStrap(){
        return bootstrap;
    }

    @Override
    public AbstractChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    @Override
    public NettyConfig getNettyConfig() {
        return nettyConfig;
    }

    public EventLoopGroup getBossGroup() {
        return nettyConfig.getBossGroup();
    }

    public EventLoopGroup getWorkerGroup() {
        return nettyConfig.getWorkerGroup();
    }

    @Override
    public InetSocketAddress getSocketAddress() {
        return nettyConfig.getSocketAddress();
    }

    @Override
    public String toString() {
        return "NettyServer [socketAddress=" + getSocketAddress().getHostString() + ", portNumber=" + getSocketAddress().getPort() + "]";
    }

}
