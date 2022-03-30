package newbank.database;

import org.mindrot.jbcrypt.BCrypt;
// jbcrypt library helps take passwords and store them as hashes in the database
// using a cryptographic hash algorithm called 'bcrypt'.
import org.sqlite.SQLiteException;

import java.sql.*;

public class dbCreateOperations {
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
        userId = dbReadOperations.getUserId(username);

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
                return true;

            } else {
                con.rollback(); // reverse changes made to the db
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
