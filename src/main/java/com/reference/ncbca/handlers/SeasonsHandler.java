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
                    List<Game> teamsGames = allGamesInSeason.stream().filter(game -> game.getWinningTeamId().equals(metric.getTeamId()) || game.getLosingTeamId().equals(metric.getTeamId())).toList();
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
        Integer q1Wins = Integer.valueOf(q1Record.split("-")[0]);
        Integer q2Wins = Integer.valueOf(q2Record.split("-")[0]);
        Integer q3Wins = Integer.valueOf(q3Record.split("-")[0]);
        Integer q4Wins = Integer.valueOf(q4Record.split("-")[0]);
        Integer q1Losses = Integer.valueOf(q1Record.split("-")[1]);
        Integer q2Losses = Integer.valueOf(q2Record.split("-")[1]);
        Integer q3Losses = Integer.valueOf(q3Record.split("-")[1]);
        Integer q4Losses = Integer.valueOf(q4Record.split("-")[1]);
        Season teamSeason = getSeasonForTeamAndYear(metric.getTeamName(), metric.getSeason());
        if (q1Wins + q2Wins + q3Wins + q4Wins != teamSeason.getGamesWon()) {
            System.out.println("ERROR WITH WINS WITH TEAM: " + teamSeason.getTeamName());
        }
        if (q1Losses + q2Losses + q3Losses + q4Losses != teamSeason.getGamesLost()) {
            System.out.println("ERROR WITH LOSSES WITH TEAM: " + teamSeason.getTeamName());
        }
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
