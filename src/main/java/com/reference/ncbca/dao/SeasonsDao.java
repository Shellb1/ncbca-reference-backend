package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.SeasonsMapper;
import com.reference.ncbca.model.dao.Season;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SeasonsDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO Seasons (team_id, team_name, coach_name, games_won, games_lost, season) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE team_name = VALUES(team_name), coach_name = VALUES(coach_name), games_won = VALUES(games_won), games_lost = VALUES(games_lost), season = VALUES(season);";
    private static final String FIND_SEASONS_BY_YEAR_SQL = "SELECT * FROM Seasons WHERE season = ?";
    private static final String FIND_SEASONS_BY_COACH_SQL = "SELECT * FROM Seasons WHERE coach_name = ?";
    private static final String FIND_SEASONS_BY_TEAM_SQL = "SELECT * FROM Seasons WHERE team_name = ?";
    private static final String GET_SEASON_BY_TEAM_AND_YEAR_SQL = "SELECT * FROM Seasons WHERE team_name = ? AND season = ?";
    private static final String GET_ALL_SEASONS_SQL = "SELECT * FROM Seasons";
    private static final String GET_MOST_RECENT_YEAR_SQL = "SELECT * FROM Seasons ORDER BY season DESC LIMIT 1";

    private final SeasonsMapper mapper;

    public SeasonsDao(SeasonsMapper mapper) {
        this.mapper = mapper;
    }
    public void load(List<Season> seasons) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
            for (Season season : seasons) {
                pstmt.setInt(1, season.teamId());
                pstmt.setString(2, season.teamName());
                if (season.coach() != null) {
                    pstmt.setString(3, season.coach());
                } else {
                    pstmt.setNull(3, Types.VARCHAR);
                }
                pstmt.setInt(4, season.gamesWon());
                pstmt.setInt(5, season.gamesLost());
                pstmt.setInt(6, season.seasonYear());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Season> findSeasonsByYear(Integer year) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_SEASONS_BY_YEAR_SQL);
            preparedStatement.setInt(1, year);
            ResultSet results = preparedStatement.executeQuery();
            return mapper.mapResult(results);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Season> findSeasonsByCoachName(String coachName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_SEASONS_BY_COACH_SQL);
            preparedStatement.setString(1, coachName);
            ResultSet results = preparedStatement.executeQuery();
            return mapper.mapResult(results);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Season> findSeasonsByTeamName(String teamName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_SEASONS_BY_TEAM_SQL);
            preparedStatement.setString(1, teamName);
            ResultSet results = preparedStatement.executeQuery();
            return mapper.mapResult(results);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Season> listAllSeasons() {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement preparedStatement = conn.prepareStatement(GET_ALL_SEASONS_SQL);
            ResultSet results = preparedStatement.executeQuery();
            return mapper.mapResult(results);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public Season getSeasonForTeamAndYear(String teamName, Integer year) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement preparedStatement = conn.prepareStatement(GET_SEASON_BY_TEAM_AND_YEAR_SQL);
            preparedStatement.setString(1, teamName);
            preparedStatement.setInt(2, year);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
               return mapper.mapSingleResult(result);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Integer determineMostRecentYear() {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement preparedStatement = conn.prepareStatement(GET_MOST_RECENT_YEAR_SQL);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt("season");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}
