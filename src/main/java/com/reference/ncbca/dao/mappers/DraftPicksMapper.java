package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.dao.DraftPick;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DraftPicksMapper {

    public List<DraftPick> mapResult(ResultSet resultSet) throws SQLException {
        List<DraftPick> resultList = new ArrayList<>();

        while (resultSet.next()) {
            String playerName = resultSet.getString("player_name");
            String collegeTeamName = resultSet.getString("college_team_name");
            int round = resultSet.getInt("round");
            int pick = resultSet.getInt("pick");
            int season = resultSet.getInt("season");
            String position = resultSet.getString("position");
            String coachName = resultSet.getString("coach_name");
            resultList.add(new DraftPick(playerName, collegeTeamName, round, pick, season, position, coachName));
        }

        return resultList;
    }
}
