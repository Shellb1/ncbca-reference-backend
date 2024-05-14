package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.PostseasonMapper;
import com.reference.ncbca.model.PostseasonGame;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class PostseasonDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO postseason_games(game_id, season, winning_team_id, losing_team_id, winning_team_score, losing_team_score, winning_team_name, losing_team_name, game_type) VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String LIST_POSTSEASON_GAMES_SQL = "SELECT * FROM postseason_games WHERE season = ?";

    private final PostseasonMapper postseasonMapper;

    public PostseasonDao(PostseasonMapper postseasonMapper) {
        this.postseasonMapper = postseasonMapper;
    }

    public void insert(List<PostseasonGame> games) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
            for (PostseasonGame postseasonGame : games) {
                pstmt.setInt(1, postseasonGame.gameId());
                pstmt.setInt(2, postseasonGame.season());
                pstmt.setInt(3, postseasonGame.winningTeamId());
                pstmt.setInt(4, postseasonGame.losingTeamId());
                pstmt.setInt(5, postseasonGame.winningTeamScore());
                pstmt.setInt(6, postseasonGame.losingTeamScore());
                pstmt.setString(7, postseasonGame.winningTeamName());
                pstmt.setString(8, postseasonGame.losingTeamName());
                pstmt.setString(9, postseasonGame.gameType());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<PostseasonGame> listPostseasonGamesForSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(LIST_POSTSEASON_GAMES_SQL);
            pstmt.setInt(1, season);
            ResultSet results = pstmt.executeQuery();
            return postseasonMapper.mapPostseasonGameResults(results);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }
}
