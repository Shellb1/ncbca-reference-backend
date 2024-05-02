package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.Coach;
import com.reference.ncbca.model.Team;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CoachesMapper {

    public List<Coach> mapResult(ResultSet resultSet) throws SQLException {
        List<Coach> resultList = new ArrayList<>();

        while (resultSet.next()) {
            String coachName = resultSet.getString("coach_name");
            Integer start_season = resultSet.getInt("start_season");
            Integer end_season = resultSet.getInt("end_season");
            Boolean active = resultSet.getBoolean("active");
            String currentTeam = resultSet.getString("current_team");
            resultList.add(new Coach(coachName, start_season, end_season, active, currentTeam));
        }

        return resultList;
    }
}
