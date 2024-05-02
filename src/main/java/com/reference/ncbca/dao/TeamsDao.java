package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.TeamResultMapper;
import com.reference.ncbca.model.Team;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class TeamsDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/teams.db";
    private static final String INSERT_SQL = "INSERT INTO teams(team_id,team_name,conference_id,conference_name,coach) VALUES(?,?,?,?,?)";
    private static final String GET_BY_ID_SQL = "SELECT * FROM teams WHERE team_id = ?";

    private final TeamResultMapper mapper;

    public TeamsDao(TeamResultMapper mapper) {
        this.mapper = mapper;
    }

    public void insert(List<Team> teams) {
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (Team team : teams) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                pstmt.setInt(1, team.teamId());
                pstmt.setString(2, team.name());
                pstmt.setInt(3, team.conferenceId());
                pstmt.setString(4, team.conferenceName());
                if (team.coach() != null) {
                    pstmt.setString(5, team.coach());
                } else {
                    pstmt.setNull(5, Types.VARCHAR);
                }
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Team get(Integer teamId) {
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(GET_BY_ID_SQL);
            pstmt.setInt(1, teamId);
            ResultSet result = pstmt.executeQuery();
            List<Team> results = mapper.mapResult(result);
            if (!results.isEmpty()) {
                return results.getFirst();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
