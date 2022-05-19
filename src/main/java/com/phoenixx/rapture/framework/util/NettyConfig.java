package com.phoenixx.rapture.framework.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:52 p.m [2020-11-12]
 */
public class NettyConfig {
    private int portNumber = 18090;
    private InetSocketAddress socketAddress;

    private String serverVersion = "1.0.0";

    private int bossThreadCount;
    private int workerThreadCount;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    private Map<ChannelOption<?>, Object> channelOptions;

    protected ChannelInitializer<? extends Channel> channelInitializer;

    public NettyConfig(InetSocketAddress inetSocketAddress) {
        this.socketAddress = inetSocketAddress;
        this.setPortNumber(this.socketAddress.getPort());
    }

    public NettyConfig setPortNumber(int portNumber) {
        this.portNumber = portNumber;
        return this;
    }

    public NettyConfig setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
        return this;
    }

    public NettyConfig setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
        return this;
    }

    public NettyConfig setChannelOptions(Map<ChannelOption<?>, Object> channelOptions) {
        this.channelOptions = channelOptions;
        return this;
    }

    public NettyConfig setBossGroup(NioEventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
        return this;
    }

    public NettyConfig setWorkerGroup(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
        return this;
    }

    public NettyConfig setBossThreadCount(int bossThreadCount) {
        this.bossThreadCount = bossThreadCount;
        return this;
    }

    public NettyConfig setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
        return this;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public synchronized InetSocketAddress getSocketAddress() {
        if (null == this.socketAddress) {
            this.socketAddress = new InetSocketAddress(this.portNumber);
        }
        return this.socketAddress;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public int getWorkerThreadCount() {
        return this.workerThreadCount;
    }

    public int getBossThreadCount() {
        return this.bossThreadCount;
    }

    public Map<ChannelOption<?>, Object> getChannelOptions() {
        return channelOptions;
    }

    public synchronized NioEventLoopGroup getBossGroup() {
        if (null == this.bossGroup) {
            if (0 >= this.bossThreadCount) {
                this.bossGroup = new NioEventLoopGroup();
            } else {
                this.bossGroup = new NioEventLoopGroup(this.bossThreadCount);
            }
        }
        return this.bossGroup;
    }


    public synchronized NioEventLoopGroup getWorkerGroup() {
        if (null == this.workerGroup) {
            if (0 >= this.workerThreadCount) {
                this.workerGroup = new NioEventLoopGroup();
            } else {
                this.workerGroup = new NioEventLoopGroup(this.workerThreadCount);
            }
        }
        return this.workerGroup;
    }
}
