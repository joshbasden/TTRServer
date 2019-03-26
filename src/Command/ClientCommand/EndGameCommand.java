package Command.ClientCommand;

import Model.GameSummary;

public class EndGameCommand {
    private GameSummary summary;
    private String winner;
    private int winnerPoints;

    public GameSummary getSummary() {
        return summary;
    }

    public void setSummary(GameSummary summary) {
        this.summary = summary;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getWinnerPoints() {
        return winnerPoints;
    }

    public void setWinnerPoints(int winnerPoints) {
        this.winnerPoints = winnerPoints;
    }
}
