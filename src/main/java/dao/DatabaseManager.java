package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static String URL = "jdbc:mysql://localhost:3306/Platinum_Masters";
    private static String USER = "root";
    private static String PASSWORD = "Isen44";

    public static Connection getConnection(){
        Connection connection = null;

        try{
            // We try to connect here and return the connection open
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Successfully connected to the bdd");
        }
        catch (SQLException e){
            System.out.println("Failed to connect to the database." + e.getMessage());
        }
        // The connection must remain open so the DAO classes can use it.
        return connection;
    }
}
