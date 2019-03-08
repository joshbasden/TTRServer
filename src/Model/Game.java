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
    private Map<String, Player> gamePlayers = new HashMap<>();
    private List<PlayerInfo> playerStats = new ArrayList<>();
    private GameInfo gameInfo = new GameInfo();
    private boolean started;
    private Board board = new Board();
    private List<Event> eventHistory = new ArrayList<>();
    private List<String> turnOrder = new ArrayList<>();
    private List<PlayerColor> playerColors = new ArrayList<PlayerColor>(Arrays.asList(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.BLACK));
    private int numDestinationCardChoicesReceived;

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

    public void addEvent(Event event) {
        eventHistory.add(event);
    }

    public List<Event> getEventHistory() {
        return eventHistory;
    }

    public void readInCardLists() {
        try {
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/DestinationCardsWithIds.json")));
            JsonObject obj = gson.fromJson(jsonString, JsonObject.class);
            JsonArray cards = (JsonArray)obj.get("cards");
            iDeck destinationDeck = new DestinationCardDeck();
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
            ((DestinationCardDeck) destinationDeck).setCards(destinationCards);
            board.setDestinationDeck(destinationDeck);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/TrainCarCards.json")));
            JsonObject obj = gson.fromJson(jsonString, JsonObject.class);
            JsonArray cards = (JsonArray)obj.get("cards");
            iDeck trainCarCardDeck = new TrainCarCardDeck();
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
            ((TrainCarCardDeck) trainCarCardDeck).setDrawPile(trainCarCards);
            board.setTrainDeck(trainCarCardDeck);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public List<iCard> shuffle(List<iCard>()) {
//        List<iCard> temp = new ArrayList<iCard>();
//        return temp;
//        //TODO:Implement
//    }

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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setEventHistory(List<Event> eventHistory) {
        this.eventHistory = eventHistory;
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

    public static void main(String[] args) {
        new Game().readInCardLists();
    }
}
