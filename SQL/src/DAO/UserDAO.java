package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO {
    /**
     * Handles all interaction
     * with the user table
     * in the database
     */
    Connection conn;

    public UserDAO(){}

    public boolean verifyPassword(String username, String password) {
        /**
         * get the specified user from the user table
         * given the username
         */
        String sql = "SELECT Password FROM Users WHERE Username = ?";

        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.isBeforeFirst()){
                    String pwd = rs.getString("Password");

                    if (pwd.equals(password)){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    System.out.println("Could not find user to verify password");
                    return false;
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to get user " + username + " to verify password. " + e.getMessage());
            return false;
        }

    }
    public boolean addNewUser(String username, String password){
        /**
         * create the user in the database
         */
        String sql = "INSERT INTO Users (Username, Password) VALUES (?,?);";
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);

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
            System.out.println("Failed to create user " + username + " " + password);
            return false;
        }
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}