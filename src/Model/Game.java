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
 *      board: Board, the game board and all of its components
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

import Result.DrawFaceUpResult;
import Result.DrawTrainCarCardResult;

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
    private Board board = new Board();
    private List<Event> eventHistory = new ArrayList<>();
    private List<String> turnOrder = new ArrayList<>();
    // TODO add trainCarCardDeck
    private List<PlayerColor> playerColors = new ArrayList<PlayerColor>(Arrays.asList(PlayerColor.BLUE,
            PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.BLACK));
    private int numDestinationCardChoicesReceived = 0;

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

    private PlayerInfo findPlayerInfo(String user){
        PlayerInfo playerInfo = null;
        for (PlayerInfo p: playerStats){
            if (p.getUsername().equals(user)){
                playerInfo = p;
                break;
            }
        }
        return playerInfo;
    }

    public ArrayList<iCard> drawFaceUp(int ind, String user){
        Player player = gamePlayers.get(user);
        PlayerInfo playerInfo = findPlayerInfo(user);

        //first index is the chosen face up card
        //second index is drawpile card that replaces it
        // TODO
        ArrayList<iCard> cards = trainCarCardDeck.drawFaceUp(ind);

        player.addTrainCard(cards.get(0));
        playerInfo.incrementNumTrainCards(1);

        return cards;

    }

    public DrawTrainCarCardResult drawTopCard(String user){
        DrawTrainCarCardResult result = new DrawTrainCarCardResult();
        Player player = gamePlayers.get(user);
        PlayerInfo pInfo = findPlayerInfo(user);

        try{
            //Todo need to add trainCarCardDeck
            iCard card = trainCarCardDeck.drawTopCard();
            player.addTrainCard(card);
            pInfo.incrementNumTrainCards(1);

            result.setCard(card);
            result.setSuccess(true);

            return result;
        }catch (Exception e){
            result.setErrorMessage("Could not draw Train Car Card");
            result.setSuccess(false);
            return result;
        }

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

    public void initializeCardLists() {
        board.setDestinationDeck(model.getDestinationDeckCopy());
        board.setTrainDeck(model.getTrainCarCardDeckCopy());
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
        return true;
        //TODO: Implement
    }

    /**
     * Getters and setters defined below
     * I assume these are self-explanatory enough, and do not need to be commented.
     */
    //TODO put train card deck in Game
    public int getTrainDeckSize(){
        return trainCarCardDeck.size();
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
//    public Player getPlayer(String player){
//        return gamePlayers.get(player);
//    }

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

}
