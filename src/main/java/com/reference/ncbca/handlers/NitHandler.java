package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.NitDao;
import com.reference.ncbca.model.NitGame;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NitHandler {

    private final NitDao nitDao;

    public NitHandler(NitDao nitDao) {
        this.nitDao = nitDao;
    }

    public void load(List<NitGame> nitGames) {
        nitDao.insert(nitGames);
    }

    public List<NitGame> getNitGamesForSeason(Integer season) {
        return nitDao.listNitGamesForSeason(season);
    }
}
