package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.CoachesMapper;
import com.reference.ncbca.model.dao.Coach;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class CoachesDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO Coaches (coach_name, start_season, end_season, active, current_team) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_COACH_FROM_TEAM_NAME_SQL = "SELECT * FROM Coaches WHERE active = 1 AND current_team = ?";
    private static final String GET_ALL_COACHES_SQL = "SELECT * FROM Coaches";
    private static final String UPDATE_SQL = "UPDATE Coaches "
            + "SET active = ?, current_team = ? "
            + "WHERE coach_name = ?";
    private final CoachesMapper mapper;

    public CoachesDao(CoachesMapper mapper) {
        this.mapper = mapper;
    }
    public void load(List<Coach> coachList) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
            for (Coach coach : coachList) {
                pstmt.setString(1, coach.coachName());
                pstmt.setInt(2, coach.startSeason());
                if (coach.endSeason() != null) {
                    pstmt.setObject(3, coach.endSeason());
                } else {
                    pstmt.setNull(3, Types.INTEGER);
                }
                pstmt.setBoolean(4, coach.active());
                pstmt.setString(5, coach.currentTeam());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Coach getCoachFromTeam(String teamName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(GET_COACH_FROM_TEAM_NAME_SQL);
            pstmt.setString(1, teamName);
            ResultSet results = pstmt.executeQuery();
            List<Coach> coaches = mapper.mapResult(results);
            pstmt.close();
            if (coaches.size() == 1) {
                return coaches.getFirst();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Coach> getAllCoaches() {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING); PreparedStatement pstmt = conn.prepareStatement(GET_ALL_COACHES_SQL);) {
            ResultSet results = pstmt.executeQuery();
            List<Coach> coaches = mapper.mapResult(results);
            pstmt.close();
            return coaches;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void updateCoaches(List<Coach> coaches) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING); PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            for (Coach coach : coaches) {
                pstmt.setBoolean(1, true);
                pstmt.setString(2, coach.currentTeam());
                pstmt.setString(3, coach.coachName());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
