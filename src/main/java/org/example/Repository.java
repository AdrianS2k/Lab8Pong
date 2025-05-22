package org.example;

// import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.net.ssl.HostnameVerifier;


public class Repository {

    private static Repository instance;
    private final Player host;
    private final Player client;
    private int count = 0;
    private final PropertyChangeSupport pcs;
//    public int hostBarPos = host.getBarPos();
//    public int clientBarPos = client.getBarPos();

    private Repository() {
        this.host = new Player();
        this.client = new Player();
        this.pcs = new PropertyChangeSupport(this);
    }


    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }


    public Player getHost(){
        return host;
    }
    public Player getClient(){
        return client;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }
    public void firePropertyChange(String property, Object oldVal, Object newVal){
        pcs.firePropertyChange(property, oldVal, newVal);
    }
    // public Player getPlayer1() { return player1; }
    // public Player getPlayer2() { return player2; }
    
}
