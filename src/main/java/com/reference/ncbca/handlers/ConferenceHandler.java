package com.reference.ncbca.handlers;

import com.reference.ncbca.model.*;
import com.reference.ncbca.model.dao.Game;
import com.reference.ncbca.model.dao.Season;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConferenceHandler {

    private final SeasonsHandler seasonsHandler;
    private final GamesHandler gamesHandler;

    public ConferenceHandler(SeasonsHandler seasonsHandler, GamesHandler gamesHandler) {
        this.seasonsHandler = seasonsHandler;
        this.gamesHandler = gamesHandler;
    }

    public List<ConferenceSummary> getConferenceSummaries(Integer year) {
        List<Season> seasons = seasonsHandler.listSeasonsForYear(year);
        List<Game> allGamesForSeason = gamesHandler.getAllGamesInSeason(year);
        List<ConferenceSummary> conferenceSummaries = new ArrayList<>();

        List<String> conferenceNames = Arrays.asList(
                "Atlantic Coast Conference",
                "Big Ten Conference",
                "Southeastern Conference",
                "Pacific Coast Conference",
                "Big Sedici Conference",
                "Big East Conference",
                "American Athletic Conference",
                "Mountain West Conference"
        );

        for (String conferenceName : conferenceNames) {
            ConferenceSummary conferenceSummary = buildConferenceSummary(seasons, conferenceName, year, allGamesForSeason);
            conferenceSummaries.add(conferenceSummary);
        }

        return conferenceSummaries;
    }

    public ConferenceSummary getConferenceSummary(String conferenceName, Integer year) {
        if (year == null) {
            year = seasonsHandler.determineMostRecentYear();
            if (year == -1) {
                return null;
            }
        }
        List<Season> seasons = seasonsHandler.listSeasonsForYear(year);
        List<Game> allGamesForSeason = gamesHandler.getAllGamesInSeason(year);
        return buildConferenceSummary(seasons, conferenceName, year, allGamesForSeason);
    }

    private ConferenceSummary buildConferenceSummary(List<Season> seasons, String conference, Integer year, List<Game> allGamesInyear) {
        List<Season> conferenceSeasons = seasons.stream().filter(season -> season.getConferenceName().equals(conference)).toList();
        List<ConferenceRecord> conferenceRecordList = new ArrayList<>();
        for (Season season: conferenceSeasons) {
            String teamName = season.getTeamName();
            String conferenceName = season.getConferenceName();
            String overallRecord = buildRecord(season.getGamesWon(), season.getGamesLost());
            String conferenceRecord = determineConferenceRecord(teamName, allGamesInyear);
            ConferenceRecord record = new ConferenceRecord(teamName, conferenceName, overallRecord, conferenceRecord, year);
            conferenceRecordList.add(record);
        }
        List<ConferenceRecord> sortedList = conferenceRecordList.stream().sorted((o1, o2) -> {
            Integer wins1 = Integer.valueOf(o1.conferenceRecord().split("-")[0]);
            Integer wins2 = Integer.valueOf(o2.conferenceRecord().split("-")[0]);
            return wins2.compareTo(wins1);
        }).toList();
        return new ConferenceSummary(conference, sortedList, year);
    }

    private String determineConferenceRecord(String teamName, List<Game> allGamesInyear) {
        List<Game> conferenceGamesTeamWasInvolvedIn = determineConferenceGamesTeamWasInvolvedIn(teamName, allGamesInyear);
        Integer gamesWon = 0;
        Integer gamesLost = 0;
        for (Game game: conferenceGamesTeamWasInvolvedIn) {
            if (game.winningTeamName().equals(teamName)) {
                gamesWon++;
            } else {
                gamesLost++;
            }
        }
        return buildRecord(gamesWon, gamesLost);
    }

    private List<Game> determineConferenceGamesTeamWasInvolvedIn(String teamName, List<Game> allGamesInyear) {
        allGamesInyear.sort(Comparator.comparing(Game::gameId));
        List<Game> allGamesForTeam = allGamesInyear.stream().filter(game -> game.gameType().equals("REGULAR_SEASON") && (game.winningTeamName().equals(teamName) || game.losingTeamName().equals(teamName))).toList();
        if (allGamesForTeam.size() <= 12) {
            return Collections.emptyList();
        } else {
            return allGamesInyear.stream().filter(game -> game.gameType().equals("REGULAR_SEASON") && (game.winningTeamName().equals(teamName) || game.losingTeamName().equals(teamName))).toList().subList(11, 33);
        }
    }

    private String buildRecord(Integer gamesWon, Integer gamesLost) {
        return gamesWon.toString().concat("-").concat(gamesLost.toString());
    }

}
