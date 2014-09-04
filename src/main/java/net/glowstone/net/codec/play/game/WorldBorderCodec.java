package net.glowstone.net.codec.play.game;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.game.WorldBorderMessage;

import java.io.IOException;

//TODO: the longs need to be a VarLong
public class WorldBorderCodec implements Codec<WorldBorderMessage> {
    @Override
    public WorldBorderMessage decode(ByteBuf buffer) throws IOException {
        WorldBorderMessage worldBorderMessage = null;
        int action = ByteBufUtils.readVarInt(buffer);
        if (action == WorldBorderMessage.SET_SIZE) {
            double radius = buffer.readDouble();
            worldBorderMessage = new WorldBorderMessage(action, radius);
        } else if (action == WorldBorderMessage.LERP_SIZE) {
            double oldRadius = buffer.readDouble();
            double newRadius = buffer.readDouble();
            long speed = buffer.readLong();
            worldBorderMessage = new WorldBorderMessage(action, oldRadius, newRadius, speed);
        } else if (action == WorldBorderMessage.SET_CENTER) {
            double x = buffer.readDouble();
            double z = buffer.readDouble();
            worldBorderMessage = new WorldBorderMessage(action, x, z);
        } else if (action == WorldBorderMessage.INITIALIZE) {
            double x = buffer.readDouble();
            double z = buffer.readDouble();
            double oldRadius = buffer.readDouble();
            double newRadius = buffer.readDouble();
            long speed = buffer.readLong();
            int portalTeleportBoundary = ByteBufUtils.readVarInt(buffer);
            int warningTime = ByteBufUtils.readVarInt(buffer);
            int warningBlocks = ByteBufUtils.readVarInt(buffer);
            worldBorderMessage = new WorldBorderMessage(action, x, z, oldRadius, newRadius, speed, portalTeleportBoundary, warningTime, warningBlocks);
        } else if (action == WorldBorderMessage.SET_WARNING_TIME || action == WorldBorderMessage.SET_WARNING_BLOCKS) {
            int warningTime = ByteBufUtils.readVarInt(buffer);
            worldBorderMessage = new WorldBorderMessage(action, warningTime);
        }
        return worldBorderMessage;
    }

    @Override
    public ByteBuf encode(ByteBuf buf, WorldBorderMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buf, message.getAction());
        if (message.getAction() == WorldBorderMessage.SET_SIZE) {
            buf.writeDouble(message.getRadius());
        } else if (message.getAction() == WorldBorderMessage.LERP_SIZE) {
            buf.writeDouble(message.getOldRadius());
            buf.writeDouble(message.getNewRadius());
            buf.writeLong(message.getSpeed());
        } else if (message.getAction() == WorldBorderMessage.SET_CENTER) {
            buf.writeDouble(message.getX());
            buf.writeDouble(message.getZ());
        } else if (message.getAction() == WorldBorderMessage.INITIALIZE) {
            buf.writeDouble(message.getX());
            buf.writeDouble(message.getZ());
            buf.writeDouble(message.getOldRadius());
            buf.writeDouble(message.getNewRadius());
            buf.writeLong(message.getSpeed());
            ByteBufUtils.writeVarInt(buf, message.getPortalTeleportBoundary());
            ByteBufUtils.writeVarInt(buf, message.getWarningTime());
            ByteBufUtils.writeVarInt(buf, message.getWarningBlocks());
        } else if (message.getAction() == WorldBorderMessage.SET_WARNING_TIME) {
            ByteBufUtils.writeVarInt(buf, message.getWarningTime());
        } else if (message.getAction() == WorldBorderMessage.SET_WARNING_BLOCKS) {
            ByteBufUtils.writeVarInt(buf, message.getWarningBlocks());
        }
        return buf;
    }
}
