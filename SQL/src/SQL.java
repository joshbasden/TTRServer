
import Database.Database;
import Database.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQL implements Database {

    UserDAO userDAO;
    CommandDAO commandDAO;
    GameDAO gameDAO;
    private Connection conn;

    public SQL(){
        userDAO = new UserDAO();
        commandDAO = new CommandDAO();
        gameDAO = new GameDAO();
    }

    @Override
    public boolean addNewUser(String s, String s1) throws DatabaseException {
        return userDAO.addNewUser(s, s1);
    }

    @Override
    public boolean updateGame(String s, String s1) throws DatabaseException {
        return gameDAO.updateGame(s, s1);
    }

    @Override
    public boolean addCommand(String s, String s1) throws DatabaseException {
        return commandDAO.addCommand(s, s1);
    }

    @Override
    public boolean clearCommandsForGame(String s) throws DatabaseException {
        return commandDAO.clearCommandsForGame(s);
    }

    @Override
    public int getCommandsLength(String s) throws DatabaseException {
        return 0;
    }

    @Override
    public boolean verifyPassword(String s, String s1) throws DatabaseException {
        return userDAO.verifyPassword(s, s1);
    }

    @Override
    public boolean openConnection() throws DatabaseException {
        try {
            Class.forName("org.sqlite.JDBC");
            final String CONNECTION_URL = "jdbc:sqlite:SQLDB.db";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            userDAO.setConn(conn);
            gameDAO.setConn(conn);
            commandDAO.setConn(conn);

            // Start a transaction
            conn.setAutoCommit(false);

            return true;
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Could not open a connection");
            return false;
        }
    }

    @Override
    public boolean closeConnection(boolean commit) throws DatabaseException {
        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
            }

            conn.close();
            conn = null;
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean initializeSchemas() throws DatabaseException {
        String command =    "CREATE TABLE IF NOT EXISTS 'Commands' ('ID' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'Command' BLOB, 'GameName' TEXT)";
        String game = "CREATE TABLE IF NOT EXISTS 'Games' ('GameName' TEXT NOT NULL UNIQUE," +
                "'Data' BLOB NOT NULL)";
        String user = "CREATE TABLE IF NOT EXISTS 'Users' ('Username' TEXT NOT NULL, 'Password' TEXT NOT NULL)";
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(game);
                stmt.executeUpdate(command);
                stmt.executeUpdate(user);

                return true;
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }
        catch (SQLException e) {
            System.out.println("createTables failed " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<String> getGames() throws DatabaseException {
        return gameDAO.getGames();
    }

    @Override
    public ArrayList<String> getCommandsForGame(String s) throws DatabaseException {
        return commandDAO.getCommandsForGame(s);
    }

    public static void main(String[] args){
//        SQL mongoDB = new SQL();
//        mongoDB.openConnection();
//        mongoDB.initializeSchemas();
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.addCommand("weirdo", "blah");
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.addCommand("Dallin", "blah");
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.addCommand("Dallin", "b");
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.addCommand("Dallin", "bla");
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.addCommand("Dallin", "blahasdfasdfs");
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        System.out.println();
//        System.out.println(mongoDB.getCommandsForGame("Dallin").toString());
//        System.out.println();
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.clearCommandsForGame("Dallin");
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        System.out.println();
//        System.out.println(mongoDB.getCommandsForGame("weirdo").toString());
//        System.out.println();
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.updateGame("dallinajfdlajsd", "jlasdfjlaksfjdTED!");
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.updateGame("dadlajsd", "TED!");
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        System.out.println(mongoDB.getGames().toString());
//        mongoDB.closeConnection();
//        mongoDB.openConnection();
//        mongoDB.addNewUser("dallin", "boop");
//        mongoDB.closeConnection();
    }

}
