package Model;

public class PlayerInfo {
    private String username;
    private PlayerColor color;
    private int score = 0;
    private int numTrains = 45;
    private int numTrainCards;
    private int numDestCards;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumTrains() {
        return numTrains;
    }

    public void setNumTrains(int numTrains) {
        this.numTrains = numTrains;
    }

    public int getNumTrainCards() {
        return numTrainCards;
    }

    public void setNumTrainCards(int numTrainCards) {
        this.numTrainCards = numTrainCards;
    }

    public int getNumDestCards() {
        return numDestCards;
    }

    public void setNumDestCards(int numDestCards) {
        this.numDestCards = numDestCards;
    }
}
