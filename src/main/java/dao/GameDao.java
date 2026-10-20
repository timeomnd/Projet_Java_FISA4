package dao;

import models.dto.GameDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDao {

    // Fetch the global catalog of games
    public List<GameDto> getAllGames() {
        List<GameDto> gamesList = new ArrayList<>();
        String query = "SELECT id, title, cover FROM games ORDER BY title ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String cover = rs.getString("cover");

                gamesList.add(new GameDto(id, title, cover));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching games catalog: " + e.getMessage());
        }
        return gamesList;
    }
}
