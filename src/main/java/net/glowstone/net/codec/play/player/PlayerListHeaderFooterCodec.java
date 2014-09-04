package net.glowstone.net.codec.play.player;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.JsonMessage;
import net.glowstone.net.message.play.player.PlayerListHeaderFooterMessage;
import org.json.simple.JSONObject;

import java.io.IOException;

public class PlayerListHeaderFooterCodec implements Codec<PlayerListHeaderFooterMessage> {
    @Override
    public PlayerListHeaderFooterMessage decode(ByteBuf buffer) throws IOException {
        return new PlayerListHeaderFooterMessage(JsonMessage.toTextJson(ByteBufUtils.readUTF8(buffer)), JsonMessage.toTextJson(ByteBufUtils.readUTF8(buffer)));
    }

    @Override
    public ByteBuf encode(ByteBuf buf, PlayerListHeaderFooterMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buf, message.getHeader().toJSONString());
        ByteBufUtils.writeUTF8(buf, message.getFooter().toJSONString());
        return buf;
    }
}
