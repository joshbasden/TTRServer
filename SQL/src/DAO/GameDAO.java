package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAO {
    /**
     * handles all interaction
     * with the Event table in
     * the database
     */
    Connection conn;

    public GameDAO() {
    }

    public boolean updateGame(String gameName, String game) {
        String sql1 = "Select GameName FROM Games WHERE GameName = ?";
        String sql2 = "UPDATE Games SET Data = ? WHERE GameName = ?;";
        String sql3 = "INSERT INTO Games VALUES(?,?)";
        try {
            PreparedStatement stmt1 = null;
            PreparedStatement stmt2 = null;
            try {
                // check if already exists
                stmt1 = conn.prepareStatement(sql1);
                stmt1.setString(1, gameName);
                ResultSet rs = stmt1.executeQuery();

                if (rs.next()){
                    stmt2 = conn.prepareStatement(sql2);
                    stmt2.setString(1, game);
                    stmt2.setString(2, gameName);
                    stmt2.executeUpdate();
                }
                else{
                    // not in database
                    stmt2 = conn.prepareStatement(sql3);
                    stmt2.setString(1, gameName);
                    stmt2.setString(2, game);
                    stmt2.executeUpdate();
                }
//
                return true;

            } finally {
                if (stmt1 != null) {
                    stmt1.close();
                    stmt1 = null;
                }
                if (stmt2 != null) {
                    stmt2.close();
                    stmt2 = null;
                }

            }

        } catch (SQLException e) {
            System.out.println("Failed to update " + gameName + ". " + e.getMessage());
            return false;
        }
    }



    public ArrayList<String> getGames() {
        String sql = "SELECT * FROM Games";

        ArrayList<String> gameList = null;

        try {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                if (rs.isBeforeFirst()) {
                    gameList = new ArrayList<>();
                    // loop through result set and add games to list
                    while (rs.next()) {
                        gameList.add(rs.getString("Data"));
                    }
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting all the games. " + e.getMessage());
        }

        return gameList;

    }
    public void setConn(Connection conn) {
        this.conn = conn;
    }

}
