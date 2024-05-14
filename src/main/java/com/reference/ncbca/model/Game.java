package com.reference.ncbca.model;

public record Game(
        Integer gameId,
        Integer season,
        Boolean neutralSite,
        Integer homeTeamId,
        Integer awayTeamId,
        String homeTeamName,
        String awayTeamName,
        Integer winningTeamId,
        String winningTeamName,
        Integer winningTeamScore,
        Integer losingTeamId,
        String losingTeamName,
        Integer losingTeamScore,
        String winningCoachName,
        String losingCoachName
) {}


