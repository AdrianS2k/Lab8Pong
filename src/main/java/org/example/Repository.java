package org.example;

// import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.net.ssl.HostnameVerifier;

/**
 * Singleton repository for managing game state, including host and client players.
 * Provides methods for accessing players, updating scores, and firing property change events
 * to notify listeners of state changes (such as score or paddle position updates).
 * T4b
 * @author Marco
 */

public class Repository {

    private static Repository instance;
    private final Player.Host host;
    private final Player.Client client;
    private int count = 0;
    private final PropertyChangeSupport pcs;
//    public int hostBarPos = host.getBarPos();
//    public int clientBarPos = client.getBarPos();

    private Repository() {
        this.host = new Player.Host();
        this.client = new Player.Client();
        this.pcs = new PropertyChangeSupport(this);
    }


    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }


    public Player.Host getHost(){
        return host;
    }
    public Player.Client getClient(){
        return client;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
        host.addPropertyChangeListener(listener);
        client.addPropertyChangeListener(listener);
    }
    public void firePropertyChange(String property, Object oldVal, Object newVal){
        pcs.firePropertyChange(property, oldVal, newVal);
    }
    // public Player getPlayer1() { return player1; }
    // public Player getPlayer2() { return player2; }
    public void addHostScore(int points) {
        int oldScore = host.getScore();
        host.addScore(points);
        pcs.firePropertyChange("HostScore", oldScore, host.getScore());
    }

    public void addClientScore(int points) {
        int oldScore = client.getScore();
        client.addScore(points);
        pcs.firePropertyChange("ClientScore", oldScore, client.getScore());
    }

    public void setScores(int newHostScore, int newClientScore) {
        int oldH = host.getScore();
        host.score = newHostScore;
        pcs.firePropertyChange("HostScore", oldH, newHostScore);

        int oldC = client.getScore();
        client.score = newClientScore;
        pcs.firePropertyChange("ClientScore", oldC, newClientScore);
    }
}
