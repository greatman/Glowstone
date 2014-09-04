package net.glowstone.net.codec.play.player;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.player.CameraMessage;

import java.io.IOException;

public class CameraCodec implements Codec<CameraMessage> {
    @Override
    public CameraMessage decode(ByteBuf buffer) throws IOException {
        return new CameraMessage(ByteBufUtils.readVarInt(buffer));
    }

    @Override
    public ByteBuf encode(ByteBuf buf, CameraMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buf, message.getCameraID());
        return buf;
    }
}
