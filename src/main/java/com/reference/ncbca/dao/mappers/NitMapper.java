package com.reference.ncbca.dao.mappers;

import com.reference.ncbca.model.Game;
import com.reference.ncbca.model.NitGame;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class NitMapper {

    public List<NitGame> mapResult(ResultSet resultSet) throws SQLException {
        List<NitGame> games = new ArrayList<>();

        while (resultSet.next()) {
            Integer gameId = resultSet.getInt("game_id");
            Integer season = resultSet.getInt("season");
            games.add(new NitGame(gameId, season));
        }
        return games;
    }
}
