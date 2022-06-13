package com.phoenixx.rapture.framework.pipeline;

import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.packet.IHandshakePacket;
import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 12:21 a.m [2020-11-17]
 *
 * This class handles all early requests from the client before they've been logged in
 */
public class ByteDecoder extends SimpleChannelInboundHandler<ByteBuf> {

    private final NetServerHandler<?,?,?> netHandler;

    private final Logger LOGGER = LogManager.getLogger(ByteDecoder.class);

    public ByteDecoder(NetServerHandler<?,?,?> netHandler) {
        this.netHandler = netHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // Read the initial request value, validate it.
        if (msg.readableBytes() < 2 || netHandler == null) {
            return;
        }

        PacketBuffer packetBuffer = new PacketBuffer(msg);
        IConnection<?,?> connection = ctx.channel().attr(IConnection.CONNECTION_ATTR).get();

        if(connection != null) {
            IProtocol protocol = connection.getProtocol();

            if(protocol != null && protocol.getProtocolID() != -1 && protocol.getPacketRegistry() != null) {
                //TODO Should redo this so we always get the packet ID first and dont have to do all the bullshit with getPacket
                //TODO Maybe use a BiSupplier or whatever here to return both the packetClass and packetID?
                //TODO Actually wait does this even work properly for packet Ids nested inside abstractPacketRegistries?
                int packetID = protocol.getPacketRegistry().getPacketID(packetBuffer.copyPacketBuffer());
                Class<? extends IPacket> packetClass = protocol.getPacketRegistry().getPacket(packetBuffer.copyPacketBuffer());

                if (packetClass != null) {
                    IPacket packet = packetClass.getConstructor().newInstance();
                    packet.setPacketBuffer(packetBuffer);
                    packet.setPacketID(packetID);
                    packet.deserialize();

                    if (packet instanceof IHandshakePacket) {

                        LOGGER.info("Passing message with ID: {} over to HANDSHAKE HANDLER:\n{}", packetID, ByteBufUtil.prettyHexDump(packet.getPacketBuffer().copyPacketBuffer()));

                        ctx.fireChannelRead(packet);
                        ctx.pipeline().remove(this);
                        return;
                    }
                }
                this.LOGGER.warn("Client {} failed to provide a valid handshake request! Received packet: \n{} ", ctx.channel().remoteAddress(), ByteBufUtil.prettyHexDump(packetBuffer));
                //throw new Exception("Invalid login request [" + packetTypeId + "]");
            } else {
                LOGGER.warn("Cannot handle packet from client {} because no protocol has been set or can't find registry!", ctx.channel().remoteAddress());
            }
        } else {
            LOGGER.warn("Client {} attempted to send a packet with no previous connection! Disconnecting...", ctx.channel().remoteAddress());
        }
        ctx.channel().closeFuture().sync();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(!(cause instanceof ReadTimeoutException)) {
            LOGGER.error("Cannot exception for client {} while handling handshake request. Cause: ", ctx.channel().remoteAddress(), cause);
        }
    }
}