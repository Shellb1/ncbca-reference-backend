package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.ScheduleDao;
import com.reference.ncbca.model.ScheduleGame;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleHandler {

    private final ScheduleDao scheduleDao;

    public ScheduleHandler(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    public void load(List<ScheduleGame> games) {
        scheduleDao.load(games);
    }

    public List<ScheduleGame> getEntireSchedule(Integer year) {
        return scheduleDao.getEntireSchedule(year);
    }
}
