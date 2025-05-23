package org.example;

import java.beans.PropertyChangeSupport;


/**
 * Represents a player in the Pong game, tracking score, paddle position, and chat messages.
 * This class provides methods for updating and retrieving player state.
 * It also contains static inner classes for Host and Client players, which support property change listeners for paddle movement.
 * T4b
 * @author Marco
 */

public class T4b_Player {

    protected int score;
    protected int barPos;
    protected String chatMessageIn;
    protected String chatMessageOut;

    public T4b_Player() {
        reset();
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getBarPos() {
        return barPos;
    }

    public void setBarPos(int barPos) {
        this.barPos = barPos;
    }

    public String getChatMessageIn() {
        return chatMessageIn;
    }

    public void setChatMessageIn(String chatMessageIn) {
        this.chatMessageIn = chatMessageIn;
    }

    public String getChatMessageOut() {
        return chatMessageOut;
    }

    public void setChatMessageOut(String chatMessageOut) {
        this.chatMessageOut = chatMessageOut;
    }

    public void reset() {
        this.score = 0;
        this.barPos = 0;
        this.chatMessageIn = "";
        this.chatMessageOut = "";
    }

    public static class Host extends T4b_Player {
        private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        public Host() {
            super();
        }

        @Override
        public void setBarPos(int barPos) {
            int oldBarPos = this.barPos;
            this.barPos = barPos;
            pcs.firePropertyChange("HostBarPosition", oldBarPos, barPos);
        }
        public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

    }

    public static class Client extends T4b_Player {
        private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        public Client() {
            super();
        }

        @Override
        public void setBarPos(int barPos) {
            int oldBarPos = this.barPos;
            this.barPos = barPos;
            pcs.firePropertyChange("ClientBarPosition", oldBarPos, barPos);
        }
        public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

    }
}
