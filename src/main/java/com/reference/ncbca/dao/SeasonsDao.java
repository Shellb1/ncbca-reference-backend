package com.reference.ncbca.dao;

import com.reference.ncbca.model.Season;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class SeasonsDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/seasons.db";
    private static final String INSERT_STRING = "INSERT INTO Seasons (team_id, team_name, coach_name, games_won, games_lost, season) VALUES (?, ?, ?, ?, ?, ?)";

    public void load(List<Season> seasons) {

        try (Connection conn = this.connect()) {
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
