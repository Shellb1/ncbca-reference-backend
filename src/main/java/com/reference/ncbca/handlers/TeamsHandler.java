package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.GamesDao;
import com.reference.ncbca.dao.TeamsDao;
import com.reference.ncbca.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamsHandler {

    private final TeamsDao teamsDao;
    private final GamesDao gamesDao;
    private final SeasonsHandler seasonsHandler;
    private final NBLHandler nblHandler;
    private final PostseasonHandler postseasonHandler;

    public TeamsHandler(TeamsDao teamsDao, GamesDao gamesDao, SeasonsHandler seasonsHandler, NBLHandler nblHandler, PostseasonHandler postseasonHandler) {
        this.teamsDao = teamsDao;
        this.gamesDao = gamesDao;
        this.seasonsHandler = seasonsHandler;
        this.nblHandler = nblHandler;
        this.postseasonHandler = postseasonHandler;
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
        return new TeamSeasonSummary(teamSeason.teamId(), teamSeason.teamName(), teamSeason.gamesWon(), teamSeason.gamesLost(), teamSeason.seasonYear(), teamSeason.coach(), games);
    }

    public TeamSummary getTeamSummary(String teamName) {
        Team team = teamsDao.getTeam(teamName);
        List<Season> seasons = seasonsHandler.listSeasonsForTeam(teamName);
        List<DraftPick> draftPicks = nblHandler.getAllDraftPicksForTeam(teamName);
        List<PostseasonGame> postseasonGames = postseasonHandler.getPostseasonGamesForTeam(teamName);
        return new TeamSummary(team, seasons, draftPicks, postseasonGames);
    }
}
