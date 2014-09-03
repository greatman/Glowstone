package net.glowstone.net.codec.play.player;

import com.flowpowered.networking.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.player.ServerDifficultyMessage;

import java.io.IOException;

public class ServerDifficultyCodec implements Codec<ServerDifficultyMessage> {

    @Override
    public ServerDifficultyMessage decode(ByteBuf buffer) throws IOException {
        return new ServerDifficultyMessage((int)buffer.readUnsignedByte());
    }

    @Override
    public ByteBuf encode(ByteBuf buf, ServerDifficultyMessage message) throws IOException {
        return buf.writeByte(message.getDifficulty());
    }
}
