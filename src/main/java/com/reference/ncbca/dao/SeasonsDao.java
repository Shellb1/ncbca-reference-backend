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
    private static final String INSERT_STRING = "INSERT INTO Seasons (team_id, team_name, coach_name, games_won, games_lost, season) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_SEASONS_BY_YEAR_SQL = "SELECT * FROM Seasons WHERE season = ?";

    private final SeasonsMapper mapper;

    public SeasonsDao(SeasonsMapper mapper) {
        this.mapper = mapper;
    }
    public void load(List<Season> seasons) {

        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (Season season : seasons) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_STRING);
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
}
