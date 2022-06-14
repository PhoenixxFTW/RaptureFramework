package com.phoenixx.rapture.example.packets;

import com.phoenixx.rapture.example.server.ExamplePlayerConnection;
import com.phoenixx.rapture.example.server.ExampleProtocol;
import com.phoenixx.rapture.framework.packet.IHandshakePacket;
import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.IPacketHandler;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import com.phoenixx.rapture.framework.packet.impl.AbstractPacket;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 7:42 PM [18-05-2022]
 */
public class ExampleLoginPacket extends AbstractPacket implements IHandshakePacket, IPacketHandler<ExampleLoginPacket, IPacket, ExamplePlayerConnection> {

    @Override
    public PacketBuffer serialize() throws Exception {
        this.getPacketBuffer().writeInt(this.getPacketID());
        return this.getPacketBuffer();
    }

    @Override
    public void deserialize() throws Exception {
        this.LOGGER.info("Deserializing login packet with ID: " + this.getPacketID());
    }

    @Override
    public IPacket processPacket(ExampleLoginPacket packet, ExamplePlayerConnection connection) throws Exception {
        LOGGER.info("LOG IN PACKET CALLED @@@@");
        connection.setProtocol(ExampleProtocol.GAME);
        ExampleLoginResponsePacket responsePacket = new ExampleLoginResponsePacket();
        responsePacket.type = 1;
        responsePacket.serialize();
        connection.sendPacket(responsePacket);
        return this;
    }
}
