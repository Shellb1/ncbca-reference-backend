package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.SeasonsDao;
import com.reference.ncbca.model.dao.Game;
import com.reference.ncbca.model.dao.NTSeed;
import com.reference.ncbca.model.dao.Season;
import com.reference.ncbca.model.dao.SeasonMetrics;
import com.reference.ncbca.util.GameUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SeasonsHandler {

    private final SeasonsDao seasonsDao;
    private final SeasonMetricsHandler seasonMetricsHandler;
    private final NTSeedsHandler ntSeedsHandler;
    private final GamesHandler gamesHandler;

    public SeasonsHandler(SeasonsDao seasonsDao, SeasonMetricsHandler seasonMetricsHandler, NTSeedsHandler ntSeedsHandler, GamesHandler gamesHandler) {
        this.seasonMetricsHandler = seasonMetricsHandler;
        this.seasonsDao = seasonsDao;
        this.ntSeedsHandler = ntSeedsHandler;
        this.gamesHandler = gamesHandler;
    }

    public void load(List<Season> seasons) {
        seasonsDao.load(seasons);
    }

    public List<Season> listSeasonsForYear(Integer year) {
        List<Season> seasons = seasonsDao.findSeasonsByYear(year);
        List<SeasonMetrics> metrics = seasonMetricsHandler.getSeasonMetricsForSeason(year);
        List<Game> allGamesInSeason = gamesHandler.getAllGamesInSeason(year);
        List<NTSeed> ntSeedsForSeason = ntSeedsHandler.getAllSeedsForSeason(year);
        for (Season season: seasons) {
            for (NTSeed seed: ntSeedsForSeason) {
                if (season.getTeamId().equals(seed.teamId())) {
                    season.setNtSeed(seed.seed());
                    break;
                }
            }
            for (SeasonMetrics metric: metrics) {
                if (season.getTeamId().equals(metric.getTeamId())) {
                    List<Game> teamsGames = allGamesInSeason.stream().filter(game -> game.winningTeamId().equals(metric.getTeamId()) || game.losingTeamId().equals(metric.getTeamId())).toList();
                    buildQuadrantsForTeam(teamsGames, metric, metrics);
                   season.setSeasonMetrics(metric);
                   break;
                }
            }
        }
        seasons.sort((o1, o2) -> o2.getGamesWon().compareTo(o1.getGamesWon()));
        return seasons;
    }

    private void buildQuadrantsForTeam(List<Game> games, SeasonMetrics metric, List<SeasonMetrics> allSeasonMetrics) {
        String q1Record = GameUtils.buildQ1Record(games, metric, allSeasonMetrics);
        String q2Record = GameUtils.buildQ2Record(games, metric, allSeasonMetrics);
        String q3Record = GameUtils.buildQ3Record(games, metric, allSeasonMetrics);
        String q4Record = GameUtils.buildQ4Record(games, metric, allSeasonMetrics);
        metric.setQ1Record(q1Record);
        metric.setQ2Record(q2Record);
        metric.setQ3Record(q3Record);
        metric.setQ4Record(q4Record);
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
