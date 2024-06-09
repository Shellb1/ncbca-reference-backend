package com.reference.ncbca.util;

import com.reference.ncbca.model.dao.Game;

import java.util.List;
import java.util.Objects;

public class SosCalculator {

    public static double calculateSOS(Integer teamId, List<Game> allGamesForSeason) {
        double opponentsWinningPercentage = calculateOpponentsWinningPercentage(teamId, allGamesForSeason);
        double opponentsOpponentsWinningPercentage = calculateOpponentsOpponentsWinningPercentage(teamId, allGamesForSeason);
        return (2.0 * opponentsWinningPercentage + opponentsOpponentsWinningPercentage) / 3.0;
    }

    private static double calculateOpponentsWinningPercentage(Integer teamId, List<Game> allGamesForSeason) {
        List<Integer> opponentsTids = allGamesForSeason.stream().map(game -> {
            if (game.winningTeamId().equals(teamId) || Objects.equals(game.losingTeamId(), teamId)) {
                if (game.winningTeamId().equals(teamId)) {
                    return game.losingTeamId();
                } else {
                    return game.winningTeamId();
                }
            }
            return null;
        }).toList();
        double opponentGamesWon = opponentsTids.stream().mapToDouble(tid -> GameUtils.determineGamesWonFromGames(tid, allGamesForSeason)).sum();
        double opponentGamesLost = opponentsTids.stream().mapToDouble(tid -> GameUtils.determineGamesLostFromGames(tid, allGamesForSeason)).sum();
        return opponentGamesWon / (opponentGamesWon + opponentGamesLost);

    }

    private static double calculateOpponentsOpponentsWinningPercentage(Integer teamId, List<Game> allGamesForSeason) {
        List<Integer> opponents = GameUtils.getOpponents(teamId, allGamesForSeason);
        List<Integer> opponentsOfOpponents = opponents.stream()
                .flatMap(opponentId -> GameUtils.getOpponents(opponentId, allGamesForSeason).stream())
                .toList();
        double oppOppGamesWon = opponentsOfOpponents.stream().mapToDouble(tid -> GameUtils.determineGamesWonFromGames(tid, allGamesForSeason)).sum();
        double oppOppGamesLost = opponentsOfOpponents.stream().mapToDouble(tid -> GameUtils.determineGamesLostFromGames(tid, allGamesForSeason)).sum();
        return oppOppGamesWon / (oppOppGamesWon + oppOppGamesLost);
    }
}
