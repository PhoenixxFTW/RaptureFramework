package com.phoenixx.rapture.framework.pipeline;

import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 1:30 a.m [2020-11-17]
 */
public class PacketDecoder extends SimpleChannelInboundHandler<IPacket> {
    protected PacketDecoder() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacket packet) throws Exception {
        IConnection<?,?> connection = ctx.channel().attr(IConnection.CONNECTION_ATTR).get();
        if(connection != null) {

            IProtocol protocol = connection.getProtocol();

            if(protocol != null && protocol.getProtocolID() != -1 && protocol.getPacketRegistry() != null) {
                protocol.getPacketRegistry().decodePacket(ctx, packet);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.flush();
    }
}
