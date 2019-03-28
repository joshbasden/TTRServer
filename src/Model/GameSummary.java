package Model;

import java.util.ArrayList;
import java.util.List;

public class GameSummary {
    List<PlayerSummary> players;

    public void sort() {
        List<PlayerSummary> summaries = new ArrayList<>();
        PlayerSummary bestSummarySoFar = new PlayerSummary();
        while (players.size() > 0) {
            int biggestPointsSoFar = -1000; //Assume no one can get this bad of a score haha
            for (PlayerSummary summary: players) {
                if (summary.getTotalPoints() > biggestPointsSoFar) {
                    biggestPointsSoFar = summary.getTotalPoints();
                    bestSummarySoFar = summary;
                }
            }
            summaries.add(bestSummarySoFar);
            players.remove(bestSummarySoFar);
        }
        players = summaries;
    }

    public void addPlayerSummary(PlayerSummary summary) {
        players.add(summary);
    }

    public List<PlayerSummary> getPlayers() {
        return players;
    }
}
