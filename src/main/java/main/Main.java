package main;

import dao.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    static void main() {
        System.out.println("Testing database connection...");

        // Try to get a connection from the DatabaseManager
        Connection connection = DatabaseManager.getConnection();

        // Check if the connection is successfully established
        if (connection != null) {
            System.out.println("Test successful! The connection is open and ready to use.");

            // We close the connection immediately since this is just a test
            try {
                connection.close();
                System.out.println("Connection closed successfully after the test.");
            } catch (SQLException e) {
                System.out.println("Error while closing the connection: " + e.getMessage());
            }
        } else {
            System.out.println("Test failed. The connection object is null.");
        }
    }
}
