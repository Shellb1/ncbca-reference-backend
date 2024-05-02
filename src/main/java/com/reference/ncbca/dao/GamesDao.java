package com.reference.ncbca.dao;

import com.reference.ncbca.model.Game;
import com.reference.ncbca.model.Season;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GamesDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/games.db";
    private static final String INSERT_SQL = "INSERT INTO Games (game_id, season, neutral_site, home_team_id, away_team_id, home_team_score, away_team_score, home_team_name, away_team_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public void load(List<Game> games) {

        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (Game game : games) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                pstmt.setInt(1, game.gameId());
                pstmt.setInt(2, game.season());
                pstmt.setBoolean(3, game.neutralSite());
                pstmt.setInt(4, game.homeTeamId());
                pstmt.setInt(5, game.awayTeamId());
                pstmt.setInt(6, game.homeTeamScore());
                pstmt.setInt(7, game.awayTeamScore());
                pstmt.setString(8, game.homeTeamName());
                pstmt.setString(9, game.awayTeamName());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
