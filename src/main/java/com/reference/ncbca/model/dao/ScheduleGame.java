package com.reference.ncbca.model.dao;

public record ScheduleGame(Integer gameId, Integer season, Integer homeTeamId, Integer awayTeamId, String homeTeamName,
                           String awayTeamName) {
}
