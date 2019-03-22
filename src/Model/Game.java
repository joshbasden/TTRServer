/**
 * The Game class is used to store and modify
 * the attributes of a single Ticket To Ride game.
 *
 * Operations are provided for adding a new player,
 * adding an event to the event history (chat or turn),
 * parsing json files for the cards that will be used in the game,
 * determining an order of play for the game players,
 * computing the statistics for each player in the game,
 * as well as getters and setters for the class attributes.
 *
 * Domain:
 *      gamePlayers: Map<String, Player>, the players in the game (username to Player object)
 *      playerStats: List<PlayerInfo>, statistics for each player
 *      gameInfo: GameInfo, class defining the name of the game as well as number of expected players
 *      started: Boolean, used to determine if the game has enough players or is still in the waiting lobby
 *      eventHistory: List<Event>, the list of chats that have been sent, and turns that have been taken
 *      turnOrder: List<String> the usernames of the players defining how the game should progress
 *      playerColors: List<PlayerColor> Predefined constants for the colors of the (up to) 5 players
 *      numDestinationChoicesReceived: int number of players who have selected their initial destination tickets
 *
 * @invariant eventHistory.size() >= 0
 * @invariant playerColors = ["BLUE", "GREEN", "RED", "YELLOW", "BLACK"] (using an enumeration)
 * @invariant 0 <= numDestinationCardChoicesReceived <= gamePlayers.size()
 *
 */

package Model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by jbasden on 1/29/19.
 */

public class Game {
    Model model = Model.getInstance();
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

    /**
     * Adds a player to the gamePlayers map (for this phase, only the username was needed)
     *
     * @param username The username of the player being added
     * @return true if the player was added successfully, false otherwise (game was full, or username was already taken)
     *
     * @pre username != ""
     * @pre gamePlayers.size < gameInfo.getSize()
     * @pre gamePlayers.containsKey(username) == false
     * @pre started == false
     *
     * @post gamePlayers.size() == old(gamePlayers.size()) + 1
     * @post gamePlayers[gamePlayers.size() - 1].getUsername() == username
     * @post gameInfo == old(gameInfo)
     * @post started == false
     * @post board == old(board)
     * @post eventHistory == old(eventHistory)
     * @post turnOrder == old(turnOrder)
     * @post numDestinationCardChoicesReceived = 0
     *
     */
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

    public String getNextTurn(String curPlayer){
        int size = turnOrder.size();
        int nextInd = turnOrder.indexOf(curPlayer) + 1;
        int nextPlayer = nextInd % size;
        return turnOrder.get(nextPlayer);
    }

    public boolean isLastTurn(String curPlayer){
        Player player = gamePlayers.get(curPlayer);
        int numTrains = player.getNumTrains();
        return numTrains <= 2;
    }


    /**
     * Adds an event to the game's eventHistory list
     *
     * @param event The event to add to the list
     *
     * @pre event.getType() == (TURN || MESSAGE)
     * @pre event.getContent() != ""
     * @pre gamePlayers.containsKey(event.getUsername()) == true
     *
     * @post gamePlayers == old(gamePlayers)
     * @post playerStats == old(playerStats)
     * @post gameInfo == old(gameInfo)
     * @post started == old(started)
     * @post board == old(board)
     * @post eventHistory.size() == old(eventHistory.size()) + 1
     * @post eventHistory[eventHistory.size - 1] == event
     * @post turnOrder == old(turnOrder)
     * @post numDestinationCardChoicesReceived == old(numDestinationCardChoicesReceieved)
     *
     */
    public void addEvent(Event event) {
        eventHistory.add(event);
    }

    public void readInCardLists() {
        Gson gson;
        //Destination Cards
        try {
            gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/DestinationCards.json")));
            JsonObject obj = gson.fromJson(jsonString, JsonObject.class);
            JsonArray cards = (JsonArray)obj.get("cards");
            List<iCard> destinationCards = new ArrayList<>();
            for (int i = 0; i < cards.size(); ++i) {
                DestinationCard card = new DestinationCard();
                JsonObject jsonCard = (JsonObject)cards.get(i);
                card.setId(i);
                card.setCity1(Integer.parseInt(jsonCard.get("city1").toString()));
                card.setCity2(Integer.parseInt(jsonCard.get("city2").toString()));
                card.setPoints(Integer.parseInt(jsonCard.get("points").toString()));
                destinationCards.add(card);
            }
            //destinationDeck.setCards(destinationCards); TODO: FIX
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //TrainCards
        try {
            gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/TrainCarCards.json")));
            JsonObject obj = gson.fromJson(jsonString, JsonObject.class);
            JsonArray cards = (JsonArray)obj.get("cards");
            List<iCard> trainCarCards = new ArrayList<>();
            for (int i = 0; i < cards.size(); ++i) {
                TrainCarCard card = new TrainCarCard();
                JsonObject jsonCard = (JsonObject)cards.get(i);
                String type = jsonCard.get("type").toString();
                type = type.substring(1,type.length() - 1);
                TrainCarCardType enumType = TrainCarCardType.valueOf(type);
                card.setType(enumType);
                trainCarCards.add(card);
            }
            //trainCarCardDeck.setDrawPile(trainCarCards); TODO: FIX
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //Cities
        try {
            gson = new Gson();
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
        try {
            gson = new Gson();
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
                route.setColor(RouteColor.valueOf(color.substring(1, color.length() - 1)));
                routes.put(i, route);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines an order of play for the players involved in the game
     *
     * @pre gamePlayers != null
     * @pre 2 <= gamePlayers.size() <= 5
     * @pre started == true
     *
     * @post gamePlayers == old(gamePlayers)
     * @post playerStats == old(playerStats)
     * @post gameInfo == old(gameInfo)
     * @post started == old(started)
     * @post board == old(board)
     * @post eventHistory == old(eventHistory)
     * @post turnOrder.size() == gamePlayers.size()
     * @post turnOrder.size() == gameInfo.getNumGamePlayers()
     * @post (set)turnOrder == (set)gamePlayers.keySet()
     * @post numDestinationCardChoicesReceived == old(numDestinationCardChoicesReceieved)
     */
    public void determineOrder() {
        List<String> order = new ArrayList<>();
        for (Player player: gamePlayers.values()) {
            order.add(player.getUsername());
        }
        setTurnOrder(order);
    }

    /**
     * Computes the statistics for each player in the game and stores this data
     *
     * @pre gamePlayers != null
     *
     * @post gamePlayers == old(gamePlayers)
     * @post playerStats.size() == gamePlayers.size()
     * @post gameInfo == old(gameInfo)
     * @post started == old(started)
     * @post board == old(board)
     * @post eventHistory == old(eventHistory)
     * @post turnOrder == old(turnOrder)
     * @post numDestinationCardChoicesReceived == old(numDestinationCardChoicesReceieved)
     */
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

    public boolean claimRoute(String username, int id) {
        Player player = gamePlayers.get(username);
        Route route = routes.get(id);
        if (player == null) {
            return false;
        }
        if (!route.getOwner().equals("")) {
            return false;
        }
        int numTracks = route.getNumTracks();
        RouteColor color = route.getColor();
        if (player.getTrainCarCardHand().getCount(color) < numTracks) {
            return false;
        }
        if (player.getNumTrains() < numTracks) {
            return false;
        }
        player.getTrainCarCardHand().removeCards(color, numTracks);
        gameTrainDeck.addToDiscardPile(color, numTracks);
        player.setNumTrains(player.getNumTrains() - numTracks);
        return true;
    }

    /**
     * Getters and setters defined below
     * I assume these are self-explanatory enough, and do not need to be commented.
     */

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

    public static void main(String[] args) {
        new Game().readInCardLists();
    }

}
