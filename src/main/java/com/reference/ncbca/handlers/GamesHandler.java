package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.GamesDao;
import com.reference.ncbca.model.Game;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamesHandler {

    private final GamesDao gamesDao;

    public GamesHandler(GamesDao gamesDao) {
        this.gamesDao = gamesDao;
    }

    public void load(List<Game> games) {
        gamesDao.load(games);
    }

    public Integer getLatestGameId(Integer season) {
        return gamesDao.getLatestGameForSeason(season);
    }

}
