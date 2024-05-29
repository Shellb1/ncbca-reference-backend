package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.SeasonsDao;
import com.reference.ncbca.model.dao.Season;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SeasonsHandler {

    private final SeasonsDao seasonsDao;

    public SeasonsHandler(SeasonsDao seasonsDao) {
        this.seasonsDao = seasonsDao;
    }

    public void load(List<Season> seasons) {

        seasonsDao.load(seasons);
    }

    public List<Season> listSeasonsForYear(Integer year) {
        List<Season> seasons = seasonsDao.findSeasonsByYear(year);
        seasons.sort((o1, o2) -> o2.gamesWon().compareTo(o1.gamesWon()));
        return seasons;
    }

    public List<Season> listSeasonsForCoach(String coach) {
        List<Season> seasons = seasonsDao.findSeasonsByCoachName(coach);
        seasons.sort(Comparator.comparing(Season::seasonYear));
        return seasons;
    }

    public List<Season> listSeasonsForTeam(String teamName) {
        return seasonsDao.findSeasonsByTeamName(teamName);
    }

    public Season getSeasonForTeamAndYear(String teamName, Integer year) {
        return seasonsDao.getSeasonForTeamAndYear(teamName, year);
    }

    public List<Season> listAllSeasons() {
        return seasonsDao.listAllSeasons();
    }

    public Integer determineMostRecentYear() {
        return seasonsDao.determineMostRecentYear();
    }
}
