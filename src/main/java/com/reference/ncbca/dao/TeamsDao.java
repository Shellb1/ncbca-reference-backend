package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.TeamResultMapper;
import com.reference.ncbca.model.Team;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collections;
import java.util.List;

@Repository
public class TeamsDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO teams(team_id,team_name,conference_id,conference_name,coach) VALUES(?,?,?,?,?)";
    private static final String LIST_ALL_ACTIVE_TEAMS_SQL = "SELECT * FROM teams WHERE active = true";
    private static final String LIST_ALL_TEAMS_SQL = "SELECT * FROM teams";
    private static final String GET_TEAM_SQL = "SELECT * FROM teams WHERE team_name = ?";



    private final TeamResultMapper mapper;

    public TeamsDao(TeamResultMapper mapper) {
        this.mapper = mapper;
    }

    public void insert(List<Team> teams) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
            for (Team team : teams) {
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
            }
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Team> listAllActiveTeams() {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(LIST_ALL_ACTIVE_TEAMS_SQL);
            ResultSet result = pstmt.executeQuery();
            return mapper.mapResult(result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }

    public List<Team> listAllTeams() {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(LIST_ALL_TEAMS_SQL);
            ResultSet result = pstmt.executeQuery();
            return mapper.mapResult(result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }

    public Team getTeam(String teamName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(GET_TEAM_SQL);
            pstmt.setString(1, teamName);
            ResultSet result = pstmt.executeQuery();
            return mapper.mapResult(result).getFirst();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
