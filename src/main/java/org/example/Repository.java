package org.example;

// import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class Repository {

    private static Repository instance;
    private final Player host = new Player();
    private final Player client = new Player();
    private int count = 0;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
        }

    
    public class Player {
        private int playerNumber;
        private int score;
        private int barPosition;
        private String chatMessageIn;
        private String chatMessageOut;
        
        public Player() {
            
            this.score = 0;
        }
        
        public int getPlayerNumber() {
            return playerNumber;
        }
        
        public int getScore() {
            return score;
        }
        
        public void addScore(int points) {
            this.score += points;
        }
        public int getBarPosition() {
            return barPosition;
        }
        public void setBarPosition(int barPosition) {
            int oldbarPosition = this.barPosition;
            this.barPosition = barPosition;
            pcs.firePropertyChange("barPosition", oldbarPosition, barPosition);
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
            this.barPosition = 0;
            this.chatMessageIn = "";
            this.chatMessageOut = "";
        }
    }

    


    public Player getPlayer1() { return host; }
    public Player getPlayer2() { return client; }
    
   
    
    }

