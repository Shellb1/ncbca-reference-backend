package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.CoachesDao;
import com.reference.ncbca.model.Coach;
import com.reference.ncbca.model.CoachSummary;
import com.reference.ncbca.model.Season;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachesHandler {

    private final CoachesDao coachesDao;
    private final SeasonsHandler seasonsHandler;

    public CoachesHandler(CoachesDao coachesDao, SeasonsHandler seasonsHandler) {
        this.coachesDao = coachesDao;
        this.seasonsHandler = seasonsHandler;
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
}
