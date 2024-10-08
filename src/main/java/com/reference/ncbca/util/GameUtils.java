package com.reference.ncbca.util;

import com.reference.ncbca.model.dao.Game;
import com.reference.ncbca.model.dao.SeasonMetrics;

import java.util.Comparator;
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
                .filter(game -> game.getHomeTeamId().equals(teamId) || game.getAwayTeamId().equals(teamId))
                .map(game -> game.getHomeTeamId().equals(teamId) ? game.getAwayTeamId() : game.getHomeTeamId())
                .collect(Collectors.toList());
    }

    public static double determineGamesWonFromGames(Integer teamId, List<Game> allGamesForSeason) {
        return (double) allGamesForSeason.stream()
                .filter(game -> Objects.equals(game.getWinningTeamId(), teamId))
                .count();
    }

    public static double determineGamesLostFromGames(Integer teamId, List<Game> allGamesForSeason) {
        return (double) allGamesForSeason.stream()
                .filter(game -> Objects.equals(game.getLosingTeamId(), teamId))
                .count();
    }

    public static String buildQ1Record(List<Game> games, SeasonMetrics metric, List<SeasonMetrics> allSeasonMetrics) {
        int q1GamesWon = 0;
        int q1GamesLost = 0;
        Integer teamId = metric.getTeamId();
        for (Game game: games) {
            Integer otherTeamId;
            if (game.getWinningTeamId().equals(teamId)) {
                otherTeamId = game.getLosingTeamId();
            } else {
                otherTeamId = game.getWinningTeamId();
            }
            int otherTeamSrsRank = getSrsRank(allSeasonMetrics, otherTeamId);

            // Q1 home win
            if (game.getHomeTeamId().equals(metric.getTeamId()) && !game.getNeutralSite()) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 15) {
                        q1GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 15) {
                        q1GamesLost++;
                    }
                }
            }

            // Q1 away win
            if (game.getAwayTeamId().equals(metric.getTeamId()) && game.getGameType().equals("REGULAR_SEASON")) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 25) {
                        q1GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 25) {
                        q1GamesLost++;
                    }
                }
            }
            // Q1 neutral win
            if (!game.getGameType().equals("REGULAR_SEASON")) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 20) {
                        q1GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 20) {
                        q1GamesLost++;
                    }
                }
            }
        }
        return q1GamesWon + "-" + q1GamesLost;
    }

    public static String buildQ2Record(List<Game> games, SeasonMetrics metric, List<SeasonMetrics> allSeasonMetrics) {
        int q2GamesWon = 0;
        int q2GamesLost = 0;
        int teamId = metric.getTeamId();
        for (Game game: games) {
            Integer otherTeamId;
            if (game.getWinningTeamId().equals(teamId)) {
                otherTeamId = game.getLosingTeamId();
            } else {
                otherTeamId = game.getWinningTeamId();
            }
            int otherTeamSrsRank = getSrsRank(allSeasonMetrics, otherTeamId);

            // Q2 home win
            if (game.getHomeTeamId().equals(teamId) && !game.getNeutralSite()) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 16 && otherTeamSrsRank <= 40) {
                        q2GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(teamId)) {
                    if (otherTeamSrsRank >= 16 && otherTeamSrsRank <= 40) {
                        q2GamesLost++;
                    }
                }
            }

            // Q2 away win
            if (game.getAwayTeamId().equals(teamId) && !game.getNeutralSite()) {
                if (game.getWinningTeamId().equals(teamId)) {
                    if (otherTeamSrsRank >= 26 && otherTeamSrsRank <= 53) {
                        q2GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(teamId)) {
                    if (otherTeamSrsRank >= 26 && otherTeamSrsRank <= 53) {
                        q2GamesLost++;
                    }
                }
            }
            // Q2 neutral win
            if (game.getNeutralSite()) {
                if (game.getWinningTeamId().equals(teamId)) {
                    if (otherTeamSrsRank >= 21 && otherTeamSrsRank <= 46) {
                        q2GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(teamId)) {
                    if (otherTeamSrsRank >= 21 && otherTeamSrsRank <= 46) {
                        q2GamesLost++;
                    }
                }
            }
        }
        return q2GamesWon + "-" + q2GamesLost;
    }

    public static String buildQ3Record(List<Game> games, SeasonMetrics metric, List<SeasonMetrics> allSeasonMetrics) {
        int q3GamesWon = 0;
        int q3GamesLost = 0;
        int teamId = metric.getTeamId();
        for (Game game: games) {
            Integer otherTeamId;
            if (game.getWinningTeamId().equals(teamId)) {
                otherTeamId = game.getLosingTeamId();
            } else {
                otherTeamId = game.getWinningTeamId();
            }
            int otherTeamSrsRank = getSrsRank(allSeasonMetrics, otherTeamId);

            // Q3 home win
            if (game.getHomeTeamId().equals(metric.getTeamId()) && !game.getNeutralSite()) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 41 && otherTeamSrsRank <= 74) {
                        q3GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 41 && otherTeamSrsRank <= 74) {
                        q3GamesLost++;
                    }
                }
            }

            // Q3 away win
            if (game.getAwayTeamId().equals(metric.getTeamId()) && game.getGameType().equals("REGULAR_SEASON")) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 54 && otherTeamSrsRank <= 90) {
                        q3GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 54 && otherTeamSrsRank <= 90) {
                        q3GamesLost++;
                    }
                }
            }

            // Q3 neutral win
            if (game.getNeutralSite()) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 47 && otherTeamSrsRank <= 82) {
                        q3GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 47 && otherTeamSrsRank <= 82) {
                        q3GamesLost++;
                    }
                }
            }
        }
        return q3GamesWon + "-" + q3GamesLost;
    }

    public static String buildQ4Record(List<Game> games, SeasonMetrics metric, List<SeasonMetrics> allSeasonMetrics) {
        int q4GamesWon = 0;
        int q4GamesLost = 0;
        for (Game game: games) {
            Integer otherTeamId;
            if (game.getWinningTeamId().equals(metric.getTeamId())) {
                otherTeamId = game.getLosingTeamId();
            } else {
                otherTeamId = game.getWinningTeamId();
            }
            int otherTeamSrsRank = getSrsRank(allSeasonMetrics, otherTeamId);

            // Q4 home win
            if (game.getHomeTeamId().equals(metric.getTeamId()) && game.getGameType().equals("REGULAR_SEASON")) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 75) {
                        q4GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 75) {
                        q4GamesLost++;
                    }
                }
            }

            // Q4 away win
            if (game.getAwayTeamId().equals(metric.getTeamId()) && game.getGameType().equals("REGULAR_SEASON")) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 91) {
                        q4GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 91) {
                        q4GamesLost++;
                    }
                }
            }
            // Q4 neutral win
            if (!game.getGameType().equals("REGULAR_SEASON")) {
                if (game.getWinningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 83 ) {
                        q4GamesWon++;
                    }
                } else if (game.getLosingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 83) {
                        q4GamesLost++;
                    }
                }
            }
        }
        return q4GamesWon + "-" + q4GamesLost;
    }


    public static Integer getSrsRank(List<SeasonMetrics> allSeasonMetrics, Integer teamId) {
        allSeasonMetrics.sort(Comparator.comparing(SeasonMetrics::getSrs).reversed());
        SeasonMetrics teamSeasonMetrics = allSeasonMetrics.stream().filter(seasonMetrics -> seasonMetrics.getTeamId().equals(teamId)).findFirst().orElse(null);
        return allSeasonMetrics.indexOf(teamSeasonMetrics);
    }
}
