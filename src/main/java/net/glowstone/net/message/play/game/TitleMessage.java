package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public class TitleMessage implements Message {

    public static final int TITLE = 0, SUBTITLE = 1, TIMES = 2, CLEAR = 3, RESET = 4;
    private int action;
    private String text;
    private int fadeIn, stay, fadeOut;


    public TitleMessage(int action, String text) {
        this.action = action;
        this.text = text;
    }

    public TitleMessage(int action, int fadeIn, int stay, int fadeOut) {
        this.action = action;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public TitleMessage(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        stay = stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        fadeOut = fadeOut;
    }

    @Override
    public String toString() {
        return "TitleMessage{" +
                "action=" + action +
                ", text='" + text + '\'' +
                ", fadeIn=" + fadeIn +
                ", stay=" + stay +
                ", fadeOut=" + fadeOut +
                '}';
    }
}
