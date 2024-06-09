package com.reference.ncbca.util;

public class RpiCalculator {

    public static double calculateRPI(Integer gamesWonHome, Integer gamesWonAway, Integer gamesLostHome, Integer gamesLostAway, double sos) {
        double teamWinningPercentage = calculateWeightedTeamWinPercentage(gamesWonHome, gamesWonAway, gamesLostHome, gamesLostAway);
        return (teamWinningPercentage + (sos * 3.0000)) / 4.0000;
    }

    private static double calculateWeightedTeamWinPercentage(Integer gamesWonHome, Integer gamesWonAway, Integer gamesLostHome, Integer GamesLostAway) {
        double gamesWonHomeWeighted = gamesWonHome * 0.6;
        double gamesWonAwayWeighted = gamesWonAway * 1.4;
        double gamesLostHomeWeighted = gamesLostHome * 1.4;
        double gamesLostAwayWeighted = GamesLostAway * 0.6;
        double gamesWonWeighted = gamesWonHomeWeighted + gamesWonAwayWeighted;
        double gamesLostWeighted = gamesLostHomeWeighted + gamesLostAwayWeighted;
        return gamesWonWeighted / (gamesWonWeighted + gamesLostWeighted);
    }


}
