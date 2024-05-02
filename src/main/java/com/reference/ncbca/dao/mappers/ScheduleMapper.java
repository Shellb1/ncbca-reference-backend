package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.ScheduleGame;
import com.reference.ncbca.model.Team;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleMapper {

    public List<ScheduleGame> mapResult(ResultSet resultSet) throws SQLException {
        List<ScheduleGame> resultList = new ArrayList<>();

        while (resultSet.next()) {
            Integer gameId = resultSet.getInt("game_id");
            Integer season = resultSet.getInt("season");
            Integer home_team_id = resultSet.getInt("home_team_id");
            Integer away_team_id = resultSet.getInt("away_team_id");
            String homeTeamName = resultSet.getString("home_team_name");
            String awayteamName = resultSet.getString("away_team_name");
            ScheduleGame scheduleGame = new ScheduleGame(gameId, season, home_team_id, away_team_id, homeTeamName, awayteamName);
            resultList.add(scheduleGame);
        }
        return resultList;

    }
}
