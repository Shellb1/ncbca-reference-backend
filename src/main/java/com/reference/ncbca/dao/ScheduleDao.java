package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.ScheduleMapper;
import com.reference.ncbca.model.dao.ScheduleGame;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class ScheduleDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO Schedule (game_id, season, home_team_id, away_team_id, home_team_name, away_team_name) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String LIST_ALL_SQL = "SELECT * FROM Schedule WHERE season = ?";

    private final ScheduleMapper mapper;

    public ScheduleDao(ScheduleMapper mapper) {
        this.mapper = mapper;
    }

    public void load(List<ScheduleGame> games) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
            for (ScheduleGame game : games) {
                pstmt.setInt(1, game.gameId());
                pstmt.setInt(2, game.season());
                pstmt.setInt(3, game.homeTeamId());
                pstmt.setInt(4, game.awayTeamId());
                pstmt.setString(5, game.homeTeamName());
                pstmt.setString(6, game.awayTeamName());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<ScheduleGame> getEntireSchedule(Integer year) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(LIST_ALL_SQL);
            pstmt.setInt(1, year);
            ResultSet results = pstmt.executeQuery();
            return mapper.mapResult(results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
