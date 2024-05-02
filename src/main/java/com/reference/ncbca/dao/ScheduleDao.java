package com.reference.ncbca.dao;

import com.reference.ncbca.model.Game;
import com.reference.ncbca.model.ScheduleGame;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ScheduleDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/schedule.db";
    private static final String INSERT_SQL = "INSERT INTO Schedule (game_id, season, home_team_id, away_team_id, home_team_name, away_team_name) VALUES (?, ?, ?, ?, ?, ?)";

    public void load(List<ScheduleGame> games) {

        try (Connection conn = this.connect()) {
            for (ScheduleGame game : games) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                pstmt.setInt(1, game.gameId());
                pstmt.setInt(2, game.season());
                pstmt.setInt(3, game.homeTeamId());
                pstmt.setInt(4, game.awayTeamId());
                pstmt.setString(5, game.homeTeamName());
                pstmt.setString(6, game.awayTeamName());
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
