package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbasden on 1/29/19.
 */

public class Player {
    private String username;
    private PlayerColor color;
    private int score = 0;
    private int numTrains = 45;
    private DestinationCardHand destinationCardHand = new DestinationCardHand();
    private TrainCarCardHand trainCarCardHand = new TrainCarCardHand();
    private List<Route> routesOwned = new ArrayList<>();

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

    public DestinationCardHand getDestinationCardHand() {
        return destinationCardHand;
    }

    public void setDestinationCardHand(DestinationCardHand destinationCardHand) {
        this.destinationCardHand = destinationCardHand;
    }

    public TrainCarCardHand getTrainCarCardHand() {
        return trainCarCardHand;
    }

    public void setTrainCarCardhand(TrainCarCardHand trainCarCardhand) {
        this.trainCarCardHand = trainCarCardhand;
    }

    public List<Route> getRoutesOwned() {
        return routesOwned;
    }

    public void setRoutesOwned(List<Route> routesOwned) {
        this.routesOwned = routesOwned;
    }
}
