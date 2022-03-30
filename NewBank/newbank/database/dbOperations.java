package newbank.database;

import newbank.server.Account;
import newbank.server.Customer;
// jbcrypt library helps take passwords and store them as hashes in the database
// using a cryptographic hash algorithm called 'bcrypt'.
import org.mindrot.jbcrypt.BCrypt;
import org.sqlite.SQLiteException;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class dbOperations {

    // for creating a user account
    public static boolean insert(String username, String password) {
        // hash the password first for security, then store the hash in the database
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT into users(username, password) VALUES(?,?)";

        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.toLowerCase());
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

    // for opening a new bank account and linking that account to a user
    public static boolean addAccount(String username, String accountName, double startingBalance) {
        int accountId = -1;
        int userId;

        // get user id
        userId = getUserId(username);

        String sql = "INSERT into accounts(type, balance) VALUES(?,?)";
        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            con.setAutoCommit(false);
            ps.setString(1, accountName);
            ps.setDouble(2, startingBalance);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                accountId = rs.getInt(1);
            }

            // link user and account and put into mapping table
            if (accountId > 0 && userId > 0) {
                String mappingSql = "INSERT into accountmappings(userid, accountid) VALUES(?,?)";
                PreparedStatement link = con.prepareStatement(mappingSql);
                link.setInt(1, userId);
                link.setInt(2, accountId);
                link.executeUpdate();
                con.commit(); // save changes made to db
                System.out.println("For: " + username + ", a new " + accountName + " account has been opened with starting balance of " + startingBalance);

            } else {
                con.rollback(); // reverse changes made to the db
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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


    // used to insert the test customers into database
    public static void main(String[] args) {
//        insert("Bhagy", "password1");
//        insert("John", "password1");
//        insert("Christina", "password1");

        //addAccount("Bhagy", "savings", 30000);
        // addAccount("christina", "savings", 23300);

        //getAccounts("bhagy");
    }

}
