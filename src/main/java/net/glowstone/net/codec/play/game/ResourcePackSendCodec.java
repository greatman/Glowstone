package net.glowstone.net.codec.play.game;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.game.ResourcePackSendMessage;

import java.io.IOException;

public class ResourcePackSendCodec implements Codec<ResourcePackSendMessage> {
    @Override
    public ResourcePackSendMessage decode(ByteBuf buffer) throws IOException {
        return new ResourcePackSendMessage(ByteBufUtils.readUTF8(buffer), ByteBufUtils.readUTF8(buffer));
    }

    @Override
    public ByteBuf encode(ByteBuf buf, ResourcePackSendMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buf, message.getUrl());
        ByteBufUtils.writeUTF8(buf, message.getHash());
        return buf;
    }
}
