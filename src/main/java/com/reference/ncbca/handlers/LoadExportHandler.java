package com.reference.ncbca.handlers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.reference.ncbca.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final GamesHandler gamesHandler;

    public LoadExportHandler(Map<Integer, String> conferencesMap, TeamsHandler teamsHandler, SeasonsHandler seasonsHandler, ScheduleHandler scheduleHandler, CoachesHandler coachesHandler, GamesHandler gamesHandler) {
        this.conferencesMap = conferencesMap;
        this.teamsHandler = teamsHandler;
        this.seasonsHandler = seasonsHandler;
        this.scheduleHandler = scheduleHandler;
        this.coachesHandler = coachesHandler;
        this.gamesHandler = gamesHandler;
    }


    public void loadExport(MultipartFile file, Boolean loadTeams, Boolean loadSeasons, Boolean loadGames, Boolean loadSchedules, Boolean loadCoaches, Integer season) throws IOException {
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
            List<Coach> coaches = getCoachList();
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
                JsonNode seasonsArray = jsonTeam.get("seasons");
                JsonNode seasonToEnter = null;
                for (JsonNode seasonJson: seasonsArray) {
                    if (seasonJson.get("season").intValue() == season) {
                        seasonToEnter = seasonJson;
                        break;
                    }
                }
                Integer gamesWon = seasonToEnter.get("won").intValue();
                Integer gamesLost = seasonToEnter.get("lost").intValue();
                Coach coachOfTeam = coachesHandler.getCoachOfTeam(teamName);
                String coachName = null;
                if (coachOfTeam != null) {
                    coachName = coachOfTeam.coachName();
                }
                seasons.add(new Season(teamId, teamName, gamesWon, gamesLost, season, coachName));
            }
            seasonsHandler.load(seasons);
        }

        if (loadGames) {
            List<Game> gamesPlayed = new ArrayList<>();
            JsonNode gamesArray = export.get("games");
            List<ScheduleGame> scheduledGames = scheduleHandler.getEntireSchedule();
            for (JsonNode game : gamesArray) {
                Integer gameId = game.get("gid").intValue();
                Integer seasonYear = game.get("season").intValue();
                Boolean neutralSite = false; // no way to determine this unless game is in playoffs phase
                Integer homeTeamId = scheduledGames.stream().filter(scheduleGame1 -> scheduleGame1.gameId().equals(gameId)).toList().getFirst().homeTeamId();
                Integer awayTeamId = scheduledGames.stream().filter(scheduleGame1 -> scheduleGame1.gameId().equals(gameId)).toList().getFirst().awayTeamId();
                String homeTeamName = determineTeamFromTid(homeTeamId);
                String awayTeamName = determineTeamFromTid(awayTeamId);
                Integer winningTeamId = game.get("won").get("tid").intValue();
                String winningTeamName = determineTeamFromTid(winningTeamId);
                Integer winningTeamScore = game.get("won").get("pts").intValue();
                Integer losingTeamId = game.get("lost").get("tid").intValue();
                String losingTeamName = determineTeamFromTid(losingTeamId);
                Integer losingTeamScore = game.get("lost").get("pts").intValue();
                Game gamePlayed = new Game(gameId, seasonYear, neutralSite,
                        homeTeamId, awayTeamId, homeTeamName, awayTeamName,
                        winningTeamId, winningTeamName, winningTeamScore, losingTeamId,
                        losingTeamName, losingTeamScore);
                gamesPlayed.add(gamePlayed);
            }
            gamesHandler.load(gamesPlayed);

        }

        if (loadSchedules) {
            List<ScheduleGame> games = new ArrayList<>();
            JsonNode gamesArray = export.get("schedule");
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

    private static List<Coach> getCoachList() throws IOException {
        List<Coach> coaches = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/databases/coaches.csv"))) {
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                if (!line[1].isBlank()) {
                    Coach coach = new Coach(line[1], 2078, null, true, line[0]);
                    coaches.add(coach);
                }
            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return coaches;
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
