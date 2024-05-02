package com.reference.ncbca.model;

public record Game(
        Integer gameId,
        Integer year,
        Boolean neutralSite,
        Integer homeTeamId,
        Integer awayTeamId,
        Integer homeTeamScore,
        Integer awayTeamScore,
        String homeTeamName,
        String awayTeamName,
        Integer season
) {}

