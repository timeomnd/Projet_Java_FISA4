package dao;

import models.dto.GameDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameDao {

    public List<GameDto> getAllGames() {
        List<GameDto> gamesList = new ArrayList<>();
        String query = "SELECT id, title, cover FROM games ORDER BY title ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                gamesList.add(new GameDto(rs.getInt("id"), rs.getString("title"), rs.getString("cover")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching games catalog: " + e.getMessage());
        }
        return gamesList;
    }

    // New method to insert a custom game and return its new generated ID
    public int addCustomGame(String title, String coverPath) {
        String query = "INSERT INTO games (title, cover) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, title);
            stmt.setString(2, coverPath);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding new game: " + e.getMessage());
        }
        return -1;
    }
}