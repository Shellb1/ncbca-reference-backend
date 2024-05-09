package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.GamesMapper;
import com.reference.ncbca.model.Game;
import com.reference.ncbca.model.Season;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class GamesDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO Games (game_id, season, neutral_site, home_team_id, away_team_id, home_team_name, away_team_name, winning_team_id, winning_team_name, winning_team_score, losing_team_id, losing_team_name, losing_team_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_GAMES_FOR_TEAM_BY_YEAR_SQL = "SELECT * FROM Games WHERE (home_team_name = ? OR away_team_name = ?) AND season = ?";
    private static final String GET_LATEST_GAME_ID_FOR_SEASON = "SELECT MAX(game_id) AS largest_id FROM Games WHERE season = ?";
    private static final String GET_ALL_EXISTING_GAMES_FOR_SEASON = "SELECT * FROM Games WHERE season = ?";
    private final GamesMapper mapper;

    public GamesDao(GamesMapper mapper) {
        this.mapper = mapper;
    }
    public List<Game> getGamesForTeamByYear(String teamName, Integer year) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
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

    public void load(List<Game> games, Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);

            List<Game> existingGames = getAllExistingGamesForSeason(season);
            for (Game game : games) {
                // Check if the game already exists in the database
                if (!isGameExists(game.gameId(), game.season(), existingGames)) {
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
                    pstmt.addBatch();
                } else {
                    System.out.println("Game with ID " + game.gameId() + " already exists in the database. Skipping insertion.");
                }
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isGameExists(Integer gameId, Integer season, List<Game> existingGames) {
        for (Game game: existingGames) {
            if (game.gameId() == gameId && game.season() == season) {
                return true;
            }
        }
        return false;
    }

    public Integer getLatestGameForSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement statement = connection.prepareStatement(GET_LATEST_GAME_ID_FOR_SEASON); {
            statement.setInt(1, season);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapper.mapResultForLatestGameId(resultSet);
            }
        }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Game> getAllExistingGamesForSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING); PreparedStatement statement = connection.prepareStatement(GET_ALL_EXISTING_GAMES_FOR_SEASON)) {
            statement.setInt(1, season);
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResult(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
