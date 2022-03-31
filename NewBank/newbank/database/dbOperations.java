package newbank.database;

import newbank.server.Customer;



import java.sql.*;
import java.util.HashMap;

public class dbOperations {


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
//        insert("Bhagy", "password1");
//        insert("John", "password1");
//        insert("Christina", "password1");

        //addAccount("Bhagy", "savings", 30000);
        // addAccount("christina", "savings", 23300);


    }

}
