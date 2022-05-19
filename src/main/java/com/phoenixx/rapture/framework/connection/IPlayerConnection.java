package com.phoenixx.rapture.framework.connection;

import com.phoenixx.rapture.framework.NetHandler;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import com.phoenixx.rapture.framework.server.NetServerHandler;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:38 PM [16-05-2022]
 */
public interface IPlayerConnection<T extends NetHandler> extends IConnection<DefaultSession, IProtocol> {

    /**
     * Calculate the connections ping, by getting the difference between the last received
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
     * Sets the {@link DefaultSession} for this connection
     *
     * @param defaultSession The given {@link DefaultSession}
     */
    @Override
    default void setSession(DefaultSession defaultSession) {
        throw new UnsupportedOperationException("Cannot set the DefaultSession from a IPlayerConnection!");
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

