package Database;

import java.util.ArrayList;

public interface Database {
    boolean addNewUser(String username, String password);
    boolean updateGame(String gameName, String game);
    boolean addCommand(String gameName, String command);
    boolean clearCommandsForGame(String gameName);
    int getCommandsLength(String gameName);
    boolean verifyPassword(String username, String password);
    boolean openConnection();
    boolean closeConnection();
    ArrayList<String> getGames();
    ArrayList<String> getCommandsForGame(String gameName);
}
