package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public class WorldBorderMessage implements Message {

    public static final int SET_SIZE = 0, LERP_SIZE = 1, SET_CENTER = 2, INITIALIZE = 3, SET_WARNING_TIME = 4, SET_WARNING_BLOCKS = 5;
    private int action;
    private double radius;
    private double oldRadius, newRadius;
    private long speed;
    private double x, z;
    private int portalTeleportBoundary, warningTime, warningBlocks;

    //SET_SIZE
    public WorldBorderMessage(int action, double radius) {
        this.action = action;
        this.radius = radius;
    }

    //LERP_SIZE
    public WorldBorderMessage(int action, double oldRadius, double newRadius, long speed) {
        this.action = action;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
    }

    //SET_SENTER
    public WorldBorderMessage(int action, double x, double z) {
        this.action = action;
        this.x = x;
        this.z = z;
    }

    //INITIALIZE
    public WorldBorderMessage(int action, double x, double z, double oldRadius, double newRadius, long speed, int portalTeleportBoundary, int warningTime, int warningBlocks) {
        this.action = action;
        this.x = x;
        this.z = z;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
        this.portalTeleportBoundary = portalTeleportBoundary;
        this.warningTime = warningTime;
        this.warningBlocks = warningBlocks;
    }

    //SET_WARNING_TIME
    public WorldBorderMessage(int action, int warning) {
        if (action == SET_WARNING_TIME) {
            this.warningTime = warning;
        } else if (action == SET_WARNING_BLOCKS) {
            this.warningBlocks = warning;
        }
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getOldRadius() {
        return oldRadius;
    }

    public void setOldRadius(double oldRadius) {
        this.oldRadius = oldRadius;
    }

    public double getNewRadius() {
        return newRadius;
    }

    public void setNewRadius(double newRadius) {
        this.newRadius = newRadius;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getPortalTeleportBoundary() {
        return portalTeleportBoundary;
    }

    public void setPortalTeleportBoundary(int portalTeleportBoundary) {
        this.portalTeleportBoundary = portalTeleportBoundary;
    }

    public int getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(int warningTime) {
        this.warningTime = warningTime;
    }

    public int getWarningBlocks() {
        return warningBlocks;
    }

    public void setWarningBlocks(int warningBlocks) {
        this.warningBlocks = warningBlocks;
    }

    @Override
    public String toString() {
        return "WorldBorderMessage{" +
                "action=" + action +
                ", radius=" + radius +
                ", oldRadius=" + oldRadius +
                ", newRadius=" + newRadius +
                ", speed=" + speed +
                ", x=" + x +
                ", z=" + z +
                ", portalTeleportBoundary=" + portalTeleportBoundary +
                ", warningTime=" + warningTime +
                ", warningBlocks=" + warningBlocks +
                '}';
    }
}
