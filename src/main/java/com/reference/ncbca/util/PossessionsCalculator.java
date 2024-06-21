package com.reference.ncbca.util;

import com.fasterxml.jackson.databind.JsonNode;

public class PossessionsCalculator {

    public static double calculatePossesionsForSeason(JsonNode export, Integer teamId) {
        JsonNode games = export.get("games");
        int fgaSeason = 0;
        int ftaSeason = 0;
        int toSeason = 0;
        int orbSeason = 0;
        for (JsonNode game: games) {
            JsonNode team = null;
            if (game.get("teams").get(0).get("tid").intValue() == teamId) {
                team = game.get("teams").get(0);
            } else if (game.get("teams").get(1).get("tid").intValue() == teamId) {
                team = game.get("teams").get(1);
            }
            if (team != null) {
                fgaSeason += team.get("fga").intValue();
                toSeason += team.get("tov").intValue();
                orbSeason += team.get("orb").intValue();
                ftaSeason += team.get("fta").intValue();
            }
        }
        return (fgaSeason - orbSeason) + toSeason + (0.4 * ftaSeason);
    }
}
