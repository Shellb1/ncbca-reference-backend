package com.reference.ncbca.dao;

import com.reference.ncbca.model.Season;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SeasonsDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/seasons.db";
    private static final String INSERT_STRING = "INSERT INTO Seasons (team_id, team_name, games_won, games_lost, season) VALUES (?, ?, ?, ?, ?)";

    public void load(List<Season> seasons) {

        try (Connection conn = this.connect()) {
            for (Season season: seasons) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_STRING);
                pstmt.setInt(1, season.teamId());
                pstmt.setString(2, season.teamName());
                pstmt.setInt(3, season.gamesWon());
                pstmt.setInt(4, season.gamesLost());
                pstmt.setInt(5, season.seasonYear());
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
