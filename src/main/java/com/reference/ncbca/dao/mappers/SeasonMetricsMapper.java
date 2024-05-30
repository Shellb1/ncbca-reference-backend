package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.SeasonMetrics;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SeasonMetricsMapper {

    public List<SeasonMetrics> mapResult(ResultSet resultSet) throws SQLException {
        List<SeasonMetrics> resultList = new ArrayList<>();

        while (resultSet.next()) {
            String teamName = resultSet.getString("team_name");
            Integer teamId = resultSet.getInt("team_id");
            Integer season = resultSet.getInt("season");
            double rpi = resultSet.getDouble("rpi");
            double sos = resultSet.getDouble("sos");
            resultList.add(new SeasonMetrics(teamName, teamId, season, rpi, sos));
        }
        return resultList;

    }

}
