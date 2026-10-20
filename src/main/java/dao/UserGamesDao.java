package dao;
import models.dto.UserGameDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
