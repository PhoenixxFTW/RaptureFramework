package com.phoenixx.rapture.framework.server;

import java.net.InetSocketAddress;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:39 p.m [2020-11-12]
 */
public interface IServer {

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
     * Gets the {@link InetSocketAddress} that this server should be hosted on
     *
     * @return {@link InetSocketAddress}
     */
    InetSocketAddress getSocketAddress();
}
