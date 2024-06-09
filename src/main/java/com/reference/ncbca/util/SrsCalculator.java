package com.reference.ncbca.util;

import com.reference.ncbca.model.dao.Game;
import com.reference.ncbca.model.dao.Season;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SrsCalculator {

    public static Map<Integer, Double> calculateSRS(List<Season> seasonsForYear, List<Game> games, double tolerance, int maxIterations) {
        Map<Integer, Double> totalPointsFor = new HashMap<>();
        Map<Integer, Double> totalPointsAgainst = new HashMap<>();
        Map<Integer, Double> srs = new HashMap<>();

        // Step 1: Calculate total points for and against for each team
        for (Season teamSeason : seasonsForYear) {
            int teamId = teamSeason.getTeamId();
            double pointsFor = games.stream().mapToDouble(game -> {
                if (game.winningTeamId().equals(teamId)) {
                    return game.winningTeamScore();
                }
                if (game.losingTeamId().equals(teamId)) {
                    return game.losingTeamScore();
                }
                return 0.0;
            }).sum();
            double pointsAgainst = games.stream().mapToDouble(game -> {
                if (game.winningTeamId().equals(teamId)) {
                    return game.losingTeamScore();
                }
                if (game.losingTeamId().equals(teamId)) {
                    return game.winningTeamScore();
                }
                return 0.0;
            }).sum();
            totalPointsFor.put(teamId, pointsFor);
            totalPointsAgainst.put(teamId, pointsAgainst);
        }

        // Step 2: Initialize SRS with average point differentials
        for (Integer teamId : totalPointsFor.keySet()) {
            double pointsFor = totalPointsFor.get(teamId);
            double pointsAgainst = totalPointsAgainst.get(teamId);
            double gamesPlayedByTeam = games.stream().filter(game -> game.homeTeamId().equals(teamId) || game.awayTeamId().equals(teamId)).count();
            double initialSRS = (pointsFor - pointsAgainst) / gamesPlayedByTeam;
            srs.put(teamId, initialSRS);
        }

        boolean converged = false;
        int iteration = 0;

        while (!converged && iteration < maxIterations) {
            Map<Integer, Double> newSrs = new HashMap<>();
            converged = true;

            // Step 3: Calculate new SRS for each team
            for (Integer teamId : srs.keySet()) {
                double totalOpponentSRS = 0.0;
                double opponentGames = 0.0;

                for (Game game : games) {
                    if (game.homeTeamId().equals(teamId)) {
                        totalOpponentSRS += srs.get(game.awayTeamId());
                        opponentGames++;
                    } else if (game.awayTeamId().equals(teamId)) {
                        totalOpponentSRS += srs.get(game.homeTeamId());
                        opponentGames++;
                    }
                }

                double sos = totalOpponentSRS / opponentGames;
                double gamesPlayedByTeam = games.stream().filter(game -> game.homeTeamId().equals(teamId) || game.awayTeamId().equals(teamId)).count();
                double avgPointDifferential = (totalPointsFor.get(teamId) - totalPointsAgainst.get(teamId)) / gamesPlayedByTeam;
                double newSrsValue = avgPointDifferential + sos;

                newSrs.put(teamId, newSrsValue);

                // Check for convergence
                if (Math.abs(newSrsValue - srs.get(teamId)) > tolerance) {
                    converged = false;
                }
            }

            srs = newSrs;
            iteration++;
        }

        return srs;
    }
}
