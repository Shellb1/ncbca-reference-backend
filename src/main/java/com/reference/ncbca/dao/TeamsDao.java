package com.reference.ncbca.dao;

import com.reference.ncbca.model.Team;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TeamsDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/teams.db";
    private static final String INSERT_SQL = "INSERT INTO teams(team_id,team_name,conference_id,conference_name,coach) VALUES(?,?,?,?,?)";
    public void insert(List<Team> teams) {

        try (Connection conn = this.connect()) {
            for (Team team: teams) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                pstmt.setInt(1, team.teamId());
                pstmt.setString(2, team.name());
                pstmt.setInt(3, team.conferenceId());
                pstmt.setString(4, team.conferenceName());
                pstmt.setString(5, team.coach());
                pstmt.executeUpdate();
                pstmt.close();
            }
            } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
