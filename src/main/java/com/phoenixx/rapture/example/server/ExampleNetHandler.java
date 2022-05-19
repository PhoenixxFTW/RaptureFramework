package com.phoenixx.rapture.example.server;

import com.phoenixx.rapture.framework.connection.DefaultSession;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 7:42 PM [18-05-2022]
 */
public class ExampleNetHandler extends NetServerHandler<ExampleLoginPacket, ExamplePlayerConnection, DefaultSession> {

    public final Logger LOGGER = LogManager.getLogger(ExampleNetHandler.class);

    @Override
    public @Nullable ExamplePlayerConnection createConnection(Channel channel) {
        return null;
    }

    @Override
    public @Nullable DefaultSession processHandshake(ExampleLoginPacket packet, ExamplePlayerConnection connection) {
        return null;
    }
}
