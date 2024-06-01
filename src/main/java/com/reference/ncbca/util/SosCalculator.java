package com.reference.ncbca.util;

import com.reference.ncbca.model.dao.Game;

import java.util.List;

public class SosCalculator {

    public static double calculateSOS(Integer teamId, List<Game> allGamesForSeason) {
        double opponentsWinningPercentage = calculateOpponentsWinningPercentage(teamId, allGamesForSeason);
        double opponentsOpponentsWinningPercentage = calculateOpponentsOpponentsWinningPercentage(teamId, allGamesForSeason);
        return (2.0 * opponentsWinningPercentage + opponentsOpponentsWinningPercentage) / 3.0;
    }

    private static double calculateOpponentsWinningPercentage(Integer teamId, List<Game> allGamesForSeason) {
        List<Integer> opponents = GameUtils.getOpponents(teamId, allGamesForSeason);

        return opponents.stream()
                .mapToDouble(opponentId -> GameUtils.calculateTeamWinPercentage(opponentId, allGamesForSeason))
                .average().orElse(0.0);
    }

    private static double calculateOpponentsOpponentsWinningPercentage(Integer teamId, List<Game> allGamesForSeason) {
        List<Integer> opponents = GameUtils.getOpponents(teamId, allGamesForSeason);
        List<Integer> opponentsOfOpponents = opponents.stream()
                .flatMap(opponentId -> GameUtils.getOpponents(opponentId, allGamesForSeason).stream())
                .distinct()
                .toList();

        return opponentsOfOpponents.stream()
                .mapToDouble(opponentOfOpponentId -> GameUtils.calculateTeamWinPercentage(opponentOfOpponentId, allGamesForSeason))
                .average().orElse(0.0);
    }
}
