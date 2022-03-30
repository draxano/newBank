package newbank.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dbDeleteOperations {
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
}
