package com.reference.ncbca.handlers;

import com.reference.ncbca.model.AllTimeProgramRanking;
import com.reference.ncbca.model.Game;
import com.reference.ncbca.model.Season;
import com.reference.ncbca.model.Team;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AllTimeHandler {

    private final SeasonsHandler seasonsHandler;
    private final CoachesHandler coachesHandler;
    private final TeamsHandler teamsHandler;
    private final GamesHandler gamesHandler;

    public AllTimeHandler(SeasonsHandler seasonsHandler, CoachesHandler coachesHandler, TeamsHandler teamsHandler, GamesHandler gamesHandler) {
        this.seasonsHandler = seasonsHandler;
        this.coachesHandler = coachesHandler;
        this.teamsHandler = teamsHandler;
        this.gamesHandler = gamesHandler;
    }

    public List<AllTimeProgramRanking> getAllTimeProgramRankings() {
        List<Season> allSeasons = seasonsHandler.listAllSeasons();
        List<Team> allTeams = teamsHandler.listAllTeams();
        List<Game> allGames = gamesHandler.listAll();
        List<AllTimeProgramRanking> allTimeProgramRankings = new ArrayList<>();
        for (Team team: allTeams) {
            String teamName = team.name();
            List<Season> teamSeasons = getAllSeasonsForTeam(allSeasons, teamName);
            Integer allTimeWins = getAllTimeWins(teamSeasons);
            Integer allTimeLosses = getAllTimeLosses(teamSeasons);
            String conferenceName = team.conferenceName();
            Integer championships = determineChampionshipsWon(allGames, teamName, teamSeasons);
            Integer final4s = determineFinalFours(allGames, teamName, teamSeasons);
            Integer elite8s = determineElite8s(allGames, teamName, teamSeasons);
            Integer sweet16s = determineSweet16s(allGames, teamName, teamSeasons);
            Integer tourneyAppearances = determineTournamentAppearances(allGames, teamName, teamSeasons);
            AllTimeProgramRanking allTimeProgramRanking = new AllTimeProgramRanking(teamName, allTimeWins, allTimeLosses, conferenceName, championships, final4s, elite8s, sweet16s, tourneyAppearances);
            allTimeProgramRankings.add(allTimeProgramRanking);
        }
        return allTimeProgramRankings.stream().sorted(Comparator.comparing(AllTimeProgramRanking::wins).reversed()).toList();
    }

    private Integer determineTournamentAppearances(List<Game> allGames, String teamName, List<Season> teamSeasons) {
        Integer tourneyAppearances = 0;
        List<Integer> yearsPlayed = teamSeasons.stream().map(Season::seasonYear).sorted().toList();
        for (Integer year: yearsPlayed) {
            List<Game> ntGamesPlayedByTeam = allGames.stream().filter(game -> (game.gameType().equals("NT") || game.gameType().equals("FIRST_SIXTEEN")) && Objects.equals(game.season(), year) && (game.winningTeamName().contentEquals(teamName) || game.losingTeamName().contentEquals(teamName))).sorted(Comparator.comparing(Game::gameId)).toList();
            if (!ntGamesPlayedByTeam.isEmpty()) {
                tourneyAppearances++;
            }
        }
        return tourneyAppearances;
    }

    private Integer determineSweet16s(List<Game> allGames, String teamName, List<Season> teamSeasons) {
        Integer sweetSixteens = 0;
        List<Integer> yearsPlayed = teamSeasons.stream().map(Season::seasonYear).sorted().toList();
        for (Integer year: yearsPlayed) {
            List<Game> ntGamesPlayedByTeam = allGames.stream().filter(game -> game.gameType().equals("NT") && Objects.equals(game.season(), year) && (game.winningTeamName().contentEquals(teamName) || game.losingTeamName().contentEquals(teamName))).sorted(Comparator.comparing(Game::gameId)).toList();
            if (ntGamesPlayedByTeam.size() >= 2) {
                sweetSixteens++;
            }
        }
        return sweetSixteens;
    }

    private Integer determineElite8s(List<Game> allGames, String teamName, List<Season> teamSeasons) {
        Integer eliteEights = 0;
        List<Integer> yearsPlayed = teamSeasons.stream().map(Season::seasonYear).sorted().toList();
        for (Integer year: yearsPlayed) {
            List<Game> ntGamesPlayedByTeam = allGames.stream().filter(game -> game.gameType().equals("NT") && Objects.equals(game.season(), year) && (game.winningTeamName().contentEquals(teamName) || game.losingTeamName().contentEquals(teamName))).sorted(Comparator.comparing(Game::gameId)).toList();
            if (ntGamesPlayedByTeam.size() >= 3) {
                eliteEights++;
            }
        }
        return eliteEights;
    }

    private Integer determineFinalFours(List<Game> allGames, String teamName, List<Season> teamSeasons) {
        Integer finalFours = 0;
        List<Integer> yearsPlayed = teamSeasons.stream().map(Season::seasonYear).sorted().toList();
        for (Integer year: yearsPlayed) {
            List<Game> ntGamesPlayedByTeam = allGames.stream().filter(game -> game.gameType().equals("NT") && Objects.equals(game.season(), year) && (game.winningTeamName().contentEquals(teamName) || game.losingTeamName().contentEquals(teamName))).sorted(Comparator.comparing(Game::gameId)).toList();
            if (ntGamesPlayedByTeam.size() >= 4) {
                finalFours++;
            }
        }
        return finalFours;
    }

    private Integer determineChampionshipsWon(List<Game> allGames, String teamName, List<Season> teamSeasons) {
        Integer championshipsWon = 0;
        List<Integer> yearsPlayed = teamSeasons.stream().map(Season::seasonYear).sorted().toList();
        for (Integer year: yearsPlayed) {
            List<Game> ntGamesPlayedByTeam = allGames.stream().filter(game -> game.gameType().equals("NT") && Objects.equals(game.season(), year) && (game.winningTeamName().contentEquals(teamName) || game.losingTeamName().contentEquals(teamName))).sorted(Comparator.comparing(Game::gameId)).toList();
            if (ntGamesPlayedByTeam.size() == 5 && ntGamesPlayedByTeam.get(4).winningTeamName().equals(teamName)) {
                championshipsWon++;
            }
        }
        return championshipsWon;
    }

    private Integer getAllTimeLosses(List<Season> teamSeasons) {
        int losses = 0;
        for (Season season: teamSeasons) {
            losses = losses + season.gamesLost();
        }
        return losses;
    }

    private Integer getAllTimeWins(List<Season> teamSeasons) {
        int wins = 0;
        for (Season season: teamSeasons) {
            wins = wins + season.gamesWon();
        }
        return wins;
    }

    private List<Season> getAllSeasonsForTeam(List<Season> allSeasons, String name) {
        return allSeasons.stream().filter(season -> season.teamName().equals(name)).toList();
    }
}
