import Database.Database;

import java.util.ArrayList;

public class SQL implements Database {

    @Override
    public boolean addNewUser(String s, String s1) {
        return false;
    }

    @Override
    public boolean updateGame(String s, String s1) {
        return false;
    }

    @Override
    public boolean addCommand(String s, String s1) {
        return false;
    }

    @Override
    public boolean clearCommandsForGame(String s) {
        return false;
    }

    @Override
    public int getCommandsLength(String s) {
        return 0;
    }

    @Override
    public boolean verifyPassword(String s, String s1) {
        return false;
    }

    @Override
    public boolean openConnection() {
        return false;
    }

    @Override
    public boolean closeConnection() {
        return false;
    }

    @Override
    public boolean initializeSchemas() {
        return false;
    }

    @Override
    public ArrayList<String> getGames() {
        return null;
    }

    @Override
    public ArrayList<String> getCommandsForGame(String s) {
        return null;
    }
}
