package com.phoenixx.rapture.framework.pipeline;

import com.phoenixx.rapture.framework.NetHandler;
import com.phoenixx.rapture.framework.connection.AbstractConnection;
import com.phoenixx.rapture.framework.connection.AbstractSession;
import com.phoenixx.rapture.framework.connection.ConnectionStatus;
import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.packet.IHandshakePacket;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:30 p.m [2020-11-16]
 *
 * This class handles all handshake packets that get passed down the pipeline from the {@link ByteDecoder}
 */
public class HandshakeHandler<REQ extends IHandshakePacket, H extends NetServerHandler<REQ, C, ?>, C extends AbstractConnection<?,?,?>> extends SimpleChannelInboundHandler<REQ> {

    public static final AttributeKey<NetHandler> HANDSHAKE_HANDLER_ATTR = AttributeKey.valueOf("handshake_handler");

    private static final Logger LOGGER = LogManager.getLogger(HandshakeHandler.class);

    private final H netHandler;

    public HandshakeHandler(Class<REQ> handshakePacketType, H netHandler) {
        super(handshakePacketType);
        this.netHandler = netHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, REQ msg) throws Exception {
        IConnection<?,?> connection = ctx.channel().attr(IConnection.CONNECTION_ATTR).get();
        if(connection != null) {
            AbstractSession abstractSession = netHandler.processHandshake(msg, (C) connection);
            if (abstractSession != null) {
                connection.setSession(abstractSession);
                connection.setConnectionStatus(ConnectionStatus.CONNECTED);

                ctx.channel().attr(NetHandler.NET_HANDLER_ATTR).set(netHandler);
                ctx.channel().pipeline().addLast(PacketDecoder.PIPELINE_NAME, new PacketDecoder());
                ctx.channel().pipeline().addLast(AbstractConnection.CONNECTION_ATTR.name(), connection);

                ctx.fireChannelRead(msg);

                ctx.channel().pipeline().remove(this);
            } else {
                LOGGER.info("{} has failed to establish a session. Failed the handshake?", ctx.channel().remoteAddress().toString());
            }
        }
    }

}
