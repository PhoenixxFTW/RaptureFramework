package com.phoenixx.rapture.example.packets;

import com.phoenixx.rapture.example.server.ExamplePlayerConnection;
import com.phoenixx.rapture.framework.packet.IHandshakePacket;
import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.IPacketHandler;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import com.phoenixx.rapture.framework.packet.impl.AbstractPacket;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 6:40 PM [13-06-2022]
 */
public class ExampleTestPacket extends AbstractPacket implements IHandshakePacket, IPacketHandler<ExampleTestPacket, IPacket, ExamplePlayerConnection> {

    public String test = "NONE";

    @Override
    public PacketBuffer serialize() throws Exception {
        this.getPacketBuffer().writeInt(this.getPacketID());
        this.getPacketBuffer().writeString(this.test);
        return this.getPacketBuffer();
    }

    @Override
    public void deserialize() throws Exception {
        this.test = this.getPacketBuffer().readString();
    }

    @Override
    public IPacket processPacket(ExampleTestPacket packet, ExamplePlayerConnection connection) {
        LOGGER.info("SERVER WAS SENT EXAMPLE TEST PACKET");
        return null;
    }
}
