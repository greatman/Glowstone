package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public class CombatEventMessage implements Message {

    public static final int ENTER_COMBAT = 0, END_COMBAT = 1, ENTITY_DEAD = 2;
    private int event;
    private int duration;
    private int entityID, playerID;
    private String message;

    private CombatEventMessage(int event, int duration, int entityID, int playerID, String message) {
        this.event = event;
        this.duration = duration;
        this.entityID = entityID;
        this.playerID = playerID;
        this.message = message;
    }

    public CombatEventMessage(int event) {
        this(event, 0, 0, 0, null);
    }

    public CombatEventMessage(int event, int duration, int entityID) {
        this(event, duration, entityID, 0, null);
    }

    public CombatEventMessage(int event, int entityID, int playerID, String message) {
        this(event, 0, entityID, playerID, message);
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CombatEventMessage{" +
                "event=" + event +
                ", duration=" + duration +
                ", entityID=" + entityID +
                ", playerID=" + playerID +
                ", message='" + message + '\'' +
                '}';
    }
}
