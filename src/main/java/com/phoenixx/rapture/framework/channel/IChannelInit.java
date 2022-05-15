package com.phoenixx.rapture.framework.channel;

import com.phoenixx.rapture.framework.connection.IConnection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 3:24 p.m [2020-11-18]
 */
public interface IChannelInit {

    /**
     * Gets called during {@link ChannelInitializer #initChannel(Channel)}
     *
     * @param channel The {@link Channel}
     * @param connection The {@link IConnection}
     */
    void channelInit(Channel channel, IConnection<?,?> connection);

    /**
     * Used to add the timeout handler and channel filter to the pipeline before {@link #channelInit}
     *
     * @return Boolean depending on whether we need the other handlers or not
     */
    default boolean doOriginalPipeLineSetup(){
        return true;
    }
}
