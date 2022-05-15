package com.phoenixx.rapture.framework.channel;

import com.phoenixx.rapture.framework.connection.ConnectionStatus;
import com.phoenixx.rapture.framework.connection.IConnection;
import com.phoenixx.rapture.framework.pipeline.ByteDecoder;
import com.phoenixx.rapture.framework.pipeline.RaptureChannelFilter;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-18
 * 3:18 p.m.
 */
public abstract class AbstractChannelInitializer extends ChannelInitializer<Channel> implements IChannelInit {

    private final NetServerHandler<?,?,?> netHandler;
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    public AbstractChannelInitializer(NetServerHandler<?,?,?> netHandler) {
        this.netHandler = netHandler;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        IConnection<?,?> connection = this.netHandler.createConnection(channel);

        if(connection != null) {
            LOGGER.info("Client {} is attempting to connect", channel.remoteAddress().toString());

            connection.setConnectionStatus(ConnectionStatus.CONNECTING);
            channel.attr(IConnection.CONNECTION_ATTR).set(connection);

            this.netHandler.getAbstractNettyServer().getConnectionManager().putConnection(connection.getConnectionID(),connection);

            if (doOriginalPipeLineSetup()) {
                channel.pipeline().addLast("read_timeout", new ReadTimeoutHandler(10));
                channel.pipeline().addLast(RaptureChannelFilter.FILTER_KEY.name(), new RaptureChannelFilter(this.netHandler));
                channel.pipeline().addLast(ByteDecoder.BYTE_DECODER_KEY.name(), new ByteDecoder(this.netHandler));
            }

            this.channelInit(channel, connection);
        }
    }

    @Override
    public void channelInit(Channel channel, IConnection<?,?> connection) {}

    public NetServerHandler<?, ?, ?> getNetHandler() {
        return netHandler;
    }

    public Logger getLOGGER() {
        return LOGGER;
    }
}
