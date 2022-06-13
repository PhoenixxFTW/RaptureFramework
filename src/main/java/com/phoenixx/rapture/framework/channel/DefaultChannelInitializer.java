package com.phoenixx.rapture.framework.channel;

import com.phoenixx.rapture.framework.connection.ConnectionStatus;
import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.pipeline.ByteDecoder;
import com.phoenixx.rapture.framework.pipeline.RaptureChannelFilter;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 3:18 p.m [2020-11-18]
 */
public class DefaultChannelInitializer extends ChannelInitializer<Channel> implements IChannelInit {
    private final Logger LOGGER = LogManager.getLogger(DefaultChannelInitializer.class);

    private final NetServerHandler<?,?,?> netHandler;
    private final Consumer<Channel> initConsumer;

    public DefaultChannelInitializer(NetServerHandler<?,?,?> netHandler, Consumer<Channel> initConsumer) {
        this.netHandler = netHandler;
        this.initConsumer = initConsumer;
    }

    @Override
    protected void initChannel(@NotNull Channel channel) throws Exception {
        if (this.doOriginalPipeLineSetup()) {
            //channel.pipeline().addLast("read_timeout", new ReadTimeoutHandler(10));
            channel.pipeline().addLast("channel_filter", new RaptureChannelFilter(this.netHandler));
        }

        IConnection<?,?> connection = this.netHandler.createConnection(channel);

        if(connection != null) {
            this.LOGGER.info("Client {} is attempting to connect", channel.remoteAddress().toString());

            connection.setConnectionStatus(ConnectionStatus.CONNECTING);
            channel.attr(IConnection.CONNECTION_ATTR).set(connection);

            this.netHandler.getAbstractNettyServer().getConnectionManager().putConnection(connection.getConnectionID(), connection);

            channel.pipeline().addLast("byte_decoder", new ByteDecoder(this.netHandler));

            this.initConsumer.accept(channel);

            this.channelInit(channel, connection);
        }
    }

    @Override
    public void channelInit(Channel channel, IConnection<?,?> connection) {}

    public Logger getLOGGER() {
        return LOGGER;
    }
}
