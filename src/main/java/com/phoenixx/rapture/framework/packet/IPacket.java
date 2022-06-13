package com.phoenixx.rapture.framework.packet;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 11:01 p.m [2020-11-16]
 */
public interface IPacket {

    /**
     * Set the ID of this packet
     *
     * @param packetID The given ID
     */
    void setPacketID(int packetID);

    /**
     * Sets the {@link PacketBuffer} for this packet. This is the pure bytes that are stored.
     * @param packetBuffer The given {@link PacketBuffer}
     */
    void setPacketBuffer(PacketBuffer packetBuffer);

    /**
     * Serialise data into this {@link IPacket}'s {@link PacketBuffer}
     *
     * @return {@link PacketBuffer} that was just written to
     */
    PacketBuffer serialize() throws Exception;

    /**
     * Deserialize data from this {@link IPacket}'s {@link PacketBuffer}
     *
     */
    void deserialize() throws Exception;

    /**
     * Gets the {@link PacketBuffer} for this packet. The pure bytes.
     * @return {@link PacketBuffer}
     */
    PacketBuffer getPacketBuffer();

    /**
     * Get the ID of this packet
     *
     * @return The ID
     */
    int getPacketID();
}
