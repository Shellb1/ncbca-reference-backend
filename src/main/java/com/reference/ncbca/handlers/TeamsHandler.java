package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.GamesDao;
import com.reference.ncbca.dao.TeamsDao;
import com.reference.ncbca.model.*;
import com.reference.ncbca.model.dao.*;
import com.reference.ncbca.util.GameUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamsHandler {

    private final TeamsDao teamsDao;
    private final GamesDao gamesDao;
    private final SeasonsHandler seasonsHandler;
    private final NBLHandler nblHandler;
    private final NTSeedsHandler ntSeedsHandler;
    private final SeasonMetricsHandler seasonMetricsHandler;

    public TeamsHandler(TeamsDao teamsDao, GamesDao gamesDao, SeasonsHandler seasonsHandler, NBLHandler nblHandler, NTSeedsHandler ntSeedsHandler, SeasonMetricsHandler seasonMetricsHandler) {
        this.teamsDao = teamsDao;
        this.gamesDao = gamesDao;
        this.seasonsHandler = seasonsHandler;
        this.nblHandler = nblHandler;
        this.ntSeedsHandler = ntSeedsHandler;
        this.seasonMetricsHandler = seasonMetricsHandler;
    }

    public void loadTeams(List<Team> teams) {
        List<Team> currentTeams = listAllActiveTeams();
        List<Team> newTeams = new ArrayList<>();
        for (Team team: teams) {
            if (!currentTeams.contains(team)) {
                newTeams.add(team);
            }
        }
        List<Team> teamsToRetire = new ArrayList<>();
        for (Team team: currentTeams) {
            if (!teams.contains(team)) {
                teamsToRetire.add(team);
            }
        }
        teamsDao.insert(newTeams);
        teamsDao.retireTeams(teamsToRetire);
    }

    public List<Team> listAllActiveTeams() {
        return teamsDao.listAllActiveTeams();
    }

    public List<Team> listAllTeams() {
        return teamsDao.listAllTeams();
    }

    public TeamSeasonSummary buildTeamSummary(String teamName, Integer year) {
        List<Game> games = gamesDao.getGamesForTeamByYear(teamName, year);
        List<Season> allSeasonsForYear = seasonsHandler.listSeasonsForYear(year);
        List<Game> augmentedGamesWithOppRecords = buildOpponentsRecordForGames(games, allSeasonsForYear);
        Season teamSeason = allSeasonsForYear.stream().filter(season -> season.getTeamName().equals(teamName)).findAny().orElse(null);
        assert teamSeason != null;
        Integer teamId = teamSeason.getTeamId();
        Optional<NTSeed> seed = ntSeedsHandler.getSeedForTeamAndSeason(teamId, year);
        Integer seedLine = seed.map(NTSeed::seed).orElse(null);
        List<SeasonMetrics> allSeasonMetrics = seasonMetricsHandler.getSeasonMetricsForSeason(year);
        SeasonMetrics teamMetrics = allSeasonMetrics.stream().filter(seasonMetrics -> seasonMetrics.getTeamId().equals(teamId)).findAny().orElse(null);
        assert teamMetrics != null;
        teamMetrics.setQ1Record(teamSeason.getSeasonMetrics().getQ1Record());
        teamMetrics.setQ2Record(teamSeason.getSeasonMetrics().getQ2Record());
        teamMetrics.setQ3Record(teamSeason.getSeasonMetrics().getQ3Record());
        teamMetrics.setQ4Record(teamSeason.getSeasonMetrics().getQ4Record());
        determineAndAugmentRPIRank(allSeasonMetrics, teamMetrics);
        determineAndAugmentSOSRank(allSeasonMetrics, teamMetrics);
        determineAndAugmentSRSRank(allSeasonMetrics, teamMetrics);
        teamSeason.setSeasonMetrics(teamMetrics);
        return new TeamSeasonSummary(teamSeason.getTeamId(), teamSeason.getTeamName(), teamSeason.getGamesWon(), teamSeason.getGamesLost(), teamSeason.getSeasonYear(), teamSeason.getCoach(), augmentedGamesWithOppRecords, seedLine, teamMetrics);
    }

    private void determineAndAugmentRPIRank(List<SeasonMetrics> allSeasonMetrics, SeasonMetrics teamMetrics) {
        List<SeasonMetrics> sortedMetrics = allSeasonMetrics.stream().sorted(Comparator.comparing(SeasonMetrics::getRpi).reversed()).toList();
        int rpiRank = sortedMetrics.indexOf(teamMetrics) + 1;
        teamMetrics.setRpi((double) rpiRank);
    }
    private void determineAndAugmentSOSRank(List<SeasonMetrics> allSeasonMetrics, SeasonMetrics teamMetrics) {
        List<SeasonMetrics> sortedMetrics = allSeasonMetrics.stream().sorted(Comparator.comparing(SeasonMetrics::getSos).reversed()).toList();
        int sosRank = sortedMetrics.indexOf(teamMetrics) + 1;
        teamMetrics.setSos((double) sosRank);
    }

    private void determineAndAugmentSRSRank(List<SeasonMetrics> allSeasonMetrics, SeasonMetrics teamMetrics) {
        List<SeasonMetrics> sortedMetrics = allSeasonMetrics.stream().sorted(Comparator.comparing(SeasonMetrics::getSrs).reversed()).toList();
        int srsRank = sortedMetrics.indexOf(teamMetrics) + 1;
        teamMetrics.setSrs((double) srsRank);
    }

    private List<Game> buildOpponentsRecordForGames(List<Game> games, List<Season> allSeasonsForYear) {
        List<Game> augmentedGames = new ArrayList<>();
        for (Game game: games) {
            Integer winningTeamId = game.getWinningTeamId();
            Integer winningTeamGamesWon = Objects.requireNonNull(allSeasonsForYear.stream().filter(season -> season.getTeamId().equals(winningTeamId)).findAny().orElse(null)).getGamesWon();
            Integer winningTeamGamesLost = Objects.requireNonNull(allSeasonsForYear.stream().filter(season -> season.getTeamId().equals(winningTeamId)).findAny().orElse(null)).getGamesLost();
            String winningTeamRecord = winningTeamGamesWon + "-" + winningTeamGamesLost;
            Integer losingTeamId = game.getLosingTeamId();
            Integer losingTeamGamesWon = Objects.requireNonNull(allSeasonsForYear.stream().filter(season -> season.getTeamId().equals(losingTeamId)).findAny().orElse(null)).getGamesWon();
            Integer losingTeamGamesLost = Objects.requireNonNull(allSeasonsForYear.stream().filter(season -> season.getTeamId().equals(losingTeamId)).findAny().orElse(null)).getGamesLost();
            String losingTeamRecord = losingTeamGamesWon + "-" + losingTeamGamesLost;
            Game augmentedGame = new Game.Builder(game).winningTeamRecord(winningTeamRecord).losingTeamRecord(losingTeamRecord).build();
            augmentedGames.add(augmentedGame);
        }
        return augmentedGames;
    }

    public TeamSummary getTeamSummary(String teamName) {
        Team team = teamsDao.getTeam(teamName);
        List<Season> seasons = seasonsHandler.listSeasonsForTeam(teamName);
        List<DraftPick> draftPicks = nblHandler.getAllDraftPicksForTeam(teamName);
        List<Game> games = gamesDao.getAllGamesForTeam(teamName);
        List<NTSeed> ntSeeds = ntSeedsHandler.getAllSeedsForTeam(teamName).stream().sorted(Comparator.comparing(NTSeed::season)).toList();
        return new TeamSummary(team, seasons, draftPicks, games, ntSeeds);
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
        Season teamSeason = seasonsHandler.getSeasonForTeamAndYear(metric.getTeamName(), metric.getSeason());
        if (q1Wins + q2Wins + q3Wins + q4Wins != teamSeason.getGamesWon()) {
            System.out.println("ERROR WITH WINS");
        }
        if (q1Losses + q2Losses + q3Losses + q4Losses != teamSeason.getGamesLost()) {
            System.out.println("ERROR WITH LOSSES WITH TEAM: " + teamSeason.getTeamName());
        }
        metric.setQ1Record(q1Record);
        metric.setQ2Record(q2Record);
        metric.setQ3Record(q3Record);
        metric.setQ4Record(q4Record);
    }
}
