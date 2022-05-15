package com.phoenixx.rapture.framework.server;

import com.phoenixx.rapture.framework.channel.AbstractChannelInitializer;
import com.phoenixx.rapture.framework.util.NettyConfig;

import java.net.InetSocketAddress;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:47 p.m [2020-11-12]
 *
 * An interface that's meant to be implemented by a class that will start up a Netty server on a specified port.
 */
public interface NettyServer extends IServer {

    /**
     * Handles all the preloading for the server. Creating a bootstrap, adding channel handlers,
     * setting netty options etc.
     */
    void initializeServer();

    /**
     * Binds this server (starts it) on a specific IP / Port.
     *
     * @param socketAddress The provided {@link InetSocketAddress}.
     * @throws Exception Throws an exception during the start-up process if anything goes wrong.
     */
    void bind(InetSocketAddress socketAddress) throws Exception;

    /**
     * Pretty self-explanatory, stops the server. Ex. {@link AbstractNettyServer#stopServer()}.
     *
     * @throws Exception Throws an exception during the shutdown process if anything goes wrong.
     */
    void stopServer() throws Exception;

    /**
     * Can be used to set the pipeline factory that is to be used by the netty server.
     *
     * @param channelInitializer The {@link AbstractChannelInitializer} which will create a pipeline on
     *                           each incoming connection.
     */
    void setChannelInitializer(AbstractChannelInitializer channelInitializer);

    /**
     * Returns the channel pipeline factory that is associated with this netty server,
     * in our case, a The {@link AbstractChannelInitializer}. Probably will change this
     * in the future to support much more.
     *
     * @return {@link AbstractChannelInitializer}.
     */
    AbstractChannelInitializer getChannelInitializer();

    /**
     * Get the {@link NettyConfig} associated with this server.
     *
     * @return {@link NettyConfig}.
     */
    NettyConfig getNettyConfig();
}
