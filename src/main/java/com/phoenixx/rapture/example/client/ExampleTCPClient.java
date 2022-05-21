package com.phoenixx.rapture.example.client;


import com.phoenixx.rapture.example.server.ExampleLoginPacket;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jetbrains.annotations.NotNull;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 10:09 PM [18-05-2022]
 */
public class ExampleTCPClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 7777;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();

        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ExampleChannelHandler())
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(@NotNull SocketChannel channel) throws Exception {
                channel.pipeline().addLast("client_handler", new ExampleChannelHandler());
                /*System.out.println("Sending Login Packet....");
                ExampleLoginPacket loginPacket = new ExampleLoginPacket();
                loginPacket.setPacketID(0);
                channel.writeAndFlush(loginPacket.serialize(new PacketBuffer()).getByteBuf()).sync();*/
            }

            @Override
            public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
                super.channelActive(ctx);
                System.out.println("Sending Login Packet....");
                ExampleLoginPacket loginPacket = new ExampleLoginPacket();
                loginPacket.setPacketID(0);
                ctx.channel().writeAndFlush(loginPacket.serialize(new PacketBuffer()).getByteBuf()).sync();
            }
        });

        try {
            ChannelFuture future = bootstrap.connect(SERVER_IP, SERVER_PORT).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}