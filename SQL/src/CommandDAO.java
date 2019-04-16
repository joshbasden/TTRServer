import Database.DatabaseException;

import java.sql.*;
import java.util.ArrayList;

public class CommandDAO {
    /**
     * Handles all interaction
     * with the Person table
     * in the database
     */
    Connection conn;


    public CommandDAO(){}

    public boolean addCommand(String gamename, String command) throws DatabaseException {
        String sql = "INSERT INTO Commands (GameName, Command) VALUES (?,?);";
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, gamename);
                stmt.setString(2, command);

                // update in table
                stmt.executeUpdate();
                return true;
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to add Command: " + command + " for " + gamename + ". ", e);
        }
    }

    public boolean clearCommandsForGame(String gamename) throws DatabaseException{
        String sql = "DELETE FROM Commands WHERE GameName = ?;";

        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, gamename);
                stmt.executeUpdate();

                return true;
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to clear commands for " + gamename + ". ", e);
        }

    }

    public int getCommandsLength(String gameName) throws DatabaseException {
        ArrayList<String> commandList = null;

        try{
            commandList = getCommandsForGame(gameName);
            return commandList.size();

        } catch (Exception e) {
            throw new DatabaseException("Error when trying to get command size for " + gameName + ". ", e);
        }

    }

    public ArrayList<String> getCommandsForGame(String gameName) throws DatabaseException {

        String sql = "SELECT Command FROM Commands WHERE GameName = ?";

        ArrayList<String> commandList = null;

        try {
            PreparedStatement stmt = null;
            try {
                System.out.println("attempting to get commands");
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, gameName);
                ResultSet rs = stmt.executeQuery();

                if (rs.isBeforeFirst()){
                    // loop through result set and add command to list
                    commandList = new ArrayList<>();
                    while(rs.next()){
                        String blob = rs.getString("Command");
                        commandList.add(blob);
                    }
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to get Commands for " + gameName, e);
        }
        return commandList;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }


}
