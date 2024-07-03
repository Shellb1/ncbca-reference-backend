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
            totalPointsFor.put(teamId, 0.0);
            totalPointsAgainst.put(teamId, 0.0);
        }

        for (Game game : games) {
            int winningTeamId = game.getWinningTeamId();
            int losingTeamId = game.getLosingTeamId();
            double winningTeamScore = game.getWinningTeamScore();
            double losingTeamScore = game.getLosingTeamScore();

            totalPointsFor.put(winningTeamId, totalPointsFor.get(winningTeamId) + winningTeamScore);
            totalPointsAgainst.put(winningTeamId, totalPointsAgainst.get(winningTeamId) + losingTeamScore);

            totalPointsFor.put(losingTeamId, totalPointsFor.get(losingTeamId) + losingTeamScore);
            totalPointsAgainst.put(losingTeamId, totalPointsAgainst.get(losingTeamId) + winningTeamScore);
        }

        // Step 2: Initialize SRS with average point differentials
        for (Integer teamId : totalPointsFor.keySet()) {
            double pointsFor = totalPointsFor.get(teamId);
            double pointsAgainst = totalPointsAgainst.get(teamId);
            long gamesPlayedByTeam = games.stream().filter(game -> game.getHomeTeamId().equals(teamId) || game.getAwayTeamId().equals(teamId)).count();
            double initialSRS = (gamesPlayedByTeam > 0) ? (pointsFor - pointsAgainst) / gamesPlayedByTeam : 0.0;
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
                    if (game.getHomeTeamId().equals(teamId)) {
                        totalOpponentSRS += srs.getOrDefault(game.getAwayTeamId(), 0.0);
                        opponentGames++;
                    } else if (game.getAwayTeamId().equals(teamId)) {
                        totalOpponentSRS += srs.getOrDefault(game.getHomeTeamId(), 0.0);
                        opponentGames++;
                    }
                }

                double sos = (opponentGames > 0) ? totalOpponentSRS / opponentGames : 0.0;
                long gamesPlayedByTeam = games.stream().filter(game -> game.getHomeTeamId().equals(teamId) || game.getAwayTeamId().equals(teamId)).count();
                double avgPointDifferential = (gamesPlayedByTeam > 0) ? (totalPointsFor.get(teamId) - totalPointsAgainst.get(teamId)) / gamesPlayedByTeam : 0.0;
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
