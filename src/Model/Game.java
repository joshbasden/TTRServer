package Model;

import Result.DrawTrainCarCardResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by jbasden on 1/29/19.
 */

public class Game {
    private Map<String, Player> gamePlayers = new HashMap<>();
    private List<PlayerInfo> playerStats = new ArrayList<>();
    private GameInfo gameInfo = new GameInfo();
    private boolean started;
    private List<Event> eventHistory = new ArrayList<>();
    private List<String> turnOrder = new ArrayList<>();
    private List<PlayerColor> playerColors = new ArrayList<PlayerColor>(Arrays.asList(PlayerColor.BLUE,
            PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.BLACK));
    private int numDestinationCardChoicesReceived = 0;
    private DestinationCardDeck gameDestinationDeck = new DestinationCardDeck();
    private TrainCarCardDeck gameTrainDeck = new TrainCarCardDeck();
    private Map<Integer, Route> routes = new HashMap<>();
    private Map<Integer, City> cities = new HashMap<>();
    private List<TrainCarCard> faceUpTrainCarCards = new ArrayList<>(); //Initial
    private int destinationDeckSize = 30;
    private int trainCarCardDeckSize = 105; //initial - 5 faceUps
    private String lastPlayerToTakeTurn = "";
    private boolean lastRound;

    public boolean addPlayer(String username) {
        if (gamePlayers.containsKey(username)) {
            return false;
        }
        if (gamePlayers.size() == gameInfo.getNumPlayers()) {
            return false;
        }
        Player newPlayer = new Player();
        newPlayer.setUsername(username);
        int numPlayersSoFar = gamePlayers.size();
        newPlayer.setColor(playerColors.get(numPlayersSoFar));
        gamePlayers.put(username, newPlayer);
        return true;
    }

    public PlayerInfo findPlayerInfo(String user){
        PlayerInfo playerInfo = null;
        for (PlayerInfo p: playerStats){
            if (p.getUsername().equals(user)){
                playerInfo = p;
                break;
            }
        }
        return playerInfo;
    }

    public ArrayList<iCard> replaceAllFaceUp(){
        int locomotiveCounter = 0;
        ArrayList<iCard> newHand = new ArrayList<>();
        ArrayList<iCard> discardPile = (ArrayList) gameTrainDeck.getDiscardPile();

        //add old face up cards to discard
        for (iCard c: faceUpTrainCarCards){
            gameTrainDeck.addCardToDiscardPile((TrainCarCard) c);
        }
        //clear old face ups
//        gameTrainDeck.clearAllFaceUp();
        faceUpTrainCarCards.clear();

        //get new face up cards
        for (int i = 0; i < 5; i++){
            iCard card = gameTrainDeck.draw();
            if (((TrainCarCard)card).getType() == TrainCarCardType.LOCOMOTIVE){
                locomotiveCounter += 1;
            }
            if (((TrainCarCard)card).getType() == TrainCarCardType.LOCOMOTIVE && locomotiveCounter == 2){
                while (((TrainCarCard)card).getType() == TrainCarCardType.LOCOMOTIVE){
                    // add card to discard
                    gameTrainDeck.addCardToDiscardPile((TrainCarCard) card);
                    card = gameTrainDeck.draw();
                }
            }
            newHand.add(card);
        }

//        if (locomotiveCounter >= 3){
//            return replaceAllFaceUp();
//        }

        //add new cards to face up cards
        for (iCard c: newHand){
//            gameTrainDeck.addCardToFaceUp((TrainCarCard) c);
            faceUpTrainCarCards.add((TrainCarCard) c);
        }

        return newHand;
    }

    public ArrayList<iCard> drawFaceUp(int ind, String user){
        Player player = gamePlayers.get(user);
        PlayerInfo playerInfo = findPlayerInfo(user);

        //make arraylist of icard to send back
        ArrayList<iCard> cards = new ArrayList<iCard>();

        iCard faceUpCard = faceUpTrainCarCards.get(ind);
        iCard drawCard = gameTrainDeck.draw();

        faceUpTrainCarCards.set(ind, (TrainCarCard)drawCard);

        // check if we need to replace all of the face up cards
        if (needToReplaceAll()){
            cards = replaceAllFaceUp();

            return cards;
        }

        //first index is the draw card to replace it
        //second index is face up card
        cards.add(drawCard);
        cards.add(faceUpCard);

        player.addTrainCard(cards.get(1));
        playerInfo.incrementNumTrainCards(1);

        return cards;

    }

    public DrawTrainCarCardResult drawTopCard(String user){
        DrawTrainCarCardResult result = new DrawTrainCarCardResult();
        Player player = gamePlayers.get(user);
        PlayerInfo pInfo = findPlayerInfo(user);

        try{
            iCard card = gameTrainDeck.draw();

            player.addTrainCard(card);
            pInfo.incrementNumTrainCards(1);

            result.setCard(card);
            result.setSuccess(true);

            return result;
        }
        catch (Exception e){
            result.setErrorMessage("Could not draw Train Car Card");
            result.setSuccess(false);
            return result;
        }

    }

    public String getNextTurn(String curPlayer){
        int size = turnOrder.size();
        int nextInd = turnOrder.indexOf(curPlayer) + 1;
        int nextPlayer = nextInd % size;
        Player player = gamePlayers.get(curPlayer);
        int numTrains = player.getNumTrains();
        if (!isLastRound() && numTrains <= 2) {
            setLastRound(true);
        }
        return turnOrder.get(nextPlayer);
    }

    public void addEvent(Event event) {
        eventHistory.add(event);
    }

    public void readInDestinationCards() {
        try {
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/DestinationCards.json")));
            JsonObject obj = gson.fromJson(jsonString, JsonObject.class);
            JsonArray cards = (JsonArray)obj.get("cards");
            for (int i = 0; i < cards.size(); ++i) {
                DestinationCard card = new DestinationCard();
                JsonObject jsonCard = (JsonObject)cards.get(i);
                card.setId(i);
                card.setCity1(Integer.parseInt(jsonCard.get("city1").toString()));
                card.setCity2(Integer.parseInt(jsonCard.get("city2").toString()));
                card.setPoints(Integer.parseInt(jsonCard.get("points").toString()));
                gameDestinationDeck.addCard(card);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInTrainCarCards() {
        try {
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/TrainCarCards.json")));
            JsonObject obj = gson.fromJson(jsonString, JsonObject.class);
            JsonArray cards = (JsonArray)obj.get("cards");
            for (int i = 0; i < cards.size(); ++i) {
                TrainCarCard card = new TrainCarCard();
                JsonObject jsonCard = (JsonObject)cards.get(i);
                String type = jsonCard.get("type").toString();
                type = type.substring(1,type.length() - 1);
                TrainCarCardType enumType = TrainCarCardType.valueOf(type);
                card.setType(enumType);
                gameTrainDeck.addCard(card);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInCities() {
        try {
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/Cities.json")));
            JsonObject citiesJson = gson.fromJson(jsonString, JsonObject.class);
            JsonObject cityJson;
            City city;
            for (int i = 0; i < citiesJson.size(); ++i) {
                city = new City();
                cityJson = (JsonObject)citiesJson.get(Integer.toString(i));
                city.setId(i);
                city.setLatitude(cityJson.get("latitude").getAsDouble());
                city.setLongitude(cityJson.get("longitude").getAsDouble());
                String nameLong = cityJson.get("name").toString();
                city.setName(nameLong.substring(1,nameLong.length() - 1));
                cities.put(i,city);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInRoutes() {
        try {
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/Routes.json")));
            JsonObject routesJson = gson.fromJson(jsonString, JsonObject.class);
            JsonObject routeJson;
            Route route;
            for (int i = 0; i < routesJson.size(); ++i) {
                routeJson = (JsonObject) routesJson.get(Integer.toString(i));
                route = new Route();
                int city1 = routeJson.get("city1").getAsInt();
                int city2 = routeJson.get("city2").getAsInt();
                String color = routeJson.get("color").toString();
                route.setId(i);
                route.setCity1(cities.get(city1));
                route.setCity2(cities.get(city2));
                route.setNumTracks(routeJson.get("numTracks").getAsInt());
//                route.setPoints(routeJson.get("points").getAsInt());

                route.setOwner("");
                //TODO: Read in if double route or not
                route.setColor(RouteColor.valueOf(color.substring(1, color.length() - 1)));
                routes.put(i, route);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInJsonFiles() {
        readInDestinationCards();
        readInTrainCarCards();
        readInCities();
        readInRoutes();
    }

    public void chooseInitialFaceUps() {
        for (int i = 0; i < 5; ++i) {
            faceUpTrainCarCards.add((TrainCarCard) gameTrainDeck.draw());
        }
    }

    public void determineOrder() {
        List<String> order = new ArrayList<>();
        for (Player player: gamePlayers.values()) {
            order.add(player.getUsername());
        }
        setTurnOrder(order);
    }

    public void computePlayerStats() {
        List<PlayerInfo> playerInfos = new ArrayList<>();
        PlayerInfo playerInfo;
        for (Player player: gamePlayers.values()) {
            playerInfo = new PlayerInfo();
            playerInfo.setUsername(player.getUsername());
            playerInfo.setColor(player.getColor());
            playerInfo.setNumDestCards(player.getDestinationCardHand().getCards().size());
            playerInfo.setNumTrainCards(player.getTrainCarCardHand().getCards().size());
            playerInfo.setNumTrains(player.getNumTrains());
            playerInfo.setScore(player.getScore());
            playerInfos.add(playerInfo);
        }
        playerStats =  playerInfos;
    }

    public boolean claimRoute(String username, int id, TrainCarCardType colorIfGray) {
        Player player = gamePlayers.get(username);
        Route route = routes.get(id);
        if (player == null) {
            return false;
        }
        if (!route.getOwner().equals("")) {
            return false;
        }
        int numTracks = route.getNumTracks();
        if (player.getNumTrains() < numTracks) {
            return false;
        }
        RouteColor color = route.getColor();
        String stringColor = color.toString();
        if (stringColor.equals("GRAY")) {
            if (colorIfGray == null) {
                System.out.println("ERROR!! User did not specify a color");
                return false;
            }
            stringColor = colorIfGray.toString();
        }
        TrainCarCardType type = TrainCarCardType.valueOf(stringColor);
        if (player.getTrainCarCardHand().getCount(type) >= numTracks) {
            player.getTrainCarCardHand().removeCards(type, numTracks);
            gameTrainDeck.addToDiscardPile(type, numTracks);
            player.setNumTrains(player.getNumTrains() - numTracks);
        }
        else if (player.getTrainCarCardHand().getCount(type) + player.getTrainCarCardHand().getCount(TrainCarCardType.LOCOMOTIVE) >= numTracks) {
            int numOfActualColor = player.getTrainCarCardHand().getCount(type);
            int numOfRainbows = numTracks - numOfActualColor;
            player.getTrainCarCardHand().removeCards(type, numOfActualColor);
            player.getTrainCarCardHand().removeCards(TrainCarCardType.LOCOMOTIVE, numOfRainbows);
            gameTrainDeck.addToDiscardPile(type, numOfActualColor);
            gameTrainDeck.addToDiscardPile(TrainCarCardType.LOCOMOTIVE, numOfRainbows);
            player.setNumTrains(player.getNumTrains() - numTracks);
        }
        else {
            return false;
        }
        player.addRouteOwned(route);
        return true;
    }

    public boolean needToReplaceAll() {
        int counter = 0;
        for (TrainCarCard c : faceUpTrainCarCards) {
            if (c.getType() == TrainCarCardType.LOCOMOTIVE) {
                counter += 1;
            }
        }

        if (counter > 2) {
            return true;
        }


        return false;
    }

    public List<String> getBonusPlayers() {
        //TODO: Possibly make this longest path instead of most claimed routes
        List<String> bonusPlayers = new ArrayList<>();
        int biggestSoFar = 0;
        for (Player player: getGamePlayers().values()) {
            if (player.getRoutesOwned().size() > biggestSoFar) {
                biggestSoFar = player.getRoutesOwned().size();
            }
        }
        for (Player player: getGamePlayers().values()) {
            if (player.getRoutesOwned().size() == biggestSoFar) {
                bonusPlayers.add(player.getUsername());
                player.setScore(player.getScore() + 10);
            }
        }
        return bonusPlayers;
    }


    public int getTrainDeckSize(){
        return gameTrainDeck.getDrawPile().size();
    }

    public String getGameName() {
        return gameInfo.getGameName();
    }

    public void setGameName(String gameName) {
        gameInfo.setGameName(gameName);
    }

    public int getNumPlayers() {
        return gameInfo.getNumPlayers();
    }

    public void setNumPlayers(int numPlayers) {
        gameInfo.setNumPlayers(numPlayers);
    }

    public Map<String, Player> getGamePlayers() {
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

    public void setEventHistory(List<Event> eventHistory) {
        this.eventHistory = eventHistory;
    }

    public List<Event> getEventHistory() {
        return eventHistory;
    }

    public List<String> getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(List<String> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public List<PlayerInfo> getPlayerStats() {
        return playerStats;
    }

    public int getNumDestinationCardChoicesReceived() {
        return numDestinationCardChoicesReceived;
    }

    public void setNumDestinationCardChoicesReceived(int numDestinationCardChoicesReceived) {
        this.numDestinationCardChoicesReceived = numDestinationCardChoicesReceived;
    }

    public DestinationCardDeck getGameDestinationDeck() {
        return gameDestinationDeck;
    }

    public void setGameDestinationDeck(DestinationCardDeck gameDestinationDeck) {
        this.gameDestinationDeck = gameDestinationDeck;
    }

    public TrainCarCardDeck getGameTrainDeck() {
        return gameTrainDeck;
    }

    public void setGameTrainDeck(TrainCarCardDeck gameTrainDeck) {
        this.gameTrainDeck = gameTrainDeck;
    }

    public Route getRoute(int id) {
        return routes.get(id);
    }

    public String getLastPlayerToTakeTurn() {
        return lastPlayerToTakeTurn;
    }

    public void setLastPlayerToTakeTurn(String lastPlayerToTakeTurn) {
        this.lastPlayerToTakeTurn = lastPlayerToTakeTurn;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }
}
