package com.phoenixx.rapture.framework.connection;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 1:06 a.m [2020-11-19]
 *
 * Used to determine what state a connection is in
 */
public enum ConnectionStatus {
    NOT_CONNECTED, CONNECTING, REJECTED, CONNECTED, CLOSED
}
