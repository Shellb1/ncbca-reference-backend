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

    public void load(List<Game> games, Integer season) {
        gamesDao.load(games, season);
    }

    public Integer getLatestGameId(Integer season) {
        return gamesDao.getLatestGameForSeason(season);
    }

    public Integer determineGamesWonForTeam(Integer teamId, Integer season) {
        return gamesDao.getGamesWonForTeamInSeason(teamId, season);
    }

    public Integer determineGamesLostForTeam(Integer teamId, Integer season) {
        return gamesDao.getGamesLostForTeamInSeason(teamId, season);
    }

}
