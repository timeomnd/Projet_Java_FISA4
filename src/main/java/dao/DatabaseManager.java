package dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {
    private static String url = "jdbc:mysql://localhost:3306/Platinum_Masters";
    private static String user = "root";
    private static String password = "Isen44";

    Connection connection = null;

    try{
        connection = DriverManager.getConnection(url, user, password);
        
    }
}
