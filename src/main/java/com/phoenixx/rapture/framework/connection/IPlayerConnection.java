package com.phoenixx.rapture.framework.connection;

import com.phoenixx.rapture.framework.NetHandler;
import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import com.phoenixx.rapture.framework.server.NetServerHandler;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:38 PM [16-05-2022]
 */
public interface IPlayerConnection<T extends NetHandler> extends IConnection<AbstractSession, IProtocol> {

    /**
     * Used to send a {@link IPacket} to this connection
     *
     * @param packet The given {@link IPacket}
     */
    void sendPacket(IPacket packet);

    /**
     * Calculate the this connections ping, by getting the difference between the last received
     * ping and the current time
     */
    void calculatePing();

    /**
     * Sets the {@link ConnectionStatus} for this connection which determines if the connection is still
     * connecting, connected, rejected, etc etc
     *
     * @param status The given {@link ConnectionStatus}
     */
    @Override
    default void setConnectionStatus(ConnectionStatus status){
        throw new UnsupportedOperationException("Cannot set the ConnectionStatus from a IPlayerConnection!");
    }

    /**
     * Sets the {@link AbstractSession} for this connection
     *
     * @param abstractSession The given {@link AbstractSession}
     */
    @Override
    default void setSession(AbstractSession abstractSession) {
        throw new UnsupportedOperationException("Cannot set the AbstractSession from a IPlayerConnection!");
    }

    /**
     * Sets the current {@link IProtocol} for this connection, used to retrieve packets
     *
     * @param protocol The given {@link IProtocol}
     */
    @Override
    default void setProtocol(IProtocol protocol) {
        throw new UnsupportedOperationException("Cannot set the IProtocol from a IPlayerConnection!");
    }

    /**
     * Get this connections current ping
     *
     * @return Ping int
     */
    int getPing();

    /**
     * Get this connections {@link NetServerHandler}
     *
     * @return The {@link NetServerHandler}
     */
    T getNetHandler();
}

