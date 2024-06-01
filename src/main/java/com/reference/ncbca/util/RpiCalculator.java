package com.reference.ncbca.util;

import com.reference.ncbca.model.dao.Game;

import java.util.List;

public class RpiCalculator {

    public static double calculateRPI(Integer teamId, List<Game> allGamesForSeason, double sos) {
        double teamWinningPercentage = calculateWeightedTeamWinPercentage(teamId, allGamesForSeason);
        return (teamWinningPercentage + (sos * 3.0000)) / 4.0000;
    }

    private static double calculateWeightedTeamWinPercentage(Integer teamId, List<Game> allGamesForSeason) {
        double gamesWon = determineWeightedGamesWonFromGames(teamId, allGamesForSeason);
        double gamesLost = determineWeightedGamesLostFromGames(teamId, allGamesForSeason);
        return gamesWon / (gamesWon + gamesLost);
    }

    private static double determineWeightedGamesLostFromGames(Integer teamId, List<Game> allGamesForSeason) {
        List<Game> lostHomeGames = allGamesForSeason.stream().filter(game -> game.losingTeamId().equals(teamId) && game.homeTeamId().equals(teamId)).toList();
        List<Game> lostAwayGames = allGamesForSeason.stream().filter(game -> game.losingTeamId().equals(teamId) && game.awayTeamId().equals(teamId)).toList();
        return ((double) lostHomeGames.size() * 1.4) + ((double) lostAwayGames.size() * 0.6);
    }

    private static double determineWeightedGamesWonFromGames(Integer teamId, List<Game> allGamesForSeason) {
        List<Game> wonHomeGames = allGamesForSeason.stream().filter(game -> game.winningTeamId().equals(teamId) && game.homeTeamId().equals(teamId)).toList();
        List<Game> wonAwayGames = allGamesForSeason.stream().filter(game -> game.winningTeamId().equals(teamId) && game.awayTeamId().equals(teamId)).toList();
        return (wonHomeGames.size() * 0.6) + (wonAwayGames.size() * 1.4);
    }
}
