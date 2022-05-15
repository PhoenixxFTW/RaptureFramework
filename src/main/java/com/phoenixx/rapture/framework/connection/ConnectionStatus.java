package com.phoenixx.rapture.framework.connection;

/**
 * @author Phoenixx
 * RaptureAPI
 * 2020-11-19
 * 1:06 a.m.
 *
 * Used to determine what state a connection is in
 */
public enum ConnectionStatus {
    NOT_CONNECTED, CONNECTING, REJECTED, CONNECTED, CLOSED
}
