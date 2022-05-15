package com.phoenixx.rapture.framework.pipeline;

import com.phoenixx.rapture.framework.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

@Deprecated
public class Varint21FrameDecoder extends ByteToMessageDecoder {

	public static final String PIPELINE_NAME = "splitter";

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		byte[] abyte = new byte[3];

		for (int i = 0; i < abyte.length; ++i) {
			if (!in.isReadable()) {
				in.resetReaderIndex();
				return;
			}

			abyte[i] = in.readByte();

			if (abyte[i] >= 0) {
				ByteBuf packetBytes = Unpooled.wrappedBuffer(abyte);

				try {
					int j = ByteBufUtils.readVarInt(packetBytes);

					if (in.readableBytes() >= j) {
						out.add(in.readBytes(j));
						return;
					}

					in.resetReaderIndex();
				} finally {
					packetBytes.release();
				}

				return;
			}
		}

		throw new CorruptedFrameException("Length wider than 21-bit");
	}
}