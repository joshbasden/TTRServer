package Model;

import Command.ClientCommand.*;
import Result.GameInfoResult;
import Result.GetCommandsResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jbasden on 1/30/19.
 */

public class Model {
    private static final Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }

    private Model() {}

    private HashMap<String, Game> games = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();

    public boolean createGame(String gameName, int numPlayers, String userName) {
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
        GameInfoResult gameInfo = new GameInfoResult();
        gameInfo.setNumPlayers(numPlayers);
        gameInfo.setGameName(gameName);
        addGameCommand.setGameInfo(gameInfo);

        commandData.setData(new Gson().toJson(addGameCommand));
        for (User user: users.values()) {
            if (!user.getUserName().equals(userName)) {
                user.addCommand(commandData);
            }
        }
        return true;
    }

    public boolean authenticateUser(String userName, String password) {
        if (!users.containsKey(userName)) {
            return false;
        }
        return users.get(userName).getPassword().equals(password);
    }

    public boolean createUser(String userName, String password) {
        if (users.containsKey(userName)) {
            return false;
        }
        if (userName.length() == 0 || password.length() == 0) {
            return false;
        }
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        users.put(userName, user);
        return true;
    }

    public boolean joinGame(String userName, String gameName) {
        Game game = games.get(gameName);
        if (game == null) {
            return false;
        }
        if (game.addPlayer(userName)) {
            if (game.getNumPlayers() == game.getGamePlayers().size()) {
                List<String> userNamesOfPlayers = new ArrayList<>();
                for (Player player : game.getGamePlayers().values()) {
                    userNamesOfPlayers.add(player.getUserName());
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
                    if (!userNamesOfPlayers.contains(user.getUserName())) {
                        user.addCommand(commandData);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public String getPlayerColor(String gameName, String userName) {
        Game game = games.get(gameName);
        if (game == null) {
            return "False Color";
        }
        Player player = game.getGamePlayers().get(userName);
        if (player == null) {
            return "False Color";
        }
        return player.getColor();
    }

    public GetCommandsResult getCommands(String userName) {
        GetCommandsResult res = new GetCommandsResult();
        User user = users.get(userName);
        res.setUserName(userName);
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
                GameInfoResult gameInfo = new GameInfoResult();
                gameInfo.setGameName(gameToCheck.getGameName());
                System.out.println(gameToCheck.getGameName());
                gameInfo.setNumPlayers(gameToCheck.getNumPlayers());
                addGameCommand.setGameInfo(gameInfo);
                commandData.setData(new Gson().toJson(addGameCommand));
                users.get(userName).addCommand(commandData);
            }
        }
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
