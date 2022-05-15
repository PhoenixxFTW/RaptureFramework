package com.phoenixx.rapture.framework.connection;

import com.phoenixx.rapture.framework.server.NetServerHandler;

import java.util.UUID;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:03 p.m [2020-11-16]
 *
 * A session which is created by the {@link NetServerHandler} for a {@link IConnection} that has fully connected
 * and been successfully authenticated
 */
public abstract class AbstractSession {

    private String name;
    private final UUID uuid;

    public AbstractSession(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
