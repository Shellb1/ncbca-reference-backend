package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.GamesDao;
import com.reference.ncbca.dao.TeamsDao;
import com.reference.ncbca.model.Game;
import com.reference.ncbca.model.Season;
import com.reference.ncbca.model.Team;
import com.reference.ncbca.model.TeamSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamsHandler {

    private final TeamsDao teamsDao;
    private final GamesDao gamesDao;
    private final SeasonsHandler seasonsHandler;

    public TeamsHandler(TeamsDao teamsDao, GamesDao gamesDao, SeasonsHandler seasonsHandler) {
        this.teamsDao = teamsDao;
        this.gamesDao = gamesDao;
        this.seasonsHandler = seasonsHandler;
    }

    public Team getTeam(Integer id) {
        return teamsDao.get(id);
    }

    public void loadTeams(List<Team> teams) {
        teamsDao.insert(teams);
    }

    public List<Team> listAllTeams() {
        return teamsDao.listAllTeams();
    }

    public TeamSummary buildTeamSummary(String teamName, Integer year) {
        List<Game> games = gamesDao.getGamesForTeamByYear(teamName, year);
        Season teamSeason = seasonsHandler.getSeasonForTeamAndYear(teamName, year);
        return new TeamSummary(teamSeason.teamId(), teamSeason.teamName(), teamSeason.gamesWon(), teamSeason.gamesLost(), teamSeason.seasonYear(), teamSeason.coach(), games);
    }
}
