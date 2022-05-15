package com.phoenixx.rapture.framework.connection;

import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.util.AttributeKey;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-23
 * 6:31 p.m.
 *
 * This interface is used by any connection thats made to the server.
 */
public interface IConnection<S extends AbstractSession, T extends IProtocol> extends ChannelHandler {

    AttributeKey<IConnection<?,?>> CONNECTION_ATTR = AttributeKey.valueOf("connection_handler");

    /**
     * Sets the {@link ConnectionStatus} for this connection which determines if the connection is still
     * connecting, connected, rejected, etc etc
     *
     * @param status The given {@link ConnectionStatus}
     */
    void setConnectionStatus(ConnectionStatus status);

    /**
     * Sets the {@link AbstractSession} for this connection
     *
     * @param abstractSession The given {@link AbstractSession}
     */
    void setSession(AbstractSession abstractSession);

    /**
     * Sets the current {@link IProtocol} for this connection, used to retrieve packets
     *
     * @param protocol The given {@link IProtocol}
     */
    void setProtocol(IProtocol protocol);

    /**
     * Used to send a {@link IPacket} to this connection
     *
     * @param packet The given {@link IPacket}
     */
    default void sendPacket(IPacket packet) {
        try {
            if(this.getChannel().isOpen()) {
                this.getChannel().writeAndFlush(packet.serialize(new PacketBuffer()).getByteBuf()).sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the ID of this connection, once a client connects to the server their connection
     * is given a unique and incrementing ID
     *
     * @return The given ID
     */
    int getConnectionID();

    /**
     * Gets the UUID for this connection
     *
     * @return UUID
     */
    UUID getConnectionUUID();

    /**
     * Gets the status of this connection
     *
     * @return {@link ConnectionStatus}
     */
    ConnectionStatus getConnectionStatus();

    /**
     * The {@link Channel} associated with this connection
     *
     * @return This connections {@link Channel}
     */
    Channel getChannel();

    /**
     * Gets this connections name if it has a {@link AbstractSession}, otherwise, just returns the connection ID
     *
     * @return This connections Name or ID
     */
    default String getName(){
        return getSession()!=null ? getSession().getName() : String.valueOf(getConnectionID());
    }

    /**
     * Gets this connection address
     *
     * @return A InetSocketAddress
     */
    default InetSocketAddress getAddress(){
        return ((InetSocketAddress)getChannel().remoteAddress());
    }

    /**
     * The {@link AbstractSession} is created once this connection has fully established a connection
     * with the given server, otherwise its null
     *
     * @return This connections {@link AbstractSession}
     */
    @Nullable
    S getSession();

    /**
     * Gets the {@link IProtocol} that this connection is on currently, which is used to get
     * the packets / registries
     *
     * @return The {@link IProtocol}
     */
    T getProtocol();
}
