package Model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Route> routes = new HashMap<>();
    private Map<Integer, City> cities = new HashMap<>();
    private iDeck destinationDeck = new DestinationCardDeck();
    private iDeck trainDeck = new TrainCarCardDeck();

    public Map<Integer, Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<Integer, Route> routes) {
        this.routes = routes;
    }

    public Map<Integer, City> getCities() {
        return cities;
    }

    public void setCities(Map<Integer, City> cities) {
        this.cities = cities;
    }

    public iDeck getDestinationDeck() {
        return destinationDeck;
    }

    public void setDestinationDeck(iDeck destinationDeck) {
        this.destinationDeck = destinationDeck;
    }

    public iDeck getTrainDeck() {
        return trainDeck;
    }

    public void setTrainDeck(iDeck trainDeck) {
        this.trainDeck = trainDeck;
    }
}