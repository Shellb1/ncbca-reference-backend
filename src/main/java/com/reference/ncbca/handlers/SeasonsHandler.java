package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.SeasonsDao;
import com.reference.ncbca.model.Season;
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
        seasons.sort(new Comparator<Season>() {

            @Override
            public int compare(Season o1, Season o2) {
                return o2.gamesWon().compareTo(o1.gamesWon());
            }
        });
        return seasons;
    }

    public List<Season> listSeasonsForCoach(String coach) {
        return seasonsDao.findSeasonsByCoachName(coach);
    }

    public Season getSeasonForTeamAndYear(String teamName, Integer year) {
        return seasonsDao.getSeasonForTeamAndYear(teamName, year);
    }
}
