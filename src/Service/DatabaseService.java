package Service;

import Command.ServerCommand.iServerCommand;
import Model.Game;
import Database.Database;
import Plugin.PluginRegistry;
import com.google.gson.Gson;
import java.util.ArrayList;

public class DatabaseService {

    private Database database = PluginRegistry.instance.getDatabase();

    public boolean addNewUser(String username, String password) {
        return database.addNewUser(username, password);
    }

    public boolean updateGame(String gameName, Game game) {
        return database.updateGame(gameName, new Gson().toJson(game));
    }

    public boolean addCommand(String gameName, iServerCommand command) {
        return database.addCommand(gameName, new Gson().toJson(command));
    }

    public boolean clearCommandsForGame(String gameName) {
        return database.clearCommandsForGame(gameName);
    }

    public int getCommandsLength(String gameName) {
        return database.getCommandsLength(gameName);
    }

    public boolean verifyPassword(String username, String password) {
        return database.verifyPassword(username, password);
    }

    public ArrayList<String> getGames() {
        return database.getGames();
    }

    public ArrayList<String> getCommandsForGame(String gameName) {
        return database.getCommandsForGame(gameName);
    }
}
