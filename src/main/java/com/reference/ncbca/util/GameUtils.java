package com.reference.ncbca.util;

import com.reference.ncbca.model.dao.Game;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameUtils {

    public static double calculateTeamWinPercentage(Integer teamId, List<Game> allGamesForSeason) {
        double gamesWon = determineGamesWonFromGames(teamId, allGamesForSeason);
        double gamesLost = determineGamesLostFromGames(teamId, allGamesForSeason);
        return gamesWon / (gamesWon + gamesLost);
    }

    public static List<Integer> getOpponents(Integer teamId, List<Game> allGamesForSeason) {
        return allGamesForSeason.stream()
                .filter(game -> game.homeTeamId().equals(teamId) || game.awayTeamId().equals(teamId))
                .map(game -> game.homeTeamId().equals(teamId) ? game.awayTeamId() : game.homeTeamId())
                .distinct()
                .collect(Collectors.toList());
    }

    public static double determineGamesWonFromGames(Integer teamId, List<Game> allGamesForSeason) {
        return (double) allGamesForSeason.stream()
                .filter(game -> Objects.equals(game.winningTeamId(), teamId))
                .count();
    }

    public static double determineGamesLostFromGames(Integer teamId, List<Game> allGamesForSeason) {
        return (double) allGamesForSeason.stream()
                .filter(game -> Objects.equals(game.losingTeamId(), teamId))
                .count();
    }
}
