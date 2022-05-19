package com.phoenixx.rapture.framework.server;

import com.phoenixx.rapture.framework.INetty;
import com.phoenixx.rapture.framework.channel.DefaultChannelInitializer;
import com.phoenixx.rapture.framework.managers.ConnectionManager;
import com.phoenixx.rapture.framework.util.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
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
 * @since 9:49 p.m [2020-11-12]
 */
public abstract class AbstractNettyServer implements INettyServer, INetty<NetServerHandler<?, ?, ?>> {

    protected final NettyConfig nettyConfig;
    private DefaultChannelInitializer channelInitializer;

    private int maxConnections;

    private NetServerHandler<?,?,?> netServerHandler;
    private ConnectionManager<Integer> connectionManager;
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final Class<? extends ServerChannel> serverChannelClass;

    private static final Logger LOGGER = LogManager.getLogger(AbstractNettyServer.class);
    public static final ChannelGroup ALL_CHANNELS = new DefaultChannelGroup("RAPTURE-CHANNELS", GlobalEventExecutor.INSTANCE);

    public AbstractNettyServer(NettyConfig nettyConfig, NetServerHandler<?,?,?> netServerHandler, ConnectionManager<Integer> connectionManager, DefaultChannelInitializer channelInitializer, Class<? extends ServerChannel> serverChannelClass) {
        this.nettyConfig = nettyConfig;
        this.netServerHandler = netServerHandler;
        this.connectionManager = connectionManager;
        this.channelInitializer = channelInitializer;
        this.serverChannelClass = serverChannelClass;

        this.netServerHandler.setAbstractNettyServer(this);

        /*String log4jConfigFile = "Rapture-Server-Framework/log4j2.properties";
        PropertyConfigurator.configure(log4jConfigFile);*/
    }

    @Override
    public void initializeServer() {
        LOGGER.info("Initialising network server");
        this.bootstrap.group(getBossGroup(), getWorkerGroup())
                .channel(this.serverChannelClass)
                .childHandler(this.channelInitializer);
        LOGGER.info("Ready to bind");
    }

    @Override
    public void startServer() throws Exception {
        if(this.netServerHandler == null){
            throw new RuntimeException("NetServerHandler was null!");
        } else if(this.nettyConfig == null) {
            throw new RuntimeException("NettyConfig was null!");
        }

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
            LOGGER.error("An error occurred while starting the server. Shutting down.", ex);
            this.stopServer();
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

            if(this.getBossGroup()!=null) {
                this.getBossGroup().shutdownGracefully();
            }

            if(this.getWorkerGroup()!=null) {
                this.getWorkerGroup().shutdownGracefully();
            }
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
        if(throwable != null) {
            throwable.printStackTrace();
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
    public void setChannelInitializer(DefaultChannelInitializer channelInitializer) {
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
        return this.connectionManager;
    }

    public ServerBootstrap getBootStrap(){
        return this.bootstrap;
    }

    @Override
    public DefaultChannelInitializer getChannelInitializer() {
        return this.channelInitializer;
    }

    @Override
    public NettyConfig getNettyConfig() {
        return this.nettyConfig;
    }

    public EventLoopGroup getBossGroup() {
        return this.nettyConfig.getBossGroup();
    }

    public EventLoopGroup getWorkerGroup() {
        return this.nettyConfig.getWorkerGroup();
    }

    @Override
    public InetSocketAddress getSocketAddress() {
        return this.nettyConfig.getSocketAddress();
    }

    @Override
    public String getServerVersion() {
        return this.nettyConfig.getServerVersion();
    }

    @Override
    public String toString() {
        return this.getServerName() + " [socketAddress=" + getSocketAddress().getHostString() + ", portNumber=" + getSocketAddress().getPort() + ", serverVersion=" + this.getServerVersion() + "]";
    }

}
