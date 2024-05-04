package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.NitGame;
import com.reference.ncbca.model.NtGame;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class NtMapper {

    public List<NtGame> mapNtGameResults(ResultSet resultSet) throws SQLException {
        List<NtGame> games = new ArrayList<>();

        while (resultSet.next()) {
            Integer gameId = resultSet.getInt("game_id");
            Integer season = resultSet.getInt("season");
            games.add(new NtGame(gameId, season));
        }
        return games;
    }

}
