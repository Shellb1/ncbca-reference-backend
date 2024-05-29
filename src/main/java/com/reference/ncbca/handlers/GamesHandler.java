package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.GamesDao;
import com.reference.ncbca.model.dao.Game;
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

    public List<Game> getAllTimeGamesPlayedByCoach(String coachName) {
        return gamesDao.getAllGamesCoachParticipatedIn(coachName);
    }


    public Integer determineGamesWonForTeam(Integer teamId, Integer season) {
        return gamesDao.getGamesWonForTeamInSeason(teamId, season);
    }

    public Integer determineGamesLostForTeam(Integer teamId, Integer season) {
        return gamesDao.getGamesLostForTeamInSeason(teamId, season);
    }

    public List<Game> listAll() {
        return gamesDao.listAll();
    }

    public List<Game> getAllGamesInSeason(Integer season) {
        return gamesDao.getAllGamesInSeason(season);
    }

    public void backload(List<Game> games) {
        gamesDao.backload(games);
    }

}
