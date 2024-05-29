package com.reference.ncbca.model.dao;

public record Season(Integer teamId, String teamName, Integer gamesWon,
                     Integer gamesLost, Integer seasonYear,
                     String coach,
                     String conferenceName) {
}
