package com.reference.ncbca.handlers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.reference.ncbca.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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
    private final PostseasonHandler postseasonHandler;

    @Value("classpath:databases/coaches.csv")
    private Resource coachesCsv;

    public LoadExportHandler(Map<Integer, String> conferencesMap, TeamsHandler teamsHandler, SeasonsHandler seasonsHandler, ScheduleHandler scheduleHandler, CoachesHandler coachesHandler, GamesHandler gamesHandler, PostseasonHandler postseasonHandler) {
        this.conferencesMap = conferencesMap;
        this.teamsHandler = teamsHandler;
        this.seasonsHandler = seasonsHandler;
        this.scheduleHandler = scheduleHandler;
        this.coachesHandler = coachesHandler;
        this.gamesHandler = gamesHandler;
        this.postseasonHandler = postseasonHandler;
    }

    public void loadExport(MultipartFile file, Boolean loadTeams, Boolean loadSeasons, Boolean loadGames, Boolean loadSchedules, Boolean loadCoaches, Integer season, Boolean loadCt, Boolean loadNIT, Boolean loadFirstFour, Boolean loadNT) throws IOException {
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
                Team team = new Team(teamId, teamName, conferenceId, conferenceName, coachName, true);
                teams.add(team);
            }
            teamsHandler.loadTeams(teams);
        }

        // only used for regular season games, use CT/NIT/First Four/NT options for postseason play
        if (loadGames) {
            // assumes teams have been loaded
            List<Team> allTeams = teamsHandler.listAllActiveTeams();
            List<Game> gamesPlayed = new ArrayList<>();
            JsonNode gamesArray = export.get("games");
            List<ScheduleGame> scheduledGames = scheduleHandler.getEntireSchedule(season);
            for (JsonNode game : gamesArray) {
                Integer gameId = game.get("gid").intValue();
                Integer seasonYear = game.get("season").intValue();
                Boolean neutralSite = false; // no way to determine this unless game is in playoffs phase
                Integer homeTeamId = scheduledGames.stream().filter(scheduleGame1 -> scheduleGame1.gameId().equals(gameId)).toList().getFirst().homeTeamId();
                Integer awayTeamId = scheduledGames.stream().filter(scheduleGame1 -> scheduleGame1.gameId().equals(gameId)).toList().getFirst().awayTeamId();
                String homeTeamName = determineTeamFromTid(homeTeamId, allTeams);
                String awayTeamName = determineTeamFromTid(awayTeamId, allTeams);
                Integer winningTeamId = game.get("won").get("tid").intValue();
                String winningTeamName = determineTeamFromTid(winningTeamId, allTeams);
                Integer winningTeamScore = game.get("won").get("pts").intValue();
                Integer losingTeamId = game.get("lost").get("tid").intValue();
                String losingTeamName = determineTeamFromTid(losingTeamId, allTeams);
                Integer losingTeamScore = game.get("lost").get("pts").intValue();
                String winningCoachName = determineCoachFromTeam(winningTeamId, allTeams);
                String losingCoachName = determineCoachFromTeam(losingTeamId, allTeams);
                Game gamePlayed = new Game(gameId, seasonYear, neutralSite,
                        homeTeamId, awayTeamId, homeTeamName, awayTeamName,
                        winningTeamId, winningTeamName, winningTeamScore, losingTeamId,
                        losingTeamName, losingTeamScore, winningCoachName, losingCoachName);
                gamesPlayed.add(gamePlayed);
            }
            gamesHandler.load(gamesPlayed, season);
        }

        if (loadSchedules) {
            // assuming active teams have already been loaded
            List<Team> teams = teamsHandler.listAllActiveTeams();
            List<ScheduleGame> games = new ArrayList<>();
            JsonNode gamesArray = export.get("schedule");
            for (JsonNode game : gamesArray) {
                Integer gameId = game.get("gid").intValue();
                Integer homeTeamId = game.get("homeTid").intValue();
                Integer awayTeamId = game.get("awayTid").intValue();
                String homeTeamName = determineTeamFromTid(homeTeamId, teams);
                String awayTeamName = determineTeamFromTid(awayTeamId, teams);
                ScheduleGame scheduleGame = new ScheduleGame(gameId, season, homeTeamId, awayTeamId, homeTeamName, awayTeamName);
                games.add(scheduleGame);
            }
            scheduleHandler.load(games);
        }

        if (loadCt) {
            // assuming active teams have already been loaded
            List<Team> teams = teamsHandler.listAllActiveTeams();
            JsonNode games = export.get("games");
            List<Game> gamesPlayed = new ArrayList<>();
            for (JsonNode game: games) {
                Integer gameId = game.get("gid").intValue();

                // loading CTs means all games are neutral site, home/away is irrelevant
                Boolean neutralSite = true;
                Integer seasonYear = game.get("season").intValue();
                Integer homeTeamId = game.get("won").get("tid").intValue();
                Integer awayTeamId = game.get("lost").get("tid").intValue();
                String homeTeamName = determineTeamFromTid(homeTeamId, teams);
                String awayTeamName = determineTeamFromTid(awayTeamId, teams);

                Integer winningTeamId = game.get("won").get("tid").intValue();
                String winningTeamName = determineTeamFromTid(winningTeamId, teams);
                Integer winningTeamScore = game.get("won").get("pts").intValue();
                Integer losingTeamId = game.get("lost").get("tid").intValue();
                String losingTeamName = determineTeamFromTid(losingTeamId, teams);
                Integer losingTeamScore = game.get("lost").get("pts").intValue();
                String winningCoachName = determineCoachFromTeam(winningTeamId, teams);
                String losingCoachName = determineCoachFromTeam(losingTeamId, teams);
                Game gamePlayed = new Game(gameId, seasonYear, neutralSite,
                        homeTeamId, awayTeamId, homeTeamName, awayTeamName,
                        winningTeamId, winningTeamName, winningTeamScore, losingTeamId,
                        losingTeamName, losingTeamScore, winningCoachName, losingCoachName);
                gamesPlayed.add(gamePlayed);
                gamesPlayed.add(gamePlayed);
            }
            gamesHandler.load(gamesPlayed, season);
        }

        if (loadNIT) {
            JsonNode games = export.get("games");
            List<Game> gamesPlayed = new ArrayList<>();
            List<PostseasonGame> nitGames = new ArrayList<>();
            // assumes all teams have been loaded
            List<Team> allTeams = teamsHandler.listAllActiveTeams();
            Integer currentGameId = gamesHandler.getLatestGameId(season);
            for (JsonNode game: games) {
                if (game.get("gid").intValue() <= currentGameId) {
                    continue;
                }
                currentGameId++;
                // loading NIT means game ids are after CT ids (differ from export because we split postseasons tournaments)
                Integer gameId = currentGameId;

                // loading NIT means means neutral site game, home/away is irrelevant
                Boolean neutralSite = true;
                Integer seasonYear = game.get("season").intValue();
                Integer homeTeamId = game.get("won").get("tid").intValue();
                Integer awayTeamId = game.get("lost").get("tid").intValue();
                String homeTeamName = determineTeamFromTid(homeTeamId, allTeams);
                String awayTeamName = determineTeamFromTid(awayTeamId, allTeams);

                Integer winningTeamId = game.get("won").get("tid").intValue();
                String winningTeamName = determineTeamFromTid(winningTeamId, allTeams);
                Integer winningTeamScore = game.get("won").get("pts").intValue();
                Integer losingTeamId = game.get("lost").get("tid").intValue();
                String losingTeamName = determineTeamFromTid(losingTeamId, allTeams);
                Integer losingTeamScore = game.get("lost").get("pts").intValue();
                String winningCoachName = determineCoachFromTeam(winningTeamId, allTeams);
                String losingCoachName = determineCoachFromTeam(losingTeamId, allTeams);
                Game gamePlayed = new Game(gameId, seasonYear, neutralSite,
                        homeTeamId, awayTeamId, homeTeamName, awayTeamName,
                        winningTeamId, winningTeamName, winningTeamScore, losingTeamId,
                        losingTeamName, losingTeamScore, winningCoachName, losingCoachName);
                gamesPlayed.add(gamePlayed);
                PostseasonGame nitGame = new PostseasonGame(gameId, season, winningTeamId, losingTeamId, winningTeamScore, losingTeamScore, winningTeamName, losingTeamName, "NIT");
                nitGames.add(nitGame);
            }
            gamesHandler.load(gamesPlayed, season);

            // keep track of NIT games for each season
            postseasonHandler.load(nitGames);
        }

        if (loadFirstFour || loadNT) {
            JsonNode games = export.get("games");
            List<Game> gamesPlayed = new ArrayList<>();
            List<PostseasonGame> postseasonGames = new ArrayList<>();
            // assumes all teams have been loaded
            List<Team> allTeams = teamsHandler.listAllActiveTeams();
            Integer currentGameId = gamesHandler.getLatestGameId(season);
            for (JsonNode game: games) {
                // not great for if we expand but will deal with when we get there!
                if (game.get("gid").intValue() <= 2295) {
                    continue;
                }
                currentGameId++;
                // loading first sixteen means game ids are after CT ids (differ from export because we split postseasons tournaments)
                Integer gameId = currentGameId;

                // loading NT means means neutral site game, home/away is irrelevant
                Boolean neutralSite = true;
                Integer seasonYear = game.get("season").intValue();
                Integer homeTeamId = game.get("won").get("tid").intValue();
                Integer awayTeamId = game.get("lost").get("tid").intValue();
                String homeTeamName = determineTeamFromTid(homeTeamId, allTeams);
                String awayTeamName = determineTeamFromTid(awayTeamId, allTeams);

                Integer winningTeamId = game.get("won").get("tid").intValue();
                String winningTeamName = determineTeamFromTid(winningTeamId, allTeams);
                Integer winningTeamScore = game.get("won").get("pts").intValue();
                Integer losingTeamId = game.get("lost").get("tid").intValue();
                String losingTeamName = determineTeamFromTid(losingTeamId, allTeams);
                Integer losingTeamScore = game.get("lost").get("pts").intValue();
                String winningCoachName = determineCoachFromTeam(winningTeamId, allTeams);
                String losingCoachName = determineCoachFromTeam(losingTeamId, allTeams);
                Game gamePlayed = new Game(gameId, seasonYear, neutralSite,
                        homeTeamId, awayTeamId, homeTeamName, awayTeamName,
                        winningTeamId, winningTeamName, winningTeamScore, losingTeamId,
                        losingTeamName, losingTeamScore, winningCoachName, losingCoachName);
                gamesPlayed.add(gamePlayed);
                PostseasonGame postseasonGame;
                if (loadFirstFour) {
                    postseasonGame = new PostseasonGame(gameId, seasonYear, winningTeamId, losingTeamId, winningTeamScore, losingTeamScore, winningTeamName, losingTeamName, "FIRST_SIXTEEN");
                } else {
                    postseasonGame = new PostseasonGame(gameId, seasonYear, winningTeamId, losingTeamId, winningTeamScore, losingTeamScore, winningTeamName, losingTeamName, "MAIN_FIELD");

                }
                postseasonGames.add(postseasonGame);
            }
            gamesHandler.load(gamesPlayed, season);
            // keep track of NT games for each season
            postseasonHandler.load(postseasonGames);
        }

        if (loadSeasons) {
            List<Season> seasons = new ArrayList<>();
            JsonNode teamsArray = export.get("teams");
            for (JsonNode jsonTeam : teamsArray) {
                String teamName = jsonTeam.get("region").textValue() + " " + jsonTeam.get("name").textValue();
                Integer teamId = jsonTeam.get("tid").intValue();
                Integer gamesWon = determineGamesWon(teamId, season);
                Integer gamesLost = determineGamesLost(teamId, season);
                Coach coachOfTeam = coachesHandler.getCoachOfTeam(teamName);
                String coachName = null;
                if (coachOfTeam != null) {
                    coachName = coachOfTeam.coachName();
                }
                seasons.add(new Season(teamId, teamName, gamesWon, gamesLost, season, coachName));
            }
            seasonsHandler.load(seasons);
        }

        parser.close();
    }

    private String determineCoachFromTeam(Integer teamId, List<Team> allTeams) {
        for (Team team: allTeams) {
            if (Objects.equals(team.teamId(), teamId)) {
                return team.coach();
            }
        }
        return "";
    }

    private Integer determineGamesWon(Integer teamId, Integer season) {
        return gamesHandler.determineGamesWonForTeam(teamId, season);
    }

    private Integer determineGamesLost(Integer teamId, Integer season) {
        return gamesHandler.determineGamesLostForTeam(teamId, season);
    }

    private List<Coach> getCoachList() throws IOException {
        List<Coach> coaches = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(coachesCsv.getFile()))) {
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

    private String determineTeamFromTid(Integer teamId, List<Team> teams) {
        for (Team team: teams) {
            if (team.teamId().equals(teamId)) {
                return team.name();
            }
        }
        return "";
    }
}
