package com.reference.ncbca.model;

public record PostseasonGame(Integer gameId,
                             Integer season,
                             Integer winningTeamId,
                             Integer losingTeamId,
                             Integer winningTeamScore,
                             Integer losingTeamScore,
                             String winningTeamName,
                             String losingTeamName,
                             String gameType) {
}

