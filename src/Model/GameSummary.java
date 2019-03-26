package Model;

import java.util.List;

public class GameSummary {
    List<PlayerSummary> players;

    public void addPlayerSummary(PlayerSummary summary) {
        //Assumes it is added in order
        players.add(summary);
    }

    public List<PlayerSummary> getPlayers() {
        return players;
    }
}
