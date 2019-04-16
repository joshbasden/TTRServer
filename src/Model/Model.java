package Model;

import Command.ClientCommand.*;
import Command.ClientCommand.ClaimRouteCommand;
import Command.ServerCommand.*;
import Request.*;
import Result.*;
import Service.DatabaseService;
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
    private DatabaseService databaseService = new DatabaseService();
    private HashMap<String, Integer> checkpointCounts = new HashMap<>();
    private boolean restarting = false;
    private int N;

    private static final Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }

    private Model() {
        initializeInfo();
    }

    //FUNCTIONS FOR SERVER COMMANDS

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

        AccountForDestinationDrawCommand accountForDestinationDrawCommand = new AccountForDestinationDrawCommand();
        accountForDestinationDrawCommand.setDeckSize(game.getDestinationDeckSize());
        CommandData destinationCountCommandData = new CommandData();
        destinationCountCommandData.setType(ClientCommandType.C_ACCOUNT_FOR_DESTINATION_DRAW);
        destinationCountCommandData.setData(new Gson().toJson(accountForDestinationDrawCommand));

        addCommandToAllPlayers(game, destinationCountCommandData);
        addCommandToAllPlayers(game, updateStatsCommandData);

        res.setSuccess(true);
        res.setCards(cardsAdded);
        AssignDestinationCardsCommand assignDestinationCardsCommand = new AssignDestinationCardsCommand(req);
        addCommandToDatabase(game.getGameName(), assignDestinationCardsCommand);
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

        AccountForDestinationDrawCommand accountForDestinationDrawCommand = new AccountForDestinationDrawCommand();
        accountForDestinationDrawCommand.setDeckSize(game.getDestinationDeckSize());
        CommandData destinationCountCommandData = new CommandData();
        destinationCountCommandData.setType(ClientCommandType.C_ACCOUNT_FOR_DESTINATION_DRAW);
        destinationCountCommandData.setData(new Gson().toJson(accountForDestinationDrawCommand));

        addCommandToAllPlayers(game, updateStatsCommandData);
        addCommandToAllPlayers(game, destinationCountCommandData);
        AssignFirstDestinationCardsCommand assignFirstDestinationCardsCommand = new AssignFirstDestinationCardsCommand(req);
        addCommandToDatabase(game.getGameName(), assignFirstDestinationCardsCommand);
        return res;
    }

    public ClaimRouteResult claimRoute(ClaimRouteRequest req, TrainCarCardType colorIfGray) {
        ClaimRouteResult res = new ClaimRouteResult();
        String username = req.getUsername();
        Game game = getAssociatedGame(username);
        boolean gray = (colorIfGray != null);
        if (game.claimRoute(username, req.getId(), colorIfGray)) {
            Route route = game.getRoute(req.getId());
            Player player = getAssociatedPlayer(username);
            player.setScore(player.getScore() + convertTracksToPoints(route.getNumTracks()));
            res.setSuccess(true);
            res.setId(req.getId());

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
            change2.setAmount(convertTracksToPoints(route.getNumTracks()));
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

            if (gray) {
                ClaimGrayRequest claimGrayRequest = new ClaimGrayRequest();
                claimGrayRequest.setColor(colorIfGray);
                claimGrayRequest.setId(req.getId());
                claimGrayRequest.setUsername(req.getUsername());
                ClaimGrayCommand claimGrayCommand = new ClaimGrayCommand(claimGrayRequest);
                addCommandToDatabase(game.getGameName(), claimGrayCommand);
            }
            else {
                Command.ServerCommand.ClaimRouteCommand serverClaimRouteCommand = new Command.ServerCommand.ClaimRouteCommand(req);
                addCommandToDatabase(game.getGameName(), serverClaimRouteCommand);
            }
            return res;
        }
        res.setSuccess(false);
        res.setErrorMessage("Route was not able to be claimed");
        return res;
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
        databaseService.updateGame(gameName, game);
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

        DrawDestinationCardsRequest req = new DrawDestinationCardsRequest();
        req.setUsername(username);
        DrawDestinationCardsCommand drawDestinationCardsCommand = new DrawDestinationCardsCommand(req);
        addCommandToDatabase(game.getGameName(), drawDestinationCardsCommand);
        return drawDestinationCardsResult;
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

        AccountForTrainCarCardDrawCommand accountForDraws = new AccountForTrainCarCardDrawCommand();
        accountForDraws.setDeckSize(game.getTrainDeckSize());

        //add an event of drawing a face up
        Event event = new Event();
        event.setType(EventType.TURN);
        event.setUsername(player);
        event.setContent("Drew a face up card");
        AddEventCommand eventCommand = new AddEventCommand();
        eventCommand.setEvent(event);

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
        CommandData commandDataEvent = new CommandData();
        commandDataAccount.setType(ClientCommandType.C_ACCOUNT_FOR_THE_FACT_THAT_SOMEONE_DREW_FROM_THE_TRAIN_CAR_CARD_DRAW_PILE);
        commandDataAccount.setData(new Gson().toJson(accountForDraws));
        commandDataUpdate.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
        commandDataUpdate.setData(new Gson().toJson(updateStatsCommand));
        commandDataEvent.setType(ClientCommandType.C_EVENT);
        commandDataEvent.setData(new Gson().toJson(eventCommand));


        try{
            addCommandToAllPlayers(game, commandDataUpdate);
            addCommandToAllPlayers(game, commandDataAccount);
            addCommandToAllPlayers(game, commandDataReplace);
            addCommandToAllPlayers(game, commandDataEvent);
            result.setSuccess(true);
            result.setCard(cards.get(1));
            DrawFaceUpRequest req = new DrawFaceUpRequest();
            req.setIndex(index);
            req.setUsername(player);
            DrawFaceUpCommand drawFaceUpCommand = new DrawFaceUpCommand(req);
            addCommandToDatabase(game.getGameName(), drawFaceUpCommand);
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

        AccountForTrainCarCardDrawCommand accountForDraws = new AccountForTrainCarCardDrawCommand();
        accountForDraws.setDeckSize(game.getTrainDeckSize());

        updateStatsCommand.setUsername(player);
        updateStatsCommand.setChanges(statsChangeArray);

        //Send event for drawing a card
        Event event = new Event();
        event.setUsername(player);
        event.setType(EventType.TURN);
        event.setContent("Drew a train card from the deck");
        AddEventCommand eventCommand = new AddEventCommand();
        eventCommand.setEvent(event);

        CommandData commandDataUpdate = new CommandData();
        CommandData commandDataAccount = new CommandData();
        CommandData commandDataEvent = new CommandData();
        commandDataAccount.setType(ClientCommandType.C_ACCOUNT_FOR_THE_FACT_THAT_SOMEONE_DREW_FROM_THE_TRAIN_CAR_CARD_DRAW_PILE);
        commandDataAccount.setData(new Gson().toJson(accountForDraws));
        commandDataUpdate.setType(ClientCommandType.C_UPDATE_PLAYER_STATS);
        commandDataUpdate.setData(new Gson().toJson(updateStatsCommand));
        commandDataEvent.setType(ClientCommandType.C_EVENT);
        commandDataEvent.setData(new Gson().toJson(eventCommand));

        try {
            addCommandToAllPlayers(game, commandDataUpdate);
            addCommandToAllPlayers(game, commandDataAccount);
            addCommandToAllPlayers(game, commandDataEvent);
            result.setSuccess(true);
            DrawTrainCarCardRequest req = new DrawTrainCarCardRequest();
            req.setUsername(player);
            DrawTrainCarCardCommand drawTrainCarCardCommand = new DrawTrainCarCardCommand(req);
            addCommandToDatabase(game.getGameName(), drawTrainCarCardCommand);
            return result;
        }
        catch (Exception e){
            result.setErrorMessage("Could not send Update Player Stats Command to everyone");
            result.setSuccess(false);
            return result;
        }

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
        EndTurnRequest req = new EndTurnRequest();
        req.setUsername(endTurnPlayer);
        EndTurnCommand endTurnCommand = new EndTurnCommand(req);
        addCommandToDatabase(game.getGameName(), endTurnCommand);
        if (game.isLastRound()) {
            if (game.getLastPlayerToTakeTurn().equals(endTurnPlayer)) {
                endTurnResult.setSuccess(true);
                endGame(game);
                return endTurnResult;
            }
            if (game.getLastPlayerToTakeTurn().equals("")) {
                game.setLastPlayerToTakeTurn(endTurnPlayer);
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

    public GetCommandsResult getCommands(String username) {
        GetCommandsResult res = new GetCommandsResult();
        User user = users.get(username);
        res.setUsername(username);
        if (user == null) {
            res.setErrorMessage("User does not exist");
            res.setSuccess(false);
            return res;
        }
        res.setCommandList(new ArrayList<>(user.getCommands()));
        res.setSuccess(true);
        user.clearCommands();
        GetCommandsRequest req = new GetCommandsRequest();
        req.setUsername(username);
        GetCommandsCommand getCommandsCommand = new GetCommandsCommand(req);
        if (res.getCommandList().size() != 0) {
            addCommandToDatabase(getAssociatedGame(username).getGameName(), getCommandsCommand);
        }
        return res;
    }

    public boolean joinGame(String username, String gameName) {
        Game game = games.get(gameName);
        if (game == null) {
            return false;
        }
        if (game.addPlayer(username)) {
            if (game.getNumPlayers() == game.getGamePlayers().size()) {
                beginGame(gameName);
            }
            JoinGameRequest req = new JoinGameRequest();
            req.setGameName(gameName);
            req.setUsername(username);
            JoinGameCommand joinGameCommand = new JoinGameCommand(req);
            addCommandToDatabase(gameName, joinGameCommand);
            return true;
        }
        return false;
    }

    public boolean authenticateUser(String username, String password) {
        if (!users.containsKey(username)) {
            return false;
        }
        //TODO: Make sure we don't need to add anything to the database here since nothing in the model is updated
        return users.get(username).getPassword().equals(password);
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
        if (!restarting) {
            databaseService.addNewUser(username, password);
        }
        return true;
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

        addCommandToAllPlayers(game, commandData);
        SendMessageRequest req = new SendMessageRequest();
        req.setData(data);
        SendMessageCommand sendMessageCommand = new SendMessageCommand(req);
        addCommandToDatabase(game.getGameName(), sendMessageCommand);
        return true;
    }

    //HELPER FUNCTIONS

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
        createUser("j11", "j");
        //createGame("game with two players", 2);
        //createGame("game with three players", 3);
        //createGame("game with four players", 4);
        //createGame("game with five players", 5);
        //createGame("Another game with two players, just in case it's needed", 2);
    }

    private void beginGame(String gameName) {
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
        if (!restarting) {
            for (User user: users.values()) {
                if (!userNamesOfPlayers.contains(user.getUsername())) {
                    user.addCommand(commandData);
                }
            }
        }
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
                if (!restarting) {
                    users.get(userName).addCommand(commandData);
                }
            }
        }
    }

    private void endGame(Game game) {
        GameSummary gameSummary = new GameSummary();
        List<String> usernamesOfBonusPlayer = game.getBonusPlayers();
        for (Player player: game.getGamePlayers().values()) {
            PlayerSummary playerSummary = new PlayerSummary();
            playerSummary.setUsername(player.getUsername());
            playerSummary.setPtsFromClaimedRoutes(player.getScore());
            List<Integer> destinationPoints = player.getDestinationPoints();
            playerSummary.setPtsFromDestinations(destinationPoints.get(0));
            playerSummary.setPtsReducedFromDestinations(destinationPoints.get(1));
            if (usernamesOfBonusPlayer.contains(player.getUsername())) {
                playerSummary.setPtsFromMostClaimedRoutes(10); //TODO: Maybe get longest path instead
                player.setScore(player.getScore() + 10);
            }
            else {
                playerSummary.setPtsFromMostClaimedRoutes(0);
            }
            playerSummary.setTotalPoints(player.getScore());
            gameSummary.addPlayerSummary(playerSummary);
        }
        gameSummary.sort();
        EndGameCommand endGameCommand = new EndGameCommand();
        endGameCommand.setSummary(gameSummary);
        endGameCommand.setWinner(gameSummary.getPlayers().get(0).getUsername());
        endGameCommand.setWinnerPoints(gameSummary.getPlayers().get(0).getTotalPoints());
        CommandData endGameCommandData = new CommandData();
        endGameCommandData.setType(ClientCommandType.C_END_GAME);
        endGameCommandData.setData(new Gson().toJson(endGameCommand));
        addCommandToAllPlayers(game, endGameCommandData);
    }

    private void addCommandToAllPlayers(Game game, CommandData command){
        if (!restarting) {
            for(Player p: game.getGamePlayers().values()){
                User user = users.get(p.getUsername());
                user.addCommand(command);
            }
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

            Player player = getAssociatedPlayer(username);

            for (int i = 0; i < 4; ++i) {
                card = (TrainCarCard)game.getGameTrainDeck().draw();
                cards.add(card);

                //add card to players hand
                player.addTrainCard(card);
            }
            
            TrainCarCardHand trainCarCardHand = new TrainCarCardHand();
            trainCarCardHand.setCards(cards);
            dealTrainCarCardsCommand.setHand(trainCarCardHand);
            dealTrainCarCardsCommandData.setType(ClientCommandType.C_FIRST_HAND);
            dealTrainCarCardsCommandData.setData(new Gson().toJson(dealTrainCarCardsCommand));
            if (!restarting) {
                users.get(username).addCommand(dealTrainCarCardsCommandData);
            }
        }

        // add update train cards command
        AccountForTrainCarCardDrawCommand accountForDraws = new AccountForTrainCarCardDrawCommand();
        accountForDraws.setDeckSize(game.getTrainDeckSize());
        CommandData updateCardNum = new CommandData();
        updateCardNum.setType(ClientCommandType.C_ACCOUNT_FOR_THE_FACT_THAT_SOMEONE_DREW_FROM_THE_TRAIN_CAR_CARD_DRAW_PILE);
        updateCardNum.setData(new Gson().toJson(accountForDraws));
        addCommandToAllPlayers(game, updateCardNum);

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

    private int convertTracksToPoints(int numTracks) {
        switch (numTracks) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 7;
            case 5:
                return 10;
            case 6:
                return 15;
            default:
                return 0;
        }
    }

    //CHECKPOINT + DELTAS LOGIC

    private void addCommandToDatabase(String gameName, iServerCommand command) {
        if (!restarting) {
            databaseService.addCommand(gameName, command., command);
        }
        increaseCheckpointCount(gameName);
    }

    private void increaseCheckpointCount(String gameName) {
        if (!checkpointCounts.containsKey(gameName)) {
            checkpointCounts.put(gameName, 0);
            return;
        }
        int numDeltas = checkpointCounts.get(gameName);
        checkpointCounts.put(gameName, numDeltas + 1);
        if (checkpointCounts.get(gameName) >= N) {
            createCheckpoint(gameName);
            checkpointCounts.put(gameName, 0);
        }
    }

    private void createCheckpoint(String gameName) {
        databaseService.updateGame(gameName, games.get(gameName));
        databaseService.clearCommandsForGame(gameName);
    }

    public void initialize() {
        restarting = true;
        List<User> usersToInitialize = databaseService.getUsers();
        for (User user: usersToInitialize) {
            users.put(user.getUsername(), user);
        }
        List<Game> gamesToInitialize = databaseService.getGames();
        for (Game game: gamesToInitialize) {
            games.put(game.getGameName(), game);
            List<iServerCommand> deltas = databaseService.getCommandsForGame(game.getGameName());
            for (iServerCommand command: deltas) {
                command.execute();
            }
        }
        restarting = false;
    }

    public HashMap<String, Game> getGames() {
        return games;
    }

    public void setN(int n) {
        N = n;
    }
}
