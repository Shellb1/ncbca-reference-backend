package com.reference.ncbca.util;

import com.reference.ncbca.model.dao.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SrsCalculator {

    public static Map<Integer, Double> calculateSRS(List<Game> games, double tolerance, int maxIterations) {
        Map<Integer, Double> totalPointsFor = new HashMap<>();
        Map<Integer, Double> totalPointsAgainst = new HashMap<>();
        Map<Integer, Integer> gamesPlayed = new HashMap<>();
        Map<Integer, Double> srs = new HashMap<>();

        // Step 1: Calculate total points for and against for each team
        for (Game game : games) {
            int homeTeamId = game.homeTeamId();
            int awayTeamId = game.awayTeamId();
            double homePoints = game.winningTeamId().equals(homeTeamId) ? game.winningTeamScore() : game.losingTeamScore();
            double awayPoints = game.winningTeamId().equals(awayTeamId) ? game.winningTeamScore() : game.losingTeamScore();
            double homePointsAgainst = game.winningTeamId().equals(homeTeamId) ? game.losingTeamScore() : game.winningTeamScore();
            double awayPointsAgainst = game.winningTeamId().equals(awayTeamId) ? game.losingTeamScore() : game.winningTeamScore();

            totalPointsFor.put(homeTeamId, totalPointsFor.getOrDefault(homeTeamId, 0.0) + homePoints);
            totalPointsFor.put(awayTeamId, totalPointsFor.getOrDefault(awayTeamId, 0.0) + awayPoints);

            totalPointsAgainst.put(homeTeamId, totalPointsAgainst.getOrDefault(homeTeamId, 0.0) + homePointsAgainst);
            totalPointsAgainst.put(awayTeamId, totalPointsAgainst.getOrDefault(awayTeamId, 0.0) + awayPointsAgainst);

            gamesPlayed.put(homeTeamId, gamesPlayed.getOrDefault(homeTeamId, 0) + 1);
            gamesPlayed.put(awayTeamId, gamesPlayed.getOrDefault(awayTeamId, 0) + 1);
        }

        // Step 2: Initialize SRS with average point differentials
        for (Integer teamId : totalPointsFor.keySet()) {
            double pointsFor = totalPointsFor.get(teamId);
            double pointsAgainst = totalPointsAgainst.get(teamId);
            double gamesPlayedByTeam = gamesPlayed.get(teamId);
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
                int opponentGames = 0;

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
                double avgPointDifferential = (totalPointsFor.get(teamId) - totalPointsAgainst.get(teamId)) / gamesPlayed.get(teamId);
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
