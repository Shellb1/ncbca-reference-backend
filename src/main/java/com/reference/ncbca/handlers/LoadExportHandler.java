package com.reference.ncbca.handlers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.reference.ncbca.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class LoadExportHandler {

    private final Map<Integer, String> conferencesMap;
    private final TeamsHandler teamsHandler;
    private final SeasonsHandler seasonsHandler;
    private final ScheduleHandler scheduleHandler;
    private final CoachesHandler coachesHandler;

    public LoadExportHandler(Map<Integer, String> conferencesMap, TeamsHandler teamsHandler, SeasonsHandler seasonsHandler, ScheduleHandler scheduleHandler, CoachesHandler coachesHandler) {
        this.conferencesMap = conferencesMap;
        this.teamsHandler = teamsHandler;
        this.seasonsHandler = seasonsHandler;
        this.scheduleHandler = scheduleHandler;
        this.coachesHandler = coachesHandler;
    }


    public void loadExport(MultipartFile file, Boolean loadTeams, Boolean loadSeasons, Boolean loadGames, Boolean loadSchedules, Boolean loadCoaches) throws IOException {
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

        if (loadCoaches) {
            List<Coach> coaches = new ArrayList<>();
            try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/databases/coaches.csv"))) {
                List<String[]> lines = reader.readAll();
                for (String[] line: lines) {
                    if (!line[1].isBlank()) {
                        Coach coach = new Coach(line[1], 2078, null, true, line[0]);
                        coaches.add(coach);
                    }
                }
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
            coachesHandler.load(coaches);
        }
        if (loadTeams) {
            List<Team> teams = new ArrayList<>();
            JsonNode teamsArray = export.get("teams");
            for (JsonNode jsonTeam : teamsArray) {
                String teamName = jsonTeam.get("region").textValue() + " " + jsonTeam.get("name").textValue();
                Integer teamId = jsonTeam.get("tid").intValue();
                Integer conferenceId = jsonTeam.get("cid").intValue();
                String conferenceName = conferencesMap.get(conferenceId);
                Coach coach = coachesHandler.getCoachOfTeam(teamName);
                String coachName = null;
                if (coach != null) {
                    coachName = coach.coachName();
                }
                Team team = new Team(teamId, teamName, conferenceId, conferenceName, coachName);
                teams.add(team);
            }
            teamsHandler.loadTeams(teams);
        }

        if (loadSeasons) {
            List<Season> seasons = new ArrayList<>();
            JsonNode teamsArray = export.get("teams");
            for (JsonNode jsonTeam : teamsArray) {
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

        if (loadGames) {
            List<Game> games = new ArrayList<>();
            JsonNode gamesArray = export.get("games");
            for (JsonNode game : gamesArray) {
                System.out.println(game);
            }
        }

        if (loadSchedules) {
            List<ScheduleGame> games = new ArrayList<>();
            JsonNode gamesArray = export.get("schedule");
            JsonNode exportAttributes = export.get("gameAttributes");
            Integer season = determineSeasonFromAttributes(exportAttributes);
            for (JsonNode game : gamesArray) {
                Integer gameId = game.get("gid").intValue();
                Integer homeTeamId = game.get("homeTid").intValue();
                Integer awayTeamId = game.get("awayTid").intValue();
                String homeTeamName = determineTeamFromTid(homeTeamId);
                String awayTeamName = determineTeamFromTid(awayTeamId);
                ScheduleGame scheduleGame = new ScheduleGame(gameId, season, homeTeamId, awayTeamId, homeTeamName, awayTeamName);
                games.add(scheduleGame);
            }
            scheduleHandler.load(games);
        }


        parser.close();
    }

    private Integer determineSeasonFromAttributes(JsonNode gameAttributes) {
        for (Iterator<JsonNode> it = gameAttributes.elements(); it.hasNext(); ) {
            JsonNode attribute = it.next();
            if ("season".equals(attribute.get("key").textValue())) {
                return attribute.get("value").intValue();
            }
        }
        return null;
    }

    private String determineTeamFromTid(Integer homeTeamId) {
        return teamsHandler.getTeam(homeTeamId).name();
    }

}
