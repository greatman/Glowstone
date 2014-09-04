package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import net.glowstone.util.nbt.CompoundTag;

public class UpdateEntityNBTMessage implements Message {

    private int entityID;
    private CompoundTag tag;

    public UpdateEntityNBTMessage(int entityID, CompoundTag tag) {
        this.entityID = entityID;
        this.tag = tag;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public CompoundTag getTag() {
        return tag;
    }

    public void setTag(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "UpdateEntityNBTMessage{" +
                "entityID=" + entityID +
                ", tag=" + tag +
                '}';
    }
}
