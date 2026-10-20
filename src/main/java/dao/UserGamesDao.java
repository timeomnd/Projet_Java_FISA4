package dao;
import models.dto.GameDetailsDto;
import models.dto.UserGameDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserGamesDao {
    public boolean saveUserGame(UserGameDto userGame) {
        String query = "INSERT INTO user_games (id_authentication, id_games, played, platinum, `100%`, wishlist_games, wishlist_platinum, game_rating, platinum_difficulty, time_played_hours) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "played = VALUES(played), platinum = VALUES(platinum), `100%` = VALUES(`100%`), " +
                "wishlist_games = VALUES(wishlist_games), wishlist_platinum = VALUES(wishlist_platinum), " +
                "game_rating = VALUES(game_rating), platinum_difficulty = VALUES(platinum_difficulty), time_played_hours = VALUES(time_played_hours)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userGame.getIdAuthentication());
            stmt.setInt(2, userGame.getIdGames());
            stmt.setBoolean(3, userGame.isPlayed());
            stmt.setBoolean(4, userGame.isPlatinum());
            stmt.setBoolean(5, userGame.is100Percent());
            stmt.setBoolean(6, userGame.isWishlistGames());
            stmt.setBoolean(7, userGame.isWishlistPlatinum());

            // Handle potential null values via the DTO getters
            if (userGame.getGameRating() != null) {
                stmt.setInt(8, userGame.getGameRating());
            } else {
                stmt.setNull(8, java.sql.Types.SMALLINT);
            }

            if (userGame.getPlatinumDifficulty() != null) {
                stmt.setInt(9, userGame.getPlatinumDifficulty());
            } else {
                stmt.setNull(9, java.sql.Types.SMALLINT);
            }

            if (userGame.getTimePlayedHours() != null) {
                stmt.setDouble(10, userGame.getTimePlayedHours());
            } else {
                stmt.setNull(10, java.sql.Types.DECIMAL);
            }

            stmt.executeUpdate();
            System.out.println("Game successfully saved/updated via DTO.");
            return true;

        } catch (SQLException e) {
            System.out.println("Error while saving the user's game: " + e.getMessage());
        }
        return false;
    }
    // Method to fetch all details needed for the Game Stats Window
    public GameDetailsDto getUserGameDetails(int idAuthentication, int idGames) {
        // Query joining both tables to get the game's static info and the user's specific stats
        String query = "SELECT g.title, g.cover, ug.played, ug.platinum, ug.`100%`, " +
                "ug.game_rating, ug.platinum_difficulty, ug.time_played_hours " +
                "FROM user_games ug " +
                "JOIN games g ON ug.id_games = g.id " +
                "WHERE ug.id_authentication = ? AND ug.id_games = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idAuthentication);
            stmt.setInt(2, idGames);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Extract data from the result set
                    String title = rs.getString("title");
                    String cover = rs.getString("cover");
                    boolean played = rs.getBoolean("played");
                    boolean platinum = rs.getBoolean("platinum");
                    boolean is100Percent = rs.getBoolean("100%");

                    // Handle potential null values for numbers using getObject
                    Integer rating = (Integer) rs.getObject("game_rating");
                    Integer difficulty = (Integer) rs.getObject("platinum_difficulty");
                    // Using Number to safely cast decimals from SQL to Double in Java
                    Number timeNumber = (Number) rs.getObject("time_played_hours");
                    Double timePlayed = (timeNumber != null) ? timeNumber.doubleValue() : null;

                    // Return the fully populated DTO to the frontend
                    return new GameDetailsDto(title, cover, played, platinum, is100Percent, rating, difficulty, timePlayed);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching game details: " + e.getMessage());
        }

        // Return null if the user hasn't added this game to their list yet
        return null;
    }
    // Checks if the user has already marked this specific game as 'played'
    public boolean isGameAlreadyPlayed(int idAuthentication, int idGames) {
        String query = "SELECT played FROM user_games WHERE id_authentication = ? AND id_games = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idAuthentication);
            stmt.setInt(2, idGames);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Return the true/false value of the 'played' column
                    return rs.getBoolean("played");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking if game is played: " + e.getMessage());
        }
        // If the row doesn't exist, the game hasn't been played
        return false;
    }
}
