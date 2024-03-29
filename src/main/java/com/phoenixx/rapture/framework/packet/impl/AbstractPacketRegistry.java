package com.phoenixx.rapture.framework.packet.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.phoenixx.rapture.framework.packet.IPacket;
import com.phoenixx.rapture.framework.packet.IPacketHandler;
import com.phoenixx.rapture.framework.packet.PacketBuffer;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 3:28 a.m [2020-11-25]
 *
 * This registry handles all packets along with handlers and sub registries.
 */
public abstract class AbstractPacketRegistry {

    private final BiMap<Integer, Class<? extends IPacket>> packetMap = HashBiMap.create();
    private final Map<Class<? extends IPacket>, IPacketHandler<?, ?, ?>> packetHandlerMap = new HashMap<>();

    private final Map<Integer, AbstractPacketRegistry> packetRegistryMap = new HashMap<>();
    private final Map<Class<? extends IPacket>, AbstractPacketRegistry> classPacketRegistryMap = new HashMap<>();

    public final Logger LOGGER = LogManager.getLogger(getClass());

    public AbstractPacketRegistry() {
        this.registerPackets();
    }

    public abstract void registerPackets();

    public void processPacket(ChannelHandlerContext ctx, IPacket packet) throws Exception {
        ctx.fireChannelRead(packet);
    }

    /**
     * Decodes a {@link IPacket} manually instead of inside the {@link IPacket}'s deserialize method.
     * (Which can still be called after this)
     *
     * @param ctx The channels {@link ChannelHandlerContext}
     * @param packet The {@link IPacket} which contains all the bytes
     * @throws Exception Throws an exception if anything goes wrong while decoding / deserializing
     */
    public void decodePacket(ChannelHandlerContext ctx, IPacket packet) throws Exception {
        int packetID = packet.getPacketID();

        if(this.getPacketMap().containsKey(packetID)) {
            this.processPacket(ctx, packet);
        } else {
            if(this.getPacketRegistryMap().containsKey(packetID)){
                this.getPacketRegistryMap().get(packetID).decodePacket(ctx, packet);
            } else {
                LOGGER.warn("Unknown packet received with type {} Other packets: {} Hex: \n{}", packetID, this.getClass(), ByteBufUtil.prettyHexDump(packet.getPacketBuffer()));
            }
        }
    }

    public void registerPacket(int discriminator, Class<? extends IPacket> packet) {
        if (packetMap.containsKey(discriminator)) {
            throw new RuntimeException("Packet with discriminator " + discriminator + " already exists!");
        } else {
            packetMap.put(discriminator, packet);
        }
    }

    public <P extends IPacket> void registerPacket(int discriminator, Class<P> packet, IPacketHandler<P, ?, ?> packetHandler) {
        this.registerPacket(discriminator, packet);
        if(!this.packetHandlerMap.containsKey(packet)) {
            this.packetHandlerMap.put(packet, packetHandler);
        }
        this.classPacketRegistryMap.put(packet, this);
    }

    public <P extends IPacket> void registerPacketRegistry(int discriminator, Class<P> packet, IPacketHandler<P, ?, ?> packetHandler, AbstractPacketRegistry packetRegistry) {
        this.packetHandlerMap.put(packet, packetHandler);
        this.packetRegistryMap.put(discriminator, packetRegistry);
        this.classPacketRegistryMap.put(packet, this);
    }

    /**
     * Gets a {@link AbstractPacketRegistry} from a {@link IPacket} and the current {@link AbstractPacketRegistry}
     *
     * @param packet The given {@link IPacket}
     * @param <P> {@link IPacket}
     * @return The {@link AbstractPacketRegistry}
     */
    public <P extends IPacket> AbstractPacketRegistry getRegistryFromPacket(Class<P> packet) {
        return getRegistryFromPacket(packet, this);
    }

    /**
     * Gets a {@link AbstractPacketRegistry} from a {@link IPacket} and a {@link AbstractPacketRegistry}
     *
     * @param packet The given {@link IPacket}
     * @param packetRegistry The current {@link AbstractPacketRegistry}
     * @param <P> {@link IPacket}
     * @return The {@link AbstractPacketRegistry}
     */
    public <P extends IPacket> AbstractPacketRegistry getRegistryFromPacket(Class<P> packet, AbstractPacketRegistry packetRegistry) {
        AbstractPacketRegistry foundRegistry = packetRegistry.classPacketRegistryMap.get(packet);
        if(foundRegistry == null){
            for (AbstractPacketRegistry registry: packetRegistry.getPacketRegistryMap().values()) {
                foundRegistry = getRegistryFromPacket(packet, registry);
                if(foundRegistry!= null){
                    break;
                }
            }
        }
        return foundRegistry;
    }

    /**
     * Get a {@link IPacket} class from a supplied {@link PacketBuffer}
     *
     * @param packetBuffer The given {@link PacketBuffer}
     * @return A {@link IPacket} class
     */
    public Class<? extends IPacket> getPacket(PacketBuffer packetBuffer) {
        return getPacket(packetBuffer, this);
    }

    /**
     * //TODO TEST IF THIS WORKS, NEVER TESTED BEFORE
     * Recursively finds the packet ID for a given {@link PacketBuffer} and the current {@link AbstractPacketRegistry}
     * @param packetBuffer The {@link IPacket}'s {@link PacketBuffer} containing the packet ID we're searching for
     * @return The packet ID if the packet is inside the registry map
     */
    public int searchForPacketID(PacketBuffer packetBuffer, AbstractPacketRegistry packetRegistry) {
        int packetID = packetRegistry.getPacketID(packetBuffer.copyPacketBuffer());
        if(!packetRegistry.getPacketMap().containsKey(packetID)) {
            if(packetRegistry.getPacketRegistryMap().size() > 0) {
                for (AbstractPacketRegistry registry : packetRegistry.getPacketRegistryMap().values()) {
                    packetID = packetRegistry.searchForPacketID(packetBuffer.copyPacketBuffer(), registry);
                }
            } else {
                return -1;
            }
        }
        return packetID;
    }

    /**
     * Get a {@link IPacket} class from a supplied {@link PacketBuffer} and {@link AbstractPacketRegistry}
     *
     * @param packetBuffer The given {@link PacketBuffer}
     * @param packetRegistry The given {@link AbstractPacketRegistry} to search for the packet
     * @return A {@link IPacket} class
     */
    public Class<? extends IPacket> getPacket(PacketBuffer packetBuffer, AbstractPacketRegistry packetRegistry) {
        int discriminator = packetRegistry.getPacketID(packetBuffer.copyPacketBuffer());
        Class<? extends IPacket> packetClass = packetRegistry.getPacketMap().get(discriminator);
        if(packetClass == null) {
            for (AbstractPacketRegistry registry: packetRegistry.getPacketRegistryMap().values()) {
                discriminator = registry.getPacketID(packetBuffer.copyPacketBuffer());
                if(registry.getPacketMap().containsKey(discriminator)){
                    packetClass = registry.getPacketMap().get(discriminator);
                }
                if(packetClass!= null) {
                    break;
                }
            }
        }
        return packetClass;
    }

    /**
     * Used by classes which extend this to manually get a custom packet ID from a {@link PacketBuffer}
     *
     * @param packetBuffer The given {@link PacketBuffer}
     * @return The Packet ID
     */
    public abstract Integer getPacketID(PacketBuffer packetBuffer);

    public Integer getDiscriminator(Class<? extends IPacket> packet) {
        return packetMap.inverse().get(packet);
    }

    @SuppressWarnings("unchecked")
    public <P extends IPacket> IPacketHandler<P, ?, ?> getPacketHandler(Class<P> packet) {
        return (IPacketHandler<P, ?, ?>) packetHandlerMap.get(packet);
    }

    public BiMap<Integer, Class<? extends IPacket>> getPacketMap() {
        return packetMap;
    }

    public Map<Class<? extends IPacket>, IPacketHandler<?, ?, ?>> getPacketHandlerMap() {
        return packetHandlerMap;
    }

    public Map<Integer, AbstractPacketRegistry> getPacketRegistryMap() {
        return packetRegistryMap;
    }
}
