package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.GamesMapper;
import com.reference.ncbca.model.dao.Game;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collections;
import java.util.List;

@Repository
public class GamesDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO Games (game_id, season, neutral_site, home_team_id, away_team_id, home_team_name, away_team_name, winning_team_id, winning_team_name, winning_team_score, losing_team_id, losing_team_name, losing_team_score, winning_coach_name, losing_coach_name, game_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_GAMES_FOR_TEAM_BY_YEAR_SQL = "SELECT * FROM Games WHERE (home_team_name = ? OR away_team_name = ?) AND season = ?";
    private static final String GET_LATEST_GAME_ID_FOR_SEASON_SQL = "SELECT MAX(game_id) AS largest_id FROM Games WHERE season = ?";
    private static final String GET_ALL_EXISTING_GAMES_FOR_SEASON_SQL = "SELECT * FROM Games WHERE season = ?";
    private static final String DETERMINE_GAMES_WON_FOR_TEAM_SQL = "SELECT COUNT(*) AS games_won FROM Games WHERE season = ? AND winning_team_id = ?";
    private static final String DETERMINE_GAMES_LOST_FOR_TEAM_SQL = "SELECT COUNT(*) AS games_lost FROM Games WHERE season = ? AND losing_team_id = ?";
    private static final String GET_ALL_TIME_GAMES_PLAYED_IN_SQL = "SELECT * FROM Games WHERE winning_coach_name = ? OR losing_coach_name = ?";
    private static final String GET_ALL_GAMES_SQL = "SELECT * FROM Games";
    private static final String EDIT_GAMES_SQL = "UPDATE Games SET neutral_site = ?, home_team_id = ?, away_team_id = ?, home_team_name = ?, away_team_name = ?, winning_team_id = ?, winning_team_name = ?, winning_team_score = ?, losing_team_id = ?, losing_team_name = ?, losing_team_score = ?, winning_coach_name = ?, losing_coach_name = ?, game_type = ? WHERE game_id = ? AND season = ?";
    private static final String GET_ALL_GAMES_FOR_TEAM = "SELECT * FROM Games WHERE winning_team_name = ? OR losing_team_name = ?";
    private static final String GET_ALL_GAMES_IN_SEASON = "SELECT * FROM Games WHERE season = ?";

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
                    pstmt.setString(14, game.winningCoachName());
                    pstmt.setString(15, game.losingCoachName());
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
        for (Game game : existingGames) {
            if (game.gameId().equals(gameId) && game.season().equals(season)) {
                return true;
            }
        }
        return false;
    }

    public Integer getLatestGameForSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement statement = connection.prepareStatement(GET_LATEST_GAME_ID_FOR_SEASON_SQL);
            statement.setInt(1, season);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapper.mapResultForLatestGameId(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Integer getGamesWonForTeamInSeason(Integer teamId, Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;;

        try (Connection connection = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement statement = connection.prepareStatement(DETERMINE_GAMES_WON_FOR_TEAM_SQL);
            statement.setInt(1, season);
            statement.setInt(2, teamId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("games_won");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Integer getGamesLostForTeamInSeason(Integer teamId, Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;

        try (Connection connection = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement statement = connection.prepareStatement(DETERMINE_GAMES_LOST_FOR_TEAM_SQL);
            statement.setInt(1, season);
            statement.setInt(2, teamId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("games_lost");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<Game> getAllExistingGamesForSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_EXISTING_GAMES_FOR_SEASON_SQL)) {
            statement.setInt(1, season);
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResult(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Game> getAllGamesCoachParticipatedIn(String coachName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_TIME_GAMES_PLAYED_IN_SQL)) {
            statement.setString(1, coachName);
            statement.setString(2, coachName);
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResult(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Game> listAll() {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_GAMES_SQL)) {
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResult(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void backload(List<Game> games) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING); PreparedStatement statement = connection.prepareStatement(EDIT_GAMES_SQL)) {
            for (Game game: games) {
                statement.setBoolean(1, game.neutralSite());
                statement.setInt(2, game.homeTeamId());
                statement.setInt(3, game.awayTeamId());
                statement.setString(4, game.homeTeamName());
                statement.setString(5, game.awayTeamName());
                statement.setInt(6, game.winningTeamId());
                statement.setString(7, game.winningTeamName());
                statement.setInt(8, game.winningTeamScore());
                statement.setInt(9, game.losingTeamId());
                statement.setString(10, game.losingTeamName());
                statement.setInt(11, game.losingTeamScore());
                statement.setString(12, game.winningCoachName());
                statement.setString(13, game.losingCoachName());
                statement.setString(14, game.gameType());
                statement.setInt(15, game.gameId());
                statement.setInt(16, game.season());
                statement.addBatch();
            }
            int[] updates = statement.executeBatch();
            System.out.println(updates.length);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Game> getAllGamesForTeam(String teamName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_GAMES_FOR_TEAM)) {
            statement.setString(1, teamName);
            statement.setString(2, teamName);
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResult(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Game> getAllGamesInSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_GAMES_IN_SEASON)) {
            statement.setInt(1, season);
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResult(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }



}
