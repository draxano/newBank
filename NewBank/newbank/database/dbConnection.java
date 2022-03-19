package newbank.database;
import java.sql.*;

public class dbConnection {

    // this method establishes a connection to the database.
    // returns a connection object which can be used by other methods to interact with the database.
    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:NewBank/newbank/database/userdatabase.db");
            System.out.println("Connected to the Database.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

}
