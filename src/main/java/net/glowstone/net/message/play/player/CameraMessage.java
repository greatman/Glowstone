package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public class CameraMessage implements Message {

    private int cameraID;

    public CameraMessage(int cameraID) {
        this.cameraID = cameraID;
    }

    public int getCameraID() {
        return cameraID;
    }

    public void setCameraID(int cameraID) {
        this.cameraID = cameraID;
    }

    @Override
    public String toString() {
        return "CameraMessage{" +
                "cameraID=" + cameraID +
                '}';
    }
}
