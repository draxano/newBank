package newbank.database;

import newbank.server.Customer;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.HashMap;

public class dbOperations {

    // for inserting data into the database table.
    public static boolean insert(String username, String password) {
        String sql = "INSERT into users(username, password) VALUES(?,?)";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
            System.out.println("\"" + username + "\"" + " added to database.");
            return true;

        } catch (SQLiteException e) {
            System.err.println("\"" + username + "\"" + " already in database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // for deleting data from the database table
    public static boolean delete(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.execute();
            System.out.println("\"" + username + "\"" + " removed from the database.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    // The NewBank class contains a HashMap of all customers. This method queries the database and
    // places all users inside a map with a new customer object.

    public static HashMap<String, Customer> loadMap() {
        HashMap<String, Customer> map = new HashMap<>();
        String sqlQuery = "SELECT username FROM users";
        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sqlQuery)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("username"), new Customer());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    // used to insert the test customers into database
    public static void main(String[] args) {
        insert("Bhagy", "password1");
        insert("John", "password1");
        insert("Christina", "password1");
    }

}
