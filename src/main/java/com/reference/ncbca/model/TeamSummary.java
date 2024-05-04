package com.reference.ncbca.model;

import java.util.List;

public record TeamSummary(
        int teamId,
        String teamName,
        int gamesWon,
        int gamesLost,
        int seasonYear,
        String coach,
        List<Game> games
) {}
