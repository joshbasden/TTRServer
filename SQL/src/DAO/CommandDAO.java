package DAO;

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

    public boolean addCommand(String gamename, String command) {
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
            System.out.println("Failed to add Command: " + command + " for " + gamename + ". " + e.getMessage());
            return false;
        }
    }

    public boolean clearCommandsForGame(String gamename) {
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
            System.out.println("Failed to clear commands for " + gamename + ". " + e.getMessage());
            return false;
        }

    }

    public int getCommandsLength(String gameName) {
        ArrayList<String> commandList = null;

        try{
            commandList = getCommandsForGame(gameName);
            return commandList.size();

        } catch (Exception e) {
            System.out.println("Error when trying to get command size for " + gameName + ". " + e.getMessage());
            return -1;
        }

    }

    public ArrayList<String> getCommandsForGame(String gameName) {

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
            System.out.println("Failed to get Commands for " + gameName);
            e.printStackTrace();
        }
        return commandList;
    }

    public void clear() throws DataBaseException{
        /**
         * clear the person table
         */

        String sql = "DELETE FROM Persons;";
        try{
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to clear Person table " + e.getMessage());
        }
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }


}
