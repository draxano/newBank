package newbank.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dbDeleteOperations {

    // for deleting a user from the database table
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

    // method to delete a user account
    public static boolean deleteAccount(int accountId) {
        if (accountId < 0) return false;

        boolean success = false;
        String sqlDelete = "DELETE FROM accounts WHERE id =?";
        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, accountId);
            ps.executeUpdate();
            success = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}
