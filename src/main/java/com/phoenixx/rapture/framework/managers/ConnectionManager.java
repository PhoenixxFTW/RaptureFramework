package com.phoenixx.rapture.framework.managers;

import com.phoenixx.rapture.framework.connection.IConnection;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 7:35 p.m [2020-11-23]
 *
 * This class will handle all connection related tasks such as saving / removing connections from server memory
 */
public class ConnectionManager<T> {

    private final Map<T, IConnection<?,?>> connections = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger(ConnectionManager.class);

    /**
     * Gets the {@link IConnection} from a key
     *
     * @param key The given key
     * @return The {@link IConnection} or null if not found
     */
    public IConnection<?,?> getConnection(T key) {
        return this.connections.getOrDefault(key, null);
    }

    /**
     * Gets a {@link IConnection} from its {@link ChannelHandlerContext}, in case we don't know the key
     *
     * @param ctx The given {@link ChannelHandlerContext}
     * @return The {@link IConnection} or null if it doesn't have one
     */
    @Nullable
    public IConnection<?,?> getConnectionFromCtx(ChannelHandlerContext ctx){
        return ctx.channel().attr(IConnection.CONNECTION_ATTR).get();
    }

    /**
     * Puts the given {@link IConnection} into the server cache to be access later on
     *
     * @param key The unique key used to identify the {@link IConnection}
     * @param connection The given {@link IConnection}
     * @return Boolean depending on whether adding the connection was a success or not
     */
    public boolean putConnection(T key, IConnection<?,?> connection) {
        if(!this.connections.containsKey(key)){
            this.connections.put(key, connection);
            return true;
        }
        LOGGER.warn("Cannot add new connection with key {} as it already exists!", key);
        return false;
    }

    /**
     * Removes a given {@link IConnection} from the server cache
     *
     * @param key The unique key used to identify the {@link IConnection}
     * @return Boolean depending on whether the removal was a success or not
     */
    public boolean removeConnection(T key) {
        if(this.connections.containsKey(key)){
            this.connections.remove(key);
            return true;
        }
        LOGGER.warn("Cannot remove connection with key {} as it does not exist!", key);
        return false;
    }

    /**
     * Gets a map of all connected clients
     *
     * @return Map of key and {@link IConnection}
     */
    public Map<T, IConnection<?,?>> getConnections() {
        return this.connections;
    }
}
