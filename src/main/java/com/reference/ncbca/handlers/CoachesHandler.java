package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.CoachesDao;
import com.reference.ncbca.model.dao.Coach;
import com.reference.ncbca.model.CoachSummary;
import com.reference.ncbca.model.dao.Game;
import com.reference.ncbca.model.dao.Season;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoachesHandler {

    private final CoachesDao coachesDao;
    private final SeasonsHandler seasonsHandler;
    private final GamesHandler gamesHandler;

    public CoachesHandler(CoachesDao coachesDao, SeasonsHandler seasonsHandler, GamesHandler gamesHandler) {
        this.coachesDao = coachesDao;
        this.seasonsHandler = seasonsHandler;
        this.gamesHandler = gamesHandler;
    }

    public void load(List<Coach> coaches, Integer season) {
        coachesDao.updateCoaches(coaches);
    }

    public Coach getCoachOfTeam(String teamName) {
        return coachesDao.getCoachFromTeam(teamName);
    }

    public List<Coach> getAllCoaches() {
        return coachesDao.getAllCoaches();
    }

    public CoachSummary getCoachSummary(String coachName) {
        List<Season> seasonsCoached = seasonsHandler.listSeasonsForCoach(coachName);
        return new CoachSummary(seasonsCoached, coachName);
    }

    public Map<String, String> getRecordVersusOtherCoaches(String coachName) {
        List<Game> gamesCoachedIn = gamesHandler.getAllTimeGamesPlayedByCoach(coachName);
        Map<String, Integer> winsMap = new HashMap<>();
        Map<String, Integer> lossesMap = new HashMap<>();
        Map<String, String> record = new LinkedHashMap<>(); // Using LinkedHashMap to maintain insertion order

        for (Game game : gamesCoachedIn) {
            String opponentCoachName;
            if (coachName.equals(game.getWinningCoachName())) {
                opponentCoachName = game.getLosingCoachName();
                winsMap.put(opponentCoachName, winsMap.getOrDefault(opponentCoachName, 0) + 1);
            } else {
                opponentCoachName = game.getWinningCoachName();
                lossesMap.put(opponentCoachName, lossesMap.getOrDefault(opponentCoachName, 0) + 1);
            }
        }

        winsMap.entrySet().removeIf(entry -> entry.getKey() == null);
        lossesMap.entrySet().removeIf(entry -> entry.getKey() == null);

        List<String> allOpponents = new ArrayList<>(new HashSet<>(winsMap.keySet()));
        allOpponents.addAll(new HashSet<>(lossesMap.keySet()));

        allOpponents.forEach(opponentCoachName -> {
            int wins = winsMap.getOrDefault(opponentCoachName, 0);
            int losses = lossesMap.getOrDefault(opponentCoachName, 0);
            record.put(opponentCoachName, wins + "-" + losses);
        });

        return record;
    }

}
