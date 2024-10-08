package com.reference.ncbca.model;

import com.reference.ncbca.model.dao.Game;
import com.reference.ncbca.model.dao.SeasonMetrics;

import java.util.List;

public record TeamSeasonSummary(
        int teamId,
        String teamName,
        int gamesWon,
        int gamesLost,
        int seasonYear,
        String coach,
        List<Game> games,
        Integer seed,
        SeasonMetrics seasonMetrics
) {}
