package com.reference.ncbca.model;

import java.util.List;

public record TeamSeasonSummary(
        int teamId,
        String teamName,
        int gamesWon,
        int gamesLost,
        int seasonYear,
        String coach,
        List<Game> games,
        Integer seed
) {}
