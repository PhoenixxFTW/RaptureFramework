package com.phoenixx.rapture.example.client;

import com.phoenixx.rapture.example.server.ExampleLoginPacket;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:08 PM [18-05-2022]
 */
public class ExampleChannelHandler extends ChannelInboundHandlerAdapter {

    public ExampleChannelHandler() {
        super();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("channelRegistered @@@@@@");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("channelUnregistered @@@@@@");
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channelActive @@@@@@");
        System.out.println("Sending Login Packet....");
        ExampleLoginPacket loginPacket = new ExampleLoginPacket();
        loginPacket.setPacketID(0);
        ctx.channel().writeAndFlush(loginPacket.serialize(new PacketBuffer()).getByteBuf()).sync();
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channelInactive @@@@@@");
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        super.channelRead(ctx, msg);
        System.out.println("channelRead @@@@@@");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println("channelReadComplete @@@@@@");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        System.out.println("userEventTriggered @@@@@@ " + evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
        System.out.println("channelWritabilityChanged @@@@@@");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("Exception caught @@@@@@");
    }
}
