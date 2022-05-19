package com.phoenixx.rapture.framework.server;

import com.phoenixx.rapture.framework.channel.DefaultChannelInitializer;
import com.phoenixx.rapture.framework.util.NettyConfig;

import java.net.InetSocketAddress;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:47 p.m [2020-11-12]
 *
 * An interface that's meant to be implemented by a class that will start up a Netty server on a specified port.
 */
public interface INettyServer {

    /**
     * Handles all the preloading for the server. Creating a bootstrap, adding channel handlers,
     * setting netty options etc.
     */
    void initializeServer();

    /**
     * Binds this server (starts it) on a specific IP / Port stored in the {@link NettyConfig}
     *
     * @throws Exception Throws an exception during the start-up process if anything goes wrong.
     */
    void startServer() throws Exception;

    /**
     * Pretty self-explanatory, stops the server. Ex. {@link AbstractNettyServer#stopServer()}.
     *
     * @throws Exception Throws an exception during the shutdown process if anything goes wrong.
     */
    void stopServer() throws Exception;

    /**
     * Can be used to set the pipeline factory that is to be used by the netty server.
     *
     * @param channelInitializer The {@link DefaultChannelInitializer} which will create a pipeline on
     *                           each incoming connection.
     */
    void setChannelInitializer(DefaultChannelInitializer channelInitializer);

    /**
     * Set the max amount of connections allowed on the server.
     *
     * @param amount Amount of connections
     */
    void setMaxConnections(int amount);

    /**
     * Gets the servers name
     *
     * @return String
     */
    String getServerName();

    /**
     * Gets the server version
     *
     * @return String
     */
    String getServerVersion();

    /**
     * Number of players currently on the server.
     *
     * @return Int
     */
    int getCurrentConnectionCount();

    /**
     * Maximum number of connections allowed on the server.
     *
     * @return Int
     */
    int getMaxConnections();

    /**
     * Returns the channel pipeline factory that is associated with this netty server,
     * in our case, a The {@link DefaultChannelInitializer}. Probably will change this
     * in the future to support much more.
     *
     * @return {@link DefaultChannelInitializer}.
     */
    DefaultChannelInitializer getChannelInitializer();

    /**
     * Get the {@link NettyConfig} associated with this server.
     *
     * @return {@link NettyConfig}.
     */
    NettyConfig getNettyConfig();

    /**
     * Gets the {@link InetSocketAddress} that this server should be hosted on
     *
     * @return {@link InetSocketAddress}
     */
    InetSocketAddress getSocketAddress();
}
