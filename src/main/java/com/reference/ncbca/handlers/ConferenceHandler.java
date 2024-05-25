package com.reference.ncbca.handlers;

import com.reference.ncbca.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        ConferenceSummary accSummary = buildConferenceSummary(seasons, "Atlantic Coast Conference", year, allGamesForSeason);
        ConferenceSummary b1gSummary = buildConferenceSummary(seasons, "Big Ten Conference", year, allGamesForSeason);
        ConferenceSummary secSummary = buildConferenceSummary(seasons, "Southeastern Conference", year, allGamesForSeason);
        ConferenceSummary pccSummary = buildConferenceSummary(seasons, "Pacific Coast Conference", year, allGamesForSeason);
        ConferenceSummary sediciSummary = buildConferenceSummary(seasons, "Big Sedici Conference", year, allGamesForSeason);
        ConferenceSummary bigEastSummary = buildConferenceSummary(seasons, "Big East Conference", year, allGamesForSeason);
        ConferenceSummary aacSummary = buildConferenceSummary(seasons, "American Athletic Conference", year, allGamesForSeason);
        ConferenceSummary mwcSummary = buildConferenceSummary(seasons, "Mountain West Conference", year,allGamesForSeason);
        return List.of(accSummary, b1gSummary, secSummary, pccSummary, sediciSummary, bigEastSummary, aacSummary, mwcSummary);
    }

    private ConferenceSummary buildConferenceSummary(List<Season> seasons, String conference, Integer year, List<Game> allGamesInyear) {
        List<Season> conferenceSeasons = seasons.stream().filter(season -> season.conferenceName().equals(conference)).toList();
        List<ConferenceRecord> conferenceRecordList = new ArrayList<>();
        for (Season season: conferenceSeasons) {
            String teamName = season.teamName();
            String conferenceName = season.conferenceName();
            String overallRecord = buildRecord(season.gamesWon(), season.gamesLost());
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
        List<Game> conferenceGamesTeamWasInvolvedIn = determineGamesTeamWasInvolvedIn(teamName, allGamesInyear);
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

    private List<Game> determineGamesTeamWasInvolvedIn(String teamName, List<Game> allGamesInyear) {
        allGamesInyear.sort(Comparator.comparing(Game::gameId));
        return allGamesInyear.stream().filter(game -> game.gameType().equals("REGULAR_SEASON") && (game.winningTeamName().equals(teamName) || game.losingTeamName().equals(teamName))).toList().subList(11, 33);
    }

    private String buildRecord(Integer gamesWon, Integer gamesLost) {
        return gamesWon.toString().concat("-").concat(gamesLost.toString());
    }

}
