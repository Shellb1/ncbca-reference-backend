package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.dao.Season;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SeasonsMapper {
    public List<Season> mapResult(ResultSet resultSet) throws SQLException {
        List<Season> resultList = new ArrayList<>();

        while (resultSet.next()) {
            Integer teamId = resultSet.getInt("team_id");
            Integer seasonYear = resultSet.getInt("season");
            String coachName = resultSet.getString("coach_name");
            String teamName = resultSet.getString("team_name");
            Integer gamesWon = resultSet.getInt("games_won");
            Integer gamesLost = resultSet.getInt("games_lost");
            String conferenceName = resultSet.getString("conference_name");
            Season season = new Season(teamId, teamName, gamesWon, gamesLost, seasonYear, coachName, conferenceName, null);
            resultList.add(season);
        }
        return resultList;
    }

    public Season mapSingleResult(ResultSet resultSet) throws SQLException {
        Integer teamId = resultSet.getInt("team_id");
        Integer seasonYear = resultSet.getInt("season");
        String coachName = resultSet.getString("coach_name");
        String teamName = resultSet.getString("team_name");
        Integer gamesWon = resultSet.getInt("games_won");
        Integer gamesLost = resultSet.getInt("games_lost");
        String conferenceName = resultSet.getString("conference_name");
        return new Season(teamId, teamName, gamesWon, gamesLost, seasonYear, coachName, conferenceName, null);
    }
}
