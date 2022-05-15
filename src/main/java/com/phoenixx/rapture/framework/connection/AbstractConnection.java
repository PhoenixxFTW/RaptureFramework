package com.phoenixx.rapture.framework.connection;

import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.IPacketHandler;
import com.phoenixx.rapture.framework.protocol.IProtocol;
import com.phoenixx.rapture.framework.server.NetServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-23
 * 7:43 p.m.
 *
 * Used by any channel that connects to the server. This is also the last handler in the pipeline for channels,
 * and so any packet that's been deserialized, is sent to this to be handled, check {@link #handlePacket(IPacket)}
 *
 * @param <NH> The {@link NetServerHandler} responsible for this connection
 * @param <S> The {@link AbstractSession} that will be applied to this connection after being authenticated
 * @param <T> The {@link IProtocol} this connection is meant to follow
 */
public abstract class AbstractConnection<NH extends NetServerHandler<?,?,S>, S extends AbstractSession, T extends IProtocol> extends SimpleChannelInboundHandler<IPacket> implements IConnection<S,T> {

    private final int id;
    private final UUID uuid;
    private final NH netHandler;
    private final Channel channel;

    private S session;
    private ConnectionStatus connectionStatus = ConnectionStatus.NOT_CONNECTED;

    public final Logger LOGGER = LogManager.getLogger(getClass());

    public AbstractConnection(int id, UUID uuid, NH netHandler, Channel channel) {
        this.id = id;
        this.uuid = uuid;
        this.netHandler = netHandler;
        this.channel = channel;
    }

    /**
     * Since we extend {@link SimpleChannelInboundHandler}, we have to override this method
     * to read our {@link IPacket}'s
     *
     * @param ctx The {@link ChannelHandlerContext} attached to the {@link Channel} for this connection
     * @param packet The {@link IPacket} we received
     * @throws Exception Throws and exception if anything goes wrong while handling this {@link IPacket}
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacket packet) throws Exception {
        this.handlePacket(packet);
    }

    /**
     * Processes / handles {@link IPacket}'s that have been sent down from the pipeline
     *
     * @param packet The provided {@link IPacket}
     * @param <P> The type of {@link IPacket}
     */
    @SuppressWarnings("unchecked")
    protected <P extends IPacket> void handlePacket(P packet) {
        IPacketHandler<P, ?, ? super AbstractConnection<NH,S,T>> handler = (IPacketHandler<P, ?, ? super AbstractConnection<NH,S,T>>) getProtocol().getPacketRegistry().getRegistryFromPacket(packet.getClass()).getPacketHandler(packet.getClass());
        if(handler != null) {
            handler.processPacket(packet, this);
        } else {
            System.out.println("HANDLER WAS NULL @@@@@@@@ FOR PACKER: " + packet.getClass().getSimpleName());
        }
    }

    /**
     * Called when a session has been established for this connection
     *
     * @param session The given {@link AbstractSession}
     */
    @Deprecated
    public abstract void sessionEstablished(S session);

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        if(cause instanceof ReadTimeoutException){
            LOGGER.error("Client " + id + " ("+ getChannel().remoteAddress().toString()+") " + "timed out! Cause: ", cause);
        } else {
            LOGGER.error("Exception caught for client " + id + " ("+ getChannel().remoteAddress().toString()+") " + " Cause: ", cause);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOGGER.info("Client {} ({}) disconnected.", this.session !=null ? this.session.getName() : id, ctx.channel().remoteAddress().toString());
        getNetHandler().getAbstractNettyServer().getConnectionManager().removeConnection(this.getConnectionID());
    }

    @Override
    public void setConnectionStatus(ConnectionStatus status) {
        this.connectionStatus = status;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setSession(AbstractSession abstractSession) {
        this.session = (S) abstractSession;
    }

    @Override
    public int getConnectionID() {
        return this.id;
    }

    @Override
    public UUID getConnectionUUID() {
        return this.uuid;
    }

    public NH getNetHandler() {
        return netHandler;
    }

    @Override
    public ConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void setProtocol(IProtocol protocol) {
        this.channel.attr(IProtocol.PROTOCOL_ATTR).set(protocol);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getProtocol() {
        return (T) this.channel.attr(IProtocol.PROTOCOL_ATTR).get();
    }

    @Nullable
    @Override
    public S getSession() {
        return this.session;
    }
}
