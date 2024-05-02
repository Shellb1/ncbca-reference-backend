package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.CoachesMapper;
import com.reference.ncbca.model.Coach;
import com.reference.ncbca.model.Game;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class CoachesDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/coaches.db";
    private static final String INSERT_SQL = "INSERT INTO Coaches (coach_name, start_season, end_season, active, current_team) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_COACH_FROM_TEAM_NAME_SQL = "SELECT * FROM Coaches WHERE active = 1 AND current_team = ?";

    private final CoachesMapper mapper;

    public CoachesDao(CoachesMapper mapper) {
        this.mapper = mapper;
    }
    public void load(List<Coach> coachList) {
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (Coach coach : coachList) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                pstmt.setString(1, coach.coachName());
                pstmt.setInt(2, coach.startSeason());
                if (coach.endSeason() != null) {
                    pstmt.setObject(3, coach.endSeason());
                } else {
                    pstmt.setNull(3, Types.INTEGER);
                }
                pstmt.setBoolean(4, coach.active());
                pstmt.setString(5, coach.currentTeam());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Coach getCoachFromTeam(String teamName) {
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


}
