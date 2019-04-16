package Service;

import Command.ServerCommand.*;
import Model.Game;
import Model.User;
import Database.Database;
import Plugin.PluginRegistry;
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
            for(int i = 0; i < users.size(); i += 2) {
                User user = new User();
                user.setUsername(users.get(i));
                user.setPassword(users.get(i + 1));
                userList.add(user);
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
            System.out.println("Still need to parse the JSON...");
            ArrayList<Game> gameList = new ArrayList<>();
            for (String game : games) {
                Gson gson = new Gson();
                gameList.add(gson.fromJson(game, Game.class));
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
            System.out.println("Still need to parse the json...");
            ArrayList<iServerCommand> serverCommands = new ArrayList<>();
            for (int i = 0; i < commands.size(); i += 2) {
                serverCommands.add(getCommandFromType(CommandType.valueOf(commands.get(i)), commands.get(i + 1)));
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

    private static iServerCommand getCommandFromType(CommandType type, String command) {
        Gson gson = new Gson();
        switch (type) {
            case S_POLL:
                return gson.fromJson(command, GetCommandsCommand.class);
            case S_LOGIN:
                return gson.fromJson(command, LoginCommand.class);
            case S_END_TURN:
                return gson.fromJson(command, EndTurnCommand.class);
            case S_REGISTER:
                return gson.fromJson(command, RegisterCommand.class);
            case S_JOIN_GAME:
                return gson.fromJson(command, JoinGameCommand.class);
            case S_CLAIM_GRAY:
                return gson.fromJson(command, ClaimGrayCommand.class);
            case S_ASSIGN_DEST:
                return gson.fromJson(command, AssignDestinationCardsCommand.class);
            case S_ASSIGN_FIRST_DEST:
                return gson.fromJson(command, AssignFirstDestinationCardsCommand.class);
            case S_CLAIM_ROUTE:
                return gson.fromJson(command, ClaimRouteCommand.class);
            case S_CREATE_GAME:
                return gson.fromJson(command, CreateGameCommand.class);
            case S_SEND_MESSAGE:
                return gson.fromJson(command, SendMessageCommand.class);
            case S_DRAW_FROM_TRAIN_PILE:
                return gson.fromJson(command, DrawTrainCarCardCommand.class);
            case S_DRAW_FACE_UP_TRAIN_CAR_CARD:
                return gson.fromJson(command, DrawFaceUpCommand.class);
            case S_DRAW_THREE_DESTINATION_CARDS_FROM_DRAW_PILE:
                return gson.fromJson(command, DrawDestinationCardsCommand.class);
        }
        return null;
    }
}
