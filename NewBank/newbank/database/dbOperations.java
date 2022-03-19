package newbank.database;

import org.sqlite.SQLiteException;

import java.sql.*;

public class dbOperations {

    // for inserting data into the database table.
    public static void insert(String username, String password) {
        String sql = "INSERT into users(username, password) VALUES(?,?)";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
            System.out.println("\"" + username + "\"" + " added to database.");

        } catch (SQLiteException e) {
            System.err.println("\"" + username + "\"" + " already in database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // for deleting data from the database table
    public static void delete(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.execute();
            System.out.println("\"" + username + "\"" + " removed from the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // query the database - returns true if username and password match.
    public static boolean checkLogin(String username, String password) {
        String sqlQuery = "SELECT * FROM users where username =? and password =?";
        int count = 0;
        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sqlQuery)) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                count++;
            }
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
