package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.dao.Game;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GamesMapper {

    public List<Game> mapResult(ResultSet resultSet) throws SQLException {
        List<Game> games = new ArrayList<>();

        while (resultSet.next()) {
            Integer gameId = resultSet.getInt("game_id");
            Integer season = resultSet.getInt("season");
            Boolean neutralSite = resultSet.getBoolean("neutral_site");
            Integer homeTeamId = resultSet.getInt("home_team_id");
            Integer awayTeamId = resultSet.getInt("away_team_id");
            String homeTeamName = resultSet.getString("home_team_name");
            String awayTeamName = resultSet.getString("away_team_name");
            Integer winningTeamId = resultSet.getInt("winning_team_id");
            String winningTeamName = resultSet.getString("winning_team_name");
            Integer winningTeamScore = resultSet.getInt("winning_team_score");
            Integer losingTeamId = resultSet.getInt("losing_team_id");
            String losingTeamName = resultSet.getString("losing_team_name");
            Integer losingTeamScore = resultSet.getInt("losing_team_score");
            String winningCoachName = resultSet.getString("winning_coach_name");
            String losingCoachName = resultSet.getString("losing_coach_name");
            String gameType = resultSet.getString("game_type");
            games.add(new Game.Builder()
                    .gameId(gameId)
                    .season(season)
                    .neutralSite(neutralSite)
                    .homeTeamId(homeTeamId)
                    .awayTeamId(awayTeamId)
                    .homeTeamName(homeTeamName)
                    .awayTeamName(awayTeamName)
                    .winningTeamId(winningTeamId)
                    .winningTeamName(winningTeamName)
                    .winningTeamScore(winningTeamScore)
                    .losingTeamId(losingTeamId)
                    .losingTeamName(losingTeamName)
                    .losingTeamScore(losingTeamScore)
                    .winningCoachName(winningCoachName)
                    .losingCoachName(losingCoachName)
                    .gameType(gameType)
                    .build());

        }

        return games;
    }

    public Integer mapResultForLatestGameId(ResultSet set) throws SQLException {
        return set.getInt("largest_id");
    }
}
