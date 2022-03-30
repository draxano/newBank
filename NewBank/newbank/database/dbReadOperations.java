package newbank.database;

import newbank.server.Account;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class dbReadOperations {
    // for getting account information for a given customer.
    // returns an arraylist containing all the user's accounts
    public static ArrayList<Account> getAccounts(String username) {
        ArrayList<Account> accounts = new ArrayList<>();
        String sqlQuery = "SELECT type, balance FROM users a JOIN accountmappings b ON a.id = b.userid " +
                "JOIN accounts c on c.id = b.accountid WHERE username =?";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sqlQuery)) {
            ps.setString(1, username.toLowerCase());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(rs.getString(1), rs.getDouble(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (accounts.isEmpty()) {
            System.err.println("No accounts found for this user.");
        }
        return accounts;
    }

    // query the database - returns true if username and password match.
    public static boolean checkLogin(String username, String candidate) {
        // query the database and extract hashed password from database
        String sqlQuery = "SELECT password FROM users where username =?";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sqlQuery)) {
            ps.setString(1, username.toLowerCase());

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

    // get a user's id from the database
    public static int getUserId(String username) {
        int userId = -1;
        String sqlQuery = "SELECT id FROM users where username =?";
        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sqlQuery)) {
            ps.setString(1, username.toLowerCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt(1);
            } else {
                System.err.println("User does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
