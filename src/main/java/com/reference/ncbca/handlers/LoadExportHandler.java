package com.reference.ncbca.handlers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reference.ncbca.model.Season;
import com.reference.ncbca.model.Team;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class LoadExportHandler {

    private final Map<Integer, String> conferencesMap;
    private final Map<String, String> coachesMap;
    private final TeamsHandler teamsHandler;
    private final SeasonsHandler seasonsHandler;

    public LoadExportHandler(Map<Integer, String> conferencesMap, Map<String, String> coachesMap, TeamsHandler teamsHandler, SeasonsHandler seasonsHandler) {
        this.conferencesMap = conferencesMap;
        this.coachesMap = coachesMap;
        this.teamsHandler = teamsHandler;
        this.seasonsHandler = seasonsHandler;
    }


    public void loadExport(MultipartFile file, Boolean loadTeams, Boolean loadSeasons) throws IOException {
        byte[] fileBytes = file.getBytes();

        // Remove BOM if present
        if (fileBytes.length > 3 && fileBytes[0] == (byte) 0xEF && fileBytes[1] == (byte) 0xBB && fileBytes[2] == (byte) 0xBF) {
            fileBytes = Arrays.copyOfRange(fileBytes, 3, fileBytes.length);
        }

        String json = new String(fileBytes, StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);
        ObjectNode export = mapper.readTree(parser);

        if (loadTeams) {
            List<Team> teams = new ArrayList<>();
            JsonNode teamsArray = export.get("teams");
            for (JsonNode jsonTeam: teamsArray) {
                String teamName = jsonTeam.get("region").textValue() + " " + jsonTeam.get("name").textValue();
                Integer teamId = jsonTeam.get("tid").intValue();
                Integer conferenceId = jsonTeam.get("cid").intValue();
                String conferenceName = conferencesMap.get(conferenceId);
                Team team = new Team(teamId, teamName, conferenceId, conferenceName, coachesMap.get(teamName));
                teams.add(team);
            }
            teamsHandler.loadTeams(teams);
        }

        if (loadSeasons) {
            List<Season> seasons = new ArrayList<>();
            JsonNode teamsArray = export.get("teams");
            for (JsonNode jsonTeam: teamsArray) {
                String teamName = jsonTeam.get("region").textValue() + " " + jsonTeam.get("name").textValue();
                Integer teamId = jsonTeam.get("tid").intValue();
                int seasonsArraySize = jsonTeam.get("seasons").size();
                Integer seasonYear = jsonTeam.get("seasons").get(seasonsArraySize - 1).get("season").intValue();
                Integer gamesWon = jsonTeam.get("seasons").get(seasonsArraySize - 1).get("won").intValue();
                Integer gamesLost = jsonTeam.get("seasons").get(seasonsArraySize - 1).get("lost").intValue();
                seasons.add(new Season(teamId, teamName, gamesWon, gamesLost, seasonYear));
            }
            seasonsHandler.load(seasons);
        }

        parser.close();
    }

}
