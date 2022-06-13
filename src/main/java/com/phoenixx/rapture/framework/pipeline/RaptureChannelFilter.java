package com.phoenixx.rapture.framework.pipeline;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:11 p.m [2020-11-17]
 *
 * An {@link AbstractRemoteAddressFilter} implementation that filters {@link Channel}s by the amount of active
 * connections they already have and whether they are blacklisted. A threshold is put on the amount of
 * successful connections allowed to be made in order to provide security from socket flooder attacks.
 *
 * One instance of this class must be shared across all pipelines in order to ensure that every
 * channel is using the same multiset.
 */
@ChannelHandler.Sharable
public final class RaptureChannelFilter extends AbstractRemoteAddressFilter<InetSocketAddress> {

    /** An immutable set containing whitelisted (filter bypassing) addresses. */
    public static final ImmutableSet<String> WHITELIST = ImmutableSet.of("127.0.0.1");

    /** A concurrent multiset containing active connection counts. */
    private final Multiset<String> connections = ConcurrentHashMultiset.create();

    /** A concurrent set containing blacklisted addresses. */
    private final Set<String> blacklist = Sets.newConcurrentHashSet();

    private final NetServerHandler<?,?,?> netHandler;

    //TODO Fully implement this channel filter
    public RaptureChannelFilter(NetServerHandler<?,?,?> netHandler) {
        this.netHandler = netHandler;
    }

    @Override
    protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception {
        String address = remoteAddress.getAddress().getHostAddress();

        if (WHITELIST.contains(address)) {
            // Bypass filter for whitelisted addresses.
            return true;
        }
        /*if (connections.count(address) >= connectionLimit()) {
            // Reject if more than CONNECTION_LIMIT active connections.
            response(ctx, LOGIN_LIMIT_EXCEEDED);
            return false;
        }*/
        if (blacklist.contains(address)) {
            // Reject if blacklisted (IP banned).
            return false;
        }
        return true;
    }

    @Override
    protected void channelAccepted(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
        String address = remoteAddress.getAddress().getHostAddress();

        // Increment connection count by 1.
        connections.add(address);

        // Remove address once disconnected.
        ChannelFuture future = ctx.channel().closeFuture();
        future.addListener(it -> connections.remove(address));
    }

    @Override
    protected ChannelFuture channelRejected(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
        Channel channel = ctx.channel();

       /* // Retrieve the response message.
        LoginResponse response = channel.attr(LOGIN_RESPONSE_KEY).get();
        LoginResponseMessage msg = new LoginResponseMessage(response);

        // Write initial message.
        ByteBuf initialMsg = ByteMessage.pooledBuffer(Long.BYTES);
        try {
            initialMsg.writeLong(0);
        } finally {
            channel.write(initialMsg, channel.voidPromise());
        }*/

        //TODO Send disconnect message
        return channel.writeAndFlush(null);
    }

    public void addToBlacklist(String address) {
        blacklist.add(address);
    }
}
