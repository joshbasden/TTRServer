package Service;

import Command.ServerCommand.iServerCommand;
import Model.Game;
import Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    public Boolean addNewUser(String username, String password) {
        //TODO: Implement
        return true;
    }

    public Boolean updateGame(String gameName, Game game) {
        //TODO: Implement
        return true;
    }

    public Boolean addCommand(String gameName, iServerCommand command) {
        //TODO: Implement
        return true;
    }

    public Boolean clearCommandsForGame(String gameName) {
        //TODO: Implement
        return true;
    }

    public int getCommandsLength(String gameName) {
        //TODO: Implement
        return 0;
    }

    public List<User> getUsers() {
        //TODO: Implement
        return new ArrayList<>();
    }

    public List<Game> getGames() {
        //TODO: Implement
        return new ArrayList<>();
    }

    public List<iServerCommand> getCommandsForGame(String gameName) {
        //TODO: Implement
        return new ArrayList<>();
    }

}
