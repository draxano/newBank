package newbank.database;

import newbank.server.Customer;
// jbcrypt library helps take passwords and store them as hashes in the database
// using a cryptographic hash algorithm called 'bcrypt'.
import org.mindrot.jbcrypt.BCrypt;
import org.sqlite.SQLiteException;


import java.sql.*;
import java.util.HashMap;

public class dbOperations {

    // for inserting data into the database table.
    public static boolean insert(String username, String password) {
        // hash the password first for security, then store the hash in the database
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT into users(username, password) VALUES(?,?)";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashed);
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
    public static boolean checkLogin(String username, String candidate) {
        // query the database and extract hashed password from database
        String sqlQuery = "SELECT password FROM users where username =?";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sqlQuery)) {
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            // if the user exists then the result set will contain a hashed password
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                // the library method will take care of the bcrypt checking
                if (BCrypt.checkpw(candidate, hashedPassword)) {
                    System.out.println("Correct password input. Login Successful.");
                    return true;
                } else {
                    System.out.println("Incorrect Password input.");
                    return false;
                }
            } else {
                System.out.println("User does not exist.");
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
