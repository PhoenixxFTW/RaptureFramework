package com.phoenixx.rapture.framework.channel;

import com.phoenixx.rapture.framework.connection.ConnectionStatus;
import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.pipeline.ByteDecoder;
import com.phoenixx.rapture.framework.pipeline.RaptureChannelFilter;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import com.phoenixx.rapture.framework.util.ConsumerSupplier;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 3:18 p.m [2020-11-18]
 */
public class DefaultChannelInitializer extends ChannelInitializer<Channel> implements IChannelInit {
    private final Logger LOGGER = LogManager.getLogger(DefaultChannelInitializer.class);

    private final ConsumerSupplier<Channel, NetServerHandler<?,?,?>> initConsumer;

    public DefaultChannelInitializer(ConsumerSupplier<Channel, NetServerHandler<?,?,?>> initConsumer) {
        this.initConsumer = initConsumer;
    }

    @Override
    protected void initChannel(@NotNull Channel channel) throws Exception {
        // We add this first, so we can reference the "read_timeout" handler
        if (this.doOriginalPipeLineSetup()) {
            channel.pipeline().addLast("read_timeout", new ReadTimeoutHandler(10));
        }

        NetServerHandler<?,?,?> netHandler = this.initConsumer.accept(channel);
        IConnection<?,?> connection = netHandler.createConnection(channel);

        if(connection != null) {
            LOGGER.info("Client {} is attempting to connect", channel.remoteAddress().toString());

            connection.setConnectionStatus(ConnectionStatus.CONNECTING);
            channel.attr(IConnection.CONNECTION_ATTR).set(connection);

            netHandler.getAbstractNettyServer().getConnectionManager().putConnection(connection.getConnectionID(), connection);

            if (this.doOriginalPipeLineSetup()) {
                // We add these after readTime out so that the original initConsumer accept call will be after all of these
                channel.pipeline().addAfter("read_timeout", RaptureChannelFilter.FILTER_KEY.name(), new RaptureChannelFilter(netHandler));
                channel.pipeline().addAfter("read_timeout", ByteDecoder.BYTE_DECODER_KEY.name(), new ByteDecoder(netHandler));
            }

            this.channelInit(channel, connection);
        }
    }

    @Override
    public void channelInit(Channel channel, IConnection<?,?> connection) {}

    public Logger getLOGGER() {
        return LOGGER;
    }
}
