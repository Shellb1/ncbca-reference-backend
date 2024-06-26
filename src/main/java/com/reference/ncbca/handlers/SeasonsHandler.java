package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.SeasonsDao;
import com.reference.ncbca.model.dao.NTSeed;
import com.reference.ncbca.model.dao.Season;
import com.reference.ncbca.model.dao.SeasonMetrics;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SeasonsHandler {

    private final SeasonsDao seasonsDao;
    private final SeasonMetricsHandler seasonMetricsHandler;
    private final NTSeedsHandler ntSeedsHandler;

    public SeasonsHandler(SeasonsDao seasonsDao, SeasonMetricsHandler seasonMetricsHandler, NTSeedsHandler ntSeedsHandler) {
        this.seasonMetricsHandler = seasonMetricsHandler;
        this.seasonsDao = seasonsDao;
        this.ntSeedsHandler = ntSeedsHandler;
    }

    public void load(List<Season> seasons) {
        seasonsDao.load(seasons);
    }

    public List<Season> listSeasonsForYear(Integer year) {
        List<Season> seasons = seasonsDao.findSeasonsByYear(year);
        List<SeasonMetrics> metrics = seasonMetricsHandler.getSeasonMetricsForSeason(year);
        for (Season season: seasons) {
            Optional<NTSeed> ntSeedOptional = ntSeedsHandler.getSeedForTeamAndSeason(season.getTeamId(), year);
            ntSeedOptional.ifPresent(ntSeed -> season.setNtSeed(ntSeed.seed()));
            for (SeasonMetrics metric: metrics) {
                if (season.getTeamId().equals(metric.teamId())) {
                   season.setSeasonMetrics(metric);
                   break;
                }
            }
        }
        seasons.sort((o1, o2) -> o2.getGamesWon().compareTo(o1.getGamesWon()));
        return seasons;
    }

    public List<Season> listSeasonsForCoach(String coach) {
        List<Season> seasons = seasonsDao.findSeasonsByCoachName(coach);
        seasons.sort(Comparator.comparing(Season::getSeasonYear));
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
