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
                .filter(game -> game.homeTeamId().equals(teamId) || game.awayTeamId().equals(teamId))
                .map(game -> game.homeTeamId().equals(teamId) ? game.awayTeamId() : game.homeTeamId())
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

    public static String buildQ1Record(List<Game> games, SeasonMetrics metric, List<SeasonMetrics> allSeasonMetrics) {
        int q1GamesWon = 0;
        int q1GamesLost = 0;
        for (Game game: games) {
            Integer otherTeamId;
            if (game.winningTeamId().equals(metric.getTeamId())) {
                otherTeamId = game.losingTeamId();
            } else {
                otherTeamId = game.winningTeamId();
            }
            Integer otherTeamSrsRank = getSrsRank(allSeasonMetrics, otherTeamId);

            // Q1 home win
            if (game.homeTeamId().equals(metric.getTeamId()) && game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 15) {
                        q1GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 15) {
                        q1GamesLost++;
                    }
                }
            }

            // Q1 away win
            if (game.awayTeamId().equals(metric.getTeamId()) && game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 25) {
                        q1GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 25) {
                        q1GamesLost++;
                    }
                }
            }
            // Q1 neutral win
            if (game.awayTeamId().equals(metric.getTeamId()) && !game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 20) {
                        q1GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
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
        for (Game game: games) {
            Integer otherTeamId;
            if (game.winningTeamId().equals(metric.getTeamId())) {
                otherTeamId = game.losingTeamId();
            } else {
                otherTeamId = game.winningTeamId();
            }
            Integer otherTeamSrsRank = getSrsRank(allSeasonMetrics, otherTeamId);

            // Q2 home win
            if (game.homeTeamId().equals(metric.getTeamId()) && game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 16 && otherTeamSrsRank <= 40) {
                        q2GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 16 && otherTeamSrsRank <= 40) {
                        q2GamesLost++;
                    }
                }
            }

            // Q2 away win
            if (game.awayTeamId().equals(metric.getTeamId()) && game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 26 && otherTeamSrsRank <= 53) {
                        q2GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 26 && otherTeamSrsRank <= 53) {
                        q2GamesLost++;
                    }
                }
            }
            // Q1 neutral win
            if (game.awayTeamId().equals(metric.getTeamId()) && !game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 20) {
                        q2GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank <= 20) {
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
        for (Game game: games) {
            Integer otherTeamId;
            if (game.winningTeamId().equals(metric.getTeamId())) {
                otherTeamId = game.losingTeamId();
            } else {
                otherTeamId = game.winningTeamId();
            }
            Integer otherTeamSrsRank = getSrsRank(allSeasonMetrics, otherTeamId);

            // Q3 home win
            if (game.homeTeamId().equals(metric.getTeamId()) && game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 40 && otherTeamSrsRank <= 74) {
                        q3GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 40 && otherTeamSrsRank <= 74) {
                        q3GamesLost++;
                    }
                }
            }

            // Q3 away win
            if (game.awayTeamId().equals(metric.getTeamId()) && game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 54 && otherTeamSrsRank <= 90) {
                        q3GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 54 && otherTeamSrsRank <= 90) {
                        q3GamesLost++;
                    }
                }
            }

            // Q1 neutral win
            if (game.awayTeamId().equals(metric.getTeamId()) && !game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 47 && otherTeamSrsRank <= 82) {
                        q3GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
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
            if (game.winningTeamId().equals(metric.getTeamId())) {
                otherTeamId = game.losingTeamId();
            } else {
                otherTeamId = game.winningTeamId();
            }
            Integer otherTeamSrsRank = getSrsRank(allSeasonMetrics, otherTeamId);

            // Q4 home win
            if (game.homeTeamId().equals(metric.getTeamId()) && game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 75) {
                        q4GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 75) {
                        q4GamesLost++;
                    }
                }
            }

            // Q4 away win
            if (game.awayTeamId().equals(metric.getTeamId()) && game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 91) {
                        q4GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 91) {
                        q4GamesLost++;
                    }
                }
            }
            // Q1 neutral win
            if (game.awayTeamId().equals(metric.getTeamId()) && !game.gameType().equals("REGULAR_SEASON")) {
                if (game.winningTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 83 ) {
                        q4GamesWon++;
                    }
                } else if (game.losingTeamId().equals(metric.getTeamId())) {
                    if (otherTeamSrsRank >= 83) {
                        q4GamesLost++;
                    }
                }
            }
        }
        return q4GamesWon + "-" + q4GamesLost;
    }


    private static Integer getSrsRank(List<SeasonMetrics> allSeasonMetrics, Integer teamId) {
        allSeasonMetrics.sort(Comparator.comparing(SeasonMetrics::getSrs).reversed());
        SeasonMetrics teamSeasonMetrics = allSeasonMetrics.stream().filter(seasonMetrics -> seasonMetrics.getTeamId().equals(teamId)).toList().getFirst();
        return allSeasonMetrics.indexOf(teamSeasonMetrics);
    }
}
