package com.reference.ncbca.handlers;


import com.reference.ncbca.dao.NitDao;
import com.reference.ncbca.dao.NtDao;
import com.reference.ncbca.model.NitGame;
import com.reference.ncbca.model.NtGame;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NtHandler {

    private final NtDao ntDao;

    public NtHandler(NtDao nitDao) {
        this.ntDao = nitDao;
    }

    public void load(List<NtGame> nitGames) {
        ntDao.insert(nitGames);
    }

    public List<NtGame> getNtGamesForSeason(Integer season) {
        return ntDao.listNtGamesForSeason(season);
    }

}
