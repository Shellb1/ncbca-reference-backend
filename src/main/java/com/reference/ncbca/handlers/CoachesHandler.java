package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.CoachesDao;
import com.reference.ncbca.model.Coach;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachesHandler {

    private final CoachesDao coachesDao;

    public CoachesHandler(CoachesDao coachesDao) {
        this.coachesDao = coachesDao;
    }

    public void load(List<Coach> coaches) {
        coachesDao.load(coaches);
    }

    public Coach getCoachOfTeam(String teamName) {
        return coachesDao.getCoachFromTeam(teamName);
    }
}
