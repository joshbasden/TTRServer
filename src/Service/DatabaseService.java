package Service;

import Command.ServerCommand.*;
import Model.Game;
import Model.User;
import Database.Database;
import Plugin.PluginRegistry;
import Request.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private Database database = PluginRegistry.instance.getDatabase();

    public boolean addNewUser(String username, String password) {
        try {
            database.openConnection();
            boolean success = database.addNewUser(username, password);
            database.closeConnection(success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return false; }
            return false;
        }
    }

    public boolean updateGame(String gameName, Game game) {
        try {
            database.openConnection();
            boolean success = database.updateGame(gameName, new Gson().toJson(game));
            database.closeConnection(success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return false; }
            return false;
        }
    }

    public boolean addCommand(String gameName, String type, iServerCommand command) {
        try {
            database.openConnection();
            boolean success = database.addCommand(gameName, type, new Gson().toJson(command));
            database.closeConnection(success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return false; }
            return false;
        }
    }

    public boolean clearCommandsForGame(String gameName) {
        try {
            database.openConnection();
            boolean success = database.clearCommandsForGame(gameName);
            database.closeConnection(success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return false; }
            return false;
        }
    }

    public int getCommandsLength(String gameName) {
        try {
            database.openConnection();
            int numCommands = database.getCommandsLength(gameName);
            database.closeConnection(true);
            return numCommands;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return -1; }
            return -1;
        }
    }

    public boolean verifyPassword(String username, String password) {
        try {
            database.openConnection();
            boolean success = database.verifyPassword(username, password);
            database.closeConnection(success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return false; }
            return false;
        }
    }

    public List<User> getUsers() {
        try {
            database.openConnection();
            ArrayList<String> users = database.getUsers();
            database.closeConnection(true);
            ArrayList<User> userList = new ArrayList<>();
            if (users != null) {
                for (int i = 0; i < users.size(); i += 2) {
                    User user = new User();
                    user.setUsername(users.get(i));
                    user.setPassword(users.get(i + 1));
                    userList.add(user);
                }
            }
            return userList;
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            }
            catch (Exception f) {
                f.printStackTrace();
            }
            return new ArrayList<>();
        }
    }

    public ArrayList<Game> getGames() {
        try {
            database.openConnection();
            ArrayList<String> games = database.getGames();
            database.closeConnection(true);
            ArrayList<Game> gameList = new ArrayList<>();
            if (games != null) {
                for (String game : games) {
                    Gson gson = new Gson();
                    gameList.add(gson.fromJson(game, Game.class));
                }
            }
            return gameList;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return new ArrayList<>(); }
            return new ArrayList<>();
        }
    }

    public ArrayList<iServerCommand> getCommandsForGame(String gameName) {
        try {
            database.openConnection();
            ArrayList<String> commands = database.getCommandsForGame(gameName);
            database.closeConnection(true);
            ArrayList<iServerCommand> serverCommands = new ArrayList<>();
            for (String command : commands) {
                ServerCommandData data = new Gson().fromJson(command, ServerCommandData.class);
                serverCommands.add(getCommandFromType(data.getType(), command));
            }
            return serverCommands;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return new ArrayList<>(); }
            return new ArrayList<>();
        }
    }

    private static String getDataFromJson(String json) {
        String reverse = new StringBuilder(json).reverse().toString();
        int ind = 0;
        char a = 'a';
        while (a != ',') {
            a = reverse.charAt(ind);
            ind++;
        }
        String hi = json.substring(8, json.length() - ind);
        return hi;
    }

    private static iServerCommand getCommandFromType(ServerCommandType type, String data) {
        Gson gson = new Gson();
        String command = getDataFromJson(data);
        switch (type) {
            case S_POLL:
                return new GetCommandsCommand(gson.fromJson(command, GetCommandsRequest.class));
            case S_LOGIN:
                return new LoginCommand(gson.fromJson(command, LoginRequest.class));
            case S_END_TURN:
                return new EndTurnCommand(gson.fromJson(command, EndTurnRequest.class));
            case S_REGISTER:
                return new RegisterCommand(gson.fromJson(command, RegisterRequest.class));
            case S_JOIN_GAME:
                return new JoinGameCommand(gson.fromJson(command, JoinGameRequest.class));
            case S_CLAIM_GRAY:
                return new ClaimGrayCommand(gson.fromJson(command, ClaimGrayRequest.class));
            case S_ASSIGN_DEST:
                return new AssignDestinationCardsCommand(gson.fromJson(command, AssignDestinationCardsRequest.class));
            case S_ASSIGN_FIRST_DEST:
                return new AssignFirstDestinationCardsCommand(gson.fromJson(command, AssignFirstDestinationCardsRequest.class));
            case S_CLAIM_ROUTE:
                return new ClaimRouteCommand(gson.fromJson(command, ClaimRouteRequest.class));
            case S_CREATE_GAME:
                return new CreateGameCommand(gson.fromJson(command, CreateGameRequest.class));
            case S_SEND_MESSAGE:
                return new SendMessageCommand(gson.fromJson(command, SendMessageRequest.class));
            case S_DRAW_FROM_TRAIN_PILE:
                return new DrawTrainCarCardCommand(gson.fromJson(command, DrawTrainCarCardRequest.class));
            case S_DRAW_FACE_UP_TRAIN_CAR_CARD:
                return new DrawFaceUpCommand(gson.fromJson(command, DrawFaceUpRequest.class));
            case S_DRAW_THREE_DESTINATION_CARDS_FROM_DRAW_PILE:
                return new DrawDestinationCardsCommand(gson.fromJson(command, DrawDestinationCardsRequest.class));
        }
        return null;
    }
}
