package newbank.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dbUpdateOperations {

    // method to update a user account balance
    public static boolean update(int accountId, double balance) {
        if (accountId < 0) return false;

        boolean success = false;
        String sqlUpdateQuery = "UPDATE accounts SET balance =? WHERE id =?";
        try (Connection con = dbConnection.connect(); PreparedStatement ps = con.prepareStatement(sqlUpdateQuery)) {
            ps.setDouble(1, balance);
            ps.setInt(2, accountId);
            ps.executeUpdate();
            success = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}
