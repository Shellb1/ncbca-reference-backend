package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.NTSeed;
import com.reference.ncbca.model.PostseasonGame;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class NTSeedsMapper {

    public List<NTSeed> mapNTSeeds(ResultSet resultSet) throws SQLException {
        List<NTSeed> ntSeeds = new ArrayList<>();

        while (resultSet.next()) {
            Integer teamId = resultSet.getInt("team_id");
            Integer season = resultSet.getInt("season");
            Integer seed = resultSet.getInt("seed");
            String teamName = resultSet.getString("team_name");
            NTSeed ntSeed = new NTSeed(teamId, teamName, season, seed);
            ntSeeds.add(ntSeed);
        }
        return ntSeeds;
    }
}
