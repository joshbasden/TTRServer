package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by jbasden on 1/29/19.
 */

public class Game {
    private HashMap<String, Player> gamePlayers = new HashMap<>();
    private ArrayList<String> playerColors = new ArrayList<>(Arrays.asList("blue", "green", "red", "yellow", "black"));
    private String gameName;
    private int numPlayers;
    private boolean started;

    public boolean addPlayer(String userName) {
        if (gamePlayers.containsKey(userName)) {
            return false;
        }
        if (gamePlayers.size() == numPlayers) {
            return false;
        }
        Player newPlayer = new Player();
        newPlayer.setUserName(userName);
        int numPlayersSoFar = gamePlayers.size();
        newPlayer.setColor(playerColors.get(numPlayersSoFar));
        gamePlayers.put(userName, newPlayer);
        return true;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public HashMap<String, Player> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(HashMap<String, Player> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
