package com.phoenixx.rapture.framework.pipeline;

import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-17
 * 1:30 a.m.
 */
public class PacketDecoder extends SimpleChannelInboundHandler<ByteBuf> {

    public static final String PIPELINE_NAME = "packet_decoder";

    protected PacketDecoder() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        if (byteBuf.readableBytes() != 0) {

            IConnection<?,?> connection = ctx.channel().attr(IConnection.CONNECTION_ATTR).get();
            if(connection != null) {

                IProtocol protocol = connection.getProtocol();

                if(protocol != null && protocol.getProtocolID() != -1 && protocol.getPacketRegistry() != null) {
                    protocol.getPacketRegistry().decodePacket(ctx, new PacketBuffer(byteBuf));
                }
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.flush();
    }
}
