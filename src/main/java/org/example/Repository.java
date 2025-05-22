package org.example;

// import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.net.ssl.HostnameVerifier;


public class Repository {

    private static Repository instance;
    private final Player host = new Player();
    private final Player client = new Player();
    private int count = 0;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public int hostBarPos = host.getBarPos();
    public int clientBarPos = client.getBarPos();
    
    public Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
        }

    // public Player getPlayer1() { return player1; }
    // public Player getPlayer2() { return player2; }
    
}
