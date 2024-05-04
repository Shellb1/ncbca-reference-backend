package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.GamesMapper;
import com.reference.ncbca.model.Game;
import com.reference.ncbca.model.Season;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collections;
import java.util.List;

@Repository
public class GamesDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/games.db";
    private static final String INSERT_SQL = "INSERT INTO Games (game_id, season, neutral_site, home_team_id, away_team_id, home_team_name, away_team_name, winning_team_id, winning_team_name, winning_team_score, losing_team_id, losing_team_name, losing_team_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_GAMES_FOR_TEAM_BY_YEAR_SQL = "SELECT * FROM Games WHERE (home_team_name = ? OR away_team_name = ?) AND season = ?";
    private static final String GET_LATEST_GAME_ID_FOR_SEASON = "SELECT MAX(game_id) AS largest_id FROM Games WHERE season = ?";
    private final GamesMapper mapper;

    public GamesDao(GamesMapper mapper) {
        this.mapper = mapper;
    }
    public List<Game> getGamesForTeamByYear(String teamName, Integer year) {
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_GAMES_FOR_TEAM_BY_YEAR_SQL)) {
            statement.setString(1, teamName);
            statement.setString(2, teamName);
            statement.setInt(3, year);
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResult(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void load(List<Game> games) {

        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (Game game : games) {
                // Check if the game already exists in the database
                if (!isGameExists(conn, game.gameId(), game.season())) {
                    PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                    pstmt.setInt(1, game.gameId());
                    pstmt.setInt(2, game.season());
                    pstmt.setBoolean(3, game.neutralSite());
                    pstmt.setInt(4, game.homeTeamId());
                    pstmt.setInt(5, game.awayTeamId());
                    pstmt.setString(6, game.homeTeamName());
                    pstmt.setString(7, game.awayTeamName());
                    pstmt.setInt(8, game.winningTeamId());
                    pstmt.setString(9, game.winningTeamName());
                    pstmt.setInt(10, game.winningTeamScore());
                    pstmt.setInt(11, game.losingTeamId());
                    pstmt.setString(12, game.losingTeamName());
                    pstmt.setInt(13, game.losingTeamScore());
                    pstmt.executeUpdate();
                    pstmt.close();
                } else {
                    System.out.println("Game with ID " + game.gameId() + " already exists in the database. Skipping insertion.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Integer getLatestGameForSeason(Integer season) {
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_LATEST_GAME_ID_FOR_SEASON)) {
            statement.setInt(1, season);
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResultForLatestGameId(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private boolean isGameExists(Connection conn, int gameId, int season) throws SQLException {
        String query = "SELECT COUNT(*) FROM Games WHERE game_id = ? AND season = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, gameId);
            pstmt.setInt(2, season);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

}
