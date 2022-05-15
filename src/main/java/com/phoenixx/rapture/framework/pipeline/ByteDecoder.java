package com.phoenixx.rapture.framework.pipeline;

import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.packet.IHandshakePacket;
import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.AttributeKey;
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

    public static final AttributeKey<Sharable> BYTE_DECODER_KEY = AttributeKey.valueOf("byte_decoder");

    public ByteDecoder(NetServerHandler<?,?,?> netHandler) {
        this.netHandler = netHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // Read the initial request value, validate it.
        if (msg.readableBytes() < 2 || netHandler == null) {
            return;
        }

        int packetTypeId = msg.copy().readByte();

        IConnection<?,?> connection = ctx.channel().attr(IConnection.CONNECTION_ATTR).get();

        if(connection != null) {
            IProtocol protocol = connection.getProtocol();

            if(protocol != null && protocol.getProtocolID() != -1 && protocol.getPacketRegistry() != null) {
                Class<? extends IPacket> packetClass = protocol.getPacketRegistry().getPacket(new PacketBuffer(msg.copy()));

                if (packetClass != null) {
                    IPacket packet = packetClass.getConstructor().newInstance();
                    if (packet instanceof IHandshakePacket) {
                        IHandshakePacket handshakePacket = (IHandshakePacket) packet;

                        handshakePacket.deserialize(new PacketBuffer(msg));
                        ctx.fireChannelRead(handshakePacket);
                        ctx.pipeline().remove(this);
                        return;
                    }
                }
                LOGGER.warn("Client {} failed to provide a valid handshake request! Received packet: {} ", ctx.channel().remoteAddress(), packetTypeId);
                //throw new Exception("Invalid login request [" + packetTypeId + "]");
            } else {
                LOGGER.warn("Cannot handle client {} with packet {} because no protocol has been set or can't find registry!", ctx.channel().remoteAddress(), packetTypeId);
            }
        } else {
            LOGGER.warn("Client {} attempted to send packet {} with no previous connection! Disconnecting...", ctx.channel().remoteAddress(), packetTypeId);
            ctx.channel().closeFuture().sync();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(!(cause instanceof ReadTimeoutException)){
            LOGGER.error("Cannot exception for client {} while handling handshake request. Cause: ", ctx.channel().remoteAddress(), cause);
        }
    }
}