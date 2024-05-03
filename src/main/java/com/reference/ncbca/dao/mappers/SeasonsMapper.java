package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.ScheduleGame;
import com.reference.ncbca.model.Season;
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
            Season season = new Season(teamId, teamName, gamesWon, gamesLost, seasonYear, coachName);
            resultList.add(season);
        }
        return resultList;

    }
}
