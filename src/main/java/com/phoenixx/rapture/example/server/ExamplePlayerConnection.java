package com.phoenixx.rapture.example.server;

import com.phoenixx.rapture.framework.connection.AbstractConnection;
import com.phoenixx.rapture.framework.connection.DefaultSession;
import io.netty.channel.Channel;

import java.util.UUID;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:34 PM [18-05-2022]
 */
public class ExamplePlayerConnection extends AbstractConnection<ExampleNetHandler, DefaultSession, ExampleProtocol> {

    public ExamplePlayerConnection(int id, UUID uuid, ExampleNetHandler netHandler, Channel channel) {
        super(id, uuid, netHandler, channel);
    }

    @Override
    public void sessionEstablished(DefaultSession session) {

    }
}
