package Service;

import Command.ServerCommand.iServerCommand;
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

    public boolean addCommand(String gameName, iServerCommand command) {
        try {
            database.openConnection();
            boolean success = database.addCommand(gameName, new Gson().toJson(command));
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
        //TODO: Implement
        return new ArrayList<>();
    }

    public ArrayList<String> getGames() {
        try {
            database.openConnection();
            ArrayList<String> games = database.getGames();
            database.closeConnection(true);
            return games;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return new ArrayList<>(); }
            return new ArrayList<>();
        }
    }

    public ArrayList<String> getCommandsForGame(String gameName) {
        try {
            database.openConnection();
            ArrayList<String> commands = database.getCommandsForGame(gameName);
            database.closeConnection(true);
            return commands;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (Exception f) { return new ArrayList<>(); }
            return new ArrayList<>();
        }
    }
}
