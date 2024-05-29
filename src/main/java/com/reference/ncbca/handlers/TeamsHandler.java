package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.GamesDao;
import com.reference.ncbca.dao.TeamsDao;
import com.reference.ncbca.model.*;
import com.reference.ncbca.model.dao.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TeamsHandler {

    private final TeamsDao teamsDao;
    private final GamesDao gamesDao;
    private final SeasonsHandler seasonsHandler;
    private final NBLHandler nblHandler;
    private final NTSeedsHandler ntSeedsHandler;

    public TeamsHandler(TeamsDao teamsDao, GamesDao gamesDao, SeasonsHandler seasonsHandler, NBLHandler nblHandler, NTSeedsHandler ntSeedsHandler) {
        this.teamsDao = teamsDao;
        this.gamesDao = gamesDao;
        this.seasonsHandler = seasonsHandler;
        this.nblHandler = nblHandler;
        this.ntSeedsHandler = ntSeedsHandler;
    }

    public void loadTeams(List<Team> teams) {
        teamsDao.insert(teams);
    }

    public List<Team> listAllActiveTeams() {
        return teamsDao.listAllActiveTeams();
    }

    public List<Team> listAllTeams() {
        return teamsDao.listAllTeams();
    }

    public TeamSeasonSummary buildTeamSummary(String teamName, Integer year) {
        List<Game> games = gamesDao.getGamesForTeamByYear(teamName, year);
        Season teamSeason = seasonsHandler.getSeasonForTeamAndYear(teamName, year);
        Integer teamId = teamSeason.teamId();
        Optional<NTSeed> seed = ntSeedsHandler.getSeedForTeamAndSeason(teamId, year);
        Integer seedLine = seed.map(NTSeed::seed).orElse(null);
        return new TeamSeasonSummary(teamSeason.teamId(), teamSeason.teamName(), teamSeason.gamesWon(), teamSeason.gamesLost(), teamSeason.seasonYear(), teamSeason.coach(), games, seedLine);
    }

    public TeamSummary getTeamSummary(String teamName) {
        Team team = teamsDao.getTeam(teamName);
        List<Season> seasons = seasonsHandler.listSeasonsForTeam(teamName);
        List<DraftPick> draftPicks = nblHandler.getAllDraftPicksForTeam(teamName);
        List<Game> games = gamesDao.getAllGamesForTeam(teamName);
        List<NTSeed> ntSeeds = ntSeedsHandler.getAllSeedsForTeam(teamName).stream().sorted(Comparator.comparing(NTSeed::season)).toList();
        return new TeamSummary(team, seasons, draftPicks, games, ntSeeds);
    }
}
