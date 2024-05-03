package com.reference.ncbca.model;

public record Season(Integer teamId, String teamName, Integer gamesWon,
                     Integer gamesLost, Integer seasonYear,
                     String coach) {
}
