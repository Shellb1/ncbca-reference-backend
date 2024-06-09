package com.reference.ncbca.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExportUtils {


    public static JsonNode getCurrentSeasonFromExport(Integer teamId, ObjectNode export, Integer season) {
        for (JsonNode team: export.get("teams")) {
            if (team.get("tid").intValue() == teamId) {
                for (JsonNode exportSeason: team.get("seasons")) {
                    if (exportSeason.get("season").intValue() == season) {
                        return exportSeason;
                    }
                }
            }
        }
        return null;
    }
}
