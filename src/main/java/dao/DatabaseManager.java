package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseManager {
    public static Connection getConnection(){
        Connection connection = null;
        Properties properties = new Properties();
        //we try to get the db properties from the db.properties file
        try(InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")){
            if(input == null){
                System.out.println("Unable to find db.properties file.");
                return null;
            }
            properties.load(input); //this can throw IOExceptioin
            Class.forName("com.mysql.cj.jdbc.Driver"); // This can throw a ClassNotFoundException
            // We try to connect here and return the connection open
            connection = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"));
            System.out.println("Successfully connected to the bdd");
        }
        catch (Exception e){
            // Catching 'Exception' handles SQLException, IOException, and ClassNotFoundException
            System.out.println("Failed to connect to the database." + e.getMessage());
        }
        // The connection must remain open so the DAO classes can use it.
        return connection;
    }
}
