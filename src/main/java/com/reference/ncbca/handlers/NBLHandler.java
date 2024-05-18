package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.DraftPicksDao;
import com.reference.ncbca.model.DraftPick;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NBLHandler {

    private final DraftPicksDao draftPicksDao;

    public NBLHandler(DraftPicksDao draftPicksDao) {
        this.draftPicksDao = draftPicksDao;
    }

    public void loadDraftPicks(List<DraftPick> draftPickList) {
        draftPicksDao.load(draftPickList);
    }

    public List<DraftPick> getAllDraftPicksForSeason(Integer season) {
        return draftPicksDao.getDraftPicksBySeason(season);
    }

    public List<DraftPick> getAllDraftPicksForTeam(String teamName) {
        return draftPicksDao.getDraftPicksByTeam(teamName);
    }

    public List<DraftPick> getAllDraftPicksForCoach(String coachName) {
        return draftPicksDao.getDraftPicksByCoach(coachName);
    }
}
