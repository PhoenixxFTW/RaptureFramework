package com.phoenixx.rapture.example.server;

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
    public void deserialize(PacketBuffer packetBuffer) throws Exception {

    }

    @Override
    public PacketBuffer serialize(PacketBuffer packetBuffer) throws Exception {
        return null;
    }

    @Override
    public IPacket processPacket(ExampleLoginPacket packet, ExamplePlayerConnection connection) {
        return null;
    }
}
