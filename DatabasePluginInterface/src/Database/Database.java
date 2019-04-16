package Database;

import java.util.ArrayList;

public interface Database {
    boolean addNewUser(String username, String password) throws DatabaseException;
    boolean updateGame(String gameName, String game) throws DatabaseException;
    boolean addCommand(String gameName, String command) throws DatabaseException;
    boolean clearCommandsForGame(String gameName) throws DatabaseException;
    int getCommandsLength(String gameName) throws DatabaseException;
    boolean verifyPassword(String username, String password) throws DatabaseException;
    boolean openConnection() throws DatabaseException;
    boolean closeConnection(boolean success) throws DatabaseException;
    boolean initializeSchemas() throws DatabaseException;
    ArrayList<String> getGames() throws DatabaseException;
    ArrayList<String> getCommandsForGame(String gameName) throws DatabaseException;
    ArrayList<String> getUsers() throws DatabaseException;
}