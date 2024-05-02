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
    private static final String INSERT_SQL = "INSERT INTO Games (game_id, season, neutral_site, home_team_id, away_team_id, home_team_name, away_team_name, winning_team_id, winning_team_name, winning_team_score, losing_team_id, losing_team_name, losing_team_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public void load(List<Game> games) {

        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (Game game : games) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                pstmt.setInt(1, game.gameId());
                pstmt.setInt(2, game.season());
                pstmt.setBoolean(3, game.neutralSite());
                pstmt.setInt(4, game.homeTeamId());
                pstmt.setInt(5, game.awayTeamId());
                pstmt.setString(6, game.homeTeamName());
                pstmt.setString(7, game.awayTeamName());
                pstmt.setInt(8, game.winningTeamId());
                pstmt.setString(9, game.winningTeamName());
                pstmt.setInt(   10, game.winningTeamScore());
                pstmt.setInt(11, game.losingTeamId());
                pstmt.setString(12, game.losingTeamName());
                pstmt.setInt(13, game.losingTeamScore());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
