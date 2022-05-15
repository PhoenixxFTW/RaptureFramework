package com.phoenixx.rapture.framework.server;

import com.phoenixx.rapture.framework.NetHandler;
import com.phoenixx.rapture.framework.connection.AbstractSession;
import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.packet.IHandshakePacket;
import io.netty.channel.Channel;
import org.jetbrains.annotations.Nullable;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-16
 * 11:13 p.m.
 *
 * An interface used to handle server side logic for authenticated clients or
 * clients attempting to authenticate.
 *
 * @param <HS> The {@link IHandshakePacket} that this {@link NetServerHandler} listens for.
 * @param <C> The {@link IConnection} this {@link NetServerHandler} creates for successfully connected clients
 * @param <S> The {@link AbstractSession} this {@link NetServerHandler} creates for authenticated clients
 */
public abstract class NetServerHandler<HS extends IHandshakePacket, C extends IConnection<S,?>, S extends AbstractSession> extends NetHandler {

    private AbstractNettyServer abstractNettyServer;

    /**
     * Creates a {@link IConnection} for any connecting client
     *
     * @param channel The channel sending the handshake
     * @return A {@link IConnection} if the connection is allowed to make anymore requests
     */
    @Nullable
    public abstract C createConnection(Channel channel);

    /**
     * Processes a handshake sent by the client, and creates a {@link AbstractSession}
     *
     * @param packet The {@link IHandshakePacket} itself
     * @param connection The {@link IConnection} sending the handshake
     * @return The {@link AbstractSession} that was created IF the client has successfully logged in
     */
    @Nullable
    public abstract S processHandshake(HS packet, C connection);

    public void setAbstractNettyServer(AbstractNettyServer abstractNettyServer) {
        this.abstractNettyServer = abstractNettyServer;
    }

    public AbstractNettyServer getAbstractNettyServer() {
        return abstractNettyServer;
    }
}
