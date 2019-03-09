package Model;

import Command.ClientCommand.*;
import Result.AssignDestinationCardsResult;
import Result.DrawDestinationCardsResult;
import Result.GetCommandsResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by jbasden on 1/30/19.
 */

public class Model {
    private HashMap<String, Game> games = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();

    private static final Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }

    private Model() {
        initializeInfo();
    }

    private void initializeInfo() {
        createUser("d", "d");
        createUser("d2", "d");
        createUser("d3", "d");
        createUser("d4", "d");
        createUser("d5", "d");
        createUser("d6", "d");
        createUser("d7", "d");
        createUser("d8", "d");
        createUser("d9", "d");
        createUser("d10", "d");
        createUser("b", "b");
        createUser("b2", "b");
        createUser("b3", "b");
        createUser("b4", "b");
        createUser("b5", "b");
        createUser("b6", "b");
        createUser("b7", "b");
        createUser("b8", "b");
        createUser("b9", "b");
        createUser("b10", "b");
        createUser("z","z");
        createUser("z2","z");
        createUser("z3","z");
        createUser("z4","z");
        createUser("z5","z");
        createUser("z6","z");
        createUser("z7","z");
        createUser("z8","z");
        createUser("z9","z");
        createUser("z10","z");
        createUser("j", "j");
        createUser("j2", "j");
        createUser("j3", "j");
        createUser("j4", "j");
        createUser("j5", "j");
        createUser("j6", "j");
        createUser("j7", "j");
        createUser("j8", "j");
        createUser("j9", "j");
        createUser("j10", "j");
        createGame("game with two players", 2);
        createGame("game with three players", 3);
        createGame("game with four players", 4);
        createGame("game with five players", 5);
        createGame("Another game with two players, just in case it's needed", 2);
    }

    public boolean createGame(String gameName, int numPlayers) {
        if (games.containsKey(gameName)) {
            return false;
        }
        if (numPlayers < 2 || numPlayers > 5) {
            return false;
        }
        if (gameName.length() == 0) {
            return false;
        }
        Game game = new Game();
        game.setGameName(gameName);
        game.setNumPlayers(numPlayers);
        game.setStarted(false);
        games.put(gameName, game);
        CommandData commandData = new CommandData();
        commandData.setType(ClientCommandType.C_CREATE_GAME);
        AddGameCommand addGameCommand = new AddGameCommand();
        GameInfo gameInfo = new GameInfo();
        gameInfo.setNumPlayers(numPlayers);
        gameInfo.setGameName(gameName);
        addGameCommand.setGameInfo(gameInfo);

        commandData.setData(new Gson().toJson(addGameCommand));
        for (User user: users.values()) {
            user.addCommand(commandData);
        }
        return true;
    }

    public boolean authenticateUser(String userName, String password) {
        if (!users.containsKey(userName)) {
            return false;
        }
        return users.get(userName).getPassword().equals(password);
    }

    public boolean createUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        if (username.length() == 0 || password.length() == 0) {
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        users.put(username, user);
        return true;
    }

    public DrawDestinationCardsResult drawDestinationCards(String username) {
        Game game = getAssociatedGame(username);
        List<DestinationCard> destinationCardsToChooseFrom = new ArrayList<>();
        List<DestinationCard> groupOfDestinationCardsSentOut = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            DestinationCard card = (DestinationCard)game.getBoard().getDestinationDeck().draw();
            groupOfDestinationCardsSentOut.add(card);
            destinationCardsToChooseFrom.add(card);
        }
        game.addGroupOfDestinationCardIdsSentOut(groupOfDestinationCardsSentOut);
        DrawDestinationCardsResult drawDestinationCardsResult = new DrawDestinationCardsResult();
        drawDestinationCardsResult.setDestinationCards(destinationCardsToChooseFrom);
        drawDestinationCardsResult.setSuccess(true);
        return drawDestinationCardsResult;
    }

    public void beginGame(String gameName) {
        Game game = games.get(gameName);
        game.readInCardLists();
        game.determineOrder();
        game.computePlayerStats();
        List<String> userNamesOfPlayers = new ArrayList<>();
        for (Player player : game.getGamePlayers().values()) {
            userNamesOfPlayers.add(player.getUsername());
        }
        CommandData commandData = new CommandData();
        commandData.setType(ClientCommandType.C_BEGIN_PLAY);
        BeginGameCommand beginGameCommand = new BeginGameCommand();
        beginGameCommand.setGameName(gameName);
        beginGameCommand.setGame(game);
        commandData.setData(new Gson().toJson(beginGameCommand));
        for (String user : userNamesOfPlayers) {
            users.get(user).addCommand(commandData);
        }
        game.setStarted(true);
        commandData = new CommandData();
        commandData.setType(ClientCommandType.C_REMOVE_GAME);
        RemoveGameCommand removeGameCommand = new RemoveGameCommand();
        removeGameCommand.setGameName(gameName);
        commandData.setData(new Gson().toJson(removeGameCommand));
        for (User user: users.values()) {
            if (!userNamesOfPlayers.contains(user.getUsername())) {
                user.addCommand(commandData);
            }
        }
    }

    public boolean joinGame(String userName, String gameName) {
        Game game = games.get(gameName);
        if (game == null) {
            return false;
        }
        if (game.addPlayer(userName)) {
            if (game.getNumPlayers() == game.getGamePlayers().size()) {
                beginGame(gameName);
            }
            return true;
        }
        return false;
    }

    public PlayerColor getPlayerColor(String gameName, String userName) {
        Game game = games.get(gameName);
        if (game == null) {
            return PlayerColor.FAKE_COLOR;
        }
        Player player = game.getGamePlayers().get(userName);
        if (player == null) {
            return PlayerColor.FAKE_COLOR;
        }
        return player.getColor();
    }

    public GetCommandsResult getCommands(String userName) {
        GetCommandsResult res = new GetCommandsResult();
        User user = users.get(userName);
        res.setUsername(userName);
        if (user == null) {
            res.setErrorMessage("User does not exist");
            res.setSuccess(false);
            return res;
        }
        res.setCommandList(new ArrayList<>(user.getCommands()));
        res.setSuccess(true);
        user.clearCommands();
        return res;
    }

    public void addAllAddableGamesToCommandLists(String userName) {
        users.get(userName).clearCommands();
        CommandData commandData;
        for (Game gameToCheck: games.values()) {
            if (!gameToCheck.isStarted()) {
                commandData = new CommandData();
                commandData.setType(ClientCommandType.C_CREATE_GAME);
                AddGameCommand addGameCommand = new AddGameCommand();
                GameInfo gameInfo = new GameInfo();
                gameInfo.setGameName(gameToCheck.getGameName());
                gameInfo.setNumPlayers(gameToCheck.getNumPlayers());
                addGameCommand.setGameInfo(gameInfo);
                commandData.setData(new Gson().toJson(addGameCommand));
                users.get(userName).addCommand(commandData);
            }
        }
    }

    private void sendDealTrainCardsCommands(Game game) {
        Board board = game.getBoard();
        for (String username: game.getGamePlayers().keySet()) {
            CommandData commandData = new CommandData();
            DealTrainCarCardsCommand dealTrainCarCardsCommand = new DealTrainCarCardsCommand();
            List<TrainCarCard> cards = new ArrayList<>();
            TrainCarCard card;
            for (int i = 0; i < 4; ++i) {
                card = (TrainCarCard)board.getTrainDeck().draw();
                cards.add(card);
            }
            TrainCarCardHand trainCarCardHand = new TrainCarCardHand();
            trainCarCardHand.setCards(cards);
            dealTrainCarCardsCommand.setHand(trainCarCardHand);
            commandData.setType(ClientCommandType.C_FIRST_HAND);
            commandData.setData(new Gson().toJson(dealTrainCarCardsCommand));
            users.get(username).addCommand(commandData);
        }
    }

    private Game getAssociatedGame(String playerUsername) {
        for (Game gameToCheck: games.values()) {
            for (Player playerToCheck: gameToCheck.getGamePlayers().values()) {
                if (playerToCheck.getUsername().equals(playerUsername)) {
                    return gameToCheck;
                }
            }
        }
        return new Game();
    }

    private Player getAssociatedPlayer(String playerUsername) {
        for (Game gameToCheck: games.values()) {
            for (Player playerToCheck: gameToCheck.getGamePlayers().values()) {
                if (playerToCheck.getUsername().equals(playerUsername)) {
                    return playerToCheck;
                }
            }
        }
        return new Player();
    }

    public AssignDestinationCardsResult assignDestinationCards(String playerUsername, List<Integer> cards) {
        AssignDestinationCardsResult res = new AssignDestinationCardsResult();
        Game game = getAssociatedGame(playerUsername);
        game.figureOutWhichDestinationCardShouldGoBackInTheDeck(cards);
        if (game.getGamePlayers().size() == 0) {
            res.setErrorMessage("No Game was found with that player");
            res.setSuccess(false);
            return res;
        }
        Player player = getAssociatedPlayer(playerUsername);
        if (!player.getUsername().equals(playerUsername)) {
            res.setErrorMessage("No game was found with that player");
            res.setSuccess(false);
            return res;
        }
        List<DestinationCard> cardsAdded = new ArrayList<>();
        DestinationCard cardToAdd = new DestinationCard();
        for (Integer cardId : cards) {
            cardToAdd = player.getDestinationCardHand().addCardById(cardId);
            cardsAdded.add(cardToAdd);
        }
        res.setSuccess(true);
        res.setDestinationCards(cardsAdded);
        int numReceivedSoFar = game.getNumDestinationCardChoicesReceived() + 1;
        game.setNumDestinationCardChoicesReceived(numReceivedSoFar);
        if (numReceivedSoFar == game.getNumPlayers()) {
            sendDealTrainCardsCommands(game);
        }
        return res;
    }

    public boolean sendMessage(Event data) {
        Game game = getAssociatedGame(data.getUsername());
        if (game.getGamePlayers().size() == 0) {
            return false;
        }
        AddEventCommand addEventCommand = new AddEventCommand();
        if (data.getType().toString().equals("MESSAGE")) {
            data.setContent(" " + data.getUsername() + ": " + data.getContent());
        }
        addEventCommand.setEvent(data);
        CommandData commandData = new CommandData();
        commandData.setType(ClientCommandType.C_EVENT);
        commandData.setData(new Gson().toJson(addEventCommand));
        game.addEvent(data);
        User user;
        for (String username: game.getGamePlayers().keySet()) {
            user = users.get(username);
            user.addCommand(commandData);
        }
        return true;
    }

    public HashMap<String, Game> getGames() {
        return games;
    }

    public void setGames(HashMap<String, Game> games) {
        this.games = games;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }
}
