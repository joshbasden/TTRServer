package Model;

import Command.ClientCommand.*;
import Request.AssignDestinationCardsRequest;
import Request.AssignFirstDestinationCardsRequest;
import Request.ClaimGrayRequest;
import Request.ClaimRouteRequest;
import Result.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


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
        //createGame("game with two players", 2);
        //createGame("game with three players", 3);
        //createGame("game with four players", 4);
        //createGame("game with five players", 5);
        //createGame("Another game with two players, just in case it's needed", 2);
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
        for (int i = 0; i < 3; ++i) {
            DestinationCard card = (DestinationCard)game.getGameDestinationDeck().draw();
            destinationCardsToChooseFrom.add(card);
        }
        DrawDestinationCardsResult drawDestinationCardsResult = new DrawDestinationCardsResult();
        drawDestinationCardsResult.setDestinationCards(destinationCardsToChooseFrom);
        drawDestinationCardsResult.setSuccess(true);
        return drawDestinationCardsResult;
    }

    public void beginGame(String gameName) {
        Game game = games.get(gameName);
        game.readInJsonFiles();
        game.chooseInitialFaceUps();
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

    public void endGame(Game game) {
        //TODO: Implement
    }

    public EndTurnResult endTurn(String endTurnPlayer) {
        EndTurnResult endTurnResult = new EndTurnResult();
        Game game = getAssociatedGame(endTurnPlayer);
        String nextPlayer = game.getNextTurn(endTurnPlayer);
        AdvanceTurnCommand advanceTurnCommand = new AdvanceTurnCommand();
        advanceTurnCommand.setUsername(nextPlayer);
        CommandData advanceCommandData = new CommandData();
        advanceCommandData.setType(ClientCommandType.C_ADVANCE_TURN);
        if (!game.isStarted()) {
            endTurnResult.setSuccess(false);
            endTurnResult.setErrorMessage("Player was not found.");
            return endTurnResult;
        }
        if (game.isLastRound()) {
            if (game.getLastPlayerToTakeTurn().equals(endTurnPlayer)) {
                endTurnResult.setSuccess(true);
                endGame(game);
                return endTurnResult;
            }
            advanceTurnCommand.setLastTurn(true);
            advanceCommandData.setData(new Gson().toJson(advanceTurnCommand));
            addCommandToAllPlayers(game, advanceCommandData);
            endTurnResult.setSuccess(true);
            return endTurnResult;
        }
        advanceTurnCommand.setLastTurn(false);
        advanceCommandData.setData(new Gson().toJson(advanceTurnCommand));
        addCommandToAllPlayers(game, advanceCommandData);
        endTurnResult.setSuccess(true);
        return endTurnResult;
    }

    public DrawFaceUpResult drawFaceUp(String player, int index){
        DrawFaceUpResult result = new DrawFaceUpResult();
        Game game = getAssociatedGame(player);
        ArrayList<iCard> cards = game.drawFaceUp(index, player);

        //make Client Commands
        UpdatePlayerStatsCommand updateStatsCommand = new UpdatePlayerStatsCommand();
        ArrayList<StatsChange> statsChangeArray = new ArrayList<>();
        StatsChange statsChange = new StatsChange();
        statsChange.setAmount(1);
        statsChange.setType(StatsChangeType.ADD_TRAIN_CAR_CARDS);
        statsChangeArray.add(statsChange);

        AccountForTrainCarCardDraw accountForDraws = new AccountForTrainCarCardDraw();
        accountForDraws.setDeckSize(game.getTrainDeckSize());

        //check if I need to replace all train cards
        CommandData commandDataReplace = new CommandData();
        if (cards.size() == 5){
            ReplaceAllFaceUpCommand replaceAllCommand = new ReplaceAllFaceUpCommand();
            replaceAllCommand.setFaceUpCards(cards);
            commandDataReplace.setType(ClientCommandType.C_REPLACE_ALL_FACE_UP);
            commandDataReplace.setData(new Gson().toJson(replaceAllCommand));
        }
        else{
            ReplaceOneFaceUpCommand replaceCommand = new ReplaceOneFaceUpCommand();
            replaceCommand.setCard(cards.get(0));
            replaceCommand.setIndex(index);

            commandDataReplace.setType(ClientCommandType.C_REPLACE_ONE_FACE_UP);
            commandDataReplace.setData(new Gson().toJson(replaceCommand));
        }

        updateStatsCommand.setUsername(player);
        updateStatsCommand.setChanges(statsChangeArray);

        CommandData commandDataUpdate = new CommandData();
        CommandData commandDataAccount = new CommandData();
        commandDataAccount.setType(ClientCommandType.C_ACCOUNT_FOR_THE_FACT_THAT_SOMEONE_DREW_FROM_THE_TRAIN_CAR_CARD_DRAW_PILE);
        commandDataAccount.setData(new Gson().toJson(accountForDraws));
        commandDataUpdate.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
        commandDataUpdate.setData(new Gson().toJson(updateStatsCommand));

        try{
            addCommandToAllPlayers(game, commandDataUpdate);
            addCommandToAllPlayers(game, commandDataAccount);
            addCommandToAllPlayers(game, commandDataReplace);
            result.setSuccess(true);
            result.setCard(cards.get(1));
            return result;
        }
        catch (Exception e){
            result.setErrorMessage("Could not send Update Player Stats Command to everyone");
            result.setSuccess(false);
            return result;
        }
    }

    public DrawTrainCarCardResult takeTopTrainCarCard(String player){
        Game game = getAssociatedGame(player);
        DrawTrainCarCardResult result = game.drawTopCard(player);

        //make Client Commands
        UpdatePlayerStatsCommand updateStatsCommand = new UpdatePlayerStatsCommand();
        ArrayList<StatsChange> statsChangeArray = new ArrayList<>();
        StatsChange statsChange = new StatsChange();
        statsChange.setAmount(1);
        statsChange.setType(StatsChangeType.ADD_TRAIN_CAR_CARDS);
        statsChangeArray.add(statsChange);

        AccountForTrainCarCardDraw accountForDraws = new AccountForTrainCarCardDraw();
        accountForDraws.setDeckSize(game.getTrainDeckSize());

        updateStatsCommand.setUsername(player);
        updateStatsCommand.setChanges(statsChangeArray);

        CommandData commandDataUpdate = new CommandData();
        CommandData commandDataAccount = new CommandData();
        commandDataAccount.setType(ClientCommandType.C_ACCOUNT_FOR_THE_FACT_THAT_SOMEONE_DREW_FROM_THE_TRAIN_CAR_CARD_DRAW_PILE);
        commandDataAccount.setData(new Gson().toJson(accountForDraws));
        commandDataUpdate.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
        commandDataUpdate.setData(new Gson().toJson(updateStatsCommand));

        try {
            addCommandToAllPlayers(game, commandDataUpdate);
            addCommandToAllPlayers(game, commandDataAccount);
            result.setSuccess(true);
            return result;
        }
        catch (Exception e){
            result.setErrorMessage("Could not send Update Player Stats Command to everyone");
            result.setSuccess(false);
            return result;
        }

    }

    public void addCommandToAllPlayers(Game game, CommandData command){
        for(Player p: game.getGamePlayers().values()){
            User user = users.get(p.getUsername());
            user.addCommand(command);
        }
    }

    private void sendDealTrainCardsCommands(Game game) {

        for (String username: game.getGamePlayers().keySet()) {
            UpdatePlayerStatsCommand updatePlayerStatsCommand = new UpdatePlayerStatsCommand();
            updatePlayerStatsCommand.setUsername(username);
            List<StatsChange> changes = new ArrayList<>();
            StatsChange change = new StatsChange();
            change.setType(StatsChangeType.ADD_TRAIN_CAR_CARDS);
            change.setAmount(4);
            changes.add(change);
            updatePlayerStatsCommand.setChanges(changes);
            CommandData updateStatsCommandData = new CommandData();
            updateStatsCommandData.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
            updateStatsCommandData.setData(new Gson().toJson(updatePlayerStatsCommand));
            addCommandToAllPlayers(game, updateStatsCommandData);
            CommandData dealTrainCarCardsCommandData = new CommandData();
            DealTrainCarCardsCommand dealTrainCarCardsCommand = new DealTrainCarCardsCommand();
            List<TrainCarCard> cards = new ArrayList<>();
            TrainCarCard card;
            for (int i = 0; i < 4; ++i) {
                card = (TrainCarCard)game.getGameTrainDeck().draw();
                cards.add(card);
            }
            TrainCarCardHand trainCarCardHand = new TrainCarCardHand();
            trainCarCardHand.setCards(cards);
            dealTrainCarCardsCommand.setHand(trainCarCardHand);
            dealTrainCarCardsCommandData.setType(ClientCommandType.C_FIRST_HAND);
            dealTrainCarCardsCommandData.setData(new Gson().toJson(dealTrainCarCardsCommand));
            users.get(username).addCommand(dealTrainCarCardsCommandData);
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

    public AssignDestinationCardsResult assignDestinationCards(AssignDestinationCardsRequest req) {
        AssignDestinationCardsResult res = new AssignDestinationCardsResult();
        String playerUsername = req.getPlayer();
        Game game = getAssociatedGame(playerUsername);
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
        DestinationCard cardToAdd;
        for (Integer cardId : req.getChosen()) {
            cardToAdd = game.getGameDestinationDeck().getCardById(cardId);
            player.getDestinationCardHand().addCard(cardToAdd);
            cardsAdded.add(cardToAdd);
        }
        for (Integer cardId: req.getNotChosen()) {
            cardToAdd = game.getGameDestinationDeck().getCardById(cardId);
            game.getGameDestinationDeck().addCard(cardToAdd);
        }
        UpdatePlayerStatsCommand updatePlayerStatsCommand = new UpdatePlayerStatsCommand();
        updatePlayerStatsCommand.setUsername(playerUsername);
        StatsChange change = new StatsChange();
        change.setType(StatsChangeType.ADD_DESTINATION_CARDS);
        change.setAmount(req.getChosen().size());
        List<StatsChange> changes = new ArrayList<>();
        changes.add(change);
        updatePlayerStatsCommand.setChanges(changes);
        CommandData updateStatsCommandData = new CommandData();
        updateStatsCommandData.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
        updateStatsCommandData.setData(new Gson().toJson(updatePlayerStatsCommand));
        addCommandToAllPlayers(game, updateStatsCommandData);
        res.setSuccess(true);
        res.setCards(cardsAdded);
        return res;
    }

    public AssignFirstDestinationCardsResult assignFirstDestinationCards(AssignFirstDestinationCardsRequest req) {
        AssignFirstDestinationCardsResult res = new AssignFirstDestinationCardsResult();
        String playerUsername = req.getPlayer();
        Game game = getAssociatedGame(playerUsername);
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
        DestinationCard cardToAdd;
        for (Integer cardId : req.getChosen()) {
            cardToAdd = game.getGameDestinationDeck().getCardById(cardId);
            cardsAdded.add(cardToAdd);
        }
        for (Integer cardId: req.getNotChosen()) {
            cardToAdd = game.getGameDestinationDeck().getCardById(cardId);
            game.getGameDestinationDeck().addCard(cardToAdd);
        }
        res.setSuccess(true);
        DestinationCardHand hand = new DestinationCardHand();
        hand.setCards(cardsAdded);
        res.setHand(hand);
        player.setDestinationCardHand(hand);
        int numReceivedSoFar = game.getNumDestinationCardChoicesReceived() + 1;
        game.setNumDestinationCardChoicesReceived(numReceivedSoFar);
        if (numReceivedSoFar == game.getNumPlayers()) {
            sendDealTrainCardsCommands(game);
        }
        UpdatePlayerStatsCommand updatePlayerStatsCommand = new UpdatePlayerStatsCommand();
        updatePlayerStatsCommand.setUsername(playerUsername);
        StatsChange change = new StatsChange();
        change.setType(StatsChangeType.ADD_DESTINATION_CARDS);
        change.setAmount(req.getChosen().size());
        List<StatsChange> changes = new ArrayList<>();
        changes.add(change);
        updatePlayerStatsCommand.setChanges(changes);
        CommandData updateStatsCommandData = new CommandData();
        updateStatsCommandData.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
        updateStatsCommandData.setData(new Gson().toJson(updatePlayerStatsCommand));
        addCommandToAllPlayers(game, updateStatsCommandData);
        return res;
    }

    public boolean sendMessage(Event data) {
        Game game = getAssociatedGame(data.getUsername());
        if (game.getGamePlayers().size() == 0) {
            return false;
        }
        AddEventCommand addEventCommand = new AddEventCommand();
        if (data.getType().toString().equals("MESSAGE")) {
            data.setContent(" " + data.getContent());
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

    public ClaimGrayResult claimGray(ClaimGrayRequest req){
        ClaimGrayResult res = new ClaimGrayResult();
        String username = req.getUsername();
        Game game = getAssociatedGame(username);
        if (game.claimRoute(username, req.getId())) {
            Route route = game.getRoute(req.getId());
            res.setSuccess(true);
            res.setId(req.getId());
            if (route.getClaimedType() != null) {
                res.setColor(route.getClaimedType());
            }
            AddEventCommand addEventCommand = new AddEventCommand();
            Event event = new Event();
            event.setUsername(username);
            event.setType(EventType.TURN);
            event.setContent(req.getUsername() + " claimed the route from " + route.getCity1().getName() + " to " + route.getCity2().getName() + ".");
            addEventCommand.setEvent(event);
            CommandData eventCommandData = new CommandData();
            eventCommandData.setType(ClientCommandType.C_EVENT);
            eventCommandData.setData(new Gson().toJson(addEventCommand));

            CommandData claimCommandData = new CommandData();
            claimCommandData.setType(ClientCommandType.C_CLAIM_ROUTE);
            ClaimRouteCommand claimRouteCommand = new ClaimRouteCommand();
            claimRouteCommand.setId(req.getId());
            claimRouteCommand.setUsername(username);
            claimCommandData.setData(new Gson().toJson(claimRouteCommand));

            CommandData statsCommandData = new CommandData();
            StatsChange change1 = new StatsChange();
            StatsChange change2 = new StatsChange();
            StatsChange change3 = new StatsChange();
            change1.setType(StatsChangeType.DECREASE_TRAIN_CARS);
            change1.setAmount(route.getNumTracks());
            change2.setType(StatsChangeType.ADD_POINTS);
            change2.setAmount(route.getPoints());
            change3.setType(StatsChangeType.DECREASE_TRAIN_CAR_CARDS);
            change3.setAmount(route.getNumTracks());
            List<StatsChange> changes = new ArrayList<>();
            changes.add(change1);
            changes.add(change2);
            changes.add(change3);
            UpdatePlayerStatsCommand updatePlayerStatsCommand = new UpdatePlayerStatsCommand();
            updatePlayerStatsCommand.setUsername(username);
            updatePlayerStatsCommand.setChanges(changes);
            statsCommandData.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
            statsCommandData.setData(new Gson().toJson(updatePlayerStatsCommand));

            addCommandToAllPlayers(game, eventCommandData);
            addCommandToAllPlayers(game, claimCommandData);
            addCommandToAllPlayers(game, statsCommandData);

            return res;
        }
        res.setSuccess(false);
        res.setErrorMessage("Route was not able to be claimed");
        return res;
    }

    public ClaimRouteResult claimRoute(ClaimRouteRequest req) {
        ClaimRouteResult res = new ClaimRouteResult();
        String username = req.getUsername();
        Game game = getAssociatedGame(username);
        if (game.claimRoute(username, req.getId())) {
            Route route = game.getRoute(req.getId());
            res.setSuccess(true);
            res.setId(req.getId());
            if (route.getClaimedType() != null) {
                res.setColorIfGray(route.getClaimedType());
            }
            AddEventCommand addEventCommand = new AddEventCommand();
            Event event = new Event();
            event.setUsername(username);
            event.setType(EventType.TURN);
            event.setContent(req.getUsername() + " claimed the route from " + route.getCity1().getName() + " to " + route.getCity2().getName() + ".");
            addEventCommand.setEvent(event);
            CommandData eventCommandData = new CommandData();
            eventCommandData.setType(ClientCommandType.C_EVENT);
            eventCommandData.setData(new Gson().toJson(addEventCommand));

            CommandData claimCommandData = new CommandData();
            claimCommandData.setType(ClientCommandType.C_CLAIM_ROUTE);
            ClaimRouteCommand claimRouteCommand = new ClaimRouteCommand();
            claimRouteCommand.setId(req.getId());
            claimRouteCommand.setUsername(username);
            claimCommandData.setData(new Gson().toJson(claimRouteCommand));

            CommandData statsCommandData = new CommandData();
            StatsChange change1 = new StatsChange();
            StatsChange change2 = new StatsChange();
            StatsChange change3 = new StatsChange();
            change1.setType(StatsChangeType.DECREASE_TRAIN_CARS);
            change1.setAmount(route.getNumTracks());
            change2.setType(StatsChangeType.ADD_POINTS);
            change2.setAmount(route.getPoints());
            change3.setType(StatsChangeType.DECREASE_TRAIN_CAR_CARDS);
            change3.setAmount(route.getNumTracks());
            List<StatsChange> changes = new ArrayList<>();
            changes.add(change1);
            changes.add(change2);
            changes.add(change3);
            UpdatePlayerStatsCommand updatePlayerStatsCommand = new UpdatePlayerStatsCommand();
            updatePlayerStatsCommand.setUsername(username);
            updatePlayerStatsCommand.setChanges(changes);
            statsCommandData.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
            statsCommandData.setData(new Gson().toJson(updatePlayerStatsCommand));
            
            Set<String> usernamesOfPlayers = game.getGamePlayers().keySet();
            for (String oneUsername: usernamesOfPlayers) {
                User user = users.get(oneUsername);
                user.addCommand(eventCommandData);
                user.addCommand(claimCommandData);
                user.addCommand(statsCommandData);
            }
            return res;
        }
        res.setSuccess(false);
        res.setErrorMessage("Route was not able to be claimed");
        return res;
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
