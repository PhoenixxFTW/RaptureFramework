package com.phoenixx.rapture.framework.packet;

import com.phoenixx.rapture.framework.connection.IConnection;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-16
 * 11:01 p.m.
 *
 * This interface is meant to be implemented by packet handlers. After a packet has been deserialized, a request
 * will be made to the given {@link IPacketHandler} to proccess / handle it
 *
 * @param <REQ> This is the actual {@link IPacket} being handled
 * @param <REPLY> This is the {@link IPacket} being returned after the original pack has been
 *                processed (currently not used)
 * @param <C> The {@link IConnection} That sent this {@link IPacket} to the server
 */
public interface IPacketHandler<REQ extends IPacket, REPLY extends IPacket, C extends IConnection<?,?>> {

    /**
     * Handles a {@link IPacket} after its been sent from the client and deserialized
     *
     * @param packet The {@link IPacket} being processed
     * @param connection The {@link IConnection} that sent this packet
     * @return A {@link IPacket} (CURRENTLY UNUSED)
     */
    REPLY processPacket(REQ packet, C connection);

}
