package com.phoenixx.rapture.example.server;

import com.phoenixx.rapture.example.packets.ExampleLoginPacket;
import com.phoenixx.rapture.framework.connection.DefaultSession;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 7:42 PM [18-05-2022]
 */
public class ExampleNetHandler extends NetServerHandler<ExampleLoginPacket, ExamplePlayerConnection, DefaultSession> {

    public final Logger LOGGER = LogManager.getLogger(ExampleNetHandler.class);

    @Override
    public @Nullable ExamplePlayerConnection createConnection(Channel channel) {
        return new ExamplePlayerConnection(this.getAbstractNettyServer().getConnectionManager().getConnections().size()+1, UUID.randomUUID(),this, channel);
    }

    @Override
    public @Nullable DefaultSession processHandshake(ExampleLoginPacket packet, ExamplePlayerConnection connection) {
        DefaultSession session = new DefaultSession(connection.getConnectionUUID());
        session.setName("ExamplePlayer");
        return session;
    }
}
