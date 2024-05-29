package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.dao.Team;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TeamResultMapper {

    public List<Team> mapResult(ResultSet resultSet) throws SQLException {
        List<Team> resultList = new ArrayList<>();

        while (resultSet.next()) {
            Integer teamId = resultSet.getInt("team_id");
            String teamName = resultSet.getString("team_name");
            Integer conferenceId = resultSet.getInt("conference_id");
            String conferenceName = resultSet.getString("conference_name");
            String coach = resultSet.getString("coach");
            Boolean active = resultSet.getBoolean("active");
            resultList.add(new Team(teamId, teamName, conferenceId, conferenceName, coach, active));
        }

        return resultList;
    }
}
