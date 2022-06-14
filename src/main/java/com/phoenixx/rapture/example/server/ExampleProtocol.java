package com.phoenixx.rapture.example.server;

import com.phoenixx.rapture.example.packets.ExampleLoginPacket;
import com.phoenixx.rapture.example.packets.ExampleLoginResponsePacket;
import com.phoenixx.rapture.example.packets.ExampleTestPacket;
import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import com.phoenixx.rapture.framework.packet.impl.AbstractPacketRegistry;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:37 PM [18-05-2022]
 */
public enum ExampleProtocol implements IProtocol {
    NONE(-1, null),
    HANDSHAKE(0, new AbstractPacketRegistry() {
        @Override
        public void registerPackets() {
            this.registerPacket(0, ExampleLoginPacket.class, new ExampleLoginPacket());
        }

        @Override
        public Integer getPacketID(PacketBuffer packetBuffer) {
            ExampleProtocol.LOGGER.info("getPacket() Called in Example Protocol with packet ID: " + packetBuffer.copyPacketBuffer().readInt());
            return packetBuffer.readInt();
        }

        @Override
        public void decodePacket(ChannelHandlerContext ctx, IPacket packet) throws Exception {
            ExampleProtocol.LOGGER.info("decodePacket() Called in Example Protocol with packet ID: " + packet.getPacketBuffer().readInt());
            super.decodePacket(ctx, packet);
        }
    }),

    GAME(1, new AbstractPacketRegistry() {
        @Override
        public void registerPackets() {
            this.registerPacket(1, ExampleLoginResponsePacket.class, new ExampleLoginResponsePacket());
            this.registerPacket(2, ExampleTestPacket.class, new ExampleTestPacket());
        }

        @Override
        public Integer getPacketID(PacketBuffer packetBuffer) {
            return (int) packetBuffer.readInt();
        }
    });

    private final int protocolID;
    private final AbstractPacketRegistry packetRegistry;

    private static final Logger LOGGER = LogManager.getLogger(ExampleProtocol.class);

    ExampleProtocol(int protocolID, AbstractPacketRegistry packetRegistry) {
        this.protocolID = protocolID;
        this.packetRegistry = packetRegistry;
    }

    @Override
    public int getProtocolID() {
        return this.protocolID;
    }

    @Override
    public AbstractPacketRegistry getPacketRegistry() {
        return this.packetRegistry;
    }
}


