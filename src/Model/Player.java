package Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jbasden on 1/29/19.
 */

public class Player {
    private String username;
    private PlayerColor color;
    private int score = 0;
    private int numTrains = 5;
    private DestinationCardHand destinationCardHand = new DestinationCardHand();
    private TrainCarCardHand trainCarCardHand = new TrainCarCardHand();
    private List<Route> routesOwned = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void addDestCard(iCard card){
        destinationCardHand.addCard((DestinationCard)card);
    }

    public DestinationCard findDestCardInHand(int id){
        ArrayList<DestinationCard> cards = (ArrayList<DestinationCard>)destinationCardHand.getCards();
        for (int i = 0; i < destinationCardHand.getCards().size(); i++){
            if (cards.get(i).getId() == id){
                return cards.get(i);
            }
        }
        return null;
    }

    public void removeDestCard(int id){
        ArrayList<DestinationCard> cards = (ArrayList<DestinationCard>)destinationCardHand.getCards();
        for (int i = 0; i < destinationCardHand.getCards().size(); i++){
            if (cards.get(i).getId() == id){
                cards.remove(i);
            }
        }
    }

    public Boolean achievedDestination(DestinationCard card) {
        int startCity = card.getCity1();
        int endCity = card.getCity2();
        Set<Integer> reachablesCities = new HashSet<>();
        reachablesCities.add(startCity);
        int addedNum = 1;
        while (addedNum != 0) {
            addedNum = reachablesCities.size();
            for (Route route: routesOwned) {
                int city1 = route.getCity1().getId();
                int city2 = route.getCity2().getId();
                if (reachablesCities.contains(city1)) {
                    reachablesCities.add(city2);
                }
                if (reachablesCities.contains(city2)) {
                    reachablesCities.add(city1);
                }
            }
            addedNum = reachablesCities.size() - addedNum;
        }
        return reachablesCities.contains(endCity);
    }

    public List<Integer> getDestinationPoints() {
        int positivePoints = 0;
        int negativePoints = 0;
        List<DestinationCard> destinationCards = getDestinationCardHand().getCards();
        for (DestinationCard destinationCard: destinationCards) {
            if (achievedDestination(destinationCard)) {
                positivePoints += destinationCard.getPoints();
                score += destinationCard.getPoints();
            }
            else {
                negativePoints += destinationCard.getPoints();
                score -= destinationCard.getPoints();
            }

        }
        List<Integer> points = new ArrayList<>();
        points.add(positivePoints);
        points.add(negativePoints);
        return points;
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

    public void addTrainCard(iCard card){
        this.trainCarCardHand.addCard(card);
    }

    public TrainCarCardHand getTrainCarCardHand() {
        return trainCarCardHand;
    }

    public void setTrainCarCardhand(TrainCarCardHand trainCarCardhand) {
        this.trainCarCardHand = trainCarCardhand;
    }

    public void addRouteOwned(Route route) {
        routesOwned.add(route);
    }

    public List<Route> getRoutesOwned() {
        return routesOwned;
    }

    public void setRoutesOwned(List<Route> routesOwned) {
        this.routesOwned = routesOwned;
    }
}
