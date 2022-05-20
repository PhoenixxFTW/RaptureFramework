package com.phoenixx.rapture.example.server;

import com.phoenixx.rapture.example.client.ClientChannelHandler;
import com.phoenixx.rapture.framework.connection.DefaultSession;
import com.phoenixx.rapture.framework.pipeline.HandshakeHandler;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import com.phoenixx.rapture.framework.server.impl.NettyTCPServer;
import com.phoenixx.rapture.framework.util.NettyConfig;

import java.net.InetSocketAddress;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 5:25 PM [18-05-2022]
 */
public class ExampleServer {
    private static final int PORT = 7777;

    public static void main(String[] args) throws Exception {
        NetServerHandler<ExampleLoginPacket,ExamplePlayerConnection, DefaultSession> netServerHandler = new ExampleNetHandler();
        NettyTCPServer tcpServer = new NettyTCPServer(new NettyConfig(new InetSocketAddress(PORT)).setServerVersion("1.0.0"), netServerHandler, channel -> {
            channel.pipeline().addLast("channel_client_init", new ClientChannelHandler());
            channel.pipeline().addLast(HandshakeHandler.HANDSHAKE_HANDLER_ATTR.name(), new HandshakeHandler<>(ExampleLoginPacket.class, netServerHandler));
            return netServerHandler;
        });

        Runtime.getRuntime().addShutdownHook(new Thread(tcpServer::stopServer));

        tcpServer.initializeServer();
        tcpServer.startServer();
    }
}
