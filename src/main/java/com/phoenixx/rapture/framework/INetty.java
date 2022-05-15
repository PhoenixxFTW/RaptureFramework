package com.phoenixx.rapture.framework;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-16
 * 11:43 p.m.
 *
 *  Used by different netty related objects such as the server, and or clients.
 *
 *  @param <NH> The {@link NetHandler} that this {@link INetty} object should use.
 */
public interface INetty<NH extends NetHandler> {

    /**
     * Sets the {@link NetHandler} for this {@link INetty} object.
     *
     * @param netHandler The given {@link NetHandler}.
     */
    void setNetHandler(NH netHandler);

    /**
     * Gets the {@link NetHandler} connected to this {@link INetty} object.
     *
     * @return A {@link NetHandler}.
     */
    NH getNetHandler();
}
