package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.PostseasonGame;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostseasonMapper {

    public List<PostseasonGame> mapPostseasonGameResults(ResultSet resultSet) throws SQLException {
        List<PostseasonGame> games = new ArrayList<>();

        while (resultSet.next()) {
            Integer gameId = resultSet.getInt("game_id");
            Integer season = resultSet.getInt("season");
            Integer winningTeamId = resultSet.getInt("winning_team_id");
            Integer losingTeamId = resultSet.getInt("losing_team_id");
            Integer winningTeamScore = resultSet.getInt("winning_team_score");
            Integer losingTeamScore = resultSet.getInt("losing_team_score");
            String winningTeamName = resultSet.getString("winning_team_name");
            String losingTeamName = resultSet.getString("losing_team_name");
            String gameType = resultSet.getString("game_type");

            PostseasonGame postseasonGame = new PostseasonGame(gameId, season, winningTeamId, losingTeamId, winningTeamScore, losingTeamScore, winningTeamName, losingTeamName, gameType);
            games.add(postseasonGame);
        }
        return games;
    }
}
