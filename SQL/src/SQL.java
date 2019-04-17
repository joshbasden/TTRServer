
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
    public boolean addCommand(String s, String s1, String s2) throws DatabaseException {
        return commandDAO.addCommand(s, s1, s2);
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
            throw new DatabaseException("Could not open a connection", e);
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
            throw new DatabaseException("error while closing connection", e);
        }
    }


    @Override
    public boolean initializeSchemas() throws DatabaseException {
//        String dropComm = "DROP TABLE IF EXISTS Commands";
        String command =    "CREATE TABLE IF NOT EXISTS 'Commands' ('ID' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'Command' BLOB, 'GameName' TEXT, 'Type' TEXT)";
        String game = "CREATE TABLE IF NOT EXISTS 'Games' ('GameName' TEXT NOT NULL UNIQUE," +
                "'Data' BLOB NOT NULL)";
        String user = "CREATE TABLE IF NOT EXISTS 'Users' ('Username' TEXT NOT NULL, 'Password' TEXT NOT NULL)";
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
//                stmt.executeUpdate(dropComm);
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
            throw new DatabaseException("createTables failed ", e);
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

    @Override
    public ArrayList<String> getUsers() throws DatabaseException {
        return userDAO.getUsers();
    }

    public static void main(String[] args){
        SQL mongoDB = new SQL();
        try{
            mongoDB.openConnection();
            mongoDB.initializeSchemas();
            mongoDB.closeConnection(true);
            mongoDB.openConnection();
            mongoDB.addCommand("Dallin", "NewUser","bla");
            mongoDB.closeConnection(true);
            mongoDB.openConnection();
            System.out.println();
            System.out.println(mongoDB.getCommandsForGame("Dallin").toString());
            System.out.println();
            mongoDB.closeConnection(true);
        }catch (DatabaseException d){
            try{
                mongoDB.closeConnection(false);
                d.printStackTrace();
            }catch (DatabaseException e){

            }
        }
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
