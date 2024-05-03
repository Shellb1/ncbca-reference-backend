package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.SeasonsDao;
import com.reference.ncbca.model.Season;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeasonsHandler {

    private final SeasonsDao seasonsDao;

    public SeasonsHandler(SeasonsDao seasonsDao) {
        this.seasonsDao = seasonsDao;
    }

    public void load(List<Season> seasons) {

        seasonsDao.load(seasons);
    }

    public List<Season> listSeasonsForYear(Integer year) {
        return seasonsDao.findSeasonsByYear(year);
    }

    public void loadSeason() {

    }
}
