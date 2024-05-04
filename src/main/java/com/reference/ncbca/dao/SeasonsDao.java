package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.SeasonsMapper;
import com.reference.ncbca.model.Season;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SeasonsDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/seasons.db";
    private static final String INSERT_SQL = "INSERT OR REPLACE INTO seasons (team_id, team_name, coach_name, games_won, games_lost, season) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_SEASONS_BY_YEAR_SQL = "SELECT * FROM Seasons WHERE season = ?";
    private static final String FIND_SEASONS_BY_COACH_SQL = "SELECT * FROM Seasons WHERE coach_name = ?";
    private static final String GET_SEASON_BY_TEAM_AND_YEAR_SQL = "SELECT * FROM Seasons WHERE team_name = ? AND season = ?";

    private final SeasonsMapper mapper;

    public SeasonsDao(SeasonsMapper mapper) {
        this.mapper = mapper;
    }
    public void load(List<Season> seasons) {

        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (Season season : seasons) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
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
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Season> findSeasonsByYear(Integer year) {
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_SEASONS_BY_YEAR_SQL);
            preparedStatement.setInt(1, year);
            ResultSet results = preparedStatement.executeQuery();
            return mapper.mapResult(results);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Season> findSeasonsByCoachName(String coachName) {
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

    public Season getSeasonForTeamAndYear(String teamName, Integer year) {
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement preparedStatement = conn.prepareStatement(GET_SEASON_BY_TEAM_AND_YEAR_SQL);
            preparedStatement.setString(1, teamName);
            preparedStatement.setInt(2, year);
            ResultSet result = preparedStatement.executeQuery();
            return mapper.mapSingleResult(result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
