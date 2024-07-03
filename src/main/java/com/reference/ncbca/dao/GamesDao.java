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
    private static final String GET_ALL_GAMES_FOR_TEAM_IN_SEASON = "SELECT * FROM Games WHERE winning_team_name = ? OR losing_team_name = ? AND season = ?";
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
                if (!isGameExists(game.getGameId(), game.getSeason(), existingGames)) {
                    pstmt.setInt(1, game.getGameId());
                    pstmt.setInt(2, game.getSeason());
                    pstmt.setBoolean(3, game.getNeutralSite());
                    pstmt.setInt(4, game.getHomeTeamId());
                    pstmt.setInt(5, game.getAwayTeamId());
                    pstmt.setString(6, game.getHomeTeamName());
                    pstmt.setString(7, game.getAwayTeamName());
                    pstmt.setInt(8, game.getWinningTeamId());
                    pstmt.setString(9, game.getWinningTeamName());
                    pstmt.setInt(10, game.getWinningTeamScore());
                    pstmt.setInt(11, game.getLosingTeamId());
                    pstmt.setString(12, game.getLosingTeamName());
                    pstmt.setInt(13, game.getLosingTeamScore());
                    pstmt.setString(14, game.getWinningCoachName());
                    pstmt.setString(15, game.getLosingCoachName());
                    pstmt.setString(16, game.getGameType());
                    pstmt.addBatch();

                } else {
                    System.out.println("Game with ID " + game.getGameId() + " already exists in the database. Skipping insertion.");
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
            if (game.getGameId().equals(gameId) && game.getSeason().equals(season)) {
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
                statement.setBoolean(1, game.getNeutralSite());
                statement.setInt(2, game.getHomeTeamId());
                statement.setInt(3, game.getAwayTeamId());
                statement.setString(4, game.getHomeTeamName());
                statement.setString(5, game.getAwayTeamName());
                statement.setInt(6, game.getWinningTeamId());
                statement.setString(7, game.getWinningTeamName());
                statement.setInt(8, game.getWinningTeamScore());
                statement.setInt(9, game.getLosingTeamId());
                statement.setString(10, game.getLosingTeamName());
                statement.setInt(11, game.getLosingTeamScore());
                statement.setString(12, game.getWinningCoachName());
                statement.setString(13, game.getLosingCoachName());
                statement.setString(14, game.getGameType());
                statement.setInt(15, game.getGameId());
                statement.setInt(16, game.getSeason());
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


    public List<Game> getAllGamesForTeamInSeason(String teamName, Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_GAMES_FOR_TEAM_IN_SEASON)) {
            statement.setString(1, teamName);
            statement.setString(2, teamName);
            statement.setInt(3, season);
            ResultSet resultSet = statement.executeQuery();
            return mapper.mapResult(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }
}
