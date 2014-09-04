package net.glowstone.net.codec.play.game;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.game.TitleMessage;

import java.io.IOException;

public class TitleCodec implements Codec<TitleMessage> {
    @Override
    public TitleMessage decode(ByteBuf buffer) throws IOException {
        TitleMessage titleMessage = null;
        int action = ByteBufUtils.readVarInt(buffer);
        if (action == TitleMessage.TITLE || action == TitleMessage.SUBTITLE) {
            String text = ByteBufUtils.readUTF8(buffer);
            titleMessage = new TitleMessage(action, text);
        } else if (action == TitleMessage.TIMES) {
            int fadeIn = buffer.readInt();
            int stay = buffer.readInt();
            int fadeOut = buffer.readInt();
            titleMessage = new TitleMessage(action, fadeIn, stay, fadeOut);
        } else {
            titleMessage = new TitleMessage(action);
        }
        return titleMessage;
    }

    @Override
    public ByteBuf encode(ByteBuf buf, TitleMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buf, message.getAction());
        if (message.getAction() == TitleMessage.TITLE || message.getAction() == TitleMessage.SUBTITLE) {
            ByteBufUtils.writeUTF8(buf, message.getText());
        } else if (message.getAction() == TitleMessage.TIMES) {
            buf.writeInt(message.getFadeIn());
            buf.writeInt(message.getStay());
            buf.writeInt(message.getFadeOut());
        }
        return buf;
    }
}
