package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.CoachesDao;
import com.reference.ncbca.model.Coach;
import com.reference.ncbca.model.CoachSummary;
import com.reference.ncbca.model.Game;
import com.reference.ncbca.model.Season;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void load(List<Coach> coaches) {
        coachesDao.load(coaches);
    }

    public Coach getCoachOfTeam(String teamName) {
        return coachesDao.getCoachFromTeam(teamName);
    }

    public CoachSummary getCoachSummary(String coachName) {
        List<Season> seasonsCoached = seasonsHandler.listSeasonsForCoach(coachName);
        return new CoachSummary(seasonsCoached, coachName);
    }

    public Map<String, String> getRecordVersusOtherCoaches(String coachName) {
        List<Game> gamesCoachedIn = gamesHandler.getAllTimeGamesPlayedByCoach(coachName);
        Map<String, Integer> winsMap = new HashMap<>();
        Map<String, Integer> lossesMap = new HashMap<>();
        Map<String, String> record = new HashMap<>();

        for (Game game : gamesCoachedIn) {
            String opponentCoachName;
            if (coachName.equals(game.winningCoachName())) {
                opponentCoachName = game.losingCoachName();
                winsMap.put(opponentCoachName, winsMap.getOrDefault(opponentCoachName, 0) + 1);
            } else {
                opponentCoachName = game.winningCoachName();
                lossesMap.put(opponentCoachName, lossesMap.getOrDefault(opponentCoachName, 0) + 1);
            }
        }

        winsMap.remove(null);
        lossesMap.remove(null);
        for (String opponentCoachName : winsMap.keySet()) {
            int wins = winsMap.getOrDefault(opponentCoachName, 0);
            int losses = lossesMap.getOrDefault(opponentCoachName, 0);
            record.put(opponentCoachName, wins + "-" + losses);
        }
        record.remove(null);

        return record;
    }

}
