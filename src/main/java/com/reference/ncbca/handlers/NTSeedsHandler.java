package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.NTSeedsDao;
import com.reference.ncbca.model.NTSeed;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NTSeedsHandler {

    private final NTSeedsDao ntSeedsDao;

    public NTSeedsHandler(NTSeedsDao ntSeedsDao) {
        this.ntSeedsDao = ntSeedsDao;
    }

    public void load(List<NTSeed> seeds) {
        ntSeedsDao.load(seeds);
    }

    public Optional<NTSeed> getSeedForTeamAndSeason(Integer teamId, Integer season) {
        return ntSeedsDao.getNTSeedForTeamAndYear(teamId, season);
    }

    public List<NTSeed> getAllSeedsForTeam(String teamName) {
        return ntSeedsDao.getAllNTSeedsForTeam(teamName);
    }
}
