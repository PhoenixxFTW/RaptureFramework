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
 * @since 6:33 PM [13-06-2022]
 */
public class ExampleLoginResponsePacket extends AbstractPacket implements IHandshakePacket, IPacketHandler<ExampleLoginResponsePacket, IPacket, ExamplePlayerConnection> {

    public int type = -1;

    @Override
    public PacketBuffer serialize() throws Exception {
        this.getPacketBuffer().writeInt(this.getPacketID());
        this.getPacketBuffer().writeInt(this.type);
        return this.getPacketBuffer();
    }

    @Override
    public void deserialize() throws Exception {
        this.type = this.getPacketBuffer().readInt();
    }

    @Override
    public IPacket processPacket(ExampleLoginResponsePacket packet, ExamplePlayerConnection connection) throws Exception {
        if(packet.type == 1) {
            // Success
            LOGGER.info("CLIENT SUCCESSFULLY CONNECTED AND RETURN THE RESPONSE PACKET");
            ExampleTestPacket testPacket = new ExampleTestPacket();
            testPacket.test = "SUCCESS";
            testPacket.serialize();
            connection.sendPacket(testPacket);
        } else {
            // Fail
            LOGGER.info("CLIENT FAILED CONNECTION AND RETURNED THE RESPONSE PACKET");
        }
        return null;
    }
}
